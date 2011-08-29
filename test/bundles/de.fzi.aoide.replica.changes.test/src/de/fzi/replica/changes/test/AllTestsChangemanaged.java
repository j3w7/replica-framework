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

package de.fzi.replica.changes.test;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import de.fzi.replica.test.AbstractOWLReplicaOntologyTestCase;
import de.fzi.replica.test.AddOWLReplicaOntologyTestCase;
import de.fzi.replica.test.OWLReplicaOntologyChangeListeningTestCase;
import de.fzi.replica.test.OWLReplicaOntologyModificationTestCase;
import de.fzi.replica.test.OWLReplicaOntologyReasoningTest;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTestsChangemanaged {

	public static Test suite() {
		// Set change management scheme
		AbstractOWLReplicaOntologyTestCase.setOntologyID(new OWLOntologyID(
				IRI.create("replica+changemanaged://mySharedOntology/")));
		
		TestSuite suite = new TestSuite(AllTestsChangemanaged.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(OWLReplicaOntologyReasoningTest.class);
		//suite.addTestSuite(OWLReplicaOntologyAccessRestrictionsTestCase.class);
		suite.addTestSuite(AddOWLReplicaOntologyTestCase.class);
		//suite.addTestSuite(OWLReplicaOntologyConcurrentAccessTest.class);
		suite.addTestSuite(OWLReplicaOntologyChangeListeningTestCase.class);
		suite.addTestSuite(OWLReplicaOntologyModificationTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
