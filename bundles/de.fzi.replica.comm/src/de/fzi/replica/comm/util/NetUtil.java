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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Helper class for serializing/deserializing objects by using byte arrays
 * which is the suitable form for the messaging APIs of the signal channel
 * concept and the underlying ECF methods.
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 19.07.2011
 */
public class NetUtil {
	
	public static byte[] toByteArray(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("toByteArray() failed, obj="+obj, e);
		}
	}
	
	public static Object toObject(byte[] raw) {
		ByteArrayInputStream bos = new ByteArrayInputStream(raw);
		ObjectInputStream oos;
		try {
			oos = new ObjectInputStream(bos);
			return oos.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("toByteArray() failed, cause: "
					+e.getCause(), e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("toByteArray() failed, cause: "
					+e.getCause(), e);
		}
	}

}
