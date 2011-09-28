//package de.fzi.aoide.replica;
//
//import java.io.Serializable;
//import uk.ac.manchester.cs.owl.owlapi.OWLObjectImpl;
//import uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl;
//import org.semanticweb.owlapi.model.OWLObject;
//import org.semanticweb.owlapi.model.OWLOntologyChange;
//import org.semanticweb.owlapi.model.OWLOntologyID;
//import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.model.OWLDataFactory;
//import org.semanticweb.owlapi.model.AddAxiom;
//import org.semanticweb.owlapi.model.AxiomType;
//import uk.ac.manchester.cs.owl.owlapi.OWLSubClassOfAxiomImpl;
//
///**
// * 
// * The nested private classes in OWLOntologyImpl require this aspect
// * to be privileged.
// * 
// * FIXME is this possible at all?
// * 
// * @author Jan Novacek novacek@fzi.de
// * @version 1.1, 31.08.2010
// * 
// */
//public privileged aspect Serializability {
//	
//	declare parents: org.semanticweb.owlapi.model.OWLOntologyID implements Serializable;
//	
//	declare parents: com.clarkparsia.owlapi.explanation.* implements Serializable;
//	declare parents: com.clarkparsia.owlapi.explanation.util.* implements Serializable;
//	declare parents: de.uulm.ecs.ai.owlapi.krssrenderer.KRSSObjectRenderer implements Serializable;
//	declare parents: org.coode.owlapi.functionalparser.* implements Serializable;
//	declare parents: org.coode.owlapi.obo.parser.* implements Serializable;
//	declare parents: org.coode.owlapi.owlxmlparser.* implements Serializable;
//	declare parents: org.coode.owlapi.rdf.model.* implements Serializable;
//	declare parents: org.coode.owlapi.rdf.renderer.* implements Serializable;
//	declare parents: org.coode.owlapi.rdfxml.parser.* implements Serializable;
//	declare parents: org.coode.xml.* implements Serializable;
//	declare parents: org.semanticweb.owlapi.* implements Serializable;
//	declare parents: org.semanticweb.owlapi.debugging.* implements Serializable;
//	declare parents: org.semanticweb.owlapi.io.* implements Serializable;
//	declare parents: org.semanticweb.owlapi.metrics.* implements Serializable;
//	declare parents: org.semanticweb.owlapi.model.* implements Serializable;
//	declare parents: org.semanticweb.owlapi.profiles.* implements Serializable;
//	declare parents: org.semanticweb.owlapi.reasoner.* implements Serializable;
//	declare parents: org.semanticweb.owlapi.util.* implements Serializable;
//	declare parents: uk.ac.manchester.cs.owl.owlapi.* implements Serializable;
//	declare parents: uk.ac.manchester.cs.owl.explanation.ordering.* implements Serializable;
//	declare parents: uk.ac.manchester.cs.owlapi.modularity.* implements Serializable;
//	
////	declare parents: com.clarkparsia.owlapi.explanation.util.DefinitionTracker implements Serializable;
////	declare parents: com.clarkparsia.owlapi.explanation.util.OntologyUtils implements Serializable;
////	declare parents: com.clarkparsia.owlapi.explanation.BlackBoxExplanation implements Serializable;
////	declare parents: com.clarkparsia.owlapi.explanation.SingleExplanationGeneratorImpl implements Serializable;
////	declare parents: de.uulm.ecs.ai.owlapi.krssrenderer.KRSSObjectRenderer implements Serializable;
////	declare parents: org.coode.owlapi.functionalparser.OWLFuntionalSyntaxParser implements Serializable;
////	declare parents: org.coode.owlapi.obo.parser.OBOConsumer implements Serializable;
////	declare parents: org.coode.owlapi.owlxmlparser.OWLXMLParserHandler implements Serializable;
////	declare parents: org.coode.owlapi.rdf.model.AbstractTranslator implements Serializable;
////	declare parents: org.coode.owlapi.rdf.renderer.RDFRendererBase implements Serializable;
////	declare parents: org.coode.owlapi.rdfxml.parser.OWLRDFConsumer implements Serializable;
////	declare parents: org.coode.xml.RDFXMLParser implements Serializable;
////	declare parents: org.coode.xml.OWLOntologyXMLNamespaceManager implements Serializable;
//
//	transient private OWLOntologyManager com.clarkparsia.owlapi.explanation.util.DefinitionTracker.manager = null;
//	transient private OWLOntologyManager com.clarkparsia.owlapi.explanation.BlackBoxExplanation.owlOntologyManager = null;
//	transient private OWLOntologyManager com.clarkparsia.owlapi.explanation.SingleExplanationGeneratorImpl.owlOntologyManager = null;
//	transient private OWLOntologyManager de.uulm.ecs.ai.owlapi.krssrenderer.KRSSObjectRenderer.manager = null;
//	transient private OWLOntologyManager org.coode.owlapi.functionalparser.OWLFunctionalSyntaxParser.man = null;
//	transient private OWLOntologyManager org.coode.owlapi.obo.parser.OBOConsumer.owlOntologyManager = null;
//	transient private OWLOntologyManager org.coode.owlapi.owlxmlparser.OWLXMLParserHandler.owlOntologyManager = null;
//	transient private OWLOntologyManager org.coode.owlapi.rdf.model.AbstractTranslator.manager = null;
//	transient private OWLOntologyManager org.coode.owlapi.rdf.renderer.RDFRendererBase.manager = null;
//	transient private OWLOntologyManager org.coode.owlapi.rdfxml.parser.OWLRDFConsumer.owlOntologyManager = null;
//	transient private OWLOntologyManager org.coode.owlapi.rdfxml.parser.RDFXMLParser.owlOntologyManager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.debugging.AbstractOWLDebugger.owlOntologyManager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.debugging.BlackBoxOWLDebugger.owlOntologyManager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.io.AbstractOWLParser.owlOntologyManager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.io.AbstractOWLRenderer.owlOntologyManager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.metrics.AbstractOWLMetric.owlOntologyManager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.profiles.OWL2DLProfile.OWL2DLProfileObjectVisitor.manager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.profiles.OWL2ELProfile.OWL2ELProfileObjectVisitor.manager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.profiles.OWL2Profile.OWL2ProfileObjectWalker.manager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.reasoner.impl.OWLReasonerBase.manager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter.man = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.util.ImportsStructureObjectSorter.manager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.util.OWLEntityRenamer.owlOntologyManager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.util.OWLEntityURIConverter.manager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.util.OWLObjectPropertyManager.man = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.util.OWLOntologyImportsClosureSetProvider.manager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.util.OWLOntologyURIChanger.manager = null;
//	transient private OWLOntologyManager org.semanticweb.owlapi.ShortForm2AnnotationGenerator.ontologyManager = null;
//	transient private OWLOntologyManager uk.ac.manchester.cs.owl.explanation.ordering.DefaultExplanationOrderer.man = null;
//	transient private OWLOntologyManager uk.ac.manchester.cs.owl.owlapi.AbstractInMemOWLOntologyFactory.ontologyManager = null;
//	transient private OWLOntologyManager uk.ac.manchester.cs.owl.owlapi.OWLObjectImpl.dataFactory = null;
//	transient private OWLOntologyManager uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl.manager = null;
//	transient private OWLOntologyManager uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor.manager = null;
//	
//}

//import java.io.Serializable;
//
//import org.semanticweb.owlapi.model.OWLOntologyManager;
//
//public aspect Serializability {
//	
//	private final transient OWLOntologyManager uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl.manager;
//	
//	declare parents: uk.ac.manchester.cs.owl.owlapi.OWLObjectImpl implements Serializable;
//	declare parents: uk.ac.manchester.cs.owl.owlapi.OWLOntologyImpl implements Serializable;
//	declare parents: uk.ac.manchester.cs.owl.owlapi.AbstractInternalsImpl implements Serializable;
//	declare parents: org.semanticweb.owlapi.model.IRI implements Serializable;
//	declare parents: org.semanticweb.owlapi.model.AxiomType implements Serializable;
//	declare parents: org.semanticweb.owlapi.model.OWLOntologyID implements Serializable;
//	
//}
