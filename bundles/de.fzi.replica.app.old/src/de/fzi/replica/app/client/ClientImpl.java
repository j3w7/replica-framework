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

package de.fzi.replica.app.client;

import static de.fzi.replica.app.msg.Message.REQUEST_ADD;
import static de.fzi.replica.app.msg.Message.REQUEST_GROUPS;
import static de.fzi.replica.app.msg.Message.REQUEST_ONTOIDS_GROUP;
import static de.fzi.replica.app.msg.Message.REQUEST_SOIDS;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObjectManager;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.OWLReplicaManager;
import de.fzi.replica.OWLReplicaOntology;
import de.fzi.replica.app.AbstractMasterSlaveConceptApplication;
import de.fzi.replica.app.ApplicationContext;
import de.fzi.replica.app.client.Client.AsyncMethodCallback.Result;
import de.fzi.replica.app.client.ClientConnectionActivityStates.AddOntology;
import de.fzi.replica.app.client.ClientConnectionActivityStates.GetGroups;
import de.fzi.replica.app.client.ClientConnectionActivityStates.GetOntologyIDs;
import de.fzi.replica.app.msg.Message;
import de.fzi.replica.comm.Connection;
import de.fzi.replica.comm.Connection.ConnectionContext;
import de.fzi.replica.comm.Connection.ConnectionContext.ConnectionActivity;
import de.fzi.replica.comm.channel.OnSignalReceivedListener;
import de.fzi.replica.comm.channel.Signal;
import de.fzi.replica.comm.channel.SignalChannel;
import de.fzi.replica.comm.channel.SignalChannel.SendMessageException;
import de.fzi.replica.comm.channel.SignalChannelManager.AddChannelException;
import de.fzi.replica.comm.util.MessageCarrier;
import de.fzi.replica.comm.util.ObjectMapBuilder;
import de.fzi.replica.util.OWLOntologyToOWLReplicaOntologyCopier;


/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.3, 22.07.2011
 *
 */
public class ClientImpl extends AbstractMasterSlaveConceptApplication 
		implements Client {
	
	// Cache method callbacks and other arguments
	private Map<Object, AsyncMethodCallback> methodCallbacks;
	private Map<Object, Object> args;
	
	protected ClientImpl(ApplicationContext context) {
		super(context);
		methodCallbacks = Collections.synchronizedMap(
				new HashMap<Object, AsyncMethodCallback>());
		args = Collections.synchronizedMap(
				new HashMap<Object, Object>());
	}
	
	@Override
	public void start() throws StartupException {
		super.start();
		try {
			Connection c = null;
			setConnection(c = getCommManager().createConnection(getConfiguration()));
			c.initialize();
			c.getSignalChannelManager().addChannel(
					CHANNEL_PRIMARY, getOnSignalReceivedListenerIDInfo(), null);
		} catch (AddChannelException e) {
			throw new StartupException("Exception when registering IDInfo" +
					"signal channel listener", e);
		} catch (ContainerCreateException e) {
			throw new StartupException("Exception when creating ECF "+
					"container, cause: "+e.getCause(), e);
		}
		System.out.println("ReplicaOntologyClient started");
	}
	
	private OnSignalReceivedListener getOnSignalReceivedListenerIDInfo() {
		return new OnSignalReceivedListener() {
			@Override
			public void onSignalReceived(Signal signal) {
				MessageCarrier mc = MessageCarrier.of(signal.getContent());
				Map<Object, Object> c = mc.getContent();
				long requestId = (Long) c.get("requestID");
				AsyncMethodCallback l = getCallback(requestId);
				switch(Message.getMsgTypeByID(signal.getType())) {
				case RESPONSE_ADD_OK:
					try {
						ISharedObjectManager soM = 
							getConnection().getSharedObjectContainer().
								getSharedObjectManager();
						OWLOntology onto = (OWLOntology) getArgument(requestId);
						OWLReplicaOntology rOnto = createOWLReplicaOntologyOf(onto);
						soM.addSharedObject(
								(ID) c.get("soid"),
								rOnto,
								null);
						new OWLOntologyToOWLReplicaOntologyCopier().copy(
								Collections.singleton(onto),
								rOnto);
						getConnectionActivityById(requestId).
							setConnectionActivityState(AddOntology.RECEIVED_OK);
						((OnOntologyAddedListener) l).
							onOntologyAdded(Result.OK);
					} catch (SharedObjectAddException e) {
						e.printStackTrace();
						getConnectionActivityById(requestId).
							setConnectionActivityState(AddOntology.EXCEPTION);
						((OnOntologyAddedListener) l).
							onOntologyAdded(Result.EXCEPTION);
					}
					break;
				case RESPONSE_SOIDS_OK:
					ISharedObjectManager soM = 
						getConnection().getSharedObjectContainer().
							getSharedObjectManager();
					OWLOntology onto = (OWLOntology)
						soM.getSharedObject((ID) c.get("soid"));
					((OnOntologyReceivedListener) l).
						onOntologyReceived(Result.OK, onto);
					break;
				case RESPONSE_ONTOIDS_GROUP_OK:
					try {						
						@SuppressWarnings("unchecked")
						Map<Object, Set<OWLOntologyID>> map =
							(Map<Object, Set<OWLOntologyID>>) c.get("ontoIDs");
						if (map != null && !map.isEmpty()) {
							((OnOntologyIDsReceivedListener) l).
								onOntologyIDsGot(Result.OK, map);
						} else {
							((OnOntologyIDsReceivedListener) l).
								onOntologyIDsGot(Result.NULL, null);
						}
					} catch (ClassCastException e) {
						((OnOntologyIDsReceivedListener) l).
							onOntologyIDsGot(Result.EXCEPTION, null);
					}
					break;
				case RESPONSE_SOIDS_FAIL_NOT_FOUND:
					((OnOntologyReceivedListener) l).
						onOntologyReceived(Result.NULL, null);
					break;
				case RESPONSE_GROUPS_OK:
					try {
						@SuppressWarnings("unchecked")
						Set<String> groups = (Set<String>) c.get("groups");
						if (groups == null) {
							throw new FetchException(
									"could not fetch ontology", null);
						}
						getConnectionActivityById(requestId).
							setConnectionActivityState(GetGroups.RECEIVED_OK);
						((OnGroupsReceivedListener) l).
							onGroupsGot(Result.OK, groups);
					} catch(FetchException e) {
						getConnectionActivityById(requestId).
							setConnectionActivityState(GetGroups.EXCEPTION);
						((OnGroupsReceivedListener) l).
							onGroupsGot(Result.EXCEPTION, null);
					}
					break;
				default:
					// TODO throw exception
					break;
				}
			}
		};
	}
	
	@Override
	public void connect() throws ConnectException {
		try {
			getConnection().connect();
		} catch (ContainerConnectException e) {
			throw new ConnectException("Could not connect", e);
		}
	}
	
	@Override
	public void setConfiguration(Properties config) {
		if(config != null) {
			getApplicationContext().setConfiguration(config);
			getCommManager().setConfiguration(config);
		}
	}
	
	@Override
	public Properties getConfiguration() {
		Properties config = new Properties();
		config.putAll(getApplicationContext().getConfiguration());		
		// Append CommManager's configuration
		config.putAll(getCommManager().getConfiguration());
		return config;
	}

	@Override
	public void stop() {
		super.stop();
		System.out.println("ReplicaOntology Client stopped");
	}
	
	@Override
	public void addOWLOntology(final OWLOntology ontology, final String group,
			OnOntologyAddedListener listener) throws AddException {
		Connection c = getConnection();
		ConnectionContext cntxt = c.getConnectionContext();
		final long requestId = generateRequestID();
		ConnectionActivity a = cntxt.addConnectionActivity(requestId);
		registerConnectionActivity(requestId, a);
		registerArgument(requestId, ontology);
		a.setConnectionActivityStates(AddOntology.stateMap);
		a.setConnectionActivityState(AddOntology.WAIT_FOR_SOID);
		try {
			SignalChannel idInfoChannel = getPrimaryChannel(c);
			idInfoChannel.sendSignal(
					IDFactory.getDefault().createStringID(
							cntxt.getConfiguration().
								getProperty(CONFIG_KEYWORD_TARGET_ID)
							),
					new Signal() {
						@Override
						public int getType() { return REQUEST_ADD.getId(); }
						@Override
						public byte[] getContent() {
							return MessageCarrier.create(
									ObjectMapBuilder.create().
										add("ontoID", ontology.getOntologyID()).
										add("groupID", group != null ? group : "").
										add("requestID", requestId).
										add("senderID", getLocalContainerID()).
										getMap()
									);
						}
					});
			registerCallback(requestId, listener);
		} catch (IDCreateException e) {
			e.printStackTrace();
			throw new AddException("Could not create ID", e);
		} catch (SendMessageException e) {
			e.printStackTrace();
			throw new AddException("Could not send message", e);
		} catch (ContainerCreateException e) {
			e.printStackTrace();
			throw new AddException("Could not create container", e);
		}
	}
	
	@Override
	public void getOWLOntology(final OWLOntologyID ontologyID,
			OnOntologyReceivedListener listener) throws FetchException {
		Connection c = getConnection();
		ConnectionContext cntxt = c.getConnectionContext();
		final long requestId = generateRequestID();
		ConnectionActivity a = cntxt.addConnectionActivity(requestId);
		registerConnectionActivity(requestId, a);
		registerArgument(requestId, ontologyID);
		a.setConnectionActivityStates(AddOntology.stateMap);
		a.setConnectionActivityState(AddOntology.WAIT_FOR_SOID);
		try {
			SignalChannel primaryChannel = getPrimaryChannel(c);
			primaryChannel.sendSignal(
					IDFactory.getDefault().createStringID(
							cntxt.getConfiguration().
								getProperty(CONFIG_KEYWORD_TARGET_ID)
							),
					new Signal() {
						@Override
						public int getType() { return REQUEST_SOIDS.getId(); }
						@Override
						public byte[] getContent() {
							return MessageCarrier.create(
									ObjectMapBuilder.create().
										add("ontoID", ontologyID).
										add("requestID", requestId).
										add("senderID", getLocalContainerID()).
										getMap()
									);
						}
					});
			registerCallback(requestId, listener);
		} catch (IDCreateException e) {
			e.printStackTrace();
			throw new FetchException("Could not create ID", e);
		} catch (SendMessageException e) {
			e.printStackTrace();
			throw new FetchException("Could not send message", e);
		} catch (ContainerCreateException e) {
			e.printStackTrace();
			throw new FetchException("Could not create container", e);
		}
	}
	
	@Override
	public void getOWLOntologyIDs(final Set<Object> groups,
			 OnOntologyIDsReceivedListener listener) throws FetchException {
		final long requestId = generateRequestID();
		Connection c = getConnection();
		ConnectionContext cntxt = c.getConnectionContext();
		ConnectionActivity a = cntxt.addConnectionActivity(requestId);
		registerConnectionActivity(requestId, a);
		a.setConnectionActivityStates(GetOntologyIDs.stateMap);
		a.setConnectionActivityState(GetOntologyIDs.WAIT_FOR_IDS);
		SignalChannel idInfoChannel;
		try {
			idInfoChannel = getPrimaryChannel(c);
			idInfoChannel.sendSignal(
					IDFactory.getDefault().createStringID(
							cntxt.getConfiguration().
								getProperty(CONFIG_KEYWORD_TARGET_ID)
							),
					new Signal() {
						@Override
						public int getType() { return REQUEST_ONTOIDS_GROUP.getId(); }
						@Override
						public byte[] getContent() {
							return MessageCarrier.create(
									ObjectMapBuilder.create().
									add("groups", groups).
									add("requestID", requestId).
									add("senderID", getLocalContainerID()).
									getMap()
							);
						}
					});
			registerCallback(requestId, listener);
		} catch (ContainerCreateException e) {
			e.printStackTrace();
			throw new FetchException("Could not create container, cause: "
					+ e.getCause(), e);
		} catch (IDCreateException e) {
			e.printStackTrace();
			throw new FetchException("Could not create ID, cause: "
					+ e.getCause(), e);
		} catch (SendMessageException e) {
			e.printStackTrace();
			throw new FetchException("Sending message failed, cause: "
					+ e.getCause(), e);
		}
	}
	
	@Override
	public void getGroups(OnGroupsReceivedListener listener) throws FetchException {
		final long requestId = generateRequestID();
		Connection c = getConnection();
		ConnectionContext cntxt = c.getConnectionContext();
		ConnectionActivity a = cntxt.addConnectionActivity(requestId);
		registerConnectionActivity(requestId, a);
		a.setConnectionActivityStates(GetGroups.stateMap);
		a.setConnectionActivityState(GetGroups.WAIT_FOR_GROUPS);
		SignalChannel idInfoChannel;
		try {
			idInfoChannel = getPrimaryChannel(c);
			idInfoChannel.sendSignal(
					IDFactory.getDefault().createStringID(
							cntxt.getConfiguration().
								getProperty(CONFIG_KEYWORD_TARGET_ID)
							),
					new Signal() {
						@Override
						public int getType() { return REQUEST_GROUPS.getId(); }
						@Override
						public byte[] getContent() {
							return MessageCarrier.create(
									ObjectMapBuilder.create().
									add("requestID", requestId).
									add("senderID", getLocalContainerID()).
									getMap()
							);
						}
					});
			registerCallback(requestId, listener);
		} catch (ContainerCreateException e) {
			e.printStackTrace();
			throw new FetchException("Could not create container, cause: "
					+ e.getCause(), e);
		} catch (IDCreateException e) {
			e.printStackTrace();
			throw new FetchException("Could not create ID, cause: "
					+ e.getCause(), e);
		} catch (SendMessageException e) {
			e.printStackTrace();
			throw new FetchException("Sending message failed, cause: "
					+ e.getCause(), e);
		}
	}
	
//	protected IConnectContext getConnectContext() {
//		// TODO get the password from secure location
//		IConnectContext connectContext = ConnectContextFactory.
//			createPasswordConnectContext("somesecretpassword");
//		return connectContext;
//	}
	
//	private void getCredentials() {
//	// Store
//	
//	IIDStore idStore = Activator.getDefault().getIDStore();
//	// Create an ID
//	ID newGUID = IDFactory.getDefault().createStringID("someid");
//	// Store it using the idStore
//	IIDEntry newGUIDEntry = idStore.store(newGUID);
//	// Associate and save password to secure store also
//	try {
//		newGUIDEntry.getPreferences().put("password","mypassword",true);
//	} catch (StorageException e1) {
//		e1.printStackTrace();
//	}
//
//	// Retrieve
//	
//	// Get namespace entry from store
//	INamespaceEntry restoredNamespace = idStore.getNamespaceEntry(newGUID.getNamespace());
//	// Get previously stored IDs
//	IIDEntry[] idEntries = restoredNamespace.getIDEntries();
//	// Create ID instance from idEntries[0]
//	ID restoredNewGUID = idEntries[0].createID();
//	// Get password from idEntries[0]
//	
//	try {
//		String restoredPassword = idEntries[0].getPreferences().get("password", "");
//		System.out.println("password is: "+restoredPassword); // lol...
//	} catch (StorageException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//
//	// Now can use restoredNewGUID and restoredPassword
//	
////	System.out.println();
////	try {
////		ISecurePreferences prefs = SecurePreferencesFactory.getDefault();
////		if(prefs != null) {
//////			prefs.put("somepassword", "mypassword", true);
//////			prefs.flush();
////			System.out.println("absolutPath()="+prefs.absolutePath());
////			System.out.println("secure prefs password: "+prefs.get("somepassword", "")+", is encrypted: "+prefs.isEncrypted("somepassword"));
////			System.out.println("is encrypted: "+prefs.isEncrypted("somepassword"));
////		}
////	} catch (StorageException e) {
////		// TODO Auto-generated catch block
////		e.printStackTrace();
////	}
//}
	
	private OWLReplicaOntology createOWLReplicaOntologyOf(OWLOntology onto) {
		OWLOntologyManager man = OWLReplicaManager.createOWLOntologyManager();
		try {
			return (OWLReplicaOntology) man.createOntology(onto.getOntologyID());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private AsyncMethodCallback getCallback(Object id) {
		return methodCallbacks.get(id);
	}
	
	private void registerCallback(Object id, AsyncMethodCallback c) {
		methodCallbacks.put(id, c);
	}
	
	private synchronized Object getArgument(Object id) {
		return args.get(id);
	}
	
	private void registerArgument(Object id, Object arg) {
		args.put(id, arg);
	}

}
