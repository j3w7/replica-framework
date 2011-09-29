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

package de.fzi.replica.comm.test;

import java.util.Collections;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;

import de.fzi.replica.OWLReplicaOntology;
import de.fzi.replica.util.OWLOntologyToOWLReplicaOntologyCopier;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 07.02.2011
 *
 */
public class SharedOntologyTest extends AbstractCommTestCase {
	
	private static final String A = "clientA";
	private static final String B = "clientB";

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		createServer();
		createServerConnection(createConnectionPropertiesServer());
		startServer(getConnectionOfServer());
		createClients(A, B);
		createClientConnection(A, createConnectionPropertiesClient(A));
		createClientConnection(B, createConnectionPropertiesClient(B));
		connectAllClients();
	}
	
	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		// TODO disconnect
	}

	@Test
	public void testAddEmptyOWLReplicaOntology()
			throws SharedObjectAddException, InterruptedException, OWLOntologyCreationException {
		OWLOntologyID ontoID =  new OWLOntologyID(IRI.create("replica://mySharedOntology/"));
		ID sharedObjID = IDFactory.getDefault().createGUID();
		
		// Create and add an empty OWLReplicaOntology
		OWLReplicaOntology sharedOnto = createOWLReplicaOntology(ontoID);
		getConnectionOfClient(A).getSharedObjectContainer().getSharedObjectManager().
			addSharedObject(sharedObjID, (ISharedObject) sharedOnto, null);
		
		// Wait for the object to be added
		Thread.sleep(1000);
		
		// B should get a reference to the replica now
		OWLReplicaOntology replica = (OWLReplicaOntology) getConnectionOfClient(B).
			getSharedObjectContainer().getSharedObjectManager().getSharedObject(sharedObjID);
		
		assertNotNull(replica);
		assertNotSame(sharedOnto, replica);
		assertEquals(sharedOnto.getOntologyID(), replica.getOntologyID());
	}
	
	@Test
	public void testAddEmptyOWLReplicaOntologyAndFill()
			throws SharedObjectAddException, OWLOntologyCreationException, InterruptedException {
		OWLOntologyID ontoID =  new OWLOntologyID(IRI.create("replica://mySharedOntology/"));
		ID sharedObjID = IDFactory.getDefault().createGUID();
		
		// Create and add an empty SharedOWLOntology
		OWLReplicaOntology sharedOnto = createOWLReplicaOntology(ontoID);
		getConnectionOfClient(A).getSharedObjectContainer().getSharedObjectManager().
			addSharedObject(sharedObjID, (ISharedObject) sharedOnto, null);
		
		// Wait for the object to be added
		Thread.sleep(1000);
		
		// B should get a reference to the replica now
		OWLReplicaOntology replica = (OWLReplicaOntology) getConnectionOfClient(B).
			getSharedObjectContainer().getSharedObjectManager().getSharedObject(sharedObjID);
		assertNotNull(replica);
		assertNotSame(sharedOnto, replica);
		
		// Load the pizza ontology and copy all axioms into our shared ontology
		IRI pizzaOntologyIRI = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
		OWLOntology pizzaOnto = OWLManager.createOWLOntologyManager().loadOntology(pizzaOntologyIRI);
		new OWLOntologyToOWLReplicaOntologyCopier().copy(Collections.singleton(pizzaOnto), sharedOnto);
		
		// Wait for change promotion
		Thread.sleep(2000);
		assertNotSame(pizzaOnto, sharedOnto);
		assertEquals(pizzaOnto.getAxioms(), sharedOnto.getAxioms());
		assertEquals(pizzaOnto.getAxioms(), replica.getAxioms());
	}
	
}
