/*
   Copyright 2012 Jan Novacek

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

package de.fzi.replica.comm.test;

import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_ID;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_TYPE;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_TARGET_ID;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;

import de.fzi.replica.OWLReplicaOntology;
import de.fzi.replica.comm.CommManager;
import de.fzi.replica.comm.CommManagerFactory;
import de.fzi.replica.comm.Connection;
import de.fzi.replica.comm.DefaultCommManagerFactory;
import de.fzi.replica.util.OWLOntologyToOWLReplicaOntologyCopier;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 16.01.2012
 *
 */
public class MultipleThreadsTest extends AbstractCommTestCase {
	
	private static final String A = "clientA";
	private static final String B = "clientB";

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
//		createServer();
//		createServerConnection(createConnectionPropertiesServer());
//		startServer(getConnectionOfServer());
//		createClients(A, B);
//		createClientConnection(A, createConnectionPropertiesClient(A));
//		createClientConnection(B, createConnectionPropertiesClient(B));
//		connectAllClients();
		
//		ServerThread t = new ServerThread();
//		t.start();
//		ClientAThread ca = new ClientAThread();
//		ca.start();
//		ClientBThread cb = new ClientBThread();
//		cb.start();
	}
	
	class ServerThread extends Thread {
		public void run() {
			try {
				createServer();
				createServerConnection(createConnectionPropertiesServer());
				startServer(getConnectionOfServer());
				System.out.println("Server started");
			} catch (ContainerCreateException e) {
				e.printStackTrace();
			} catch (ContainerConnectException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ClientAThread extends Thread {
		public void run() {
			Map<String, CommManager> clients = createAndReturnClients(A);
//			createClientConnection(A, createConnectionPropertiesClient(A));
			Connection c = clients.get(A).createConnection(createConnectionPropertiesClient(A));
			try {
//				connectAllClients();
				c.connect();
				assertNotNull(c.getSharedObjectContainer().getConnectedID());
				assertTrue(true);
				System.out.println("Client A connected");
			} catch (ContainerConnectException e) {
				e.printStackTrace();
				assertTrue(false);
				System.out.println("Client A connecting failed");
			}
		}
	}
	
	class ClientBThread extends Thread {
		public void run() {
			Map<String, CommManager> clients = createAndReturnClients(A);
//			createClientConnection(A, createConnectionPropertiesClient(A));
			Connection c = clients.get(A).createConnection(createConnectionPropertiesClient(A));
			try {
//				connectClient(A);
				c.connect();
				assertNotNull(c.getSharedObjectContainer().getConnectedID());
				assertTrue(true);
				System.out.println("Client B connected");
			} catch (ContainerConnectException e) {
				e.printStackTrace();
				assertTrue(false);
				System.out.println("Client B connecting failed");
			}
		}
	}
	
	public void testClients() {
		try {
			ServerThread t = new ServerThread();
			t.start();
			// wait until server has started
			Thread.sleep(1000);
			ClientAThread ca = new ClientAThread();
			ca.start();
			Thread.sleep(1000);
			// wait for client to connect
			ClientBThread cb = new ClientBThread();
			cb.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected HashMap<String, CommManager> createAndReturnClients(String... ids) {
		HashMap<String, CommManager> clients = new HashMap<String, CommManager>();
		CommManagerFactory fac = new DefaultCommManagerFactory();
		for(int i = 0; i < ids.length; i++) {
			Properties configuration = new Properties();
			configuration.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
			configuration.put(CONFIG_KEYWORD_TARGET_ID, containerIDServer);
			configuration.put(CONFIG_KEYWORD_CONTAINER_ID, ids[i]);
			CommManager client = fac.createCommManager();
			client.setConfiguration(configuration);
			clients.put(ids[i], client);
		}
		return clients;
	}
	
	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		// TODO disconnect
	}
	
//	@Test
//	public void testAddEmptyOWLReplicaOntologyAndFill()
//			throws SharedObjectAddException, OWLOntologyCreationException, InterruptedException {
//		OWLOntologyID ontoID =  new OWLOntologyID(IRI.create("replica://mySharedOntology/"));
//		ID sharedObjID = IDFactory.getDefault().createGUID();
//		
//		// Create and add an empty SharedOWLOntology
//		OWLReplicaOntology sharedOnto = createOWLReplicaOntology(ontoID);
//		getConnectionOfClient(A).getSharedObjectContainer().getSharedObjectManager().
//			addSharedObject(sharedObjID, (ISharedObject) sharedOnto, null);
//		
//		// Wait for the object to be added
//		Thread.sleep(1000);
//		
//		// B should get a reference to the replica now
//		OWLReplicaOntology replica = (OWLReplicaOntology) getConnectionOfClient(B).
//			getSharedObjectContainer().getSharedObjectManager().getSharedObject(sharedObjID);
//		assertNotNull(replica);
//		assertNotSame(sharedOnto, replica);
//		
//		// Load the pizza ontology and copy all axioms into our shared ontology
//		IRI pizzaOntologyIRI = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
//		OWLOntology pizzaOnto = OWLManager.createOWLOntologyManager().loadOntology(pizzaOntologyIRI);
//		new OWLOntologyToOWLReplicaOntologyCopier().copy(Collections.singleton(pizzaOnto), sharedOnto);
//		
//		// Wait for change promotion
//		Thread.sleep(2000);
//		assertNotSame(pizzaOnto, sharedOnto);
//		assertEquals(pizzaOnto.getAxioms(), sharedOnto.getAxioms());
//		assertEquals(pizzaOnto.getAxioms(), replica.getAxioms());
//	}
	
}
