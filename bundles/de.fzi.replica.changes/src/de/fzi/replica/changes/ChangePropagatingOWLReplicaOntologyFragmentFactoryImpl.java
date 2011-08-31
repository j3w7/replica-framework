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

package de.fzi.replica.changes;

import java.util.Properties;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;

import de.fzi.replica.OWLReplicaOntologyFactory;
import de.fzi.replica.OWLReplicaOntologyFactoryImpl;
import de.fzi.replica.OWLReplicaOntologyFormat;

public class ChangePropagatingOWLReplicaOntologyFragmentFactoryImpl 
		extends OWLReplicaOntologyFactoryImpl implements OWLReplicaOntologyFactory {
	
	static {
        supportedSchemes.add("replica+changemanaged+fragment");
        supportedSchemes.add("replica+fragment+changemanaged");
	}
	
	/**
	 * Creates an (empty) ontology.
	 * @param ontologyURI The ontologyURI
	 * @param documentIRI The physical URI
	 * @param handler The Handler
	 * @param props The Properties used for ECF
	 * @return The created Ontology
	 * @throws OWLOntologyCreationException
	 * 	 
	*/
	@Override
	public OWLOntology createOWLOntology(OWLOntologyID ontologyID, IRI documentIRI,
			OWLOntologyCreationHandler handler, Properties props)
			throws OWLOntologyCreationException {
		
		// TODO set properties, do not ignore documentIRI
		
		OWLOntology instance = new ChangePropagatingOWLReplicaOntologyFragmentImpl(manager, ontologyID);
		handler.ontologyCreated(instance);
		handler.setOntologyFormat(instance, new OWLReplicaOntologyFormat());
		return instance;
	}
	
	@Override
	public String getOWLReplicaFactoryID() {
		return "changemanagementfragment";
	}

}