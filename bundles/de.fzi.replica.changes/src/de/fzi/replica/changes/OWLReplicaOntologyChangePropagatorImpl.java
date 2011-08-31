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

import java.io.IOException;
import java.io.Serializable;

import org.eclipse.ecf.core.sharedobject.SharedObjectMsg;


public class OWLReplicaOntologyChangePropagatorImpl implements
		OWLReplicaOntologyChangePropagator, Serializable {
	
	private static final long serialVersionUID = 9644107342023043L;

	@Override
	public <T extends ChangePropagatingOWLReplicaOntology> void propagate(
			T replicaOntology,
			OWLReplicaOntologyChangeAnalysis analysis,
			OWLReplicaOntologyPropagationStrategy strategy) {
//		System.out.println("OWLReplicaOntologyChangePropagatorImpl.propagate(..)");
		// TODO design
		ChangePropagatingOWLReplicaOntologyImpl sharedOnto = null;
		if(ChangePropagatingOWLReplicaOntologyImpl.class.equals(replicaOntology.getClass())) {
			sharedOnto = (ChangePropagatingOWLReplicaOntologyImpl) replicaOntology;
		} else if(ChangePropagatingOWLReplicaOntologyFragmentImpl.class.equals(replicaOntology.getClass())) {
			sharedOnto = (ChangePropagatingOWLReplicaOntologyImpl) replicaOntology;
		} else {
			throw new IllegalArgumentException(
					"Cannot deal with this OWLReplicaOntology implementation: "+
					replicaOntology.getClass());
		}
		
		final SharedObjectMsg msg = SharedObjectMsg.createMsg("applyPropagatedChange", analysis.getOWLReplicaOntologyChange());
		try {
			sharedOnto.sendSharedObjectMsgTo(null, msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
