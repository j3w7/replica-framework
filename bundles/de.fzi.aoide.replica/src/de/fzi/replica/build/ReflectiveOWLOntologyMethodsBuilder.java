/*
   Copyright 2011 Jan Novacek

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package de.fzi.replica.build;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.model.OWLMutableOntology;

import static de.fzi.replica.build.Constants.*;
import static de.fzi.replica.build.OWLReplicaOntologyStructureBuilder.*;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.3, 28.09.2010
 *
 */
public class ReflectiveOWLOntologyMethodsBuilder implements OWLOntologyMethodsBuilder {
	
	private static final Class<?> CLASS_TO_REFLECT = OWLMutableOntology.class;
	
	private static final String[] DEFAULT_METHOD_EXCLUSIONS = {
		// overriding these could cause much pain
		"wait", "notify", "notifyAll", "getClass", "toString", "hashCode", "compareTo", "equals",
		// the ontologyID is cached for proper object creation, see OWLReplicaOntologyImpl
		"getOntologyID"
	};
	
	// added to each method
	private static final String[] ANNOTATIONS = { "@ProxyMethod" };
	
	// added only to specified method
	private static final String[][] SPECIFIC_ANNOTATIONS = {
		// first element expected to be the method name, annotations follow
		{ "applyChange", "@PropagatingMethod" },
		{ "applyChanges", "@PropagatingMethod" }
	};
	
	private static final String ARGUMENT_BASENAME = "arg";
	
	private static final Pattern parameterPattern = Pattern.compile("\\((.+)\\)");
	
	private Set<String> methodExclusions;
	
	private Set<String> annotations;
	
	private Map<String, List<String>> specificAnnotations;
	
	public ReflectiveOWLOntologyMethodsBuilder() {
		methodExclusions = new HashSet<String>();
		for(String method : DEFAULT_METHOD_EXCLUSIONS) {
			methodExclusions.add(method);
		}
		annotations = new HashSet<String>();
		for(String method : ANNOTATIONS) {
			annotations.add(method);
		}
		specificAnnotations = new HashMap<String, List<String>>();
		for(String[] row : SPECIFIC_ANNOTATIONS) {
			String methodName = "";
			List<String> annotations = new LinkedList<String>();
			for(int i = 0; i < row.length; i++) {
				if(i == 0) {
					methodName = row[i];
				} else {
					annotations.add(row[i]);
				}
			}
			specificAnnotations.put(methodName, annotations);
		}
	}
	
	public void setMethodExclusions(Set<String> methodExclusions) {
		this.methodExclusions = methodExclusions;
	}

	public Set<String> getMethodExclusions() {
		return methodExclusions;
	}

	@Override
	public void buildOWLOntologyMethods(StringBuilder sb, int indentation) {
		buildMethods(CLASS_TO_REFLECT, sb, indentation);
	}
	
	protected void buildMethods(Class<?> clazz, StringBuilder sb, int indentation) {
		for(Method method : clazz.getMethods()) {
			if(!isExcluded(method)) {
				buildMethodAnnotations(method, sb, indentation);
				
				indent(sb, indentation);
				buildMethodDeclaration(method, sb);
				sb.append(SPACE);
				sb.append(CURLY_BRACE_OPEN);
				sb.append(NEWLINE);
				
				indent(sb, indentation+1);
				buildMethodBody(method, sb);
				sb.append(NEWLINE);
				
				indent(sb, indentation);
				sb.append(CURLY_BRACE_CLOSE);
				sb.append(NEWLINE);
				sb.append(NEWLINE);
			}
		}
	}
	
	protected void buildMethodAnnotations(Method method, StringBuilder sb, int indentation) {
		for(String annotation : annotations) {
			indent(sb, indentation);
			sb.append(annotation);
			sb.append(NEWLINE);
		}
		if(specificAnnotations.containsKey(method.getName())) {
			for(String annotation : specificAnnotations.get(method.getName())) {
				indent(sb, indentation);
				sb.append(annotation);
				sb.append(NEWLINE);
			}
		}
		for(Annotation annotation : method.getAnnotations()) {
			indent(sb, indentation);
			sb.append(annotation.toString());
			sb.append(NEWLINE);
		}
	}
	
	protected void buildMethodDeclaration(Method method, StringBuilder sb) {
		buildMethodModifiers(method, sb);
		sb.append(SPACE);
		buildMethodGenerics(method, sb);
		sb.append(SPACE);
		buildMethodReturnType(method, sb);
		sb.append(SPACE);
		sb.append(method.getName());
		sb.append(BRACE_OPEN);
		buildParameterListDeclaration(method, sb);
		sb.append(BRACE_CLOSE);
	}
	
	private void buildMethodBody(Method method, StringBuilder sb) {
		// return something if we have to
		if(!method.getGenericReturnType().equals(Void.TYPE)) {
			sb.append(KEYWORD_RETURN);
		}
		sb.append(SPACE);
		sb.append("ontology."+method.getName());
		sb.append(BRACE_OPEN);
		buildParameterList(method, sb);
		sb.append(BRACE_CLOSE);
		sb.append(END);
	}
	
	public static void buildMethodModifiers(Method method, StringBuilder sb) {
		if(Modifier.isPublic(method.getModifiers())) {
			sb.append(KEYWORD_PUBLIC);
		} else if(Modifier.isPrivate(method.getModifiers())) {
			sb.append(KEYWORD_PRIVATE);
		} else if(Modifier.isProtected(method.getModifiers())) {
			sb.append(KEYWORD_PROTECTED);
		}
	}
	
	public static void buildMethodReturnType(Method method, StringBuilder sb) {
		String genericReturnType = method.getGenericReturnType().toString();
		if(genericReturnType.contains(KEYWORD_INTERFACE)) {
			genericReturnType = genericReturnType.replace(KEYWORD_INTERFACE, EMPTY).trim();
		} else if(genericReturnType.contains(KEYWORD_CLASS)) {
			genericReturnType = genericReturnType.replace(KEYWORD_CLASS, EMPTY).trim();
		}
		sb.append(genericReturnType);
	}
	
	public static void buildMethodGenerics(Method method, StringBuilder sb) {
		TypeVariable<Method>[] typeParameters = method.getTypeParameters();
		if(typeParameters.length > 0) {
			// FIXME: generic type parameters with restrictions or wildcards
			if("getAxiomCount".equals(method.getName()) || "getAxioms".equals(method.getName())) {
				sb.append("<T extends org.semanticweb.owlapi.model.OWLAxiom>");
			} else {
				sb.append("<");
				for(int i = 0; i < typeParameters.length; i++) {
					sb.append(typeParameters[i].toString());
					if(typeParameters.length > 1 && i < typeParameters.length) {
						sb.append(COMMA);
					}
				}
				sb.append(">");
			}
		}
	}
	
	/**
	 * This method works because buildParameterListDeclaration() sets the argument
	 * names to 'ARGUMENT_BASENAME+i' for argument i.
	 * As we can not know the argument names it is not a good idea to use this method
	 * in another context. Thats why it is protected.
	 * 
	 * @param method
	 * @param sb
	 */
	protected static void buildParameterList(Method method, StringBuilder sb) {
		Matcher matcher = parameterPattern.matcher(method.toGenericString());
		if(matcher.find()) {
			int argCount = 0;
			Scanner scanner = new Scanner(matcher.group(1)).useDelimiter(",");
			while(scanner.hasNext()) {
				sb.append(ARGUMENT_BASENAME+argCount);
				argCount++;
				scanner.next(); // skip type declaration
				if(scanner.hasNext()) {
					sb.append(COMMA);
					sb.append(SPACE);
				}
			}
//			char c;
//			byte[] methodAsBytes = matcher.group(1).getBytes();
//			for(int i = 0; i < methodAsBytes.length; i++) {
//				c = (char) methodAsBytes[i];
//				if(c == ',') {
//					sb.append(ARGUMENT_BASENAME+argCount+COMMA+SPACE);
//					argCount++;
//				}
//			}
			// deal with a single parameter
//			if(methodAsBytes.length > 0) {
//				sb.append("arg"+argCount);
//			}
			
		}
	}
	
	public static void buildParameterListDeclaration(Method method, StringBuilder sb) {
		Matcher matcher = parameterPattern.matcher(method.toGenericString());
		if(matcher.find()) {
			int argCount = 0;
			Scanner scanner = new Scanner(matcher.group(1)).useDelimiter(",");
			while(scanner.hasNext()) {
				sb.append(scanner.next());
				sb.append(SPACE);
				sb.append(ARGUMENT_BASENAME+argCount);
				argCount++;
				if(scanner.hasNext()) {
					sb.append(COMMA);
					sb.append(SPACE);
				}
			}
//			char c;
//			byte[] methodAsBytes = matcher.group(1).getBytes();
//			for(int i = 0; i < methodAsBytes.length; i++) {
//				c = (char) methodAsBytes[i];
//				if(c == ',') {
//					sb.append(SPACE+ARGUMENT_BASENAME+argCount);
//					argCount++;
//				}
//				sb.append(c);
//			}
//			// deal with a single parameter
//			if(methodAsBytes.length > 0) {
//				sb.append(SPACE);
//				sb.append("arg"+argCount);
//			}
		}
	}
	
	public boolean isExcluded(Method method) {
		/*
		 * TODO filter methods based on more than just the name,
		 * the second condition is to exclude compareTo(Object) and
		 * the third to exclude equals(Object)
		 */
//		boolean excluded = false;
//		excluded = methodExclusions.contains(method.getName()) || 
//		 ("compareTo".equals(method.getName()) && method.getParameterTypes()[0].equals(java.lang.Object.class)) ||
//		 ("equals".equals(method.getName()) && method.getParameterTypes()[0].equals(java.lang.Object.class));
		return methodExclusions.contains(method.getName());
	}

}
