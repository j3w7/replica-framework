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

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.IIDFactory;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.sharedobject.ISharedObjectManager;
import org.eclipse.osgi.util.NLS;
import org.junit.After;
import org.junit.Before;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.OWLReplicaManager;
import de.fzi.replica.OWLReplicaOntology;
import de.fzi.replica.test.internal.Activator;

/**
 * This base test case is used to set up the basic scenario for all test cases:
 * One or more clients connect to a server. <br>
 * It contains many convenience methods and the amount of clients and their IDs
 * can be specified if needed.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.1, 28.09.2010
 * 
 */
public abstract class AbstractOWLReplicaOntologyTestCase extends
		AbstractReplicaTestCase {

	public final static String DEFAULT_SERVER_CONTAINER_TYPE = "ecf.generic.server";
	public final static String DEFAULT_CLIENT_CONTAINER_TYPE = "ecf.generic.client";

	public final static String DEFAULT_SERVER_IDENTITY = "ecftcp://localhost:{0}/server";

	// Change management and other test cases may need to override the scheme
	private static OWLOntologyID ONTO_ID = new OWLOntologyID(
			IRI.create("replica://mySharedOntology/"));

	protected String serverContainerType = DEFAULT_SERVER_CONTAINER_TYPE;
	protected String clientContainerType = DEFAULT_CLIENT_CONTAINER_TYPE;

	protected String serverContainerIdentity = DEFAULT_SERVER_IDENTITY;

	protected IContainer server;
	protected IContainer[] clients;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		findFreePort();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public static void setOntologyID(OWLOntologyID ontologyID) {
		ONTO_ID = ontologyID;
	}

	public static OWLOntologyID getOntologyID() {
		return ONTO_ID;
	}

	protected String getServerContainerType() {
		if (serverContainerType == null) {
			serverContainerType = DEFAULT_CLIENT_CONTAINER_TYPE;
		}
		return serverContainerType;
	}

	protected String getClientContainerType() {
		if (clientContainerType == null) {
			clientContainerType = DEFAULT_CLIENT_CONTAINER_TYPE;
		}
		return clientContainerType;
	}

	protected ID getServerIdentity() {
		return getIDFactory().createStringID(
				NLS.bind(serverContainerIdentity, new Integer(
						serverContainerPort)));
	}

	protected IContainer createServer() throws ContainerCreateException,
			IDCreateException {
		return server = getContainerFactory().createContainer(
				getServerContainerType(), getServerIdentity());
	}

	protected void createClients(int count) throws ContainerCreateException,
			IDCreateException {
		createClients(new String[count]);
	}

	private IContainer createClient(ID clientID)
			throws ContainerCreateException, IDCreateException {
		return getContainerFactory().createContainer(getClientContainerType(),
				clientID);
	}

	protected IContainer getClient(String clientID) {
		return getClient(IDFactory.getDefault().createStringID(clientID));
	}

	protected IContainer getClient(ID clientID) {
		for (IContainer client : clients) {
			if (clientID.equals(client.getID())) {
				return client;
			}
		}
		return null;
	}

	protected ISharedObjectContainer getClientSharedObjectContainer(
			String clientID) {
		return getClientSharedObjectContainer(IDFactory.getDefault()
				.createStringID(clientID));
	}

	protected ISharedObjectContainer getClientSharedObjectContainer(ID clientID) {
		return (ISharedObjectContainer) getClient(clientID);
	}

	protected ISharedObjectManager getClientSharedObjectManager(String clientID) {
		return getClientSharedObjectManager(IDFactory.getDefault()
				.createStringID(clientID));
	}

	protected ISharedObjectManager getClientSharedObjectManager(ID clientID) {
		return getClientSharedObjectContainer(clientID)
				.getSharedObjectManager();
	}

	protected ISharedObjectManager getServerSharedObjectManager() {
		return getServerSharedObjectContainer().getSharedObjectManager();
	}

	protected ISharedObjectContainer getServerSharedObjectContainer() {
		return (ISharedObjectContainer) server;
	}

	protected void createClients(String... ids)
			throws ContainerCreateException, IDCreateException {
		clients = new IContainer[ids.length];
		for (int i = 0; i < clients.length; i++) {
			if (ids[i] != null && !ids[i].isEmpty()) {
				clients[i] = createClient(getIDFactory().createStringID(ids[i]));
			} else {
				clients[i] = createClient(getIDFactory().createGUID());
			}
		}
	}

	protected void connectAllClientsTo(String targetID)
			throws ContainerConnectException, IDCreateException {
		connectAllClientsTo(getIDFactory().createStringID(targetID));
	}

	protected void connectAllClientsTo(ID targetID)
			throws ContainerConnectException {
		for (IContainer client : clients) {
			IConnectContext connectContext = null;
			client.connect(targetID, connectContext);
		}
	}

	protected void connectClient(String clientID, String targetID)
			throws ContainerConnectException, IDCreateException {
		connectClient(getIDFactory().createStringID(clientID), getIDFactory()
				.createStringID(targetID));
	}

	protected void connectClient(ID clientID, ID serverID)
			throws ContainerConnectException {
		IConnectContext connectContext = null;
		getClient(clientID).connect(serverID, connectContext);
	}

	protected void disconnectClient(String clientID) {
		disconnectClient(IDFactory.getDefault().createStringID(clientID));
	}

	protected void disconnectClient(ID clientID) {
		getClient(clientID).disconnect();
	}

	protected void removeAllClients() {
		if (clients != null) {
			for (int i = 0; i < clients.length; i++) {
				clients[i].disconnect();
				clients[i].dispose();
				removeFromContainerManager(clients[i]);
				clients[i] = null;
			}
			clients = null;
		}
	}

	protected void removeServer() {
		if (server != null) {
			server.disconnect();
			server.dispose();
			removeFromContainerManager(server);
			server = null;
		}
	}

	protected void removeFromContainerManager(IContainer container) {
		IContainerManager manager = Activator.getContainerManager();
		if (manager != null)
			manager.removeContainer(container);
	}

	protected IContainerManager getContainerManager() {
		return Activator.getContainerManager();
	}

	protected IContainerFactory getContainerFactory() {
		return Activator.getContainerFactory();
	}

	protected IIDFactory getIDFactory() {
		return Activator.getIDFactory();
	}

	protected OWLReplicaOntology createOWLReplicaOntology(
			OWLOntologyID ontologyID) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLReplicaManager.createOWLOntologyManager();
		return (OWLReplicaOntology) man.createOntology(ontologyID);
	}

}
