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


public class StaticOWLOntologyMethodsBuilder implements OWLOntologyMethodsBuilder {
	
	public static final String[] METHODS = {
		"@Override\n\tpublic int compareTo(org.semanticweb.owlapi.model.OWLObject o) {\n"+
		"\t\treturn ontology.compareTo(o);\n\t}"
	};
	
	private String[] methods = METHODS;

	@Override
	public void buildOWLOntologyMethods(StringBuilder sb, int indentation) {
		for(String method: methods) {
			for(int i = 0; i < indentation; i++) {
				sb.append(TAB);
			}
			sb.append(method);
			sb.append(NEWLINE);
		}
	}

}
