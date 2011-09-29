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

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import de.fzi.replica.comm.channel.OnSignalReceivedListener;
import de.fzi.replica.comm.channel.Signal;
import de.fzi.replica.comm.channel.SignalChannel;
import de.fzi.replica.comm.channel.SignalChannelManager;
import de.fzi.replica.comm.channel.SignalChannel.SendMessageException;
import de.fzi.replica.comm.channel.SignalChannelManager.AddChannelException;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 07.02.2011
 *
 */
public class SignalChannelTestCase extends AbstractCommTestCase {
	
	private static final String A = "clientA";
	private static final String B = "clientB";
	
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
		// TODO disconnect
	}
	
	public void testSendSignalWithoutContent() 
			throws ContainerConnectException, ContainerCreateException, 
				AddChannelException, SendMessageException, InterruptedException {
		SignalChannelManager srvChMan = getConnectionOfServer().getSignalChannelManager();
		SignalChannelManager clAChMan = getConnectionOfClient(A).getSignalChannelManager();
		SignalChannelManager clBChMan = getConnectionOfClient(B).getSignalChannelManager();
		
		ID testChannel = IDFactory.getDefault().createStringID("TestChannel");
		
		// Create a channel at the server
		SignalChannel chSrv = srvChMan.addChannel(testChannel, null, null);
		
		// Create a holder object to intercept signals
		final MsgHolder holder = new MsgHolder();
		
		// Create a channel at clientA
		clAChMan.addChannel(testChannel, new OnSignalReceivedListener() {
			@Override
			public void onSignalReceived(Signal signal) {
				holder.clientAMsgTypeHolder = signal.getType();
				holder.clientAMsgContentHolder = signal.getContent();
				printSignalInfo(A, signal);
			}
		}, null);
		
		// Create a channel at clientB
		clBChMan.addChannel(testChannel, new OnSignalReceivedListener() {
			@Override
			public void onSignalReceived(Signal signal) {
				holder.clientBMsgTypeHolder = signal.getType();
				holder.clientBMsgContentHolder = signal.getContent();
				printSignalInfo(B, signal);
			}
		}, null);
		
		// Send a signal from the server
		//final byte[] signalContent = "Some signal content".getBytes();
		final int signalType = (int) (Math.random()*Integer.MAX_VALUE);
		chSrv.sendSignal(null, new Signal() {
			@Override
			public byte[] getContent() { return null; }
			@Override
			public int getType() { return signalType; }
		});
		
		// Wait for the signal to arrive
		Thread.sleep(1000);
		
		// Check message type
		assertTrue(signalType == holder.clientAMsgTypeHolder);
		assertTrue(signalType == holder.clientBMsgTypeHolder);
		// Check message content
		assertTrue(holder.clientAMsgContentHolder.length == 0);
		assertTrue(holder.clientBMsgContentHolder.length == 0);
	}
	
	public void testSendSignalWithContent() 
			throws ContainerConnectException, ContainerCreateException, 
				AddChannelException, SendMessageException, InterruptedException {
		SignalChannelManager srvChMan = getConnectionOfServer().getSignalChannelManager();
		SignalChannelManager clAChMan = getConnectionOfClient(A).getSignalChannelManager();
		SignalChannelManager clBChMan = getConnectionOfClient(B).getSignalChannelManager();
		
		ID testChannel = IDFactory.getDefault().createStringID("TestChannel");
		
		// Create a channel at the server
		SignalChannel chSrv = srvChMan.addChannel(testChannel, null, null);
		
		// Create a holder object to intercept signals
		final MsgHolder holder = new MsgHolder();
		
		// Create a channel at clientA
		clAChMan.addChannel(testChannel, new OnSignalReceivedListener() {
			@Override
			public void onSignalReceived(Signal signal) {
				holder.clientAMsgTypeHolder = signal.getType();
				holder.clientAMsgContentHolder = signal.getContent();
				printSignalInfo(A, signal);
			}
		}, null);
		
		// Create a channel at clientB
		clBChMan.addChannel(testChannel, new OnSignalReceivedListener() {
			@Override
			public void onSignalReceived(Signal signal) {
				holder.clientBMsgTypeHolder = signal.getType();
				holder.clientBMsgContentHolder = signal.getContent();
				printSignalInfo(B, signal);
			}
		}, null);
		
		// Send a signal from the server
		final byte[] signalContent = "Some signal content".getBytes();
		final int signalType = (int) (Math.random()*Integer.MAX_VALUE);
		chSrv.sendSignal(null, new Signal() {
			@Override
			public byte[] getContent() { return signalContent; }
			@Override
			public int getType() { return signalType; }
		});
		
		// Wait for the signal to arrive
		Thread.sleep(1000);
		
		// Check message type
		assertTrue(signalType == holder.clientAMsgTypeHolder);
		assertTrue(signalType == holder.clientBMsgTypeHolder);
		// Check message content
		Assert.assertArrayEquals(signalContent, holder.clientAMsgContentHolder);
		Assert.assertArrayEquals(signalContent, holder.clientBMsgContentHolder);
	}
	
	private class MsgHolder {
		byte[] clientAMsgContentHolder;
		byte[] clientBMsgContentHolder;
		int clientAMsgTypeHolder;
		int clientBMsgTypeHolder;
	}
	
	private static void printSignalInfo(String client, Signal signal) {
		System.out.println(client+" received signal type="+signal.getType()+
				", content="+new String(signal.getContent()));
	}
	
}
