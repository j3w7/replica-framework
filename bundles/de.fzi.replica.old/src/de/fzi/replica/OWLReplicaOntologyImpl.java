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

package de.fzi.replica;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.sharedobject.ReplicaSharedObjectDescription;
import org.eclipse.ecf.core.sharedobject.SharedObjectInitException;
import org.eclipse.ecf.core.sharedobject.SharedObjectMsg;
import org.eclipse.ecf.core.sharedobject.events.ISharedObjectActivatedEvent;
import org.eclipse.ecf.core.util.Event;
import org.eclipse.ecf.core.util.IEventProcessor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLMutableOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;

/**
 * This class encapsulates overrides of AbstractSharedOWLOntology methods which
 * are delegate methods of corresponding methods in BaseSharedObject.
 * It handles initialization of shared object instances (primary and replicas)
 * and declares methods for change management.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.2, 04.07.2011
 */
public class OWLReplicaOntologyImpl extends AbstractOWLReplicaOntology {
	
	private static final long serialVersionUID = 7149883204135776078L;
	
	public static final String ONTOLOGY = "ontology";
	public static final String ONTOLOGY_ID = "ontologyID";
	
	/**
	 * Primary Constructor
	 * 
	 * @param manager
	 * @param ontologyID
	 */
	public OWLReplicaOntologyImpl(OWLOntologyManager manager, OWLOntologyID ontologyID) {
		super(manager, ontologyID);
	}
	
	/**
	 * Replica Constructor
	 */
	public OWLReplicaOntologyImpl() {
		super();
	}
	
	@Override
	protected void initialize() throws SharedObjectInitException {
		super.initialize();
		if(isPrimary()) {
			// If primary, then add an event processor that handles activated
			// event by replicating to all current remote containers
			addEventProcessor(new IEventProcessor() {
				public boolean processEvent(Event event) {
					if (event instanceof ISharedObjectActivatedEvent) {
						ISharedObjectActivatedEvent ae = (ISharedObjectActivatedEvent) event;
						if (ae.getActivatedID().equals(getID()) && isConnected()) {
							System.out.println("Primary replicating to remote containers...");
							OWLReplicaOntologyImpl.this.replicateToRemoteContainers(null);
						}
					}
					return false;
				}
			});
			System.out.println("Primary(" + getID()  + ") here on " +
					getLocalContainerID());
			
			// Set up the ontology on primary object
			ontology = new OWLOntologyImpl(ontologyManager, ontologyID);
		} else {
			System.out.println("Replica(" + getID() + ") here on " +
					getLocalContainerID());
			// Set up the ontology on replica object
			// Do not use OWLReplicaManager so we don't get another OWLReplicaInstance
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLOntologyID ontologyID = (OWLOntologyID) getConfig().getProperties().get(ONTOLOGY_ID);
			try {
				ontology = (OWLMutableOntology) man.createOntology(ontologyID);
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
				throw new SharedObjectInitException(e);
			}
		}
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected ReplicaSharedObjectDescription getReplicaDescription(ID receiver) {
		// Put primary ontology ontologyID into properties and include in replica description
		final Map properties = new HashMap();
		properties.put(ONTOLOGY_ID, ontologyID);
		return new ReplicaSharedObjectDescription(this.getClass(), getConfig().
				getSharedObjectID(), getConfig().getHomeContainerID(), properties);
	}
	
	/**
	 * Subclasses need to override this method, otherwise reflection will fail.
	 */
	@Override
	protected boolean handleSharedObjectMsg(SharedObjectMsg msg) {
		try {
			msg.invoke(this);
		} catch (Exception e) {
			System.out.println("Message handler exception: "+e.getMessage());
			throw new RuntimeException(e);
		}
	    // don't let other event processors process this message
		return true;
	}

	@Override
	public OWLOntologyID getOntologyID() {
		/*
		 * Return cached ontologyID if ontology has not been created yet.
		 * Required when the OWLReplicaOntologyFactory tries to create an
		 * instance of OWLReplicaOntology and the ontology object within
		 * has not been created yet.
		 */
		if(ontology != null) {
			return ontology.getOntologyID();
		}
		return ontologyID;
	}
	
	/*
	 * Complex Change Management is now in a separate bundle
	 * see de.fzi.aoide.replica.changes.
	 * 
	 * These implementations provide change replication to every
	 * other node, so working synchronously on a shared ontology
	 * can be achieved without loading the change management bundle.
	 */
	
	@Override
	@ProxyMethod
	@PropagatingMethod
	public List<OWLOntologyChange> applyChange(OWLOntologyChange change) {
		final SharedObjectMsg msg = SharedObjectMsg.createMsg("applyChangeSilent", change);
		try {
			// Send change to everyone
			sendSharedObjectMsgTo(null, msg);
		} catch (IOException e) {
			System.out.println("Sending SharedObjectMsg on "+getLocalContainerID()+" failed");
			e.printStackTrace();
		}
		change.setOntology(ontology);
		if(calledByOntologyManager()) {
			return ontology.applyChange(change);
		}
		return ontology.getOWLOntologyManager().applyChange(change);
	}
	
	public void applyChangeSilent(OWLOntologyChange change) {
		change.setOntology(ontology);
		ontology.getOWLOntologyManager().applyChange(change);
	}
	
	@Override
	@ProxyMethod
	@PropagatingMethod
	public List<OWLOntologyChange> applyChanges(List<OWLOntologyChange> changes) {
		final SharedObjectMsg msg = SharedObjectMsg.createMsg("applyChangesSilent", changes);
		try {
			// Send changes to everyone
			sendSharedObjectMsgTo(null, msg);
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
	
	public void applyChangesSilent(List<OWLOntologyChange> changes) {
		for(OWLOntologyChange change : changes) {
			change.setOntology(ontology);
		}
		ontology.getOWLOntologyManager().applyChanges(changes);
	}
	
	protected boolean calledByOntologyManager() {
		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		for(StackTraceElement e : stackTrace) {
			if(e.getClassName().contains("OWLOntologyManager")) {
				return true;
			}
		}
		return false;
	}
	
}
