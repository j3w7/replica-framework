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

package de.fzi.replica.repo.service.internal;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.discovery.IDiscoveryAdvertiser;
import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 17.10.2011
 *
 */
public class Activator implements BundleActivator, IDistributionConstants {

	private static BundleContext context;
	private IRemoteServiceRegistration serviceRegistration;
	private ServiceTracker containerManagerTracker;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		/*
		Properties props = new Properties();
		// add OSGi service property indicated export of all interfaces exposed by service (wildcard)
		props.put(IDistributionConstants.SERVICE_EXPORTED_INTERFACES,
				IDistributionConstants.SERVICE_EXPORTED_INTERFACES_WILDCARD);
		// add OSGi service property specifying config
		props.put(IDistributionConstants.SERVICE_EXPORTED_CONFIGS,
				"ecf.r_osgi.peer");
		// register remote service
		serviceRegistration =  
			bundleContext.registerService(
				ReplicaRepositoryServiceAsync.class.getName(),
				new ReplicaRepositoryServiceAsyncImpl(), props);
		*/
		
//		// Create R-OSGi Container
//		IContainerManager containerManager = getContainerManagerService();
//		IContainer container = containerManager.getContainerFactory().
//			createContainer("ecf.r_osgi.peer");
//		// Get remote service container adapter
//		IRemoteServiceContainerAdapter containerAdapter = (IRemoteServiceContainerAdapter) container
//				.getAdapter(IRemoteServiceContainerAdapter.class);
//		// Register remote service
//		serviceRegistration = containerAdapter.registerRemoteService(
//				new String[] { ReplicaRepositoryServiceAsync.class.getName() },
//				new ReplicaRepositoryServiceAsyncImpl(), null);
		
		IContainerManager containerManagerService = getContainerManagerService();
		IContainer c = containerManagerService.getContainerFactory().
			createContainer("ecf.r_osgi.peer");
//		c.getAdapter(IDiscoveryLocator.class);
		
		System.out.println("ReplicaRepositoryServiceAsync RemoteService registered");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public IContainerManager getContainerManagerService() {
		if(containerManagerTracker == null) {
			containerManagerTracker = new ServiceTracker(context,
					IContainerManager.class.getName(), null);
			containerManagerTracker.open();
		}
		return (IContainerManager) containerManagerTracker.getService();
	}
	
}
