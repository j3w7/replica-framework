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

package de.fzi.replica.dictionary;

import java.util.HashMap;
import java.util.Map;

import de.fzi.replica.fragments.OWLReplicaOntologyFragment;
import de.fzi.replica.fragments.OWLReplicaOntologyFragmentID;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 05.03.2011
 *
 */
public class OWLReplicaOntologyDictionaryImpl implements
		OWLReplicaOntologyDictionary {
	
	Map<OWLReplicaOntologyFragmentID, OWLReplicaOntologyFragmentSignature> dictionary;
	
	public OWLReplicaOntologyDictionaryImpl() {
		dictionary = new HashMap<OWLReplicaOntologyFragmentID,
			OWLReplicaOntologyFragmentSignature>();
	}

	@Override
	public OWLReplicaOntologyFragmentSignature getSignature(
			OWLReplicaOntologyFragmentID fragmentID) {
		return dictionary.get(fragmentID);
	}

	@Override
	public void setSignature(OWLReplicaOntologyFragmentID fragmentID,
			OWLReplicaOntologyFragmentSignature alphabet) {
		dictionary.put(fragmentID, alphabet);
	}

	@Override
	public void addSignatureOf(OWLReplicaOntologyFragment fragment) {
		dictionary.put(fragment.getOWLReplicaOntologyFragmentID(),
				new OWLReplicaOntologyFragmentSignatureImpl(fragment));
	}

}
