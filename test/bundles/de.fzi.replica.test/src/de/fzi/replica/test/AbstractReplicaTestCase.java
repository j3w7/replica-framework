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

package de.fzi.replica.test;

import java.net.Socket;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.OWLReplicaManager;
import de.fzi.replica.OWLReplicaOntology;

/**
 * Base class for all test cases.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.1, 28.09.2010
 *
 */
public abstract class AbstractReplicaTestCase extends TestCase {
	
	public final static String DEFAULT_SERVER_ADRESS = "localhost";
	public final static int DEFAULT_SERVER_PORT = 10000;
	
	protected String serverContainerAdress = DEFAULT_SERVER_ADRESS;
	protected int serverContainerPort = DEFAULT_SERVER_PORT;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		findFreePort();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void findFreePort() {
		boolean found = false;
		for(int i = serverContainerPort; !found; i++) {
			try {
				final Socket s = new Socket(serverContainerAdress, serverContainerPort);
				serverContainerPort++;
				try {
					s.close();
				} catch (final Exception e) {
					// do nothing
				};
			} catch (final Exception e) {
				// could not connect, so this port is free
				return;
			}
		}
	}
	
	protected static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected OWLReplicaOntology createOWLReplicaOntology(OWLOntologyID ontologyID)
			throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLReplicaManager.createOWLOntologyManager();
		return (OWLReplicaOntology) man.createOntology(ontologyID);
	}
	
	protected OWLOntology createTestOWLOntology(OWLOntologyID ontologyID)
			throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology onto = man.createOntology(ontologyID);
		OWLDataFactory fac = man.getOWLDataFactory();
		OWLClass clA = fac.getOWLClass(IRI.create("http://blub.org/#classA"));
		OWLClass clB = fac.getOWLClass(IRI.create("http://blub.org/#classB"));
		OWLClass clC = fac.getOWLClass(IRI.create("http://blub.org/#classC"));
		man.applyChange(new AddAxiom(onto, fac.getOWLSubClassOfAxiom(clB, clA)));
		man.applyChange(new AddAxiom(onto, fac.getOWLSubClassOfAxiom(clC, clB)));
		return onto;
	}
	
}
