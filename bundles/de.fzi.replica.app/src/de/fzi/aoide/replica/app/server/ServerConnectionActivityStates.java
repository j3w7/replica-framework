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

package de.fzi.aoide.replica.app.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.fzi.aoide.replica.comm.util.AbstractConnectionActivityStates;

/**
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 19.07.2011
 */
public final class ServerConnectionActivityStates extends
		AbstractConnectionActivityStates {
	
	enum AddOntology {
		WAIT_FOR_SOID_REQUEST(1), RESPONSE_SENT(2), EXCEPTION_SENT(3);
		int id;
		static Map<Enum<?>, Set<Enum<?>>> stateMap;

		AddOntology(int ordinal) {
			id = ordinal;
		}

		static {
			stateMap = new HashMap<Enum<?>, Set<Enum<?>>>();
			stateMap.put(WAIT_FOR_SOID_REQUEST, setOf(RESPONSE_SENT));
			stateMap.put(WAIT_FOR_SOID_REQUEST, setOf(EXCEPTION_SENT));
		}
	}
	
}
