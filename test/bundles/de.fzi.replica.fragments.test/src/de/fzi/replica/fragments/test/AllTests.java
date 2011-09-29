package de.fzi.replica.fragments.test;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.fzi.replica.test.AbstractOWLReplicaOntologyTestCase;
import de.fzi.replica.test.AddOWLReplicaOntologyTestCase;
import de.fzi.replica.test.OWLReplicaOntologyModificationTestCase;
import de.fzi.replica.test.OWLReplicaOntologyReasoningTest;

public class AllTests {

	public static Test suite() {
		// Set fragment scheme
		AbstractOWLReplicaOntologyTestCase.setOntologyID(new OWLOntologyID(
				IRI.create("replica+fragment://mySharedOntology/")));
		
		TestSuite suite = new TestSuite("Test for de.fzi.aoide.replica.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(OWLReplicaOntologyReasoningTest.class);
		//suite.addTestSuite(OWLReplicaOntologyAccessRestrictionsTestCase.class);
		suite.addTestSuite(AddOWLReplicaOntologyTestCase.class);
		//suite.addTestSuite(OWLReplicaOntologyConcurrentAccessTest.class);
		suite.addTestSuite(OWLReplicaOntologyModificationTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
