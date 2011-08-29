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

package de.fzi.replica.fragments;

import org.eclipse.ecf.core.identity.ID;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.OWLReplicaOntologyImpl;

public class OWLReplicaOntologyFragmentImpl extends OWLReplicaOntologyImpl
		implements OWLReplicaOntologyFragment {

	private static final long serialVersionUID = 2381980547332566127L;
	
	OWLReplicaOntologyFragmentID fragmentID;
	
	/**
	 * Primary Constructor
	 */
	public OWLReplicaOntologyFragmentImpl(OWLOntologyManager manager,
			OWLOntologyID ontologyID) {
		super(manager, ontologyID);
		fragmentID = new OWLReplicaOntologyFragmentID(ontologyID.getOntologyIRI());
	}
	
	/**
	 * Replica Constructor
	 */
	public OWLReplicaOntologyFragmentImpl() {
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
	
	@Override
	public String toString() {
		return "OWLReplicaOntologyFragmentImpl [fragmentID=" + fragmentID + "]";
	}
	
}
