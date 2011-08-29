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

package de.fzi.replica.comm.test;

import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_ID;

import org.eclipse.ecf.core.IContainerListener;
import org.eclipse.ecf.core.events.IContainerEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.fzi.replica.comm.Connection;

/**
 * This is just meant to run a server for a demo and will be removed soon.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 10.12.2010
 */
public class DemoServerRunner extends AbstractCommTestCase {
	
	// Simplifies implementation
	protected Connection connectionServer;
	protected Connection connectionClientA;
	protected Connection connectionClientB;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		createServer();
		connectionServer = createServerConnection(createConnectionPropertiesServer());
		startServer(connectionServer);
		
		System.out.println("ReplicaServer '"+connectionServer.
				getConnectionContext().getConfiguration().getProperty(CONFIG_KEYWORD_CONTAINER_ID)+"' running");
		
		connectionServer.getSharedObjectContainer().addListener(new IContainerListener() {
			@Override
			public void handleEvent(IContainerEvent event) {
				System.out.println(event);
			}
		});
	}
	
	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testRunInfinitely() {
		while(true);
	}
	
}
