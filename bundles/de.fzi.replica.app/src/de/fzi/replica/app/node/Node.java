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

package de.fzi.replica.app.node;

import de.fzi.replica.app.client.Client;
import de.fzi.replica.app.server.Server;


/**
 * A node is a peer which can connect to any other node and provides
 * access to shared ontologies. Therefore a node has client as well as
 * server functionalities.<br>
 * For building a client/server communication structure use
 * {@link Client} and {@link Server}.
 * 
 * TODO: look into distributed ontology systems literature
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 01.12.2010
 */
public interface Node extends Server, Client {
	
	String CONFIG_KEYWORD_NODE_ID = "nodeID";
		
}
