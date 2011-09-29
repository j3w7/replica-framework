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

import static de.fzi.aoide.replica.build.Constants.*;
import static de.fzi.aoide.replica.build.OWLReplicaOntologyStructureBuilder.indent;

public class StaticOWLReplicaOntologyFieldsBuilder implements
		OWLReplicaOntologyFieldsBuilder {
	
	public static final String[] DEFAULT_FIELDS = {
		"private static final long serialVersionUID = "+generateSerialVersionUID()+"L",
		"protected org.semanticweb.owlapi.model.OWLMutableOntology ontology",
		"protected transient org.semanticweb.owlapi.model.OWLOntologyManager ontologyManager",
		"protected org.semanticweb.owlapi.model.OWLOntologyID ontologyID"
	};
	
	private String[] fields = DEFAULT_FIELDS;

	@Override
	public void buildSharedOWLOntologyFields(StringBuilder sb, int indentation) {
		for(String field : fields) {			
			indent(sb, indentation);
			sb.append(field);
			sb.append(ENDLINE);
			sb.append(NEWLINE);
		}
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}
	
	private static long generateSerialVersionUID() {
		return (long) Math.abs(10000000000L * Math.random());
	}

}
