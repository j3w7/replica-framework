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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import de.fzi.replica.OWLReplicaOntology;

/**
 * Tests reasoning with a replicated ontology.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.2, 29.03.2011
 */
public class OWLReplicaOntologyReasoningTest extends AbstractOWLReplicaOntologyTestCase {
	
	// Client IDs
	private static final String A = "clientA";
	private static final String B = "clientB";
	
	protected OWLOntology primary;
	protected OWLOntology replica;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		createServer();
		createClients(A, B);
		connectAllClientsTo(getServerIdentity());
		
		// Client A adds an empty SharedOWLOntology, B retrieves the replica.
		ID sharedObjID = IDFactory.getDefault().createGUID();
		primary = createOWLReplicaOntology(getOntologyID());
		getClientSharedObjectManager(A).addSharedObject(sharedObjID, (ISharedObject) primary, null);
		// Wait for the shared object to become active
		Thread.sleep(1000);
		replica = (OWLReplicaOntology) getClientSharedObjectManager(B).getSharedObject(sharedObjID);
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		removeAllClients();
		removeServer();
	}
	
	/**
	 * Scenario: A⊒B⊒C => A⊒C
	 * 
	 * @throws SharedObjectAddException
	 */
	@Test
	public void testTransitiveRelation() throws SharedObjectAddException {		
		assertNotNull(replica);
		assertNotSame(primary, replica);
		
		OWLDataFactory fac = primary.getOWLOntologyManager().getOWLDataFactory();
		OWLClass a = fac.getOWLClass(IRI.create("A"));
		OWLClass b = fac.getOWLClass(IRI.create("B"));
		OWLClass c = fac.getOWLClass(IRI.create("C"));
		List<OWLOntologyChange> addAxioms = new LinkedList<OWLOntologyChange>();
		addAxioms.add(new AddAxiom(primary, fac.getOWLSubClassOfAxiom(b, a)));
		addAxioms.add(new AddAxiom(primary, fac.getOWLSubClassOfAxiom(c, b)));
		primary.getOWLOntologyManager().applyChanges(addAxioms);
		
		// Wait for changes to be analyzed and propagated
		sleep(1000);
		
		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
		OWLReasoner reasoner0 = reasonerFactory.createReasoner(primary);
//		reasoner0.prepareReasoner();
		assertTrue(reasoner0.isConsistent());
		assertTrue(reasoner0.isEntailed(primary.getOWLOntologyManager().getOWLDataFactory().getOWLSubClassOfAxiom(c, a)));
		
		OWLReasoner reasoner1 = reasonerFactory.createReasoner(replica);
//		reasoner1.prepareReasoner();
		assertTrue(reasoner1.isConsistent());
		assertTrue(reasoner1.isEntailed(replica.getOWLOntologyManager().getOWLDataFactory().getOWLSubClassOfAxiom(c, a)));
	}
	
}
