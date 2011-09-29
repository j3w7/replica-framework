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

package de.fzi.replica.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;

import de.fzi.replica.comm.CommManager;
import de.fzi.replica.comm.CommManagerImpl;
import de.fzi.replica.comm.Connection;
import de.fzi.replica.comm.Connection.ConnectionContext.ConnectionActivity;
import de.fzi.replica.comm.channel.SignalChannel;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.4, 20.07.2011
 * 
 */
public abstract class AbstractReplicaOntologyApplication implements
		Application {

	/**
	 * ID of the primary messaging channel. This channel is used to transmit
	 * OntologyIDs to avoid the activation of a shared object when it is not
	 * necessary. But it can also be used to transmit arbitrary data.
	 */
	protected static final ID CHANNEL_PRIMARY = IDFactory.getDefault()
			.createStringID("replica://channel/primary");
	
	private Map<Object, ConnectionActivity> requestIdMap;

	private final Object lock = new Object();
	private boolean shutdown = false;

	private ApplicationContext context;

	protected AbstractReplicaOntologyApplication(ApplicationContext context) {
		this.context = context;
		requestIdMap = new HashMap<Object, ConnectionActivity>();
	}

	protected ApplicationContext getApplicationContext() {
		return context;
	}
	
	protected CommManager getCommManager() {
		return (CommManager) context.getService(CommManagerImpl.class);
	}
	
	protected ConnectionActivity getConnectionActivityById(Object id) {
		return requestIdMap.get(id);
	}
	
	protected void registerConnectionActivity(Object id, ConnectionActivity a) {
		requestIdMap.put(id, a);
	}
	
	@Override
	public void start() throws StartupException {
//		CommManager cm = getCommManager();
//		Properties cmDefaults = new Properties();
//		cmDefaults.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_TYPE,
//				getConfiguration().get(CONFIG_KEYWORD_DEFAULT_CONTAINER_TYPE));
//		cmDefaults.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_ID, getConfiguration()
//				.get(CONFIG_KEYWORD_DEFAULT_CONTAINER_ID));
//		cmDefaults.put(CONFIG_KEYWORD_DEFAULT_TARGET_ID, getConfiguration()
//				.get(CONFIG_KEYWORD_DEFAULT_TARGET_ID));
//		cm.setConfiguration(cmDefaults);
		getCommManager().initialize();
	}
	
	@Override
	public void stop() {
		getCommManager().dispose(); // Implies disconnecting all connections
		synchronized (lock) {
			shutdown = true;
			lock.notifyAll();
		}
	}

	/**
	 * Extracts the message which has been sent on the ID info channel from raw
	 * data, ignoring the message type ID part.
	 * 
	 * @param raw
	 * @return
	 * 
	 * @deprecated not required with new SignalChannel concept
	 */
	protected String readMessageIDInfo(byte[] raw) {
		// TODO may cause problems when different default charsets are used on
		// peers
		return new String(raw, 1, raw.length - 1);
	}

	protected void waitUntilShutdown() {
		synchronized (lock) {
			while (!shutdown) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected Connection getConnection(String connectionID) {
		return getCommManager().getConnections().get(connectionID);
	}

	protected SignalChannel getPrimaryChannel(Connection connection)
			throws ContainerCreateException {
		return connection.getSignalChannelManager().getChannels()
				.get(CHANNEL_PRIMARY);
	}
	
	protected static long generateRequestID() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	protected static Set<Enum<?>> setOf(Enum<?>... states) {
		Set<Enum<?>> statesSet = new HashSet<Enum<?>>();
		for (Enum<?> state : states) {
			statesSet.add(state);
		}
		return statesSet;
	}

}
