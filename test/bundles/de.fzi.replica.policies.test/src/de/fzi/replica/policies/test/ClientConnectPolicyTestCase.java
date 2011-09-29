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

package de.fzi.replica.policies.test;

import java.util.Collections;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.ISharedObjectManager;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import de.fzi.replica.OWLReplicaOntology;
import de.fzi.replica.comm.Connection;
import de.fzi.replica.policies.ClientConnectSynchronizePolicy;
import de.fzi.replica.test.AbstractOWLReplicaOntologyTestCase;
import de.fzi.replica.util.OWLOntologyToOWLReplicaOntologyCopier;

public class ClientConnectPolicyTestCase extends AbstractPolicyTestCase {

	private static final String A = "clientA";
	private static final String B = "clientB";

	@Before
	public void setUp() throws Exception {
		super.setUp();
		createServer();
		createServerConnection(createConnectionPropertiesServer());
		startServer(getConnectionOfServer());
	}
	
	@After
	public void tearDown() throws Exception {
		disconnectAllClients();
		stopAllClients();
		stopServer();
		super.tearDown();
	}
	
	@Test
	public void testApply() throws ContainerConnectException,
			OWLOntologyCreationException, SharedObjectAddException, IDCreateException,
			InterruptedException {
		// Create and apply policy on server side
//		new ClientConnectSynchronizePolicy(getConnectionOfServer()).apply();
		// Create and connect clients
		createClients(A, B);
		createClientConnection(A, createConnectionPropertiesClient(A));
		createClientConnection(B, createConnectionPropertiesClient(B));
		connectAllClients();
		// Create a shared ontology add it on A
		OWLOntologyID ontoID = AbstractOWLReplicaOntologyTestCase.getOntologyID();
		ID sharedObjID = IDFactory.getDefault().createStringID(ontoID.getOntologyIRI().toString());
		OWLReplicaOntology primary = createOWLReplicaOntology(ontoID);
		getConnectionOfClient(A).getSharedObjectContainer().getSharedObjectManager().
			addSharedObject(
					IDFactory.getDefault().createStringID(ontoID.getOntologyIRI().toString()),
					primary,
					null);
		// Add some Axioms
		OWLOntologyManager manager = primary.getOWLOntologyManager();
		OWLDataFactory fac = manager.getOWLDataFactory();
		OWLClass clsA = fac.getOWLClass(IRI.create("A"));
		OWLClass clsB = fac.getOWLClass(IRI.create("B"));
		OWLClass clsC = fac.getOWLClass(IRI.create("C"));
		OWLSubClassOfAxiom ax0 = fac.getOWLSubClassOfAxiom(clsB, clsA);
		OWLSubClassOfAxiom ax1 = fac.getOWLSubClassOfAxiom(clsC, clsB);
		manager.applyChange(new AddAxiom(primary, ax0));
		manager.applyChange(new AddAxiom(primary, ax1));
		Thread.sleep(2000);
		// Now retrieve shared ontology on B
		OWLOntology replica = (OWLOntology) getConnectionOfClient(B).getSharedObjectContainer().
			getSharedObjectManager().getSharedObject(sharedObjID);
		// Wait for synchronization
		Thread.sleep(20000);
		System.out.println("axioms="+replica.getAxioms());
		assertTrue(replica.getAxioms().contains(ax0));
		assertTrue(replica.getAxioms().contains(ax1));
	}
	
	@Test
	public void testAddEmptyOWLReplicaOntologyAndFillBeforeReplicaRetrieval()
			throws SharedObjectAddException, OWLOntologyCreationException, InterruptedException,
			ContainerConnectException {
		// Create and apply policy on server side
		new ClientConnectSynchronizePolicy(getConnectionOfServer()).apply();
		// Create and add an empty SharedOWLOntology
		OWLOntologyID ontoID = AbstractOWLReplicaOntologyTestCase.getOntologyID();
		ID sharedObjID = IDFactory.getDefault().createStringID(ontoID.getOntologyIRI().toString());
		OWLOntology primary = createOWLReplicaOntology(ontoID);
		createClients(A, B);
		createClientConnection(A, createConnectionPropertiesClient(A));
		createClientConnection(B, createConnectionPropertiesClient(B));
		connectClient(A);
//		connectAllClients();
		ISharedObjectManager so_manager = getConnectionOfClient(A).getSharedObjectContainer().getSharedObjectManager();
		so_manager.addSharedObject(sharedObjID, (ISharedObject) primary, null);
		
		// Wait for the object to be added
		Thread.sleep(1000);
		System.out.println("client A - shared object IDs="+so_manager.getSharedObjectIDs());
		ISharedObject sharedObj = so_manager.getSharedObject(sharedObjID);
		System.out.println("sharedObj="+sharedObj);
		
		System.out.println("about to download pizza onto...");
		// Load the pizza ontology and copy all axioms into our shared ontology
		IRI pizzaOntologyIRI = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
		OWLOntology pizzaOnto = OWLManager.createOWLOntologyManager().loadOntology(pizzaOntologyIRI);
		new OWLOntologyToOWLReplicaOntologyCopier().copy(Collections.singleton(pizzaOnto), (OWLReplicaOntology) primary);
		System.out.println("about to connect client B...");
		
		// Now get the replica on client B
		Thread.sleep(1000);
		connectClient(B);
		Thread.sleep(5000);
		if(getConnectionOfClient(B).getSharedObjectContainer().getConnectedID()
				!= null) {
			System.out.println("client B connected");
		} else {
			System.out.println("client B not connected");
		}
		OWLOntology replica = (OWLOntology) getConnectionOfClient(B).getSharedObjectContainer().
			getSharedObjectManager().getSharedObject(sharedObjID);
		System.out.println("Client B - shared object IDs="+getConnectionOfClient(B).getSharedObjectContainer().
				getSharedObjectManager().getSharedObjectIDs());
		System.out.println("Client A - shared object IDs="+so_manager.getSharedObjectIDs());
		
		// See shared object IDs on client A
		System.out.println("Client A - shared object IDs="+getConnectionOfClient(A).
			getSharedObjectContainer().getSharedObjectManager().getSharedObjectIDs());
		// Try to receive shared object on client A
		ISharedObject sharedObject = getConnectionOfClient(A).getSharedObjectContainer().
			getSharedObjectManager().getSharedObject(sharedObjID);
		System.out.println("Client A - sharedObject="+sharedObject);
		// Try to receive shared object on client B
		ISharedObject sharedObject1 = getConnectionOfClient(B).getSharedObjectContainer().
			getSharedObjectManager().getSharedObject(sharedObjID);
		System.out.println("Client B - sharedObject="+sharedObject1);
		
		
		assertNotNull(replica);
		// Wait for change propagation
		Thread.sleep(10000);
		assertNotSame(primary, replica);
		
		assertNotSame(pizzaOnto, primary);
		assertEquals(pizzaOnto.getAxioms(), primary.getAxioms());
		assertEquals(pizzaOnto.getAxioms(), replica.getAxioms());
	}
	
	@Test
	void testBlub() throws OWLOntologyCreationException, ContainerConnectException,
			InterruptedException, SharedObjectAddException {
		// create IDs
		IRI id = IRI.create("http://myTestOntology.org/");
		OWLOntologyID ontoID = new OWLOntologyID(id);
		ID sharedObjectID = IDFactory.getDefault().createStringID(id.toString());
		// create ontology and clients
		OWLReplicaOntology onto = createOWLReplicaOntology(ontoID);
		createClients(A, B);
		Connection conA = createClientConnection(A, createConnectionPropertiesClient(A));
		Connection conB = createClientConnection(B, createConnectionPropertiesClient(B));
		// connect client A and add primary
		connectClient(A); Thread.sleep(1000);
		conA.getSharedObjectContainer().getSharedObjectManager().
			addSharedObject(sharedObjectID, onto, null); Thread.sleep(1000);
		// 
	}

}
