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

public class StaticOWLReplicaOntologyPackageyDeclarationBuilder implements
		OWLReplicaOntologyPackageyDeclarationBuilder {
	
	public static final String DEFAULT_PACKAGE = "package de.fzi.aoide.replica";
	
	private String pkg = DEFAULT_PACKAGE; 

	@Override
	public void buildSharedOWLOntologyPackageDeclaration(StringBuilder sb) {
		sb.append(pkg);
		sb.append(ENDLINE);
		sb.append(NEWLINE);
	}
	
	public String getPackage() {
		return pkg;
	}

	public void setPackage(String pkg) {
		this.pkg = pkg;
	}

}
