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

package de.fzi.replica.comm;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import de.fzi.replica.comm.internal.Activator;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 30.11.2010
 */
public class CommManagerImpl implements CommManager {
	
	protected Map<String, Connection> connections;
	
	protected Properties config;
	
	private String defaultContainerType;
	private String defaultContainerID;
	private String defaultTargetID;
	
	protected CommManagerImpl() {
		connections = new HashMap<String, Connection>();
	}
	
	@Override
	public void initialize() {
		// TODO Read configuration, apply values
	}
	
	@Override
	public void dispose() {
		for(Connection connection : connections.values()) {
			connection.dispose();
			connection = null;
		}
		Activator.getDefault().getContainerManager().removeAllContainers();
	}
	
	@Override
	public Connection createConnection(Properties connectionProperties) {
		// Prepare context object containing configuration and authentication data
		ConnectionImpl connection = new ConnectionImpl();
		Connection.ConnectionContext context = connection.new ConnectionContextImpl();
		
		// TODO defaults mechanism revision
		//context.setConfiguration(applyDefaultsTo(connectionProperties));
		
		context.setConfiguration(connectionProperties);
		connection.setConnectionContext(context);
		// Keep a link to the connection
		connections.put(
				connectionProperties.getProperty(
						Connection.CONFIG_KEYWORD_CONNECTION_ID,
						"connection"+connections.size()),
				connection);
		return connection;
	}
	
	@Override
	public Map<String, Connection> getConnections() {
		return connections;
	}
	
	@Override
	public Properties getConfiguration() {
		Properties config = new Properties();
		if (defaultContainerType != null)
			config.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_TYPE, defaultContainerType);
		if (defaultTargetID != null)
			config.put(CONFIG_KEYWORD_DEFAULT_TARGET_ID, defaultTargetID);
		if (defaultContainerID != null)
			config.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_ID, defaultContainerID);
		return config;
	}
	
	@Override
	public void setConfiguration(Properties config) {
		// TODO set reasonable defaults
		defaultContainerType  = config.getProperty(CONFIG_KEYWORD_DEFAULT_CONTAINER_TYPE);
		defaultContainerID  = config.getProperty(CONFIG_KEYWORD_DEFAULT_CONTAINER_ID);
		defaultTargetID  = config.getProperty(CONFIG_KEYWORD_DEFAULT_TARGET_ID);
	}
	
	@Override
	public String toString() {
		return "CommManagerImpl [connections=" + connections + ", config="
				+ config + ", defaultContainerType=" + defaultContainerType
				+ ", defaultContainerID=" + defaultContainerID
				+ ", defaultTargetID=" + defaultTargetID + "]";
	}
	
	// TODO this is crap, think of a good mechanism for defaults
//	private Properties applyDefaultsTo(Properties connectionProperties) {
//		if(connectionProperties == null) {
//			connectionProperties = new Properties();
//			connectionProperties.put(Connection.CONFIG_KEYWORD_CONTAINER_TYPE, defaultContainerType);
//			connectionProperties.put(Connection.CONFIG_KEYWORD_CONTAINER_ID, defaultContainerID);
//			connectionProperties.put(Connection.CONFIG_KEYWORD_TARGET_ID, defaultTargetID);
//		} else {			
//			if(!connectionProperties.containsKey(Connection.CONFIG_KEYWORD_CONTAINER_TYPE)) {
//				connectionProperties.put(Connection.CONFIG_KEYWORD_CONTAINER_TYPE, defaultContainerType);
//			}
//			if(!connectionProperties.contains(Connection.CONFIG_KEYWORD_CONTAINER_ID)) {
//				connectionProperties.put(Connection.CONFIG_KEYWORD_CONTAINER_ID, defaultContainerID);
//			}
//			if(!connectionProperties.contains(Connection.CONFIG_KEYWORD_CONTAINER_ID)) {
//				connectionProperties.put(Connection.CONFIG_KEYWORD_TARGET_ID, defaultTargetID);
//			}
//		}
//		return connectionProperties;
//	}
	
}
