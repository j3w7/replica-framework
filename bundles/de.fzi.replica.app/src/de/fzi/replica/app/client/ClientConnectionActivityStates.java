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

package de.fzi.replica.app.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.fzi.replica.comm.util.AbstractConnectionActivityStates;

/**
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 19.07.2011
 */
public final class ClientConnectionActivityStates extends
		AbstractConnectionActivityStates {
	
	enum AddOntology {
		WAIT_FOR_SOID(1), RECEIVED_OK(2), EXCEPTION(3);
		int id;
		static Map<Enum<?>, Set<Enum<?>>> stateMap;

		AddOntology(int ordinal) {
			id = ordinal;
		}

		static {
			stateMap = new HashMap<Enum<?>, Set<Enum<?>>>();
			stateMap.put(WAIT_FOR_SOID, setOf(RECEIVED_OK, EXCEPTION));
		}
	}
	
	enum GetGroups {
		WAIT_FOR_GROUPS(1), RECEIVED_OK(2), EXCEPTION(3);
		int id;
		static Map<Enum<?>, Set<Enum<?>>> stateMap;

		GetGroups(int ordinal) {
			id = ordinal;
		}

		static {
			stateMap = new HashMap<Enum<?>, Set<Enum<?>>>();
			stateMap.put(WAIT_FOR_GROUPS, setOf(RECEIVED_OK, EXCEPTION));
		}
	}
	
	enum GetOntology {
		WAIT_FOR_SOIDS(1), RECEIVED_OK(2), EXCEPTION(3);
		int id;
		static Map<Enum<?>, Set<Enum<?>>> stateMap;

		GetOntology(int ordinal) {
			id = ordinal;
		}

		static {
			stateMap = new HashMap<Enum<?>, Set<Enum<?>>>();
			stateMap.put(WAIT_FOR_SOIDS, setOf(RECEIVED_OK, EXCEPTION));
		}
	}
	
	enum GetOntologyIDs {
		WAIT_FOR_IDS(1), RECEIVED_OK(2), EXCEPTION(3);
		int id;
		static Map<Enum<?>, Set<Enum<?>>> stateMap;

		GetOntologyIDs(int ordinal) {
			id = ordinal;
		}

		static {
			stateMap = new HashMap<Enum<?>, Set<Enum<?>>>();
			stateMap.put(WAIT_FOR_IDS, setOf(RECEIVED_OK, EXCEPTION));
		}
	}
	
}
