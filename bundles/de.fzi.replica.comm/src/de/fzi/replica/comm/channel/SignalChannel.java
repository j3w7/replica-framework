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

import org.eclipse.ecf.core.identity.ID;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 09.02.2011
 *
 */
public interface SignalChannel {
	
	ID getID();

	OnSignalReceivedListener getOnSignalReceivedListener();
	
	void setOnSignalReceivedListener(OnSignalReceivedListener listener);
	
	void sendSignal(ID receiver, Signal signal) throws SendMessageException;
	
	class SendMessageException extends Exception {
		private static final long serialVersionUID = -5859776175130440304L;
		public SendMessageException(String msg, Throwable cause) { super(msg, cause); }
	}
	
}
