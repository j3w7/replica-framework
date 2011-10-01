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

package de.fzi.replica.app.server;

import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainerListener;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.IContainerEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.security.IConnectHandlerPolicy;
import org.eclipse.ecf.core.sharedobject.ReplicaSharedObjectDescription;
import org.eclipse.ecf.core.sharedobject.events.ISharedObjectActivatedEvent;
import org.eclipse.ecf.core.sharedobject.security.ISharedObjectPolicy;
import org.semanticweb.owlapi.model.OWLOntologyID;

import de.fzi.replica.comm.Connection;
import de.fzi.replica.comm.channel.OnSignalReceivedListener;
import de.fzi.replica.comm.channel.Signal;
import de.fzi.replica.comm.channel.SignalChannel.SendMessageException;
import de.fzi.replica.comm.channel.SignalChannelManager.AddChannelException;
import de.fzi.replica.comm.util.MessageCarrier;
import de.fzi.replica.comm.util.ObjectMapBuilder;
import de.fzi.replica.app.AbstractMasterSlaveConceptApplication;
import de.fzi.replica.app.ApplicationContext;
import de.fzi.replica.app.msg.Message;


/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.3, 22.07.2011
 *
 */
public class ServerImpl extends AbstractMasterSlaveConceptApplication
		implements Server {
	
//	private static final String DEFAULT_CONTAINER_TYPE = "ecf.generic.server";
//	private static final String DEFAULT_CONTAINER_IDENTITY = "ecftcp://localhost:5888/server";
	
	private List<ID> clientIDs;
	private Map<Object, Set<ID>> groups;
	// TODO use bidimap
	private Map<OWLOntologyID, ID> ontoSoid;
	private Map<ID, OWLOntologyID> soidOnto;
	
	protected ServerImpl(ApplicationContext context) {
		super(context);
		initMaps();
	}
	
	@Override
	public void start() throws StartupException {
		super.start();
		initMaps();
		try {
			Connection c = getCommManager().createConnection(getConfiguration());
			setConnection(c);
			c.initialize();
			c.connect();
			c.getSignalChannelManager().addChannel(
					CHANNEL_PRIMARY, getOnSignalReceivedListenerIDInfo(), null);
		} catch (AddChannelException e) {
			throw new StartupException("Adding channel failed, cause: "
					+ e.getCause(), e);
		} catch (ContainerConnectException e) {
			throw new StartupException("Connecting failed, cause: "
					+ e.getCause(), e);
		} catch (ContainerCreateException e) {
			throw new StartupException("Could not create container, cause: "
					+ e.getCause(), e);
		}
		registerContainerEventListeners();
		setPolicies();
		System.out.println("ReplicaOntology Server started");
	}
	
	@Override
	public void stop() {
		super.stop();
		clientIDs = null;
		groups = null;
		ontoSoid = null;
		soidOnto = null;
	}
	
	private void initMaps() {
		clientIDs = new ArrayList<ID>();
		groups = new HashMap<Object, Set<ID>>();
		ontoSoid = new HashMap<OWLOntologyID, ID>();
		soidOnto = new HashMap<ID, OWLOntologyID>();
		// TODO API revision for adding groups - client/server side?
		groups.put("devgroup0", Collections.singleton(
				IDFactory.getDefault().createStringID("onto0")));
		groups.put("devgroup1", Collections.singleton(
				IDFactory.getDefault().createStringID("onto1")));
		groups.put("devgroup2", Collections.singleton(
				IDFactory.getDefault().createStringID("onto2")));
	}
	
	@Override
	public Properties getConfiguration() {
		return getApplicationContext().getConfiguration();
	}
	
	@Override
	public void setConfiguration(Properties config) {
		getApplicationContext().setConfiguration(config);
		// Apply configuration to registered services
		getCommManager().setConfiguration(config);
		// TODO Set server specific configuration (not any yet)
	}
	
	protected void registerContainerEventListeners() {
		getConnection().getSharedObjectContainer().addListener(
				new IContainerListener() {
			@Override
			public void handleEvent(IContainerEvent event) {
				if (event instanceof IContainerConnectedEvent) {
					ID clientID = ((IContainerConnectedEvent) event).getTargetID();
					clientIDs.add(clientID);
					System.out.println("Client connected: "+clientID);
				} else if (event instanceof IContainerDisconnectedEvent) {
					ID clientID = ((IContainerDisconnectedEvent) event).getTargetID();
					clientIDs.remove(clientID);
					System.out.println("Client '"+clientID+"' disconnected");
				} else if (event instanceof ISharedObjectActivatedEvent) {
					ID activatedID = ((ISharedObjectActivatedEvent) event).getActivatedID();
					ontoSoid.put(soidOnto.get(activatedID), activatedID);
//					System.out.println("Server: shared object activated "+
//							activatedID);
				}
//				else if(event instanceof IContainerSharedObjectMessageReceivingEvent) {
//					IContainerSharedObjectMessageReceivingEvent msgReceivedEvent = (IContainerSharedObjectMessageReceivingEvent) event;
//					System.out.println("Message '"+msgReceivedEvent.getMessage()+"' from '"+msgReceivedEvent.getSendingContainerID()+"'");
//				}
			}
		});
//		getConnection().getChannelContainerAdapter().addListener(new IChannelContainerListener() {
//			@Override
//			public void handleChannelContainerEvent(IChannelContainerEvent event) {
//				System.out.println("event="+event);
//			}
//		});
	}
	
	protected OnSignalReceivedListener getOnSignalReceivedListenerIDInfo() {
		return new OnSignalReceivedListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onSignalReceived(Signal signal) {
				MessageCarrier mc = MessageCarrier.of(signal.getContent());
				final Map<Object, Object> c = mc.getContent();
				try {
					switch(Message.getMsgTypeByID(signal.getType())) {
					case REQUEST_ADD:
						// Client wants to add a new shared object and asks for an ID
						final ID newSoid = generateNewSOID();
						getPrimaryChannel(connection).sendSignal(
								(ID) c.get("senderID"), new Signal() {
							@Override
							public int getType() {
								return Message.RESPONSE_ADD_OK.getId();
							}
							@Override
							public byte[] getContent() {
								return MessageCarrier.create(
										ObjectMapBuilder.create().
											add("soid", newSoid).
											add("senderID", getLocalContainerID()).
											add("requestID", c.get("requestID")).
											getMap()
										);
							}
						});
						// Add ontology mapping TODO: what if procedure crashes here?
						OWLOntologyID ontoId = (OWLOntologyID) c.get("ontoID");
						ontoSoid.put(ontoId, newSoid);
						soidOnto.put(newSoid, ontoId);
//						System.out.println("ServerImpl: ontoSoid="+
//								ontoSoid+", soidOnto="+soidOnto);
						// Add group mappings, if groups are specified
						Object groupsIds = c.get("groupsIDs");
						if(groupsIds != null && groupsIds != "") {
							System.out.println("groupsIDs="+groupsIds);
							for(Object groupId : (Set<Object>) groupsIds) {
								Set<ID> group = groups.get(groupsIds);
								if (group != null) {
									// Group exits already
									group.add(newSoid);
								} else {
									// Group not yet existant, create one
									Set<ID> soids = new HashSet<ID>();
									soids.add(newSoid);
									groups.put(groupId, soids);
								}
							}
						}
						break;
					case REQUEST_SOIDS:
						// Client needs shared object IDs of an ontology
						final ID soid =
							ServerImpl.this.ontoSoid.get(
									(OWLOntologyID) c.get("ontoID"));
						if (soid != null) {
							// Shared ontology found, send it
							getPrimaryChannel(connection).sendSignal(
									(ID) c.get("senderID"), new Signal() {
										@Override
										public int getType() {
											return Message.RESPONSE_SOIDS_OK.getId();
										}
										@Override
										public byte[] getContent() {
											return MessageCarrier.create(
													ObjectMapBuilder.create().
													add("soid", soid).
													add("senderID", getLocalContainerID()).
													add("requestID", c.get("requestID")).
													getMap()
											);
										}
									});
						} else {
							// Shared ontology not found
							getPrimaryChannel(connection).sendSignal(
									(ID) c.get("senderID"), new Signal() {
										@Override
										public int getType() {
											return Message.RESPONSE_SOIDS_FAIL_NOT_FOUND.getId();
										}
										@Override
										public byte[] getContent() {
											return MessageCarrier.create(
													ObjectMapBuilder.create().
													add("senderID", getLocalContainerID()).
													add("requestID", c.get("requestID")).
													getMap()
											);
										}
									});
						}
						break;
					case REQUEST_ONTOIDS_GROUP:
						@SuppressWarnings("unchecked")
						Set<Object> groups = (Set<Object>) c.get("groups");
						// List ontology IDs in groups
						if (groups != null) {
							// List ontology IDs in groups
							final Map<Object, Set<OWLOntologyID>> ontoIds =
								new HashMap<Object, Set<OWLOntologyID>>();
							if (ServerImpl.this.groups.keySet().containsAll(
									groups)) {
								// Server knows all groups
								for(Object gId : groups) {
									// Create onto id set from soid map
									Set<OWLOntologyID> conv = new HashSet<OWLOntologyID>();
									for(ID id: ServerImpl.this.groups.get(gId)) {
										conv.add(ServerImpl.this.soidOnto.get(id));
									}
									ontoIds.put(gId, conv);
								}
								getPrimaryChannel(connection).sendSignal(
										(ID) c.get("senderID"), new Signal() {
											@Override
											public int getType() {
												return Message.RESPONSE_ONTOIDS_GROUP_OK.getId();
											}
											@Override
											public byte[] getContent() {
												return MessageCarrier.create(
														ObjectMapBuilder.create().
														add("ontoIDs", ontoIds).
														add("senderID", getLocalContainerID()).
														add("requestID", c.get("requestID")).
														getMap()
												);
											}
										});
							} else {
								// Not all groups are known to the server
								getPrimaryChannel(connection).sendSignal(
										(ID) c.get("senderID"), new Signal() {
											@Override
											public int getType() {
												return Message.RESPONSE_ONTOIDS_GROUP_FAIL_NOT_FOUND.getId();
											}
											@Override
											public byte[] getContent() {
												return MessageCarrier.create(
														ObjectMapBuilder.create().
														add("senderID", getLocalContainerID()).
														add("requestID", c.get("requestID")).
														getMap()
												);
											}
										});
							}
						} else {
							// List all registered IDs
//							FAIL_NOT_FOUND
							// TODO
						}
						break;
					case REQUEST_GROUPS:
						// Client requests a list of groups
						getPrimaryChannel(connection).sendSignal(
								(ID) c.get("senderID"), new Signal() {
							@Override
							public int getType() {
								return Message.RESPONSE_GROUPS_OK.getId();
							}
							@Override
							public byte[] getContent() {
								Set<Object> groups = new HashSet<Object>(
										ServerImpl.this.groups.keySet());
								return MessageCarrier.create(
										ObjectMapBuilder.create().
											add("groups", groups).
											add("senderID", getLocalContainerID()).
											add("requestID", c.get("requestID")).
											getMap()
										);
							}
						});
						break;
					default:
						break;
					}
				} catch (ContainerCreateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SendMessageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	
	public List<ID> getClientIDs() {
		return clientIDs;
	}
	
	/**
	 * @return New unique shared object ID
	 */
	protected static ID generateNewSOID() {
		// TODO manage pool of shared object IDs
		return IDFactory.getDefault().createGUID();
//		return IDFactory.getDefault().createStringID(""+(long) (Math.random() * Long.MAX_VALUE));
	}
	
	protected void setPolicies() {
//		getISharedObjectContainerManager().setConnectPolicy(createConnectPolicy());
//		getSharedObjectManager().setRemoteAddPolicy(createSharedObjectPolicy());
	}
	
	protected IConnectHandlerPolicy createConnectPolicy() {
		return new IConnectHandlerPolicy() {
			@Override
			public PermissionCollection checkConnect(Object address, ID fromID,
					ID targetID, String targetGroup, Object connectData)
					throws Exception {
				// TODO
				return null;
			}
			@Override
			public void refresh() {
				// TODO
			}
		};
	}
	
	protected ISharedObjectPolicy createSharedObjectPolicy() {
		return new ISharedObjectPolicy() {
			@Override
			public PermissionCollection checkAddSharedObject(
					ID fromID, ID toID, ID localID,
					ReplicaSharedObjectDescription newObjectDescription)
					throws SecurityException {
				// TODO
				return null;
			}
			@Override
			public void refresh() {
				// TODO
			}
		};
	}
	
}
