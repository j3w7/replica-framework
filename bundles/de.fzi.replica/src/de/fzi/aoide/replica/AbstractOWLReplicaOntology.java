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

/**
 * This class has been generated, do not modify
 * 
 * @generated
 */
public abstract class AbstractOWLReplicaOntology extends org.eclipse.ecf.core.sharedobject.TransactionSharedObject implements OWLReplicaOntology, java.io.Serializable {

	private static final long serialVersionUID = 6179417003L;

	public org.semanticweb.owlapi.model.OWLMutableOntology ontology;

	protected transient org.semanticweb.owlapi.model.OWLOntologyManager ontologyManager;

	protected org.semanticweb.owlapi.model.OWLOntologyID ontologyID;

	/**
	* Primary constructor
	* 
	* @param ontology
	*/
	public AbstractOWLReplicaOntology(org.semanticweb.owlapi.model.OWLOntologyManager manager, org.semanticweb.owlapi.model.OWLOntologyID ontologyID) {
		super();
		this.ontologyManager = manager;
		this.ontologyID = ontologyID;
	};

	/**
	* Replica constructor
	* 
	* Beware: replication fails without a default construcutor of an IShardObject!
	*/
	public AbstractOWLReplicaOntology() {
		super();
	}


	/**
	 * OWLOntology methods start here
	 */

	@ProxyMethod
	@PropagatingMethod
	public  java.util.List<org.semanticweb.owlapi.model.OWLOntologyChange> applyChange(org.semanticweb.owlapi.model.OWLOntologyChange arg0) {
		return ontology.applyChange(arg0);
	}

	@ProxyMethod
	@PropagatingMethod
	public  java.util.List<org.semanticweb.owlapi.model.OWLOntologyChange> applyChanges(java.util.List<org.semanticweb.owlapi.model.OWLOntologyChange> arg0) {
		return ontology.applyChanges(arg0);
	}

	@ProxyMethod
	public  boolean isEmpty() {
		return ontology.isEmpty();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAnnotation> getAnnotations() {
		return ontology.getAnnotations();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLEntity> getSignature() {
		return ontology.getSignature();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLEntity> getSignature(boolean arg0) {
		return ontology.getSignature(arg0);
	}

	@ProxyMethod
	public  int getAxiomCount() {
		return ontology.getAxiomCount();
	}

	@ProxyMethod
	public <T extends org.semanticweb.owlapi.model.OWLAxiom> int getAxiomCount(org.semanticweb.owlapi.model.AxiomType<T> arg0) {
		return ontology.getAxiomCount(arg0);
	}

	@ProxyMethod
	public <T extends org.semanticweb.owlapi.model.OWLAxiom> int getAxiomCount(org.semanticweb.owlapi.model.AxiomType<T> arg0, boolean arg1) {
		return ontology.getAxiomCount(arg0, arg1);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAxiom> getAxioms() {
		return ontology.getAxioms();
	}

	@ProxyMethod
	public <T extends org.semanticweb.owlapi.model.OWLAxiom> java.util.Set<T> getAxioms(org.semanticweb.owlapi.model.AxiomType<T> arg0) {
		return ontology.getAxioms(arg0);
	}

	@ProxyMethod
	public <T extends org.semanticweb.owlapi.model.OWLAxiom> java.util.Set<T> getAxioms(org.semanticweb.owlapi.model.AxiomType<T> arg0, boolean arg1) {
		return ontology.getAxioms(arg0, arg1);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLClassAxiom> getAxioms(org.semanticweb.owlapi.model.OWLClass arg0) {
		return ontology.getAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLObjectPropertyAxiom> getAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDataPropertyAxiom> getAxioms(org.semanticweb.owlapi.model.OWLDataProperty arg0) {
		return ontology.getAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLIndividualAxiom> getAxioms(org.semanticweb.owlapi.model.OWLIndividual arg0) {
		return ontology.getAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAnnotationAxiom> getAxioms(org.semanticweb.owlapi.model.OWLAnnotationProperty arg0) {
		return ontology.getAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom> getAxioms(org.semanticweb.owlapi.model.OWLDatatype arg0) {
		return ontology.getAxioms(arg0);
	}

	@ProxyMethod
	public  org.semanticweb.owlapi.model.OWLOntologyManager getOWLOntologyManager() {
		return ontology.getOWLOntologyManager();
	}

	@ProxyMethod
	public  boolean isAnonymous() {
		return ontology.isAnonymous();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.IRI> getDirectImportsDocuments() {
		return ontology.getDirectImportsDocuments();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLOntology> getDirectImports() {
		return ontology.getDirectImports();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLOntology> getImports() {
		return ontology.getImports();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLOntology> getImportsClosure() {
		return ontology.getImportsClosure();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLImportsDeclaration> getImportsDeclarations() {
		return ontology.getImportsDeclarations();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLLogicalAxiom> getLogicalAxioms() {
		return ontology.getLogicalAxioms();
	}

	@ProxyMethod
	public  int getLogicalAxiomCount() {
		return ontology.getLogicalAxiomCount();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAxiom> getTBoxAxioms(boolean arg0) {
		return ontology.getTBoxAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAxiom> getABoxAxioms(boolean arg0) {
		return ontology.getABoxAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAxiom> getRBoxAxioms(boolean arg0) {
		return ontology.getRBoxAxioms(arg0);
	}

	@ProxyMethod
	public  boolean containsAxiom(org.semanticweb.owlapi.model.OWLAxiom arg0) {
		return ontology.containsAxiom(arg0);
	}

	@ProxyMethod
	public  boolean containsAxiom(org.semanticweb.owlapi.model.OWLAxiom arg0, boolean arg1) {
		return ontology.containsAxiom(arg0, arg1);
	}

	@ProxyMethod
	public  boolean containsAxiomIgnoreAnnotations(org.semanticweb.owlapi.model.OWLAxiom arg0) {
		return ontology.containsAxiomIgnoreAnnotations(arg0);
	}

	@ProxyMethod
	public  boolean containsAxiomIgnoreAnnotations(org.semanticweb.owlapi.model.OWLAxiom arg0, boolean arg1) {
		return ontology.containsAxiomIgnoreAnnotations(arg0, arg1);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAxiom> getAxiomsIgnoreAnnotations(org.semanticweb.owlapi.model.OWLAxiom arg0) {
		return ontology.getAxiomsIgnoreAnnotations(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAxiom> getAxiomsIgnoreAnnotations(org.semanticweb.owlapi.model.OWLAxiom arg0, boolean arg1) {
		return ontology.getAxiomsIgnoreAnnotations(arg0, arg1);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLClassAxiom> getGeneralClassAxioms() {
		return ontology.getGeneralClassAxioms();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLClass> getClassesInSignature() {
		return ontology.getClassesInSignature();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLClass> getClassesInSignature(boolean arg0) {
		return ontology.getClassesInSignature(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLObjectProperty> getObjectPropertiesInSignature() {
		return ontology.getObjectPropertiesInSignature();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLObjectProperty> getObjectPropertiesInSignature(boolean arg0) {
		return ontology.getObjectPropertiesInSignature(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDataProperty> getDataPropertiesInSignature() {
		return ontology.getDataPropertiesInSignature();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDataProperty> getDataPropertiesInSignature(boolean arg0) {
		return ontology.getDataPropertiesInSignature(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLNamedIndividual> getIndividualsInSignature() {
		return ontology.getIndividualsInSignature();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLNamedIndividual> getIndividualsInSignature(boolean arg0) {
		return ontology.getIndividualsInSignature(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAnonymousIndividual> getReferencedAnonymousIndividuals() {
		return ontology.getReferencedAnonymousIndividuals();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDatatype> getDatatypesInSignature() {
		return ontology.getDatatypesInSignature();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDatatype> getDatatypesInSignature(boolean arg0) {
		return ontology.getDatatypesInSignature(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAnnotationProperty> getAnnotationPropertiesInSignature() {
		return ontology.getAnnotationPropertiesInSignature();
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAxiom> getReferencingAxioms(org.semanticweb.owlapi.model.OWLEntity arg0) {
		return ontology.getReferencingAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAxiom> getReferencingAxioms(org.semanticweb.owlapi.model.OWLEntity arg0, boolean arg1) {
		return ontology.getReferencingAxioms(arg0, arg1);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAxiom> getReferencingAxioms(org.semanticweb.owlapi.model.OWLAnonymousIndividual arg0) {
		return ontology.getReferencingAxioms(arg0);
	}

	@ProxyMethod
	public  boolean containsEntityInSignature(org.semanticweb.owlapi.model.OWLEntity arg0) {
		return ontology.containsEntityInSignature(arg0);
	}

	@ProxyMethod
	public  boolean containsEntityInSignature(org.semanticweb.owlapi.model.OWLEntity arg0, boolean arg1) {
		return ontology.containsEntityInSignature(arg0, arg1);
	}

	@ProxyMethod
	public  boolean containsEntityInSignature(org.semanticweb.owlapi.model.IRI arg0) {
		return ontology.containsEntityInSignature(arg0);
	}

	@ProxyMethod
	public  boolean containsEntityInSignature(org.semanticweb.owlapi.model.IRI arg0, boolean arg1) {
		return ontology.containsEntityInSignature(arg0, arg1);
	}

	@ProxyMethod
	public  boolean isDeclared(org.semanticweb.owlapi.model.OWLEntity arg0) {
		return ontology.isDeclared(arg0);
	}

	@ProxyMethod
	public  boolean isDeclared(org.semanticweb.owlapi.model.OWLEntity arg0, boolean arg1) {
		return ontology.isDeclared(arg0, arg1);
	}

	@ProxyMethod
	public  boolean containsClassInSignature(org.semanticweb.owlapi.model.IRI arg0) {
		return ontology.containsClassInSignature(arg0);
	}

	@ProxyMethod
	public  boolean containsClassInSignature(org.semanticweb.owlapi.model.IRI arg0, boolean arg1) {
		return ontology.containsClassInSignature(arg0, arg1);
	}

	@ProxyMethod
	public  boolean containsObjectPropertyInSignature(org.semanticweb.owlapi.model.IRI arg0) {
		return ontology.containsObjectPropertyInSignature(arg0);
	}

	@ProxyMethod
	public  boolean containsObjectPropertyInSignature(org.semanticweb.owlapi.model.IRI arg0, boolean arg1) {
		return ontology.containsObjectPropertyInSignature(arg0, arg1);
	}

	@ProxyMethod
	public  boolean containsDataPropertyInSignature(org.semanticweb.owlapi.model.IRI arg0) {
		return ontology.containsDataPropertyInSignature(arg0);
	}

	@ProxyMethod
	public  boolean containsDataPropertyInSignature(org.semanticweb.owlapi.model.IRI arg0, boolean arg1) {
		return ontology.containsDataPropertyInSignature(arg0, arg1);
	}

	@ProxyMethod
	public  boolean containsAnnotationPropertyInSignature(org.semanticweb.owlapi.model.IRI arg0) {
		return ontology.containsAnnotationPropertyInSignature(arg0);
	}

	@ProxyMethod
	public  boolean containsAnnotationPropertyInSignature(org.semanticweb.owlapi.model.IRI arg0, boolean arg1) {
		return ontology.containsAnnotationPropertyInSignature(arg0, arg1);
	}

	@ProxyMethod
	public  boolean containsIndividualInSignature(org.semanticweb.owlapi.model.IRI arg0) {
		return ontology.containsIndividualInSignature(arg0);
	}

	@ProxyMethod
	public  boolean containsIndividualInSignature(org.semanticweb.owlapi.model.IRI arg0, boolean arg1) {
		return ontology.containsIndividualInSignature(arg0, arg1);
	}

	@ProxyMethod
	public  boolean containsDatatypeInSignature(org.semanticweb.owlapi.model.IRI arg0) {
		return ontology.containsDatatypeInSignature(arg0);
	}

	@ProxyMethod
	public  boolean containsDatatypeInSignature(org.semanticweb.owlapi.model.IRI arg0, boolean arg1) {
		return ontology.containsDatatypeInSignature(arg0, arg1);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLEntity> getEntitiesInSignature(org.semanticweb.owlapi.model.IRI arg0) {
		return ontology.getEntitiesInSignature(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLEntity> getEntitiesInSignature(org.semanticweb.owlapi.model.IRI arg0, boolean arg1) {
		return ontology.getEntitiesInSignature(arg0, arg1);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom> getSubAnnotationPropertyOfAxioms(org.semanticweb.owlapi.model.OWLAnnotationProperty arg0) {
		return ontology.getSubAnnotationPropertyOfAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom> getAnnotationPropertyDomainAxioms(org.semanticweb.owlapi.model.OWLAnnotationProperty arg0) {
		return ontology.getAnnotationPropertyDomainAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom> getAnnotationPropertyRangeAxioms(org.semanticweb.owlapi.model.OWLAnnotationProperty arg0) {
		return ontology.getAnnotationPropertyRangeAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDeclarationAxiom> getDeclarationAxioms(org.semanticweb.owlapi.model.OWLEntity arg0) {
		return ontology.getDeclarationAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(org.semanticweb.owlapi.model.OWLAnnotationSubject arg0) {
		return ontology.getAnnotationAssertionAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLSubClassOfAxiom> getSubClassAxiomsForSubClass(org.semanticweb.owlapi.model.OWLClass arg0) {
		return ontology.getSubClassAxiomsForSubClass(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLSubClassOfAxiom> getSubClassAxiomsForSuperClass(org.semanticweb.owlapi.model.OWLClass arg0) {
		return ontology.getSubClassAxiomsForSuperClass(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom> getEquivalentClassesAxioms(org.semanticweb.owlapi.model.OWLClass arg0) {
		return ontology.getEquivalentClassesAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDisjointClassesAxiom> getDisjointClassesAxioms(org.semanticweb.owlapi.model.OWLClass arg0) {
		return ontology.getDisjointClassesAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDisjointUnionAxiom> getDisjointUnionAxioms(org.semanticweb.owlapi.model.OWLClass arg0) {
		return ontology.getDisjointUnionAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLHasKeyAxiom> getHasKeyAxioms(org.semanticweb.owlapi.model.OWLClass arg0) {
		return ontology.getHasKeyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom> getObjectSubPropertyAxiomsForSubProperty(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getObjectSubPropertyAxiomsForSubProperty(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom> getObjectSubPropertyAxiomsForSuperProperty(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getObjectSubPropertyAxiomsForSuperProperty(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom> getObjectPropertyDomainAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getObjectPropertyDomainAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom> getObjectPropertyRangeAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getObjectPropertyRangeAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom> getInverseObjectPropertyAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getInverseObjectPropertyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom> getEquivalentObjectPropertiesAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getEquivalentObjectPropertiesAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom> getDisjointObjectPropertiesAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getDisjointObjectPropertiesAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom> getFunctionalObjectPropertyAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getFunctionalObjectPropertyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom> getInverseFunctionalObjectPropertyAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getInverseFunctionalObjectPropertyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom> getSymmetricObjectPropertyAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getSymmetricObjectPropertyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom> getAsymmetricObjectPropertyAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getAsymmetricObjectPropertyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom> getReflexiveObjectPropertyAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getReflexiveObjectPropertyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom> getIrreflexiveObjectPropertyAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getIrreflexiveObjectPropertyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom> getTransitiveObjectPropertyAxioms(org.semanticweb.owlapi.model.OWLObjectPropertyExpression arg0) {
		return ontology.getTransitiveObjectPropertyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom> getDataSubPropertyAxiomsForSubProperty(org.semanticweb.owlapi.model.OWLDataProperty arg0) {
		return ontology.getDataSubPropertyAxiomsForSubProperty(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom> getDataSubPropertyAxiomsForSuperProperty(org.semanticweb.owlapi.model.OWLDataPropertyExpression arg0) {
		return ontology.getDataSubPropertyAxiomsForSuperProperty(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom> getDataPropertyDomainAxioms(org.semanticweb.owlapi.model.OWLDataProperty arg0) {
		return ontology.getDataPropertyDomainAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom> getDataPropertyRangeAxioms(org.semanticweb.owlapi.model.OWLDataProperty arg0) {
		return ontology.getDataPropertyRangeAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom> getEquivalentDataPropertiesAxioms(org.semanticweb.owlapi.model.OWLDataProperty arg0) {
		return ontology.getEquivalentDataPropertiesAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom> getDisjointDataPropertiesAxioms(org.semanticweb.owlapi.model.OWLDataProperty arg0) {
		return ontology.getDisjointDataPropertiesAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom> getFunctionalDataPropertyAxioms(org.semanticweb.owlapi.model.OWLDataPropertyExpression arg0) {
		return ontology.getFunctionalDataPropertyAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLClassAssertionAxiom> getClassAssertionAxioms(org.semanticweb.owlapi.model.OWLIndividual arg0) {
		return ontology.getClassAssertionAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLClassAssertionAxiom> getClassAssertionAxioms(org.semanticweb.owlapi.model.OWLClass arg0) {
		return ontology.getClassAssertionAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom> getDataPropertyAssertionAxioms(org.semanticweb.owlapi.model.OWLIndividual arg0) {
		return ontology.getDataPropertyAssertionAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom> getObjectPropertyAssertionAxioms(org.semanticweb.owlapi.model.OWLIndividual arg0) {
		return ontology.getObjectPropertyAssertionAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom> getNegativeObjectPropertyAssertionAxioms(org.semanticweb.owlapi.model.OWLIndividual arg0) {
		return ontology.getNegativeObjectPropertyAssertionAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom> getNegativeDataPropertyAssertionAxioms(org.semanticweb.owlapi.model.OWLIndividual arg0) {
		return ontology.getNegativeDataPropertyAssertionAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLSameIndividualAxiom> getSameIndividualAxioms(org.semanticweb.owlapi.model.OWLIndividual arg0) {
		return ontology.getSameIndividualAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom> getDifferentIndividualAxioms(org.semanticweb.owlapi.model.OWLIndividual arg0) {
		return ontology.getDifferentIndividualAxioms(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom> getDatatypeDefinitions(org.semanticweb.owlapi.model.OWLDatatype arg0) {
		return ontology.getDatatypeDefinitions(arg0);
	}

	@ProxyMethod
	public  void accept(org.semanticweb.owlapi.model.OWLObjectVisitor arg0) {
		 ontology.accept(arg0);
	}

	@ProxyMethod
	public <O> O accept(org.semanticweb.owlapi.model.OWLObjectVisitorEx<O> arg0) {
		return ontology.accept(arg0);
	}

	@ProxyMethod
	public  java.util.Set<org.semanticweb.owlapi.model.OWLClassExpression> getNestedClassExpressions() {
		return ontology.getNestedClassExpressions();
	}

	@ProxyMethod
	public  boolean isTopEntity() {
		return ontology.isTopEntity();
	}

	@ProxyMethod
	public  boolean isBottomEntity() {
		return ontology.isBottomEntity();
	}

	@Override
	public int compareTo(org.semanticweb.owlapi.model.OWLObject o) {
		return ontology.compareTo(o);
	}
}