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

import java.util.Map;

import org.eclipse.ecf.core.identity.ID;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 26.11.2010
 */
public interface SignalChannelManager {

	@SuppressWarnings("rawtypes")
	SignalChannel addChannel(ID channelID, OnSignalReceivedListener listener,
			Map properties) throws AddChannelException;

	Map<ID, SignalChannel> getChannels();

	/*
	 * Exceptions
	 */

	class AddChannelException extends Exception {
		private static final long serialVersionUID = 4134126273070344431L;

		public AddChannelException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}

}
