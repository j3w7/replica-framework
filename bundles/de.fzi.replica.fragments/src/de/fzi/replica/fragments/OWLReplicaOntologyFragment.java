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

import de.fzi.replica.OWLReplicaOntology;


/**
 * A fragment is a part of an Ontology. Within the ReplicaOntology
 * context a fragment is indistinguishable from a shared Ontology.
 * A shared ontology can be transparently fragmented.
 * The ID of a shared object is not sufficient to identify a fragment.
 * A fragment is therefore identified by a {@link FragmentID}. 
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 08.02.2011
 *
 */
public interface OWLReplicaOntologyFragment extends OWLReplicaOntology {
	
	OWLReplicaOntologyFragmentID getOWLReplicaOntologyFragmentID();
	
	ID getSharedObjectID();
	
}
