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

package de.fzi.aoide.replica.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import de.fzi.aoide.replica.OWLReplicaOntology;

/**
 * Very simple utility class to copy all axioms into an existing OWLReplicaOntology.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version version 1.0, 31.08.2010
 *
 */
public class OWLOntologyToOWLReplicaOntologyCopier extends OWLOntologyCopier<OWLOntology, OWLReplicaOntology> {
	
	// Maybe we want filters some day, so dont use static methods

	@Override
	public void copy(Set<OWLOntology> sources, OWLReplicaOntology target) {
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
				changes.add(new AddAxiom(target, axiom));
			}
			target.getOWLOntologyManager().applyChanges(changes);
		}
	}
	
}
