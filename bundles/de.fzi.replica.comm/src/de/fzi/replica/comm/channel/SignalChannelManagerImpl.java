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
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;

import de.fzi.replica.comm.Connection;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.2, 07.02.2011
 */
public class SignalChannelManagerImpl implements SignalChannelManager {

	protected Connection connection;
	protected Map<ID, SignalChannel> channels;

	public SignalChannelManagerImpl(Connection connection) {
		this.connection = connection;
		channels = new HashMap<ID, SignalChannel>();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public SignalChannel addChannel(ID channelID,
			OnSignalReceivedListener listener, Map properties)
			throws AddChannelException {
		SignalChannel signalChannel;
		try {
			signalChannel = new SignalChannelImpl(channelID, connection,
					listener, properties);
			channels.put(channelID, signalChannel);
		} catch (ECFException e) {
			throw new AddChannelException("Adding SignalChannel failed", e);
		}
		return signalChannel;
	}

	@Override
	public Map<ID, SignalChannel> getChannels() {
		return channels;
	}

	public static String bytesToString(byte[] data, CharsetDecoder decoder)
			throws CharacterCodingException {
		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		return decoder.decode(byteBuffer).toString();
	}

	@Override
	public String toString() {
		return "SignalChannelManagerImpl [connection=" + connection
				+ ", channels=" + channels + "]";
	}

}
