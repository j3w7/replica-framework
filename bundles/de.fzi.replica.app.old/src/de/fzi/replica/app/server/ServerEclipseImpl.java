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

package de.fzi.replica.app.server;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import de.fzi.replica.app.ApplicationContext;

/**
 * 
 * Implements an Eclipse Application. Used for testing and demonstration purposes.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.0, 11.09.2010
 *
 */
public class ServerEclipseImpl extends ServerImpl 
		implements Server, IApplication {
	
	protected ServerEclipseImpl(ApplicationContext context) {
		super(context);
	}

//	protected void processArgs(String[] args) {
//		containerType = DEFAULT_CONTAINER_TYPE;
//		containerID = DEFAULT_CONTAINER_IDENTITY;
//		
//		for (int i = 0; i < args.length; i++) {
//			if (args[i].equals("-containerType")) {
//				containerType = args[i + 1];
//				i++;
//			} else if (args[i].equals("-containerID")) {
//				containerID = args[i + 1];
//				i++;
//			}
//		}
//	}

	@Override
	public Object start(IApplicationContext context) throws Exception {
//		processArgs((String[]) context.getArguments().get("application.args"));
		super.start();
		waitUntilShutdown();
		return IApplication.EXIT_OK;
	}
	
}
