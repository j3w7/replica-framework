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

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.datashare.IChannelContainerAdapter;

import de.fzi.replica.comm.channel.SignalChannelManager;

/**
 * Represents a connection to a peer. A connection needs to be configured
 * prior to establishing a connection. Every connection has a {@link ConnectionContext}
 * associated to it which provides access to all relevant components
 * and the connection configuration. <code>Connection</code> is the main
 * interface to provide all relevant ECF components which are currently:
 * <ul>
 * 	<li>SharedObject API</li>
 * 	<li>Datashare API</li>
 * </ul>
 * Moreover it provides access to components which sit upon the
 * ECF modules which are currently:
 * <ul>
 * 	<li>{@link de.fzi.replica.comm.channel.SignalChannelManager}</li>
 * </ul>
 * A Connection has a context represented by a {@link ConnectionContext}.
 * All details relevant to a connection are accessible through this interface.
 * 
 * @see ConnectionContext
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.2, 19.07.2011
 */
public interface Connection {
	
	// TODO comments...
	String CONFIG_KEYWORD_CONNECTION_ID = "connectionID";
	String CONFIG_KEYWORD_CONTAINER_TYPE = "containerType";
	String CONFIG_KEYWORD_CONTAINER_ID = "containerID";
	String CONFIG_KEYWORD_TARGET_ID = "targetID";
	String CONFIG_KEYWORD_SHAREDOBJECTS = "sharedObjects";
	String CONFIG_KEYWORD_CHANNELS = "channels";
	
	void initialize() throws ContainerCreateException;
	
	void connect() throws ContainerConnectException;
	
	void disconnect();
	
	void dispose();
	
	// TODO adapter pattern as in ECF
	
//	Object getConnectionAspect(Class<?> service);
	
	ISharedObjectContainer getSharedObjectContainer();
	
	IChannelContainerAdapter getChannelContainerAdapter();
	
	SignalChannelManager getSignalChannelManager();
	
	ConnectionContext getConnectionContext();
	
	/**
	 * The context of a connection encapsulates all connection configuration
	 * data and provides access to {@link ConnectionActivity} objects.
	 * 
	 * @author Jan Novacek novacek@fzi.de
	 * @version 0.1, 29.11.2010
	 */
	interface ConnectionContext {
		
		/**
		 *  Recognized configuration keys are:
		 * <table border="1">
		 * 	<caption>Recognized configuration key/value paisrs</caption>
		 * 	<tr><th>Key</th><th>Description</th></tr>
		 * 	<tr><td>connectionID</td><td>ID of the connection</td></tr>
		 * 	<tr><td>containerType</td><td>The type of the underlying ECF container instance</td></tr>
		 * 	<tr><td>containerID</td><td>The ID of the container</td></tr>
		 * 	<tr><td>targetID</td><td>ID of the target container</td></tr>
		 * 	<tr><td>sharedObjects</td><td>{true|false} Whether shared object support should be enabled</td></tr>
		 * 	<tr><td>channels</td><td>{true|false} Whether channel support should be enabled</td></tr>
		 * </table>
		 * 
		 * @param configuration The connection configuration as Properties object.
		 */
		void setConfiguration(Properties configuration);
		
		Properties getConfiguration(); // should return non-modifiable properties object
		
		ConnectionActivity addConnectionActivity(Object connectionActivityID);
		
		Map<Object, ConnectionActivity> getConnectionActivities();
		
		/**
		 * A connection activity is a list of {@link ConnectionActivityState}s.
		 * They represent a sequence of connection states in a in a communication
		 * procedure and provide a way of knowing about the current position in the
		 * procedure and the ability to set a state change listener.
		 * 
		 * @author Jan Novacek novacek@fzi.de
		 * @version 0.2, 21.07.2011
		 */
		interface ConnectionActivity {
			
			Object getID();
			
//			void setConnectionActivityStates(Map<ConnectionActivityState,
//					Set<ConnectionActivityState>> stateMap);
//			
//			ConnectionActivityState setConnectionActivityState(
//					ConnectionActivityState newState);
//			
//			ConnectionActivityState getConnectionActivityState();
			
			void setConnectionActivityStates(Map<Enum<?>, Set<Enum<?>>> stateMap);
			
			Enum<?> setConnectionActivityState(Enum<?> newState);
			
			Enum<?> getConnectionActivityState();
			
			void resetConnectionActivityState();
			
			void registerConnectionActivityStateChangeListener(
					ConnectionActivityStateChangeListener listener);
			
			void unregisterConnectionActivityStateListener(
					ConnectionActivityStateChangeListener listener);
			
			/**
			 * Represents a specific point in a communication procedure.
			 * 
			 * @author Jan Novacek novacek@fzi.de
			 * @version 0.1, 30.11.2010
			 * 
			 * @deprecated Using enum now
			 */
//			interface ConnectionActivityState {
//				
//				int getID();
//				
//			}
			
			/**
			 * @author Jan Novacek novacek@fzi.de
			 * @version 0.2, 21.07.2011
			 */
			interface ConnectionActivityStateChangeEvent {
				
//				ConnectionActivityState getNewState();
//				
//				ConnectionActivityState getPreviousState();
				
				Enum<?> getNewState();
				
				Enum<?> getPreviousState();
				
			}
			
			/**
			 * @author Jan Novacek novacek@fzi.de
			 * @version 0.1, 01.12.2010
			 */
			interface ConnectionActivityStateChangeListener {
				
				void onConnectionActivityStateChange(ConnectionActivityStateChangeEvent event);
				
			}
			
		}
		
	}

}
