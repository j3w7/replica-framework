package de.fzi.replica.comm.util;

import java.io.Serializable;
import java.util.Map;

/**
 * Meant to simplify messaging by providing easy access to arbitrary
 * identifiable content and seralization and deserialization methods.<br>
 * Use it in conjunction with the <code>ObjectMapBuilder</code>.
 * 
 * @see de.fzi.replica.comm.util.ObjectMapBuilder
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 19.07.2011
 */
public class MessageCarrier implements Serializable {

	private static final long serialVersionUID = 287189361273612873L;
	
	private Map<Object, Object> content;
	
	protected MessageCarrier(Map<Object, Object> objMap) {
		content = objMap;
	}
	
	public Map<Object, Object> getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		return "Carrier [content=" + content + "]";
	}
	
	public static byte[] create(Map<Object, Object> objMap) {
		return NetUtil.toByteArray(new MessageCarrier(objMap));
	}
	
	public static MessageCarrier of(byte[] raw) {
		return (MessageCarrier) NetUtil.toObject(raw);
	}
	
}