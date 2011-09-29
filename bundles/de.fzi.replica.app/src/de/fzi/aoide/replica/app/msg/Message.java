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

package de.fzi.aoide.replica.app.msg;

import java.util.HashMap;
import java.util.Map;


/**
 * This enumeration defines all signal types of the primary messaging
 * channel of a ReplicaOntologyApplication.<br>
 * <br>
 * The content of a message is determined by its unique type ID.
 * Another way would be to use only REQUESTS and RESPONSES and track a
 * state in the client (e.g. whether a SOID REQUEST has been made) and
 * behave according to that.
 * But this way is much easier and sufficient for now.
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 21.07.2011
 * 
 * TODO too heavy and useless strings
 */
public enum Message {
	
	/**
	 * A new shared object ID will be requested when a client adds an ontology.
	 */
	REQUEST_ADD(0, "request shared object ID"),
	
	/**
	 * New shared object ID will come along with this response.
	 */
	RESPONSE_ADD_OK(1, "response shared object ID ok"),
	
	/**
	 * If a client needs shared object IDs of ontologies this type of request is made.
	 * No argument means that all IDs will be listed.<br>
	 * Optionally an ontology group can be specified as parameter.
	 */
	REQUEST_SOIDS(2, "request shared object IDs"),
	
	RESPONSE_SOIDS_OK(3, "response shared object IDs ok"),
	
	RESPONSE_SOIDS_FAIL_NOT_FOUND(4, "response shared object IDs fail not found"),
	
	/**
	 * Will list all ontology IDs.
	 */
	REQUEST_ONTOIDS(5, "request ontology IDs"),
	
	RESPONSE_ONTOIDS_OK(6, "response ontology IDs ok"),
	
	/**
	 * Will list all ontology IDs in one or more groups.
	 */
	REQUEST_ONTOIDS_GROUP(7, "request ontology IDs in group"),
	
	RESPONSE_ONTOIDS_GROUP_OK(8, "response ontology IDs in group ok"),
	
	RESPONSE_ONTOIDS_GROUP_FAIL_NOT_FOUND(9, "response ontology IDs in group fail not found"),
	
	/**
	 * Will list all available groups.
	 */
	REQUEST_GROUPS(10, "request groups"),
	
	RESPONSE_GROUPS_OK(11, "response groups ok"),
	
	/**
	 * If a previously sent message has not been responded to and timed out
	 * this type of message is sent along with the message ID.
	 * 
	 * not sure about this...
	 */
	REQUEST_RETRY(12, "retry"),
	
	/**
	 * Indicates success. Results will be appended if any.
	 */
//	RESPONSE_OK(11, "response ok"),
	
	/**
	 * If an unknown error occurred and the request can not be satisfied.
	 * @deprecated Introduce fail messages for each request type.
	 */
	RESPONSE_FAIL(13, "response fail"),
	
	/**
	 * @deprecated Introduce fail messages for each request type.
	 * Also: Not sure about this (response could be ok but without result)
	 */
	RESPONSE_FAIL_NOT_FOUND(14, "response fail not found"),
	
	/**
	 * If the requested action was denied for an unknown reason.
	 * @deprecated Introduce fail messages for each request type.
	 */
	RESPONSE_DENIED(15, "response denied"),
	
	/**
	 * Client requests adding an ontology to a specified group.
	 */
	ADD_TO_GROUP(16, "add to group");
	
	private int id;
	private String name;
	
	// Used to speed up message type lookup method getMsgTypeByID(byte)
	private static Map<Integer, Message> map = new HashMap<Integer, Message>();
	
	static {
		map.put(REQUEST_ADD.id, REQUEST_ADD);
		map.put(RESPONSE_ADD_OK.id, RESPONSE_ADD_OK);
		map.put(REQUEST_SOIDS.id, REQUEST_SOIDS);
		map.put(RESPONSE_SOIDS_OK.id, RESPONSE_SOIDS_OK);
		map.put(RESPONSE_SOIDS_FAIL_NOT_FOUND.id, RESPONSE_SOIDS_FAIL_NOT_FOUND);
		map.put(REQUEST_ONTOIDS.id, REQUEST_ONTOIDS);
		map.put(RESPONSE_ONTOIDS_OK.id, RESPONSE_ONTOIDS_OK);
		map.put(REQUEST_ONTOIDS_GROUP.id, REQUEST_ONTOIDS_GROUP);
		map.put(RESPONSE_ONTOIDS_GROUP_OK.id, RESPONSE_ONTOIDS_GROUP_OK);
		map.put(REQUEST_GROUPS.id, REQUEST_GROUPS);
		map.put(RESPONSE_GROUPS_OK.id, RESPONSE_GROUPS_OK);
		map.put(REQUEST_RETRY.id, REQUEST_RETRY);
		
//		byteIDMap.put(RESPONSE_OK.id, RESPONSE_OK);
		map.put(RESPONSE_FAIL.id, RESPONSE_FAIL);
		map.put(RESPONSE_FAIL_NOT_FOUND.id, RESPONSE_FAIL_NOT_FOUND);
		map.put(RESPONSE_DENIED.id, RESPONSE_DENIED);
	}
	
	private Message(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static Message getMsgTypeByName(String name) {
		for(Message type : values()) {
			if(type.name.equals(name)) {
				return type;
			}
		}
		return null;
	}
	
	public static Message getMsgTypeByID(int id) {
		return map.get(id);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
}
