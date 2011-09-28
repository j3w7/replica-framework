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

package de.fzi.aoide.replica.build;

import static de.fzi.aoide.replica.build.Constants.NEWLINE;
import static de.fzi.aoide.replica.build.OWLReplicaOntologyStructureBuilder.indent;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version version 1.0, 31.08.2010
 *
 */
public class StaticOWLReplicaOntologyConstructorsBuilder implements
		OWLReplicaOntologyConstructorsBuilder {

	// i know...
	public static final String[] DEFAULT_CONSTRUCTORS = { 
		"/**\n\t* Primary constructor\n\t* \n\t* @param ontology\n\t*/\n" +
			"\tpublic AbstractOWLReplicaOntology(org.semanticweb.owlapi.model.OWLOntologyManager manager," +
			" org.semanticweb.owlapi.model.OWLOntologyID ontologyID) {\n" +
			"\t\tsuper();\n\t\tthis.ontologyManager = manager;\n" +
			"\t\tthis.ontologyID = ontologyID;\n\t};",
			"/**\n\t* Replica constructor\n\t* \n" +
			"\t* Beware: replication fails without a default construcutor of an IShardObject!\n" +
			"\t*/\n\tpublic AbstractOWLReplicaOntology() {\n\t\tsuper();\n\t}\n"};
	
	private String[] constructors = DEFAULT_CONSTRUCTORS;

	@Override
	public void buildSharedOWLOntologyConstructors(StringBuilder sb, int indentation) {
		// FIXME static indentation, comments
		for(String constructor : constructors) {
			indent(sb, indentation);
			sb.append(constructor);
			sb.append(NEWLINE);
			sb.append(NEWLINE);
		}
	}

	public String[] getConstructors() {
		return constructors;
	}

	public void setConstructors(String[] constructors) {
		this.constructors = constructors;
	}

}
