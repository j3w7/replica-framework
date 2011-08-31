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


/**
 * A Signal is a slight addition to the ECF Datashare API message concept
 * and adds a characteristic to messages by adding a type to a message.<br>
 * As the name suggests a Signal is meant to indicate something.
 * A Signal can also contain arbitrary data in form of a byte array.
 * The main advantage is the ease of implementing communication in situations
 * where peers need only simple notficiation of certain events.<br>
 * This interface is meant to be implemented by clients. TODO: example
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 26.11.2010
 */
public interface Signal {
	
	/**
	 * @return Integer representing the Signal type.
	 */
	int getType();
	
	/**
	 * You can package arbitrary content in this message easily by
	 * using the <code>MessageCarrier</code>.
	 * 
	 * @see de.fzi.replica.comm.util.MessageCarrier
	 * 
	 * @return Array of bytes representing the content.
	 */
	byte[] getContent();
	
}
