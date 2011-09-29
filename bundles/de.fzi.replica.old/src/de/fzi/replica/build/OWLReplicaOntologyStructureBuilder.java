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

/**
 * The sole purpose of this package is to provide a mechanism for
 * generating the OWLReplicaOntologyImpl class.
 */
package de.fzi.replica.build;

import static de.fzi.replica.build.Constants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.1, 28.09.2010
 *
 */
public class OWLReplicaOntologyStructureBuilder {
	
	OWLReplicaOntologyPackageyDeclarationBuilder packageDeclarationBuilder;
	OWLReplicaOntologyClassDeclarationBuilder classDeclarationBuilder;
	OWLReplicaOntologyFieldsBuilder fieldsBuilder;
	OWLReplicaOntologyConstructorsBuilder constructorsBuilder;
	
	SharedObjectMethodsBuilder sharedObjMethodsBuilder;
	OWLOntologyMethodsBuilder reflectiveOntoMethodsBuilder;
	OWLOntologyMethodsBuilder staticOntoMethodsBuilder;
	
	public OWLReplicaOntologyStructureBuilder() {
		packageDeclarationBuilder = new StaticOWLReplicaOntologyPackageyDeclarationBuilder();
		classDeclarationBuilder = new StaticOWLReplicaOntologyClassDeclarationBuilder();
		fieldsBuilder = new StaticOWLReplicaOntologyFieldsBuilder();
		constructorsBuilder = new StaticOWLReplicaOntologyConstructorsBuilder();
		sharedObjMethodsBuilder = new StaticOWLReplicaOntologySharedObjectMethodsBuilder();
		reflectiveOntoMethodsBuilder = new ReflectiveOWLOntologyMethodsBuilder();
		staticOntoMethodsBuilder = new StaticOWLOntologyMethodsBuilder();
	}
	
	public String buildSharedOWLOntology() {
		StringBuilder sb = new StringBuilder();
		
		packageDeclarationBuilder.buildSharedOWLOntologyPackageDeclaration(sb);
		
		comment(sb, 0, "This class has been generated, do not modify\n\n@generated");
		classDeclarationBuilder.buildSharedOWLOntologyClassDeclaration(sb, 0);
		fieldsBuilder.buildSharedOWLOntologyFields(sb, 1);
		constructorsBuilder.buildSharedOWLOntologyConstructors(sb, 1);
		
//		comment(sb, 1, "SharedObject methods start here");
//		sb.append(NEWLINE);
//		sharedObjMethodsBuilder.buildSharedObjectMethods(sb, 1);
		
		comment(sb, 1, "OWLOntology methods start here");
		sb.append(NEWLINE);
		reflectiveOntoMethodsBuilder.buildOWLOntologyMethods(sb, 1);
		staticOntoMethodsBuilder.buildOWLOntologyMethods(sb, 1);
		
		// close class
		sb.append(CURLY_BRACE_CLOSE);
		
		return sb.toString();
	}
	
	public static void indent(StringBuilder sb, int indentation) {
		for(int i = 0; i < indentation; i++) {
			sb.append(TAB);
		}
	}
	
	public static void comment(StringBuilder sb, int indentation, String comment) {
		BufferedReader reader = new BufferedReader(new StringReader(comment));
		String line = "";
		try {
			for(int i = 0; (line = reader.readLine()) != null; i++) {
				indent(sb, indentation);
				if(i == 0) {
					sb.append(COMMENT_BLOCK_BEGIN);
					sb.append(NEWLINE);
					indent(sb, indentation);
					sb.append(COMMENT_BLOCK_STAR);
					sb.append(SPACE);
				} else {
					sb.append(COMMENT_BLOCK_STAR);
					sb.append(SPACE);
				}
				sb.append(line);
				sb.append(NEWLINE);
			}
			indent(sb, indentation);
			sb.append(COMMENT_BLOCK_END);
			sb.append(NEWLINE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
