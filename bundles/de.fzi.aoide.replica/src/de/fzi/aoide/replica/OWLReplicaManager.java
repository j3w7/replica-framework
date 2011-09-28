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
import java.util.Set;

import org.coode.owlapi.functionalrenderer.OWLFunctionalSyntaxOntologyStorer;
import org.coode.owlapi.latex.LatexOntologyStorer;
import org.coode.owlapi.owlxml.renderer.OWLXMLOntologyStorer;
import org.coode.owlapi.rdf.rdfxml.RDFXMLOntologyStorer;
import org.coode.owlapi.turtle.TurtleOntologyStorer;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.NonMappingOntologyIRIMapper;

import uk.ac.manchester.cs.owl.owlapi.EmptyInMemOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyManagerImpl;
import uk.ac.manchester.cs.owl.owlapi.ParsableOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOntologyStorer;
import de.uulm.ecs.ai.owlapi.krssrenderer.KRSS2OWLSyntaxOntologyStorer;

// To integrate seamlessly in the OWLAPI
public class OWLReplicaManager {
	
	private static final OWLReplicaOntologyFactory DEFAULT_FACTORY = new OWLReplicaOntologyFactoryImpl();
	
	private static Set<OWLReplicaOntologyFactory> replicaFactories = new HashSet<OWLReplicaOntologyFactory>();
	
	/**
	 * Add factories for creating OWLReplicaOntology instances. It is in the
	 * responsibility of the caller to avoid supported schemes conflicts.
	 * 
	 * @param factory The factory to add for creating a ReplicaOntology instance
	 */
	public static void addOWLReplicaOntologyFactory(OWLReplicaOntologyFactory factory) {
		replicaFactories.add(factory);
	}
	
	public static Set<OWLReplicaOntologyFactory> getReplicaFactories() {
		return replicaFactories;
	}
	
	public static OWLOntologyManager createOWLOntologyManager() {
		OWLOntologyManager ontologyManager = new OWLOntologyManagerImpl(new OWLDataFactoryImpl());
		ontologyManager.addOntologyStorer(new RDFXMLOntologyStorer());
        ontologyManager.addOntologyStorer(new OWLXMLOntologyStorer());
        ontologyManager.addOntologyStorer(new OWLFunctionalSyntaxOntologyStorer());
        ontologyManager.addOntologyStorer(new ManchesterOWLSyntaxOntologyStorer());
        ontologyManager.addOntologyStorer(new KRSS2OWLSyntaxOntologyStorer());
        ontologyManager.addOntologyStorer(new TurtleOntologyStorer());
        ontologyManager.addOntologyStorer(new LatexOntologyStorer());
//      ontologyManager.addOntologyStorer(new OWLReplicaStorer()); // TODO

        ontologyManager.addIRIMapper(new NonMappingOntologyIRIMapper());
        
        ontologyManager.addOntologyFactory(new EmptyInMemOWLOntologyFactory());
        ontologyManager.addOntologyFactory(new ParsableOWLOntologyFactory());
        // Add replica factories
        if(replicaFactories.isEmpty()) {
        	replicaFactories.add(DEFAULT_FACTORY);
        }
        for(OWLReplicaOntologyFactory factory : replicaFactories) {
        	ontologyManager.addOntologyFactory(factory);
        }
		return ontologyManager;
	}
	
}
