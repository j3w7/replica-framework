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

package de.fzi.replica.app;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;

import de.fzi.replica.comm.Connection;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 05.12.2010
 */
public abstract class AbstractMasterSlaveConceptApplication
		extends AbstractReplicaOntologyApplication {
	
	protected AbstractMasterSlaveConceptApplication(ApplicationContext context) {
		super(context);
	}

	/**
	 * This is the primary connection. In case of a client
	 * this is the connection to the master, in case of
	 * the master this is the server connection instance.
	 * Used internally with protected scope for performance.
	 */
	protected Connection connection;
	
	/**
	 * Get the primary connection.
	 * @return
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Set the primary connection.
	 * @param connection
	 */
	protected void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void disconnect() {
		getConnection().disconnect();
	}
	
	protected ID getLocalContainerID() {
		return IDFactory.getDefault().createStringID(
				getConfiguration().getProperty(CONFIG_KEYWORD_CONTAINER_ID));
	}
	
}
