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

package de.fzi.aoide.replica.app;

import java.util.Properties;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.0, 12.10.2010
 *
 */
public interface Application {
	
	String CONFIG_KEYWORD_CONTAINER_TYPE = "containerType";
	String CONFIG_KEYWORD_CONTAINER_ID = "containerID";
	String CONFIG_KEYWORD_TARGET_ID = "targetID";
	
	/**
	 * Start the application. Any configuration parameters should be set before
	 * this method is called as initialization is done when starting the application.
	 * 
	 * @see de.fzi.aoide.replica.app.Application#setConfiguration
	 * @throws StartupException
	 */
	void start() throws StartupException;
	
	/**
	 * Stop the application releasing all resources and eventually disconnecting
	 * if still connected.
	 */
	void stop();
	
	/**
	 * Set the configuration. The Configuration has to be set prior to starting
	 * the application. Reasonable defaults for missing configuration properties
	 * will be chosen otherwise.<br>
	 * This method is meant to serve as a primary point for providing configuration
	 * data. Any modules that the ReplicaOntology Application bundle relies on
	 * will be configured transparently when calling this method.<br>
	 * If you want to customize the configuration of your application you can
	 * override this method to set your own parameters but make sure you call
	 * this method and also to override <code>getConfiguration()</code>.<br><br>
	 * <table border="1">
	 * 	<caption>Recognized configuration key/value pairs</caption>
	 * 	<tr><th>Key</th><th>Description</th></tr>
	 * 	<tr><td>containerType</td><td>The type of the underlying ECF container instance</td></tr>
	 * 	<tr><td>containerID</td><td>The ID of the container</td></tr>
	 * 	<tr><td>targetID</td><td>ID of the target container</td></tr>
	 * </table>
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
	 * @see de.fzi.aoide.replica.app.Application#setConfiguration
	 */
	Properties getConfiguration();
	
	/*
	 * Exceptions
	 */
	
	class StartupException extends Exception {
		private static final long serialVersionUID = -8243966095934127598L;
		public StartupException(String msg, Throwable cause) { super(msg, cause); }
	}
	
}
