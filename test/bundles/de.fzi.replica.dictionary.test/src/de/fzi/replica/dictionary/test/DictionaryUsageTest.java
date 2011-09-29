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

package de.fzi.replica.dictionary.test;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.junit.After;
import org.junit.Before;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.OWLReplicaManager;
import de.fzi.replica.fragments.OWLReplicaOntologyFragment;
import de.fzi.replica.dictionary.OWLReplicaOntologyDictionary;
import de.fzi.replica.dictionary.OWLReplicaOntologyDictionaryImpl;
import de.fzi.replica.dictionary.OWLReplicaOntologyFragmentSignature;
import de.fzi.replica.test.AbstractOWLReplicaOntologyTestCase;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 05.03.2011
 *
 */
public class DictionaryUsageTest extends AbstractOWLReplicaOntologyTestCase {
	
	// just for convenience
	private static final String A = "clientA";
	private static final String B = "clientB";
	
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
	
	public void testDictionaryLookup() throws OWLOntologyCreationException, SharedObjectAddException {
		OWLReplicaOntologyDictionary dictionary =
			new OWLReplicaOntologyDictionaryImpl();
		
		String f0ID = "replica://ontology0/fragment0";
		String f1ID = "replica://ontology0/fragment1";
		
		// Create test fragments
		OWLReplicaOntologyFragment f0 = (OWLReplicaOntologyFragment)
			createFragment(IRI.create(f0ID));
		OWLReplicaOntologyFragment f1 = (OWLReplicaOntologyFragment)
			createFragment(IRI.create(f1ID));
		
		// Create fragment IDs
		ID sharedObjectIDf0 = IDFactory.getDefault().createStringID(f0ID);
		ID sharedObjectIDf1 = IDFactory.getDefault().createStringID(f1ID);
		
		// Fragment A lives on clientA, fragment B lives on clientB
		getClientSharedObjectContainer(A).getSharedObjectManager().
			addSharedObject(sharedObjectIDf0, f0, null);
		getClientSharedObjectContainer(B).getSharedObjectManager().
			addSharedObject(sharedObjectIDf1, f1, null);
		
		// Add some Axioms to f0
		OWLDataFactory fac0 = f0.getOWLOntologyManager().getOWLDataFactory();
		OWLClass animal = fac0.getOWLClass(IRI.create("animal"));
		OWLClass cat = fac0.getOWLClass(IRI.create("cat"));
		OWLClass mainCoon = fac0.getOWLClass(IRI.create("mainCoon"));
		OWLClass mouse = fac0.getOWLClass(IRI.create("mouse"));
		OWLClass houseMouse = fac0.getOWLClass(IRI.create("houseMouse"));
		f0.applyChange(new AddAxiom(f0, fac0.getOWLSubClassOfAxiom(cat, animal)));
		f0.applyChange(new AddAxiom(f0, fac0.getOWLSubClassOfAxiom(mainCoon, cat)));
		f0.applyChange(new AddAxiom(f0, fac0.getOWLSubClassOfAxiom(mouse, animal)));
		f0.applyChange(new AddAxiom(f0, fac0.getOWLSubClassOfAxiom(houseMouse, mouse)));
		
		// Add individuals and properties to f1
		OWLDataFactory fac1 = f1.getOWLOntologyManager().getOWLDataFactory();
		OWLNamedIndividual john = fac1.getOWLNamedIndividual(IRI.create("john"));
		OWLNamedIndividual elvis = fac1.getOWLNamedIndividual(IRI.create("elvis"));
		// John is a mainCoon
		f1.applyChange(new AddAxiom(f1, fac1.getOWLClassAssertionAxiom(mainCoon, john)));
		// Elvis is a houseMouse
		f1.applyChange(new AddAxiom(f1, fac1.getOWLClassAssertionAxiom(houseMouse, elvis)));
		// Cats eat mouses
		OWLObjectProperty eats = fac1.getOWLObjectProperty(IRI.create("eats"));
		f1.applyChange(new AddAxiom(f1, fac1.getOWLObjectPropertyDomainAxiom(eats, cat)));
		f1.applyChange(new AddAxiom(f1, fac1.getOWLObjectPropertyRangeAxiom(eats, mouse)));
		// John eats Elvis
		f1.applyChange(new AddAxiom(f1, fac1.getOWLObjectPropertyAssertionAxiom(eats, john, elvis)));
		
		dictionary.addSignatureOf(f0);
		dictionary.addSignatureOf(f1);
		
		OWLReplicaOntologyFragmentSignature f0Sig = dictionary.
			getSignature(f0.getOWLReplicaOntologyFragmentID());
		OWLReplicaOntologyFragmentSignature f1Sig = dictionary.
			getSignature(f1.getOWLReplicaOntologyFragmentID());
		
		assertTrue(f0Sig.getClassesInSignature().contains(animal) &&
				f0Sig.getClassesInSignature().contains(cat) &&
				f0Sig.getClassesInSignature().contains(mainCoon) &&
				f0Sig.getClassesInSignature().contains(mouse) &&
				f0Sig.getClassesInSignature().contains(houseMouse) &&
				f0Sig.getDataPropertiesInSignature().isEmpty() &&
				f0Sig.getDatatypesInSignature().isEmpty() &&
				f0Sig.getIndividualsInSignature().isEmpty() &&
				f0Sig.getObjectPropertiesInSignature().isEmpty() &&
				f0Sig.getAnnotationPropertiesInSignature().isEmpty());
		
		assertTrue(f1Sig.getIndividualsInSignature().contains(john) &&
				f1Sig.getIndividualsInSignature().contains(elvis) &&
				f1Sig.getObjectPropertiesInSignature().contains(eats));
	}
	
	protected OWLOntology createFragment(IRI ontologyIRI)
			throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLReplicaManager.createOWLOntologyManager();
		OWLOntology o = man.createOntology(ontologyIRI);
		return o;
	}
	
}
