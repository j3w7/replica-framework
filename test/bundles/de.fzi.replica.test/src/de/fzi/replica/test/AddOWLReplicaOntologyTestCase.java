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

package de.fzi.replica.test;

import java.util.Collections;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.IIDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import de.fzi.replica.OWLReplicaOntology;
import de.fzi.replica.util.OWLOntologyToOWLReplicaOntologyCopier;


/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 18.04.2011
 */
public class AddOWLReplicaOntologyTestCase extends AbstractOWLReplicaOntologyTestCase {
	
	protected IIDFactory idFactory = IDFactory.getDefault();
	
	// just for convenience
	private static final String A = "clientA";
	private static final String B = "clientB";
	
	private static final ID SO_ID = IDFactory.getDefault().createGUID();
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		createServer();
		createClients(A, B);
		connectAllClientsTo(getServerIdentity());
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		getClientSharedObjectManager(A).removeSharedObject(SO_ID);
		getClientSharedObjectManager(B).removeSharedObject(SO_ID);
		getServerSharedObjectManager().removeSharedObject(SO_ID);
		removeAllClients();
		removeServer();
	}
	
	@Test
	public void testAddEmptyOWLReplicaOntology()
			throws SharedObjectAddException, InterruptedException, OWLOntologyCreationException {
		// Create and add an empty OWLReplicaOntology
		OWLOntology primary = createOWLReplicaOntology(getOntologyID());
		getClientSharedObjectManager(A).addSharedObject(SO_ID, (ISharedObject) primary, null);
		
		// Wait for the object to be added
		Thread.sleep(1000);
		
		// B should get a reference to the replica now
		OWLOntology replica = (OWLOntology) getClientSharedObjectManager(B).getSharedObject(SO_ID);
		
		assertNotNull(replica);
		assertNotSame(primary, replica);
		assertEquals(primary.getOntologyID(), replica.getOntologyID());
	}
	
	@Test
	public void testAddEmptyOWLReplicaOntologyAndFill()
			throws SharedObjectAddException, OWLOntologyCreationException, InterruptedException {
		// Create and add an empty SharedOWLOntology
		OWLOntology primary = createOWLReplicaOntology(getOntologyID());
		getClientSharedObjectManager(A).addSharedObject(SO_ID, (ISharedObject) primary, null);
		
		// Wait for the object to be added
		Thread.sleep(1000);
		
		// B should get a reference to the replica now
		OWLOntology replica = (OWLOntology) getClientSharedObjectManager(B).getSharedObject(SO_ID);
		assertNotNull(replica);
		assertNotSame(primary, replica);
		
		// Load the pizza ontology and copy all axioms into our shared ontology
		IRI pizzaOntologyIRI = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
		OWLOntology pizzaOnto = OWLManager.createOWLOntologyManager().loadOntology(pizzaOntologyIRI);
		new OWLOntologyToOWLReplicaOntologyCopier().copy(Collections.singleton(pizzaOnto), (OWLReplicaOntology) primary);
		
		// Wait for change propagation
		Thread.sleep(2000);
		assertNotSame(pizzaOnto, primary);
		assertEquals(pizzaOnto.getAxioms(), primary.getAxioms());
		assertEquals(pizzaOnto.getAxioms(), replica.getAxioms());
	}
	
	@Test
	public void testAddEmptyOWLReplicaOntologyAndFillBeforeReplicaRetrieval()
			throws SharedObjectAddException, OWLOntologyCreationException, InterruptedException {
		// Create and add an empty SharedOWLOntology
		OWLOntology primary = createOWLReplicaOntology(getOntologyID());
		getClientSharedObjectManager(A).addSharedObject(SO_ID, (ISharedObject) primary, null);
		
		// Wait for the object to be added
		Thread.sleep(1000);
		
		// Load the pizza ontology and copy all axioms into our shared ontology
		IRI pizzaOntologyIRI = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
		OWLOntology pizzaOnto = OWLManager.createOWLOntologyManager().loadOntology(pizzaOntologyIRI);
		new OWLOntologyToOWLReplicaOntologyCopier().copy(Collections.singleton(pizzaOnto), (OWLReplicaOntology) primary);
		// Wait for change propagation
		Thread.sleep(2000);
		
		// Now get the replica on client B
		OWLOntology replica = (OWLOntology) getClientSharedObjectManager(B).getSharedObject(SO_ID);
		assertNotNull(replica);
		assertNotSame(primary, replica);
		
		assertNotSame(pizzaOnto, primary);
		assertEquals(pizzaOnto.getAxioms(), primary.getAxioms());
		assertEquals(pizzaOnto.getAxioms(), replica.getAxioms());
	}
	
}
