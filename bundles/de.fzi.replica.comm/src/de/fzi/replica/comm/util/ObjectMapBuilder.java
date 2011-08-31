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