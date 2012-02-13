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

package de.fzi.replica.util;

import java.util.Collections;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLMutableOntology;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.AbstractOWLReplicaOntology;
import de.fzi.replica.OWLReplicaManager;
import de.fzi.replica.OWLReplicaOntology;

/**
 * Convert an OWLOntologyImpl instance to an OWLReplicaOntologyImpl.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 09.02.2012
 */
public class OWLReplicaOntologyConverter {

	public static OWLReplicaOntology convert(OWLOntology onto) {
		try {
			System.out.println("convert()");
			OWLOntologyManager man = OWLReplicaManager.createOWLOntologyManager();
			OWLOntologyID targetID = convertToReplicaOntologyID(onto.getOntologyID());
			OWLReplicaOntology ro = (OWLReplicaOntology) man.createOntology(targetID);
			((AbstractOWLReplicaOntology) ro).ontology = 
				(OWLMutableOntology) OWLManager.createOWLOntologyManager().
					createOntology(targetID);
			new OWLOntologyToOWLMutableOntologyCopier().copy(
					Collections.singleton(onto), 
						((AbstractOWLReplicaOntology) ro).ontology);
			
//			((AbstractOWLReplicaOntology) ro).ontology = (OWLMutableOntology) onto;
			return ro;
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static OWLOntologyID convertToReplicaOntologyID(OWLOntologyID id) {
		IRI ontoIri = id.getOntologyIRI();
		String newIri = ontoIri.toString();
		int dd = newIri.indexOf(':');
		newIri = "replica"+newIri.substring(dd);
		OWLOntologyID targetOntoID = new OWLOntologyID(IRI.create(newIri));
		return targetOntoID;
	}

}
