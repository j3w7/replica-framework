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

package de.fzi.replica.comm.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.fzi.replica.comm.Connection.ConnectionContext.ConnectionActivity;
import de.fzi.replica.comm.Connection.ConnectionContext.ConnectionActivity.ConnectionActivityStateChangeEvent;
import de.fzi.replica.comm.Connection.ConnectionContext.ConnectionActivity.ConnectionActivityStateChangeListener;
import de.fzi.replica.comm.channel.OnSignalReceivedListener;
import de.fzi.replica.comm.channel.Signal;
import de.fzi.replica.comm.channel.SignalChannel;
import de.fzi.replica.comm.channel.SignalChannelManager;
import de.fzi.replica.comm.channel.SignalChannel.SendMessageException;
import de.fzi.replica.comm.channel.SignalChannelManager.AddChannelException;
import de.fzi.replica.comm.test.ConnectionActivityTestCase.ClientAConnectionActivityStates.ClConActAState;
import de.fzi.replica.comm.test.ConnectionActivityTestCase.ClientBConnectionActivityStates.ClConActBState;
import de.fzi.replica.comm.util.AbstractConnectionActivityStates;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 05.12.2010
 */
public class ConnectionActivityTestCase extends AbstractCommTestCase {

	private static final String A = "clientA";
	private static final String B = "clientB";

	private static final ID TEST_CHANNEL_ID = IDFactory.getDefault()
			.createStringID("replica://channel/test");

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		createServer();
		createServerConnection(createConnectionPropertiesServer());
		startServer(getConnectionOfServer());
		createClients(A, B);
		createClientConnection(A, createConnectionPropertiesClient(A));
		createClientConnection(B, createConnectionPropertiesClient(B));
		connectAllClients();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Using enums as best practice for readability and performance.
	 */

	enum SignalID {
		REQUEST_ONE(101),
		RESPONSE_ONE(102),
		REQUEST_TWO(103),
		FINISH(105);
		int id;
		static Map<Integer, SignalID> map;

		SignalID(int id) {
			this.id = id;
		}

		static {
			map = new HashMap<Integer, SignalID>();
			map.put(REQUEST_ONE.id, REQUEST_ONE);
			map.put(RESPONSE_ONE.id, RESPONSE_ONE);
			map.put(REQUEST_TWO.id, REQUEST_TWO);
			map.put(FINISH.id, FINISH);
		}

		static SignalID getSignalID(int id) {
			return map.get(id);
		}
	}

	static class ClientAConnectionActivityStates extends
			AbstractConnectionActivityStates {
		enum ClConActAState {
			WAIT_FOR_102(102),
			RECEIVED_102(202),
			WAIT_FOR_FINISH(104),
			RECEIVED_FINISH(105);
			int id;
			static Map<Enum<?>, Set<Enum<?>>> stateMap;

			ClConActAState(int id) {
				this.id = id;
			}

			static {
				stateMap = new HashMap<Enum<?>, Set<Enum<?>>>();
				stateMap.put(WAIT_FOR_102, setOf(RECEIVED_102));
				stateMap.put(RECEIVED_102, setOf(WAIT_FOR_FINISH));
				stateMap.put(WAIT_FOR_FINISH, setOf(RECEIVED_FINISH));
			}
		}
	}

	static class ClientBConnectionActivityStates extends
			AbstractConnectionActivityStates {
		enum ClConActBState {
			WAIT_FOR_101(101),
			RECEIVED_101(201),
			WAIT_FOR_103(103),
			RECEIVED_103(203),
			WAIT_FOR_FINISH(104),
			RECEIVED_FINISH(105);
			int id;
			static Map<Enum<?>, Set<Enum<?>>> stateMap;

			ClConActBState(int id) {
				this.id = id;
			}

			static {
				stateMap = new HashMap<Enum<?>, Set<Enum<?>>>();
				stateMap.put(WAIT_FOR_101, setOf(RECEIVED_101));
				stateMap.put(RECEIVED_101, setOf(WAIT_FOR_103));
				stateMap.put(WAIT_FOR_103, setOf(RECEIVED_103));
				stateMap.put(RECEIVED_103, setOf(WAIT_FOR_FINISH));
				stateMap.put(WAIT_FOR_FINISH, setOf(RECEIVED_FINISH));
			}
		}
	}

	/**
	 * The following situation is tested:
	 * 
	 * <pre>
	 * SignalID
	 *             ClientA      ClientB
	 * waitFor102     |            | waitFor101
	 *                |----101---->|
	 *                |            | received101
	 *                |<---102-----|
	 * received102    |            | waitFor103
	 *                |----103---->|
	 * waitForFinish  |            | received103
	 *                |<---105-----|
	 * receivedFinish |            | waitForFinish
	 *                |----105---->|
	 *                |            | receivedFinish
	 * </pre>
	 * 
	 * @throws AddChannelException
	 * @throws SendMessageException
	 * @throws InterruptedException
	 */
	@Test
	public void testActivityProgress() throws AddChannelException,
			SendMessageException {
		final String requestID = "Request R100";
		final String responseID = "Response R100";

		// Create the connection activities
		final ConnectionActivity request = getConnectionOfClient(A)
				.getConnectionContext().addConnectionActivity(requestID);
		final ConnectionActivity response = getConnectionOfClient(B)
				.getConnectionContext().addConnectionActivity(responseID);
		// Set initial states
		request.setConnectionActivityState(ClConActAState.WAIT_FOR_102);
		response.setConnectionActivityState(ClConActBState.WAIT_FOR_101);
		// Set the state maps at the activities
		request.setConnectionActivityStates(ClConActAState.stateMap);
		response.setConnectionActivityStates(ClConActBState.stateMap);

		// Create a test signal channel to transmit some test messages
		SignalChannelManager sigChManClientA = getConnectionOfClient(A)
				.getSignalChannelManager();
		SignalChannelManager sigChManClientB = getConnectionOfClient(B)
				.getSignalChannelManager();

		sigChManClientA.addChannel(TEST_CHANNEL_ID,
				new OnSignalReceivedListener() {
			@Override
			public void onSignalReceived(Signal signal) {
				switch (SignalID.getSignalID(signal.getType())) {
				case RESPONSE_ONE: // 102 received, wait for FINISH
					request.setConnectionActivityState(ClConActAState.RECEIVED_102);
					break;
				case FINISH: // FINISH received, final state
					request.setConnectionActivityState(ClConActAState.RECEIVED_FINISH);
				default:
					break;
				}
			}
		}, null);
		sigChManClientB.addChannel(TEST_CHANNEL_ID,
				new OnSignalReceivedListener() {
			@Override
			public void onSignalReceived(Signal signal) {
				switch (SignalID.getSignalID(signal.getType())) {
				case REQUEST_ONE: // 101 received, wait for 103
					response.setConnectionActivityState(ClConActBState.RECEIVED_101);
					break;
				case REQUEST_TWO: // 103 received, wait for FINISH
					response.setConnectionActivityState(ClConActBState.RECEIVED_103);
					break;
				case FINISH: // FINISH received, final state
					response.setConnectionActivityState(ClConActBState.RECEIVED_FINISH);
					break;
				default:
					break;
				}
			}
		}, null);
		
		/*
		 * 
		 * Now implement state actions separately from the connection state
		 * machine
		 */

		final SignalChannel sigChClientA = sigChManClientA.getChannels().get(
				TEST_CHANNEL_ID);
		final SignalChannel sigChClientB = sigChManClientB.getChannels().get(
				TEST_CHANNEL_ID);

		request.registerConnectionActivityStateChangeListener(
				new ConnectionActivityStateChangeListener() {
					@Override
					public void onConnectionActivityStateChange(
							ConnectionActivityStateChangeEvent event) {
						System.out.print("ClientA: ");
						switch ((ClConActAState) event.getNewState()) {
						case RECEIVED_102:
							System.out.println("clientA received 102, "
									+ event.getPreviousState() + " -> "
									+ event.getNewState() + "\n" + "sending 103...");
							// Send 103
							sendSignal(sigChClientA, 103);
							request.setConnectionActivityState(ClConActAState.WAIT_FOR_FINISH);
							break;
						case RECEIVED_FINISH:
							System.out.println("clientA received FINISH, "
									+ event.getPreviousState() + " -> "
									+ event.getNewState() + "\n" + "sending FINISH...");
							// Send FINISH
							sendSignal(sigChClientA, 105);
							break;
						default:
							break;
						}
					}
				});
		response.registerConnectionActivityStateChangeListener(
				new ConnectionActivityStateChangeListener() {
					@Override
					public void onConnectionActivityStateChange(
							ConnectionActivityStateChangeEvent event) {
						System.out.print("ClientB: ");
						switch ((ClConActBState) event.getNewState()) {
						case RECEIVED_101:
							System.out.println("clientB received 101, "
									+ event.getPreviousState() + " -> "
									+ event.getNewState() + "\n" + "sending 102...");
							sendSignal(sigChClientB, A, 102);
							response.setConnectionActivityState(ClConActBState.WAIT_FOR_103);
							break;
						case RECEIVED_103:
							System.out.println("clientB received 103, "
									+ event.getPreviousState() + " -> "
									+ event.getNewState() + "\n" + "sending 105...");
							// Send FINISH
							sendSignal(sigChClientB, 105);
							response.setConnectionActivityState(ClConActBState.WAIT_FOR_FINISH);
							break;
						case RECEIVED_FINISH:
							System.out.println("clientB received FINISH, "
									+ event.getPreviousState() + " -> "
									+ event.getNewState());
							// We are done
							break;
						default:
							break;
						}
					}
				});

		// Everything is set up, now trigger the communication sequence
		sendSignal(sigChClientA, B, 101);
		sleep(1000);
		assertEquals(ClConActAState.RECEIVED_FINISH,
				request.getConnectionActivityState());
		assertEquals(ClConActBState.RECEIVED_FINISH,
				response.getConnectionActivityState());
	}

	/**
	 * Send Signal with <code>type</code> to everyone.
	 * 
	 * @param channel
	 * @param type
	 */
	private void sendSignal(SignalChannel channel, final int type) {
		sendSignal(channel, (ID) null, type);
	}

	private void sendSignal(SignalChannel channel, String clientID,
			final int type) {
		sendSignal(channel, getContainerIDOf(getConnectionOfClient(clientID)),
				type);
	}

	/**
	 * Send Signal with <code>type</code> to receiver.
	 * 
	 * @param channel
	 * @param type
	 */
	private void sendSignal(SignalChannel channel, ID receiver, final int type) {
		try {
			channel.sendSignal(receiver, new Signal() {
				@Override
				public int getType() { return type; }
				@Override
				public byte[] getContent() { return null; }
			});
		} catch (SendMessageException e) {
			e.printStackTrace();
		}
	}

}
