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

package de.fzi.replica.comm.util;

import java.util.HashSet;
import java.util.Set;

import de.fzi.replica.comm.Connection.ConnectionContext.ConnectionActivity;

/**
 * Utility class for handling connection activity states.
 * 
 * @see ConnectionActivity
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 19.07.2011
 */
public abstract class AbstractConnectionActivityStates {
	
	protected static Set<Enum<?>> setOf(Enum<?>... states) {
		Set<Enum<?>> statesSet = new HashSet<Enum<?>>();
		for (Enum<?> state : states) {
			statesSet.add(state);
		}
		return statesSet;
	}

}
