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

import static de.fzi.replica.build.Constants.*;
import static de.fzi.replica.build.OWLReplicaOntologyStructureBuilder.indent;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version version 1.1, 31.08.2010
 *
 */
public class StaticOWLReplicaOntologyClassDeclarationBuilder implements
		OWLReplicaOntologyClassDeclarationBuilder {
	
	public static final String DEFAULT_CLASS_DECLARATION = "public abstract class AbstractOWLReplicaOntology extends org.eclipse.ecf.core.sharedobject.TransactionSharedObject implements OWLReplicaOntology, java.io.Serializable";
	
	private String classDeclaration = DEFAULT_CLASS_DECLARATION;  
	
	@Override
	public void buildSharedOWLOntologyClassDeclaration(StringBuilder sb, int indentation) {
		indent(sb, indentation);
		sb.append(classDeclaration);
		sb.append(SPACE);
		sb.append(CURLY_BRACE_OPEN);
		sb.append(NEWLINE);
		sb.append(NEWLINE);
	}

	public String getClassDeclaration() {
		return classDeclaration;
	}

	public void setClassDeclaration(String classDeclaration) {
		this.classDeclaration = classDeclaration;
	}

}
