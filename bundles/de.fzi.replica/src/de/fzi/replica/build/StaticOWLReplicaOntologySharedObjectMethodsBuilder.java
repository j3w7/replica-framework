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

import static de.fzi.replica.build.Constants.ENDLINE;
import static de.fzi.replica.build.Constants.NEWLINE;
import static de.fzi.replica.build.OWLReplicaOntologyStructureBuilder.indent;

public class StaticOWLReplicaOntologySharedObjectMethodsBuilder implements SharedObjectMethodsBuilder {
	
	/*
	 * Define some delegates
	 */
	public static final String[] DEFAULT_METHODS = {
		"protected void initialize() throws org.eclipse.ecf.core.sharedobject.SharedObjectInitException { super.initialize(); sharedOWLOntologyInitialize(); }",
		"protected abstract void sharedOWLOntologyInitialize() throws org.eclipse.ecf.core.sharedobject.SharedObjectInitException;",
		
		"protected boolean handleSharedObjectMsg(org.eclipse.ecf.core.sharedobject.SharedObjectMsg msg) { return sharedOWLOntologyHandleSharedObjectMsg(msg); }",
		"protected abstract boolean sharedOWLOntologyHandleSharedObjectMsg(org.eclipse.ecf.core.sharedobject.SharedObjectMsg msg)",
		
		// workaround for an aspectj weaving problem: restricted access to BaseSharedObject in SharedOWLOntologyProxy aspect
		"protected void sendSharedObjectMsgTo(org.eclipse.ecf.core.identity.ID toID, org.eclipse.ecf.core.sharedobject.SharedObjectMsg msg) throws java.io.IOException { super.sendSharedObjectMsgTo(toID, msg); }"
	};
	
	private String[] methods = DEFAULT_METHODS;

	@Override
	public void buildSharedObjectMethods(StringBuilder sb, int indentation) {
		for(String method : methods) {
			indent(sb, indentation);
			sb.append(method);
			sb.append(ENDLINE);
			sb.append(NEWLINE);
		}
	}

}
