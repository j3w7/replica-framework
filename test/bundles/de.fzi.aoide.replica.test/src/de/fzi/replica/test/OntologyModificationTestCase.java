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

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLMutableOntology;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;


public class OntologyModificationTestCase {
	
	OWLOntology onto;
	AddAxiom addAxiom;
	
	@Before
	public void setUp() throws OWLOntologyCreationException {
		onto = OWLManager.createOWLOntologyManager().createOntology();
		OWLDataFactory fac = onto.getOWLOntologyManager().getOWLDataFactory();
		addAxiom = new AddAxiom(onto, fac.getOWLSubClassOfAxiom(
				fac.getOWLClass(IRI.create("B")),
				fac.getOWLClass(IRI.create("A")))
				);
		onto.getOWLOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
			@Override
			public void ontologiesChanged(
					List<? extends OWLOntologyChange> changes)
					throws OWLException {
				System.out.println("Ontology modified, changes: "+changes);
			}
		});
	}
	
	@Test
	public void testModificationOnOWLMutableOntology() throws OWLOntologyCreationException {
		((OWLMutableOntology) onto).applyChange(addAxiom);
	}
	
	@Test
	public void testModificationViaOWLOntologyManager() {
		onto.getOWLOntologyManager().applyChange(addAxiom);
	}
	
}
