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
import org.eclipse.ecf.core.identity.IIDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 29.03.2011
 */
public class OWLReplicaOntologyChangeListeningTestCase extends AbstractOWLReplicaOntologyTestCase {
	
	protected IIDFactory idFactory = IDFactory.getDefault();
	
	// just for convenience
	private static final String A = "clientA";
	private static final String B = "clientB";
	
	OWLOntology primary;
	OWLOntology replica;
	
	Holder holder0;
	Holder holder1;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		createServer();
		createClients(A, B);
		connectAllClientsTo(getServerIdentity());
		
		// Create and add an empty OWLReplicaOntology
		primary = createOWLReplicaOntology(getOntologyID());
		ID sharedObjID = IDFactory.getDefault().createGUID();
		getClientSharedObjectManager(A).addSharedObject(sharedObjID, (ISharedObject) primary, null);
		
		// Wait for the object to be added
		Thread.sleep(1000);
		
		// B should get a reference to the replica now
		replica = (OWLOntology) getClientSharedObjectManager(B).getSharedObject(sharedObjID);
		
		// Register change listeners
		primary.getOWLOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
			@Override
			public void ontologiesChanged(
					List<? extends OWLOntologyChange> changes)
					throws OWLException {
				holder0.changed = true;
				System.out.println("Primary changes: "+changes);
			}
		});
		replica.getOWLOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
			@Override
			public void ontologiesChanged(
					List<? extends OWLOntologyChange> changes)
					throws OWLException {
				holder1.changed = true;
				System.out.println("Replica changes: "+changes);
			}
		});
		
		holder0 = new Holder();
		holder1 = new Holder();
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		removeAllClients();
		removeServer();
	}
	
	@Test
	public void testAddEmptyOWLReplicaOntologyAndModifyPrimary()
			throws SharedObjectAddException, InterruptedException, OWLOntologyCreationException {
		// Modify primary
		OWLDataFactory fac = primary.getOWLOntologyManager().getOWLDataFactory();
		OWLClass a = fac.getOWLClass(IRI.create("A"));
		OWLClass b = fac.getOWLClass(IRI.create("B"));
		OWLClass c = fac.getOWLClass(IRI.create("C"));
		List<OWLOntologyChange> addAxioms = new LinkedList<OWLOntologyChange>();
		addAxioms.add(new AddAxiom(primary, fac.getOWLSubClassOfAxiom(b, a)));
		addAxioms.add(new AddAxiom(primary, fac.getOWLSubClassOfAxiom(c, b)));
		primary.getOWLOntologyManager().applyChanges(addAxioms);
		
		// Wait for changes to be propagated
		Thread.sleep(1000);
		
		assertTrue(holder0.changed && holder1.changed);
		assertEquals(primary.getAxioms(), replica.getAxioms());
	}
	
	@Test
	public void testAddEmptyOWLReplicaOntologyAndModifyReplica()
			throws SharedObjectAddException, InterruptedException, OWLOntologyCreationException {
		// Modify replica
		OWLDataFactory fac = replica.getOWLOntologyManager().getOWLDataFactory();
		OWLClass a = fac.getOWLClass(IRI.create("A"));
		OWLClass b = fac.getOWLClass(IRI.create("B"));
		OWLClass c = fac.getOWLClass(IRI.create("C"));
		List<OWLOntologyChange> addAxioms = new LinkedList<OWLOntologyChange>();
		addAxioms.add(new AddAxiom(replica, fac.getOWLSubClassOfAxiom(b, a)));
		addAxioms.add(new AddAxiom(replica, fac.getOWLSubClassOfAxiom(c, b)));
		replica.getOWLOntologyManager().applyChanges(addAxioms);
		
		// Wait for changes to be propagated
		Thread.sleep(1000);
		
		assertTrue(holder0.changed && holder1.changed);
		assertEquals(primary.getAxioms(), replica.getAxioms());
	}
	
	class Holder {
		boolean changed = false;
	}
	
}
