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

import de.fzi.replica.fragments.OWLReplicaOntologyFragment;
import de.fzi.replica.fragments.OWLReplicaOntologyFragmentID;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 14.03.2011
 */
public class OWLReplicaOntologyFragmentSignatureImpl implements
		OWLReplicaOntologyFragmentSignature {
	
	private OWLReplicaOntologyFragmentID ontoID;
	private Set<OWLEntity> signature;
	private Set<OWLClass> classes;
	private Set<OWLObjectProperty> objProps;
	private Set<OWLDataProperty> dataProps;
	private Set<OWLNamedIndividual> individuals;
	private Set<OWLDatatype> datatypes;
	private Set<OWLAnnotationProperty> annotationProps;
	
	protected OWLReplicaOntologyFragmentSignatureImpl(OWLReplicaOntologyFragment fragment) {
		ontoID = fragment.getOWLReplicaOntologyFragmentID();
		signature = fragment.getSignature();
		classes = fragment.getClassesInSignature();
		objProps = fragment.getObjectPropertiesInSignature();
		dataProps = fragment.getDataPropertiesInSignature();
		individuals = fragment.getIndividualsInSignature();
		datatypes = fragment.getDatatypesInSignature();
		annotationProps = fragment.getAnnotationPropertiesInSignature();
	}

	@Override
	public OWLReplicaOntologyFragmentID getOWLReplicaOntologyFragmentID() {
		return ontoID;
	}

	@Override
	public Set<OWLEntity> getSignature() {
		return signature;
	}

	@Override
	public Set<OWLClass> getClassesInSignature() {
		return classes;
	}

	@Override
	public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
		return objProps;
	}

	@Override
	public Set<OWLDataProperty> getDataPropertiesInSignature() {
		return dataProps;
	}

	@Override
	public Set<OWLNamedIndividual> getIndividualsInSignature() {
		return individuals;
	}

	@Override
	public Set<OWLDatatype> getDatatypesInSignature() {
		return datatypes;
	}

	@Override
	public Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature() {
		return annotationProps;
	}

	@Override
	public String toString() {
		return "OWLReplicaOntologyFragmentSignatureImpl [ontoID=" + ontoID
				+ ", signature=" + signature + ", classes=" + classes
				+ ", objProps=" + objProps + ", dataProps=" + dataProps
				+ ", individuals=" + individuals + ", datatypes=" + datatypes
				+ ", annotationProps=" + annotationProps + "]";
	}

}
