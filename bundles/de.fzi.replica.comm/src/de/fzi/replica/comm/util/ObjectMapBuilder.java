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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for sending arbitrary objects in messages with
 * unique identifiers.
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 19.07.2011
 */
public class ObjectMapBuilder implements Serializable {

	private static final long serialVersionUID = 287189934273611233L;

	private Map<Object, Object> map;

	protected ObjectMapBuilder() {
		map = new HashMap<Object, Object>();
	}

	public static ObjectMapBuilder create() {
		return new ObjectMapBuilder();
	}

	public ObjectMapBuilder add(Object id, Object obj) {
		map.put(id, obj);
		return this;
	}

	public Map<Object, Object> getMap() {
		return map;
	}

}