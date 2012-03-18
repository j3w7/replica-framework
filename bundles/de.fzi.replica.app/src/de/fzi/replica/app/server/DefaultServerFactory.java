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

import java.util.Properties;

import de.fzi.replica.app.ApplicationContextImpl;
import de.fzi.replica.comm.CommManager;
import de.fzi.replica.comm.DefaultCommManagerFactory;

/**
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 08.07.2011
 */
public class DefaultServerFactory implements ServerFactory {

	@Override
	public Server createServer(Properties config) {
		ApplicationContextImpl c = new ApplicationContextImpl();
		c.setConfiguration(config);
		CommManager cm = new DefaultCommManagerFactory().createCommManager();
		cm.setConfiguration(config);
		c.addService(cm);
		Server srv = new ServerImpl(c);
		return srv;
	}

}
