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

package de.fzi.aoide.replica;



/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.1, 03.02.2011
 * 
 * @deprecated Implementing change interception in OWLReplicaOntologyImpl is
 * 	a lot easier. Change management now in a separate bundle, see
 * 	de.fzi.aoide.replica.changes
 *
 */
public privileged aspect OWLReplicaOntolgyChangeInterception {
	
//	public interface OntoChangeable { }
//	
//	public void OntoChangeable.setOntology(OWLOntology onto) {
//		System.out.println("blub");
////		this.ont = onto;
//	}
//	
//	declare parents: org.semanticweb.owlapi.model.OWLOntologyChange implements OntoChangeable;
//	
//	before(AbstractOWLReplicaOntology onto, OWLOntologyChange change):
//		execution(public List<OWLOntologyChange> AbstractOWLReplicaOntology.applyChange(OWLOntologyChange))
//		&& target(onto) 
//		&& args(change) {
//		System.out.println("SETTING NEW ONTO");
////		change.ont = onto.ontology;
//		change.setOntology((OWLOntology) onto.ontology);
//		System.out.println("NEW ONTO SET");
//	}
	
//	List<OWLOntologyChange> around(OWLReplicaOntologyImpl sharedOnto):
//			execution(@ProxyMethod @PropagatingMethod * *(..)) && target(sharedOnto) {
//		System.out.println("ASPECT CHANGE BROADCAST");
//		OWLOntologyManagerImpl manager = (OWLOntologyManagerImpl) sharedOnto.ontologyManager;
////		System.out.println("manager: "+manager);
////		System.out.println("thisJoinPoint.getArgs()[0]="+thisJoinPoint.getArgs()[0]);
//		
//		System.out.println(sharedOnto.getLocalContainerID()+" "+
//				thisJoinPoint.getSignature().getName()+"("+thisJoinPoint.getArgs()[0]+")");
//		if(!msgReceived) {
//			final SharedObjectMsg msg = SharedObjectMsg.createMsg(thisJoinPoint.getSignature().getName(),
//					thisJoinPoint.getArgs()[0]);
////			final SharedObjectMsg msg = SharedObjectMsg.createMsg("b1", change);
//			try {
//				// Send change to everyone
//				sendSharedObjectMsgTo(null, msg);
//				msgReceived = true;
//			} catch (IOException e) {
//				System.out.println("Sending SharedObjectMsg failed");
//				e.printStackTrace();
//			}
//		}
//		
//		if(OWLOntologyManager.class.equals(thisJoinPoint.getSourceLocation().getWithinType())) {
//			System.out.println("called from OWLOntologyManager");
//			return ontology.applyChange(thisJoinPoint.getArgs()[0]);
//		} else {
//			System.out.println("call from other class");
//			return ontology.getOWLOntologyManager().applyChange(thisJoinPoint.getArgs()[0]);
//		}
//		
//		System.out.println("thisJoinPoint.getSourceLocation().getWithinType()="+thisJoinPoint.getSourceLocation().getWithinType());
//		System.out.println("manager.test() calling...");
//		manager.test();
//		System.out.println("manager.test() called");
//		
//		return null;
////		manager.broadcastChanges();
//	}
	
//	public void OWLOntologyChange.bla() {
//		System.out.println("change.bla()");
//	}
	
//	List<OWLOntologyChange> around(AbstractOWLReplicaOntology onto,
//			OWLOntologyChange change):
//		execution(public List<OWLOntologyChange> AbstractOWLReplicaOntology.applyChange(OWLOntologyChange))
//		&& target(onto) 
//		&& args(change) {
//		System.out.println("applyChange()");
//		
//		// Send change to everyone
//		if(!onto.msgReceived) {
//			final SharedObjectMsg msg = SharedObjectMsg.createMsg(
//					thisJoinPoint.getSignature().getName(),
//					thisJoinPoint.getArgs()[0]);
//			try {
//				onto.sendSharedObjectMsgTo(null, msg);
//				onto.msgReceived = true;
//			} catch(IOException e) {
//				System.out.println("Sending SharedObjectMsg failed");
//				e.printStackTrace();
//			}
//		}
//		
//		// Set change ontology to inner ontology, else applying the change will fail
//		change.setOntology(onto.ontology);
//		if(calledFromOWLOntologyManager()) {
//			System.out.println("called from OWLOntologyManager");
//			return onto.ontology.applyChange(change);
//		} else {
//			System.out.println("called from other class");
//			return onto.ontology.getOWLOntologyManager().applyChange(change);
//		}
//	}
//	
//	List<OWLOntologyChange> around(AbstractOWLReplicaOntology onto,
//			List<OWLOntologyChange> changes):
//		execution(public List<OWLOntologyChange> AbstractOWLReplicaOntology.applyChanges(List<OWLOntologyChange>))
//		&& target(onto) 
//		&& args(changes) {
//		System.out.println("applyChanges()");
//		
//		// Send change to everyone
//		if(!onto.msgReceived) {
//			final SharedObjectMsg msg = SharedObjectMsg.createMsg(
//					thisJoinPoint.getSignature().getName(),
//					thisJoinPoint.getArgs()[0]);
//			try {
//				onto.sendSharedObjectMsgTo(null, msg);
//				onto.msgReceived = true;
//			} catch(IOException e) {
//				System.out.println("Sending SharedObjectMsg failed");
//				e.printStackTrace();
//			}
//		}
//		
//		// Set change ontology to inner ontology, else applying the change will fail
//		for(OWLOntologyChange change : changes) {
//			change.setOntology(onto.ontology);			
//		}
//		if(calledFromOWLOntologyManager()) {
//			System.out.println("called from OWLOntologyManager");
//			return onto.ontology.applyChanges(changes);
//		} else {
//			System.out.println("called from other class");
//			return onto.ontology.getOWLOntologyManager().applyChanges(changes);
//		}
//	}
//	
//	private boolean calledFromOWLOntologyManager() {
//		for(StackTraceElement e : new Throwable().getStackTrace()) {
//			if(e.getClassName().contains("OWLOntologyManager") &&
//					// applyChange directs calls to applyChanges so this is sufficient
//					e.getMethodName().equals("applyChanges")) {
//				return true;
//			}
//		}
//		return false;
//	}
	
//	
//	@SuppressWarnings("unchecked")
//	List<OWLOntologyChange> around(AbstractOWLReplicaOntology onto):
////		execution(public List<OWLOntologyChange> applyChanges(List<OWLOntologyChange>))
//		execution(public List<OWLOntologyChange> AbstractOWLReplicaOntology.applyChanges(List<OWLOntologyChange>))
//		&& target(onto)
//		&& if(thisJoinPoint.getSourceLocation().getWithinType() != AbstractOWLReplicaOntology.class) {
//		System.out.println("applyChanges()");
//		
//		if(!msgReceived) {
//			final SharedObjectMsg msg = SharedObjectMsg.createMsg(
//					thisJoinPoint.getSignature().getName(),
//					thisJoinPoint.getArgs()[0]);
//			try {
//				System.out.println("Sending SharedObjectMsg");
//				// Send change to everyone
//				onto.sendSharedObjectMsgTo(null, msg);
//				msgReceived = true;
//			} catch(IOException e) {
//				System.out.println("Sending SharedObjectMsg failed");
//				e.printStackTrace();
//			}
//		}
//		
//		System.out.println("thisJoinPoint.getSourceLocation().getWithinType()="+
//				thisJoinPoint.getSourceLocation().getWithinType());
//		
//		if(OWLOntologyManager.class.equals(thisJoinPoint.getSourceLocation().getWithinType())) {
//			System.out.println("called from OWLOntologyManager");
//			return onto.ontology.applyChanges((List<OWLOntologyChange>) thisJoinPoint.getArgs()[0]);
//		} else {
//			System.out.println("called from other class");
//			return onto.ontology.getOWLOntologyManager().applyChanges(
//					(List<OWLOntologyChange>) thisJoinPoint.getArgs()[0]);
//		}
//	}
	
//	private boolean OWLReplicaOntologyImpl.msgReceived = false; // development...
//	
//	private boolean msgReceived = false; // development...
//	
//	private ID analyzerID = IDFactory.getDefault().createStringID("");
//	
////	To Analyzer directing method
//	before(OWLReplicaOntologyImpl sharedOnto): execution(@ProxyMethod @PropagatingMethod * *(..)) && target(sharedOnto) {
//		System.out.println(thisJoinPoint.getSignature().getName()+"() invoked, promoting to other peers...");
//		System.out.println("> arguments of "+thisJoinPoint.getSignature().getName()+"(): "+thisJoinPoint.getArgs());
//		// Build a Change object to represent the change and pack some information
//		OWLReplicaOntologyChange change = new OWLReplicaOntologyChangeImpl(
//				(List<OWLOntologyChange>) thisJoinPoint.getArgs()[0], sharedOnto.getLocalContainerID(), 5, "comment");
//		ID analzyerID = null; // TODO
//		final SharedObjectMsg msg = SharedObjectMsg.createMsg("analyze", change);
//		try {
//			// this works only with a privileged aspect - not anymore because of abstract workaround!
//			sharedOnto.sendSharedObjectMsgTo(analzyerID, msg);
//			msgReceived = true;
//			//System.out.println("SharedObjectMsg sent");
//		} catch (IOException e) {
//			System.out.println("Sending SharedObjectMsg failed");
//			e.printStackTrace();
//		}
//	}
//
//	Original method
//	List<OWLOntologyChange> around(OWLReplicaOntologyImpl onto):
//		execution(@ProxyMethod @PropagatingMethod * * (..))
//		&& target(onto) {
//		System.out.println(thisJoinPoint.getSignature().getName()+"() invoked, promoting to other peers...");
//		//System.out.println("> arguments of "+thisJoinPoint.getSignature().getName()+"(): "+thisJointPoint.getSignature().);
//		if(!msgReceived) {
//			final SharedObjectMsg msg = SharedObjectMsg.createMsg(thisJoinPoint.getSignature().getName(), thisJoinPoint.getArgs());
//			try {
//				// this works only with a privileged aspect - not anymore because of abstract workaround!
//				onto.sendSharedObjectMsgTo(null, msg);
//				msgReceived = true;
//				//System.out.println("SharedObjectMsg sent");
//			} catch (IOException e) {
//				System.out.println("Sending SharedObjectMsg failed");
//				e.printStackTrace();
//			}
//		}
//		if(OWLOntologyManager.class.equals(thisJoinPoint.getSourceLocation().getWithinType())) {
//			System.out.println("called from OWLOntologyManager");
//			return onto.ontology.applyChange((OWLOntologyChange) thisJoinPoint.getArgs()[0]);
//		} else {
//			System.out.println("called from other class");
//			return onto.ontology.getOWLOntologyManager().applyChange(
//					(OWLOntologyChange) thisJoinPoint.getArgs()[0]);
//		}
//	}
//	
////	Uses a local Propagator
////	before(OWLReplicaOntologyImpl sharedOnto): execution(@ProxyMethod @PropagatingMethod * *(..)) && target(sharedOnto) {
////		System.out.println(thisJoinPoint.getSignature().getName()+"() invoked, directing to ChangePropagator...");
////		if(!msgReceived) {
////			OWLReplicaOntologyChangePropagation propagation = 
////				new OWLReplicaOntologyChangePropagationImpl(
////						thisJoinPoint.getSignature().getName(), // Method name
////						thisJoinPoint.getArgs(), // Arguments
////						null, // Target peers
////						sharedOnto); // Ontology reference
////			sharedOnto.getChangePropagator().propagate(propagation);
////			msgReceived = true;
////		}
////	}
	
}