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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.sharedobject.ISharedObjectManager;
import org.eclipse.ecf.datashare.IChannelContainerAdapter;

import de.fzi.replica.comm.channel.SignalChannelManager;
import de.fzi.replica.comm.channel.SignalChannelManagerImpl;
import de.fzi.replica.comm.internal.Activator;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.2, 19.07.2011
 */
public class ConnectionImpl implements Connection {

	private IContainer container;

	private String containerType;
	private String containerID;
	private String targetID;

	private SignalChannelManager signalChannelManager;
	private ISharedObjectContainer soContainer;
	private IChannelContainerAdapter cContainer;

	private ConnectionContext context;

	protected ConnectionImpl() {
		
	}

	@Override
	public void initialize() throws ContainerCreateException {
		try {
			// Create communication container
			IContainerFactory containerFactory = Activator.getDefault()
					.getContainerManager().getContainerFactory();
			if (containerID != null) {
				container = containerFactory.createContainer(containerType,
						new Object[] { containerID });
			} else {
				container = containerFactory.createContainer(containerType);
			}
			// Create signal channel manager
			signalChannelManager = new SignalChannelManagerImpl(this);
			// Create shared object container
			soContainer = (ISharedObjectContainer) container
					.getAdapter(ISharedObjectContainer.class);
			if (soContainer == null) {
				throw new ContainerCreateException(
						"Could not create shared object"
								+ " container. ECF Shared Object API module missing?");
			}
			cContainer = (IChannelContainerAdapter) container
					.getAdapter(IChannelContainerAdapter.class);
			if (cContainer == null) {
				throw new ContainerCreateException("Could not create channel"
						+ " container. ECF Datashare API module missing?");
			}
		} catch (Exception e) {
			throw new ContainerCreateException(e);
		}

		// Set up listeners for debugging TODO remove
		// container.addListener(new IContainerListener() {
		// @Override
		// public void handleEvent(IContainerEvent event) {
		// if(event instanceof IContainerConnectedEvent) {
		// IContainerConnectedEvent conEv = (IContainerConnectedEvent) event;
		// System.out.println(containerID+" received msg: "+conEv.getTargetID()+" connected");
		// } else if(event instanceof ISharedObjectActivatedEvent) {
		// ISharedObjectActivatedEvent actEv = (ISharedObjectActivatedEvent)
		// event;
		// System.out.println(containerID+" received msg: Shared object '"+
		// actEv.getActivatedID()+"' activated");
		// }
		// }
		// });
	}

	@Override
	public void connect() throws ContainerConnectException {
		if (container == null) {
			try {
				initialize();
			} catch (ContainerCreateException e) {
				throw new ContainerConnectException("Initialization failed", e);
			}
		}
		if (targetID != null) {
			container.connect(
					IDFactory.getDefault().createID(
							container.getConnectNamespace(), targetID),
					getConnectContext());
			// if("StringID[clientB]".equals(container.getID().toString())) {
			// System.out.println(container.getID()+" connectedID="+container.getConnectedID());
			// ISharedObjectContainer soCont = (ISharedObjectContainer)
			// container.getAdapter(ISharedObjectContainer.class);
			// ID soId =
			// IDFactory.getDefault().createStringID("replica://mySharedOntology/");
			// System.out.println("ConnectionImpl connected, shared object="+
			// soCont.getSharedObjectManager().getSharedObject(soId));
			// }
		}
	}

	/**
	 * Clients which need to use authentication can override this method. By
	 * default, no authentication is used when connecting.
	 * 
	 * @return IConnectContext Connect context object containing authentication
	 *         information
	 */
	protected IConnectContext getConnectContext() {
		// TODO read authentication details from configuration - is it wise to
		// store account data in properties?
		return null;
	}

	@Override
	public void disconnect() {
		if (container != null) {
			container.disconnect();
		}
	}

	@Override
	public void dispose() {
		if (container != null) {
			if (container.getConnectedID() != null) {
				disconnect();
			}
			container.dispose();
		}
	}

	@Override
	public ISharedObjectContainer getSharedObjectContainer() {
		return soContainer;
	}

	@Override
	public IChannelContainerAdapter getChannelContainerAdapter() {
		return cContainer;
	}

	@Override
	public SignalChannelManager getSignalChannelManager() {
		return signalChannelManager;
	}

	@Override
	public ConnectionContext getConnectionContext() {
		return context;
	}

	protected IContainer getContainer() {
		return container;
	}

	protected ISharedObjectManager getSharedObjectManager()
			throws ContainerCreateException {
		return getSharedObjectContainer().getSharedObjectManager();
	}

	protected void setConnectionContext(ConnectionContext context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "ConnectionImpl [container=" + container + ", containerType="
				+ containerType + ", containerID=" + containerID
				+ ", targetID=" + targetID + ", signalChannelManager="
				+ signalChannelManager + ", soContainer=" + soContainer
				+ ", cContainer=" + cContainer + ", context=" + context + "]";
	}
	
	/**
	 * @see ConnectionContext
	 * 
	 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
	 * @version 0.1, 18.07.2011
	 */
	public class ConnectionContextImpl implements ConnectionContext {

		protected Properties config;

		protected Map<Object, ConnectionActivity> activities;

		protected ConnectionContextImpl() {
			activities = new HashMap<Object, ConnectionActivity>();
		}

		@Override
		public Properties getConfiguration() {
			Properties config = new Properties();
			config.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerType);
			config.put(CONFIG_KEYWORD_CONTAINER_ID, containerID);
			// A server does not have a target ID
			if (targetID != null) {
				config.put(CONFIG_KEYWORD_TARGET_ID, targetID);
			}
			return config;
		}

		@Override
		public void setConfiguration(Properties config) {
			// TODO set reasonable defaults
			containerType = config.getProperty(CONFIG_KEYWORD_CONTAINER_TYPE);
			containerID = config.getProperty(CONFIG_KEYWORD_CONTAINER_ID);
			targetID = config.getProperty(CONFIG_KEYWORD_TARGET_ID);
		}

		@Override
		public ConnectionActivity addConnectionActivity(
				Object connectionActivityID) {
			ConnectionActivity connectionActivity = new ConnectionActivityImpl(
					connectionActivityID);
			activities.put(connectionActivityID, connectionActivity);
			return connectionActivity;
		}

		@Override
		public Map<Object, ConnectionActivity> getConnectionActivities() {
			return activities;
		}

		@Override
		public String toString() {
			return "ConnectionContextImpl [config=" + config + ", activities="
					+ activities + "]";
		}
		
		/**
		 * @see ConnectionActivity
		 * 
		 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
		 * @version 0.1, 18.07.2011
		 */
		public class ConnectionActivityImpl implements ConnectionActivity {

			private Object activityID;
//			private Map<ConnectionActivityState, Set<ConnectionActivityState>> stateMap;
//			private ConnectionActivityState currentState;
//			private ConnectionActivityState initialState;
			private Map<Enum<?>, Set<Enum<?>>> stateMap;
			private Enum<?> currentState;
			private Enum<?> initialState;
			private List<ConnectionActivityStateChangeListener> listeners;

			protected ConnectionActivityImpl(Object activityID) {
				this.activityID = activityID;
//				stateMap = new HashMap<ConnectionActivityState, Set<ConnectionActivityState>>();
				stateMap = new HashMap<Enum<?>, Set<Enum<?>>>();
//				stateMap = Collections.synchronizedMap(
//						new HashMap<Enum<?>, Set<Enum<?>>>());
				listeners = new ArrayList<ConnectionActivityStateChangeListener>();
			}

			@Override
			public Object getID() {
				return activityID;
			}

//			@Override
//			public void setConnectionActivityStates(
//					Map<ConnectionActivityState, Set<ConnectionActivityState>> stateMap) {
//				this.stateMap = stateMap;
////				this.stateMap = Collections.synchronizedMap(stateMap);
//			}
			
			@Override
			public void setConnectionActivityStates(
					Map<Enum<?>, Set<Enum<?>>> stateMap) {
				this.stateMap = stateMap;
//				this.stateMap = Collections.synchronizedMap(stateMap);
			}

			@Override
			public void registerConnectionActivityStateChangeListener(
					ConnectionActivityStateChangeListener listener) {
				listeners.add(listener);
			}

			@Override
			public void unregisterConnectionActivityStateListener(
					ConnectionActivityStateChangeListener listener) {
				listeners.remove(listener);
			}

//			@Override
//			public ConnectionActivityState setConnectionActivityState(
//					final ConnectionActivityState newState) {
//				if (!isReachable(newState)) {
//					throw new IllegalArgumentException(
//							"Unreachable connection activity state "
//									+ "in activity '" + activityID
//									+ "': transition " + currentState
//									+ "->" + newState);
//				}
//				// Setting the initial state
//				if (currentState == null) {
//					initialState = newState;
//					currentState = newState;
//					return null;
//				}
//				// Normal state change
//				final ConnectionActivityState previousState = currentState;
//				currentState = newState;
//				final ConnectionActivityStateChangeEvent changeEvent =
//						new ConnectionActivityStateChangeEvent() {
//					@Override
//					public ConnectionActivityState getNewState() {
//						return newState;
//					}
//					@Override
//					public ConnectionActivityState getPreviousState() {
//						return previousState;
//					}
//
//				};
//				for (final ConnectionActivityStateChangeListener listener : listeners) {
//					listener.onConnectionActivityStateChange(changeEvent);
//				}
//				return previousState;
//			}
			
			@Override
			public Enum<?> setConnectionActivityState(final Enum<?> newState) {
				if (!isReachable(newState)) {
					throw new IllegalArgumentException(
							"Unreachable connection activity state "
									+ "in activity '" + activityID
									+ "': transition " + currentState
									+ "->" + newState);
				}
				// Setting the initial state
				if (currentState == null) {
					initialState = newState;
					currentState = newState;
					return null;
				}
				// Normal state change
				final Enum<?> previousState = currentState;
				currentState = newState;
				final ConnectionActivityStateChangeEvent changeEvent =
						new ConnectionActivityStateChangeEvent() {
					@Override
					public Enum<?> getNewState() {
						return newState;
					}
					@Override
					public Enum<?> getPreviousState() {
						return previousState;
					}
				};
				for (final ConnectionActivityStateChangeListener
						listener : listeners) {
					listener.onConnectionActivityStateChange(changeEvent);
				}
				return previousState;
			}
			
			public void resetConnectionActivityState() {
				currentState = initialState;
			}
			
			@Override
			public Enum<?> getConnectionActivityState() {
				return currentState;
			}
			
			private boolean isReachable(Enum<?> newState) {
				// Setting the initial state does not require checking
				if (currentState == null) {
					return true;
				}
				if (newState == null) {
					throw new IllegalArgumentException(
							"No new state specified!");
				}
				return stateMap.get(currentState).contains(newState);
			}
			
			@Override
			public String toString() {
				return "ConnectionActivityImpl [activityID=" + activityID
						+ ", stateMap=" + stateMap + ", currentState="
						+ currentState + ", listeners=" + listeners + "]";
			}

		} // ConnectionActivityImpl

	} // ConnectionContextImpl

} // ConnectionImpl
