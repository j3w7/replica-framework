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

package de.fzi.replica.test.internal;

import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.IIDFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private static de.fzi.replica.test.internal.Activator plugin;
	
	private static IIDFactory idFactory;
	private static IContainerFactory containerFactory;
	
	private static ServiceTracker idFactoryServiceTracker;
	private static ServiceTracker containerFactoryServiceTracker;
	private static ServiceTracker containerManagerServiceTracker;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		plugin = this;
		idFactoryServiceTracker = new ServiceTracker(context, IIDFactory.class.getName(), null);
		idFactoryServiceTracker.open();
		idFactory = (IIDFactory) idFactoryServiceTracker.getService();
		containerFactoryServiceTracker = new ServiceTracker(context, IContainerFactory.class.getName(), null);
		containerFactoryServiceTracker.open();
		containerFactory = (IContainerFactory) containerFactoryServiceTracker.getService();
		containerManagerServiceTracker = new ServiceTracker(context, IContainerManager.class.getName(), null);
		containerManagerServiceTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (idFactoryServiceTracker != null) {
			idFactoryServiceTracker.close();
			idFactoryServiceTracker = null;
		}
		if (containerFactoryServiceTracker != null) {
			containerFactoryServiceTracker.close();
			containerFactoryServiceTracker = null;
		}
		if (containerManagerServiceTracker != null) {
			containerManagerServiceTracker.close();
			containerManagerServiceTracker = null;
		}
		plugin = null;
		idFactory = null;
	}
	
	public static Activator getDefault() {
		return plugin;
	}

	public static IIDFactory getIDFactory() {
		return idFactory;
	}

	public static IContainerFactory getContainerFactory() {
		return containerFactory;
	}
	
	public static IContainerManager getContainerManager() {
		return (IContainerManager) containerManagerServiceTracker.getService();
	}

}
