/*
   Copyright 2012 Jan Novacek

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

package de.fzi.replica.app.client;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLMutableOntology;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;

import de.fzi.replica.OWLReplicaManager;
import de.fzi.replica.OWLReplicaOntologyImpl;

/**
 * 
 * Converts an OWLOntologyImpl to an OWLOntologyReplicaImpl.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 07.02.2012
 *
 */
public privileged aspect ConvertOntology {
	
//	before(
//			ClientImpl cl,
//			OWLOntology o,
//			Set<? extends Object> c,
//			OnOntologyAddedListener l):
//			execution(* addOWLOntology(OWLOntology,
//						Set<? extends Object>,
//						OnOntologyAddedListener))
//	      	&& args(o, c, l)
//	      	&& target(cl) {
//		System.out.println("Converting ontology...");
//		OWLOntologyID ontologyID = o.getOntologyID();
//		IRI ontoIri = ontologyID.getOntologyIRI();
//		String newIri = ontoIri.toString();
//		int dd = newIri.indexOf(':');
//		newIri = "replica"+newIri.substring(dd);
//		OWLOntologyID targetOntoID = new OWLOntologyID(IRI.create(newIri));
//		OWLOntology	target = null;
//		try {
//			// Create target and initialize
//			target = OWLReplicaManager.createOWLOntologyManager().
//				createOntology(targetOntoID);
//			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
//			((OWLReplicaOntologyImpl) target).ontology
//				= (OWLMutableOntology) man.createOntology(targetOntoID);
//		} catch(OWLOntologyCreationException e) {
//			System.out.println(e);
//		}
//		Util.copy(
//				Collections.singleton(o),
//				((OWLReplicaOntologyImpl) target).ontology);
//		o = target;
////		cl.registerArgument(cl.generateRequestID(), target);
//		System.out.println("Ontology converted! Ontology: "+o.getClass());
//	}
	
	OWLOntology around(OWLOntology o):
			execution(OWLOntology ClientImpl.convertToReplicaOntology(OWLOntology)) &&
			args(o) {
		if(o instanceof OWLReplicaOntologyImpl) {
			return o;
		}
		OWLOntologyID ontologyID = o.getOntologyID();
		IRI ontoIri = ontologyID.getOntologyIRI();
		String newIri = ontoIri.toString();
		int dd = newIri.indexOf(':');
		newIri = "replica"+newIri.substring(dd);
		OWLOntologyID targetOntoID = new OWLOntologyID(IRI.create(newIri));
		OWLOntology	target = null;
		try {
			// Create target and initialize
			target = OWLReplicaManager.createOWLOntologyManager().
				createOntology(targetOntoID);
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			((OWLReplicaOntologyImpl) target).ontology
				= (OWLMutableOntology) man.createOntology(targetOntoID);
		} catch(OWLOntologyCreationException e) {
			System.out.println(e);
		}
		Util.copy(
				Collections.singleton(o),
				((OWLReplicaOntologyImpl) target).ontology);
		o = target; // FIXME no effect
		return target;
	}
	
}

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 07.02.2012
 *
 */
class Util {
	public static void copy(Set<OWLOntology> sources, OWLMutableOntology ontology) {
		for(OWLOntology source : sources) {
			/*
			 * TODO set the way this is done in a global property
			 * Beware: this has a major impact on replication speed!
			 * With current (25.10.10) settings, even when replicating
			 * just locally some test cases fail because of test timings.
			 */
			
//			// One by one
//			for(OWLAxiom axiom : source.getAxioms()) {
//				AddAxiom addAxiom = new AddAxiom(target, axiom);
//				target.applyChange(addAxiom);
//			}
			
			// Aggregate changes
			List<OWLOntologyChange> changes = new LinkedList<OWLOntologyChange>();
			for(OWLAxiom axiom : source.getAxioms()) {
				changes.add(new AddAxiom(ontology, axiom));
			}
			ontology.getOWLOntologyManager().applyChanges(changes);
		}
	}
}

