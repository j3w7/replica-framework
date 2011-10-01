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

package de.fzi.replica.app.test;

import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_ID;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_TYPE;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_TARGET_ID;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.osgi.util.NLS;
import org.junit.After;
import org.junit.Before;

import de.fzi.replica.app.Application.StartupException;
import de.fzi.replica.app.client.Client;
import de.fzi.replica.app.client.ClientFactory;
import de.fzi.replica.app.client.DefaultClientFactory;
import de.fzi.replica.app.client.Client.ConnectException;
import de.fzi.replica.app.server.DefaultServerFactory;
import de.fzi.replica.app.server.Server;
import de.fzi.replica.comm.Connection;
import de.fzi.replica.test.AbstractReplicaTestCase;

/**
 * The <code>createServer()</code> and <code>createClients()</code> methods
 * prepare server and client instances by setting defaults for {@link Connection}s.
 * Whenever a Connection is created, these values will be used if they are
 * not overridden in a supplied Connection configuration.<br>
 * Also the semantics differ, the default container ID of a CommManager instance
 * is used here as an identifier for a client or a server. Of course this should
 * be handled differently in a real world implementation.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.2, 1.12.2010
 */
public class AbstractApplicationTestCase extends AbstractReplicaTestCase {
	
	protected final String DEFAULT_CONTAINER_TYPE_CLIENT = "ecf.generic.client";
	protected final String DEFAULT_CONTAINER_TYPE_SERVER = "ecf.generic.server";
	protected final String DEFAULT_CONTAINER_ID_SERVER = "ecftcp://localhost:{0}/server";;
	
	protected String containerTypeClient = DEFAULT_CONTAINER_TYPE_CLIENT;
	protected String containerTypeServer = DEFAULT_CONTAINER_TYPE_SERVER;
	protected String containerIDServer = DEFAULT_CONTAINER_ID_SERVER;
	
	protected final String DEFAULT_SERVER_IDENTITY = "ecftcp://localhost:{0}/server";
	
	protected Server server;
	protected Map<String, Client> clients;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		findFreePort();
		containerIDServer = NLS.bind(DEFAULT_CONTAINER_ID_SERVER, new Integer(serverContainerPort));
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void createServer() {
//		server = new DefaultCommManagerFactory().createCommManager();
		server = new DefaultServerFactory().createServer(createServerConfig());
	}
	
	protected Properties createServerConfig() {
		Properties connectionConfig = new Properties();
		connectionConfig.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeServer);
		connectionConfig.put(CONFIG_KEYWORD_CONTAINER_ID, containerIDServer);
//		connectionConfig.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_TYPE, containerTypeServer);
//		connectionConfig.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_ID, containerIDServer);
		return connectionConfig;
	}
	
//	protected Connection createServerConnection(Properties connectionProperties) {
//		return server.createConnection(connectionProperties);
//	}
	
	protected void startServer() throws StartupException {
//		server.initialize();
//		serverConnection.connect();
		server.start();
	}
	
	protected void stopServer() {
//		server.dispose();
		server.stop();
	}
	
	protected void createClients(String... ids) {
//		clients = new HashMap<String, CommManager>();
//		CommManagerFactory fac = new DefaultCommManagerFactory();
//		for(int i = 0; i < ids.length; i++) {
//			Properties configuration = new Properties();
//			configuration.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
//			configuration.put(CONFIG_KEYWORD_TARGET_ID, containerIDServer);
//			configuration.put(CONFIG_KEYWORD_CONTAINER_ID, ids[i]);
//			CommManager client = fac.createCommManager();
//			client.setConfiguration(configuration);
//			clients.put(ids[i], client);
//		}
		clients = new HashMap<String, Client>();
		ClientFactory fac = new DefaultClientFactory();
		for(int i = 0; i < ids.length; i++) {
			Properties configuration = new Properties();
			configuration.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
			configuration.put(CONFIG_KEYWORD_TARGET_ID, containerIDServer);
			configuration.put(CONFIG_KEYWORD_CONTAINER_ID, ids[i]);
			// TODO about to come: config defaults
//			configuration.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_TYPE, containerTypeClient);
//			configuration.put(CONFIG_KEYWORD_DEFAULT_TARGET_ID, containerIDServer);
//			configuration.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_ID, ids[i]);
			Client client = fac.createClient(configuration);
//			client.setConfiguration(configuration);
			clients.put(ids[i], client);
		}
	}
	
//	protected Properties createConnectionPropertiesClient(String clientID) {
//		Properties connectionConfig = new Properties();
//		connectionConfig.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
//		connectionConfig.put(CONFIG_KEYWORD_TARGET_ID, containerIDServer);
//		connectionConfig.put(CONFIG_KEYWORD_CONTAINER_ID, clientID);
//		return connectionConfig;
//	}
	
//	protected Connection createClientConnection(String clientID, Properties connectionProperties) {
//		return getClient(clientID).createConnection(connectionProperties);
//	}
//	
//	protected void connectClient(String clientID) throws ContainerConnectException {
//		for(Connection connection : getClient(clientID).getConnections().values()) {
//			connection.connect();
//		}
//	}
	
	protected void startAllClients() throws StartupException {
		for(Client client : clients.values()) {
			client.start();
		}
	}
	
	protected void connectAllClients() throws ConnectException {
		for(Client client : clients.values()) {
			client.connect();
//			System.out.println("client "+client+" connections="+client.getConnections());
//			for(Connection connection : client.getConnections().values()) {
//				connection.connect();
//			}
		}
	}
	
	protected void disconnectAllClients() {
		for(Client client : clients.values()) {
			client.disconnect();
//			for(Connection connection : client.getConnections().values()) {
//				connection.disconnect();
//			}
		}
	}
	
	protected void stopAllClients() {
		for(Client client : clients.values()) {
//			client.dispose();
			client.stop();
		}
	}
	
	protected Client getClient(String id) {
		return clients.get(id);
//		return clients.get(id);
	}
	
	protected Server getServer() {
		return server;
	}
	
	/**
	 * @return First Connection found.
	 */
//	protected Connection getConnectionOfServer() {
//		return server.getConnections().values().iterator().next();
//	}
	
	/**
	 * @param id Client identifier
	 * @return First Connection found
	 */
//	protected Connection getConnectionOfClient(String id) {
//		return getClient(id).getConnections().values().iterator().next();
//	}
	
	/**
	 * @param id Client identifier
	 * @param connectionId Connection identifier
	 * @return First Connection with specified connectionId found
	 */
//	protected Connection getConnectionOfClient(String id, String connectionId) {
//		for(Connection connection : getClient(id).getConnections().values()) {
//			String conId = (String) connection.getConnectionContext().
//				getConfiguration().get(CONFIG_KEYWORD_CONNECTION_ID);
//			if(connectionId.equals(conId)) {
//				return connection;
//			}
//		}
//		return null;
//	}
//	
//	protected ID getContainerIDOf(Connection connection) {
//		return IDFactory.getDefault().createStringID(
//				(String) connection.getConnectionContext().getConfiguration().get(CONFIG_KEYWORD_CONTAINER_ID));
//	}
	
//	protected static Set<ConnectionActivityState> setOf(ConnectionActivityState... states) {
//		Set<ConnectionActivityState> statesSet = new HashSet<ConnectionActivityState>();
//		for(ConnectionActivityState state : states) {
//			statesSet.add(state);
//		}
//		return statesSet;
//	}

}
