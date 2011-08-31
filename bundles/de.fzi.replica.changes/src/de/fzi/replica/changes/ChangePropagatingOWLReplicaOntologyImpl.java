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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.sharedobject.SharedObjectMsg;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.OWLReplicaOntologyImpl;

public class ChangePropagatingOWLReplicaOntologyImpl extends
		OWLReplicaOntologyImpl implements ChangePropagatingOWLReplicaOntology {
	
	private static final long serialVersionUID = -1751883845898315523L;
	
	private transient OWLReplicaOntologyChangeManager changeManager =
		new OWLReplicaOntologyChangeManagerImpl();

	/**
	 * Primary Constructor
	 * 
	 * @param manager
	 * @param ontologyID
	 */
	public ChangePropagatingOWLReplicaOntologyImpl(OWLOntologyManager manager,
			OWLOntologyID ontologyID) {
		super(manager, ontologyID);
	}
	
	/**
	 * Replica Constructor
	 */
	public ChangePropagatingOWLReplicaOntologyImpl() {
		super();
	}

	@Override
	public OWLReplicaOntologyChangeManager getOWLReplicaOntologyChangeManager() {
		return changeManager;
	}
	
	@Override
	public List<OWLOntologyChange> applyChange(OWLOntologyChange change) {
//		final SharedObjectMsg msg = SharedObjectMsg.createMsg("applyChanges", change);
//		try {
//			// this works only with a privileged aspect - not anymore because of abstract workaround!
//			sendSharedObjectMsgTo(null, msg);
////			msgReceived = true;
//			//System.out.println("SharedObjectMsg sent");
//		} catch (IOException e) {
//			System.out.println("Sending SharedObjectMsg failed");
//			e.printStackTrace();
//		}
		// TODO
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		changes.add(change);
		return applyChanges(changes);
	}
	
	@Override
	public List<OWLOntologyChange> applyChanges(List<OWLOntologyChange> changes) {
		// Build a Change object to represent the change and pack some information
		OWLReplicaOntologyChange changeSet = new OWLReplicaOntologyChangeImpl(
				changes, getLocalContainerID(), System.currentTimeMillis(), "some comment");
		ID analzyerID = null; // TODO
		final SharedObjectMsg msg = SharedObjectMsg.createMsg("analyze", changeSet);
		try {
			sendSharedObjectMsgTo(analzyerID, msg);
		} catch (IOException e) {
			System.out.println("Sending SharedObjectMsg failed");
			e.printStackTrace();
		}
		for(OWLOntologyChange change : changes) {
			change.setOntology(ontology);
		}
		if(calledByOntologyManager()) {			
			return ontology.applyChanges(changes);
		}
		return ontology.getOWLOntologyManager().applyChanges(changes);
	}
	
	protected List<OWLOntologyChange> applyPropagatedChange(OWLReplicaOntologyChange changeSet) {
		List<OWLOntologyChange> changes = changeSet.getChanges();
		for(OWLOntologyChange change : changes) {
			change.setOntology(ontology);
		}
		return ontology.getOWLOntologyManager().applyChanges(changes);
	}
	
	protected void analyze(OWLReplicaOntologyChange change) {
//		System.out.println("ChangePropagatingOWLReplicaOntologyImpl.analyze("+change+")");
		OWLReplicaOntologyChangeAnalysis analysis = changeManager.
			getOWLReplicaOntologyChangeAnalyzer().analyze(change);
		ID propagatorID = null; // TODO
		final SharedObjectMsg msg = SharedObjectMsg.createMsg("propagate", analysis);
		try {
			sendSharedObjectMsgTo(propagatorID, msg);
		} catch (IOException e) {
			System.out.println("Sending SharedObjectMsg failed");
			e.printStackTrace();
		}
	}
	
	protected void propagate(OWLReplicaOntologyChangeAnalysis analysis) {
//		System.out.println("ChangePropagatingOWLReplicaOntologyImpl.propagate("+analysis+")");
		changeManager.getOWLReplicaOntologyChangePropagator().
			propagate(this, analysis, null);
	}
	
	/**
	 * The purpose of this override is to provide the BaseSharedObject message
	 * send method to OWLReplicaOntologyChangePropagatorImpl and hide the methodd
	 * behind the OWLReplicaOntology interface and its sub interfaces.
	 */
	@Override
	protected void sendSharedObjectMsgTo(ID toID, SharedObjectMsg msg) throws IOException {
		super.sendSharedObjectMsgTo(toID, msg);
	}

}
