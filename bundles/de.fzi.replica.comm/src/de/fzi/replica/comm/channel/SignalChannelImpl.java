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

package de.fzi.replica.comm.channel;

import java.nio.ByteBuffer;
import java.util.Map;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.datashare.IChannel;
import org.eclipse.ecf.datashare.IChannelListener;
import org.eclipse.ecf.datashare.events.IChannelEvent;
import org.eclipse.ecf.datashare.events.IChannelMessageEvent;

import de.fzi.replica.comm.Connection;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.2, 20.07.2011
 */
public class SignalChannelImpl implements SignalChannel {

	// Use 4 bytes to encode the type, which is represented by an integer
	// currently
	private final static int TYPE_BYTE_COUNT = 4;

	protected IChannel channel;
	protected OnSignalReceivedListener listener;

	@SuppressWarnings("rawtypes")
	protected SignalChannelImpl(ID channelID, Connection connection,
			OnSignalReceivedListener listener, Map properties)
			throws ECFException {
		this.listener = listener;
		this.channel = connection.getChannelContainerAdapter().createChannel(
				channelID, toIChannelListener(listener), properties);
	}

	@Override
	public ID getID() {
		return channel.getID();
	}

	@Override
	public OnSignalReceivedListener getOnSignalReceivedListener() {
		return listener;
	}

	@Override
	public void setOnSignalReceivedListener(OnSignalReceivedListener listener) {
		this.listener = listener;
		channel.setListener(toIChannelListener(listener));
	}

	@Override
	public void sendSignal(ID receiver, Signal signal)
			throws SendMessageException {
		try {
			byte[] msg;
			if (signal.getContent() != null) {
				msg = new byte[signal.getContent().length + TYPE_BYTE_COUNT];
			} else {
				msg = new byte[TYPE_BYTE_COUNT];
			}
			ByteBuffer msgBuffer = ByteBuffer.wrap(msg);
			msgBuffer.putInt(signal.getType());
			if (signal.getContent() != null) {
				msgBuffer.put(signal.getContent());
			}
			channel.sendMessage(receiver, msg);
		} catch (ECFException e) {
			throw new SendMessageException(
					"Exception when sending Message on channel " + this
					+ ", cause: " + e.getCause(), e);
		}
	}

	protected IChannelListener toIChannelListener(
			final OnSignalReceivedListener listener) {
		if (listener == null) {
			return null;
		}
		return new IChannelListener() {
			@Override
			public void handleChannelEvent(IChannelEvent event) {
				if (event instanceof IChannelMessageEvent) {
					final IChannelMessageEvent cme =
						(IChannelMessageEvent) event;
					final byte[] data = cme.getData();
					final byte[] content = getContent(data);
					final int msgType = getType(data);
					listener.onSignalReceived(new Signal() {
						private static final long serialVersionUID = 
							6732423056841996123L;
						@Override
						public byte[] getContent() { return content; }
						@Override
						public int getType() { return msgType; }
					});
				}
			}
		};
	}

	/**
	 * Extracts content from a raw signal message byte array.
	 * 
	 * @param data
	 *            The array of the raw signal message.
	 * @return A byte array containing the signal content.
	 */
	private byte[] getContent(byte[] data) {
		byte[] content = new byte[data.length - TYPE_BYTE_COUNT];
		// Content is encoded after the fourth byte
		ByteBuffer buffer = ByteBuffer.wrap(data, TYPE_BYTE_COUNT, data.length
				- TYPE_BYTE_COUNT);
		buffer.get(content);
		return content;
	}

	/**
	 * Extracts the signal type of a raw signal message byte array.
	 * 
	 * @param data
	 *            The array of the raw signal message.
	 * @return The signal type identifier as Integer.
	 */
	private int getType(byte[] data) {
		// The message type is encoded in the first four bytes
		ByteBuffer buffer = ByteBuffer.wrap(data, 0, TYPE_BYTE_COUNT);
		return buffer.getInt();
	}

	@Override
	public String toString() {
		return "SignalChannelImpl [channel=" + channel + ", listener="
				+ listener + "]";
	}

}
