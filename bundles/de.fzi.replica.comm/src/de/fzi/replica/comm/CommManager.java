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

package de.fzi.replica.comm;

import java.util.Map;
import java.util.Properties;

/**
 * CommManager can handle multiple connections to other peers and
 * provides access to all ECF components relevant to the Replica project
 * through the {@link Connection} interface.
 * 
 * @see Connection
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version version, 31.10.2010
 * 
 */
public interface CommManager {
	
//	static CommManager INSTANCE = new CommManagerImpl();
	
	String CONFIG_KEYWORD_DEFAULT_CONTAINER_TYPE = "defaultContainerType";
	String CONFIG_KEYWORD_DEFAULT_CONTAINER_ID = "defaultContainerID";
	String CONFIG_KEYWORD_DEFAULT_TARGET_ID = "defaultTargetID";
	
	void initialize();
	
	void dispose();
	
	Connection createConnection(Properties connectionProperties);
	
	Map<String, Connection> getConnections();
	
	/**
	 * Set the configuration. The Configuration has to be set prior to connecting.
	 * Reasonable defaults for missing configuration properties will be chosen otherwise.<br>
	 * If you want to customize the configuration of your implementation when extending
	 * a CommManager implementation you can override this method to set your own
	 * parameters but make sure you call this method and also to override
	 * <code>getConfiguration()</code>.<br>
	 * <br>
	 * The configuration can also define defaults for connections. All configuration
	 * properties of a {@link Connection} can also be specified here. If a configuration key
	 * is missing when configuring a Connection, the value specified in the CommManager
	 * configuration will be applied.
	 * <br>
	 * <table border="1">
	 * 	<caption>Recognized configuration key/value pairs</caption>
	 * 	<tr><th>Key</th><th>Description</th></tr>
	 * 	<tr><td>defaultContainerType</td><td>The default type of the underlying ECF container instance.</td></tr>
	 * 	<tr><td>defaultContainerID</td><td>The default ID of the container.</td></tr>
	 * 	<tr><td>defaultTargetID</td><td>The default target ID.</td></tr>
	 * </table>
	 * 
	 * TODO
	 * <b>DEFAULTS ARE NOT WORKING YET, EVERY CONNECTION HAS TO BE CONFIGURED MANUALLY</b>
	 * 
	 * @see de.fzi.replica.comm.Connection
	 */
	void setConfiguration(Properties config);
	
	/**
	 * Returns the configuration.<br>
	 * If you want to customize the configuration of your application you can override
	 * this method to return your configuration parameters but make sure you also add
	 * the Properties supplied by this method to your configuration properties.<br>
	 * Have a look at the <code>setConfiguration()</code> documentation for a description of all
	 * configuration keys.
	 * 
	 * @see de.fzi.replica.comm.CommManager#setConfiguration
	 */
	Properties getConfiguration();
	
}
