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

package de.fzi.replica.dictionary;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import de.fzi.replica.fragments.OWLReplicaOntologyFragmentID;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 14.03.2011
 */
public interface OWLReplicaOntologyFragmentSignature {
	
	OWLReplicaOntologyFragmentID getOWLReplicaOntologyFragmentID();
	
	Set<OWLEntity> getSignature();
	
	Set<OWLClass> getClassesInSignature();
	
    Set<OWLObjectProperty> getObjectPropertiesInSignature();
    
    Set<OWLDataProperty> getDataPropertiesInSignature();
    
    Set<OWLNamedIndividual> getIndividualsInSignature();
    
    Set<OWLDatatype> getDatatypesInSignature();
    
    Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature();
	
}
