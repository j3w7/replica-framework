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

package de.fzi.aoide.replica;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/*
 * Taken from OWLDB, modified to work with OWLAPI v3 and ReplicaOntology
 */
public class OWLReplicaOntologyFactoryImpl implements OWLReplicaOntologyFactory {
	
	protected OWLOntologyManager manager;
	
	public final static Set<String> supportedSchemes;
	
	static {
		supportedSchemes = new HashSet<String>();
        supportedSchemes.add("replica");
	}
	
	public OWLReplicaOntologyFactoryImpl() {
		
	}

	@Override
	public OWLOntology createOWLOntology(OWLOntologyID ontologyID, IRI documentIRI,
			OWLOntologyCreationHandler handler)
			throws OWLOntologyCreationException {
		Properties props = new Properties();
		return createOWLOntology(ontologyID, documentIRI, handler, props);
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
	public OWLOntology createOWLOntology(OWLOntologyID ontologyID, IRI documentIRI,
			OWLOntologyCreationHandler handler, Properties props)
			throws OWLOntologyCreationException {
		
		// TODO set properties, do not ignore documentIRI
		
		OWLOntology instance = new OWLReplicaOntologyImpl(manager, ontologyID);
		handler.ontologyCreated(instance);
		handler.setOntologyFormat(instance, new OWLReplicaOntologyFormat());
		return instance;
	}

	@Override
	public OWLOntology loadOWLOntology(OWLOntologyDocumentSource inputSource,
			OWLOntologyCreationHandler handler) throws OWLOntologyCreationException {
		Properties props = new Properties();
		return loadOWLOntology(inputSource, handler, props);
	}
	
	/**
	 * Load an existing ontology. Not implemented right now.
	 * 
	 * @param inputSource The InputSource
	 * @param handler The Handler to notifie
	 * @param props The Properties used for ECF
	 * @return The loaded Ontology
	 * @throws OWLOntologyCreationException
	 */
	public OWLOntology loadOWLOntology(OWLOntologyDocumentSource inputSource,
			OWLOntologyCreationHandler handler, Properties props)
			throws OWLOntologyCreationException {
//		if(!(inputSource instanceof PhysicalURIInputSource))
		if(!(inputSource instanceof OWLOntologyDocumentSource))
			throw new OWLOntologyCreationException("Expected OWLOntologyDocumentSource");
		
//		IRI physicalIRI = inputSource.getDocumentIRI();
		// TODO
		OWLOntology instance = null;
//		OWLOntology instance = new OWLReplicaOntologyImpl(manager, ontologyID);
		handler.ontologyCreated(instance);
		handler.setOntologyFormat(instance, new OWLReplicaOntologyFormat());
		return instance;
	}

	@Override
	public void setOWLOntologyManager(OWLOntologyManager owlOntologyManager) {
		this.manager = owlOntologyManager;
	}

	@Override
	public boolean canCreateFromDocumentIRI(IRI documentIRI) {
		if(supportedSchemes.contains(documentIRI.getScheme())) {
		  return true;
		}
		return false;
	}
	
	@Override
	public boolean canLoad(OWLOntologyDocumentSource documentSource) {
		if(supportedSchemes.contains(documentSource.getDocumentIRI().getScheme())) { // TODO Test this
		  return true;
		}
		return false;
	}
	
	@Override
	public OWLOntology loadOWLOntology(
			OWLOntologyDocumentSource documentSource,
			OWLOntologyCreationHandler handler,
			OWLOntologyLoaderConfiguration configuration)
			throws OWLOntologyCreationException {
		// TODO implement this
		throw new UnsupportedOperationException("Operation Not Supported");
	}
	
	@Override
	public String getOWLReplicaFactoryID() {
		return "standard";
	}

}