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

package de.fzi.replica.changes;

import org.eclipse.ecf.core.identity.ID;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.fragments.OWLReplicaOntologyFragmentID;

public class ChangePropagatingOWLReplicaOntologyFragmentImpl extends
		ChangePropagatingOWLReplicaOntologyImpl implements
		ChangePropagatingOWLReplicaOntologyFragment {
	
	private static final long serialVersionUID = 3381752720579505418L;

	OWLReplicaOntologyFragmentID fragmentID;
	
	/**
	 * Primary Constructor
	 * 
	 * @param manager
	 * @param ontologyID
	 */
	public ChangePropagatingOWLReplicaOntologyFragmentImpl(OWLOntologyManager manager,
			OWLOntologyID ontologyID) {
		super(manager, ontologyID);
	}
	
	/**
	 * Replica Constructor
	 */
	public ChangePropagatingOWLReplicaOntologyFragmentImpl() {
		super();
	}

	@Override
	public OWLReplicaOntologyFragmentID getOWLReplicaOntologyFragmentID() {
		return fragmentID;
	}

	@Override
	public ID getSharedObjectID() {
		return getID();
	}

}
