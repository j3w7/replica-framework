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

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import de.fzi.replica.OWLReplicaOntology;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.0, 18.07.2011
 * 
 */
public class OWLReplicaOntologyModificationTestCase extends
		AbstractOWLReplicaOntologyTestCase {

	// Client IDs
	private static final String A = "clientA";
	private static final String B = "clientB";

	private OWLReplicaOntology primary;
	private OWLReplicaOntology replica;
	private ID sharedObjID;

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
		removeAllClients();
		removeServer();
	}

	@Test
	public void testModifiyPrimary() throws SharedObjectAddException,
			InterruptedException, OWLOntologyCreationException {
		addPrimary();
		Thread.sleep(1000);
		retrieveReplica(sharedObjID);
		assertNotNull(primary);
		assertNotNull(replica);
		assertNotSame(primary, replica);

		OWLDataFactory fac = primary.getOWLOntologyManager()
				.getOWLDataFactory();
		OWLClass a = fac.getOWLClass(IRI.create("A"));
		OWLClass b = fac.getOWLClass(IRI.create("B"));
		OWLAxiom axiom = fac.getOWLSubClassOfAxiom(a, b);
		primary.getOWLOntologyManager().applyChange(
				new AddAxiom(primary, axiom));

		// Wait until the change has been propagated
		Thread.sleep(1000);
		assertEquals(primary.getAxioms(), replica.getAxioms());
	}

	@Test
	public void testModifiyPrimaryGetReplicaAfterwards()
			throws SharedObjectAddException, InterruptedException,
			OWLOntologyCreationException {
		addPrimary();
		assertNotNull(primary);

		OWLDataFactory fac = primary.getOWLOntologyManager()
				.getOWLDataFactory();
		OWLClass a = fac.getOWLClass(IRI.create("A"));
		OWLClass b = fac.getOWLClass(IRI.create("B"));
		OWLAxiom axiom = fac.getOWLSubClassOfAxiom(a, b);
		primary.getOWLOntologyManager().applyChange(
				new AddAxiom(primary, axiom));

		// Wait until the change has been propagated
		Thread.sleep(1000);
		retrieveReplica(sharedObjID);
		assertNotNull(replica);
		Thread.sleep(1000);
		assertEquals(primary.getAxioms(), replica.getAxioms());
	}

	@Test
	public void testModifiyReplica() throws SharedObjectAddException,
			InterruptedException, OWLOntologyCreationException {
		addPrimary();
		Thread.sleep(1000);
		retrieveReplica(sharedObjID);
		assertNotNull(replica);
		assertNotSame(primary, replica);

		OWLDataFactory fac = replica.getOWLOntologyManager()
				.getOWLDataFactory();
		OWLClass a = fac.getOWLClass(IRI.create("A"));
		OWLClass b = fac.getOWLClass(IRI.create("B"));
		OWLAxiom axiom = fac.getOWLSubClassOfAxiom(a, b);
		replica.getOWLOntologyManager().applyChange(
				new AddAxiom(replica, axiom));

		// Wait until the change has been propagated
		Thread.sleep(1000);
		assertEquals(replica.getAxioms(), primary.getAxioms());
	}

	protected ID addPrimary() throws OWLOntologyCreationException,
			SharedObjectAddException {
		// Scenario: client A adds an empty SharedOWLOntology, B retrieves the
		// replica.
		sharedObjID = IDFactory.getDefault().createGUID();
		primary = createOWLReplicaOntology(getOntologyID());
		getClientSharedObjectManager(A).addSharedObject(sharedObjID,
				(ISharedObject) primary, null);
		return sharedObjID;
	}

	protected void retrieveReplica(ID sharedObjID) {
		replica = (OWLReplicaOntology) getClientSharedObjectManager(B)
				.getSharedObject(sharedObjID);
	}

}
