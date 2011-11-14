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

package de.fzi.replica.app.internal;

import java.util.Properties;

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.util.SystemLogService;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.ecf.storage.IIDStore;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import de.fzi.replica.app.server.rs.ReplicaRepositoryServiceAsync;
import de.fzi.replica.app.server.rs.ReplicaRepositoryServiceAsyncImpl;
import de.fzi.replica.comm.CommManager;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 05.10.2011
 *
 */
public class Activator implements BundleActivator {
	
	private static final String PLUGIN_ID = "de.fzi.replica.app";

	private static Activator instance;
	
	private BundleContext context; // plugin is a singleton anyway, so could be static
	
	private LogService logService = null;
	
//	private ServiceTracker containerManagerTracker;
	private ServiceTracker containerManagerTracker;
	private ServiceTracker commManagerTracker;
	private ServiceTracker idStoreTracker;
	private ServiceTracker securePreferencesTracker;
	private ServiceTracker logServiceTracker;

	private ServiceRegistration repoSrvRegistration;

	// TODO set this via properties file
	private String containerType = "ecf.server.generic";

	private IRemoteServiceRegistration serviceRegistration;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		this.context = context;
		instance = this;
		registerRemoteService();
		System.out.println("Replica Application bundle started");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		unregisterHelloRemoteService();
		context = null;
		if (logServiceTracker != null) {
			logServiceTracker.close();
			logServiceTracker = null;
			logService = null;
		}
		System.out.println("Replica Application bundle stopped");
	}
	
	public static Activator getDefault() {
		return instance;
	}
	
	void registerRemoteService() throws ContainerCreateException {
		Properties props = new Properties();
		// add OSGi service property indicated export of all interfaces
		// exposed by service (wildcard)
		props.put("service.exported.interfaces", "*");
		// add OSGi service property specifying config
		props.put("service.imported.configs", containerType);
		// register remote service
		repoSrvRegistration = context.registerService(
				ReplicaRepositoryServiceAsync.class.getName(),
				new ReplicaRepositoryServiceAsyncImpl(), props);
		
		/*
		// Create R-OSGi Container
		IContainerManager containerManager = getContainerManagerService();
		IContainer container = containerManager.getContainerFactory().
			createContainer("ecf.r_osgi.peer");
		// Get remote service container adapter
		IRemoteServiceContainerAdapter containerAdapter = (IRemoteServiceContainerAdapter) container
				.getAdapter(IRemoteServiceContainerAdapter.class);
		// Register remote service
		serviceRegistration = containerAdapter.registerRemoteService(
				new String[] { ReplicaRepositoryServiceAsync.class.getName() },
				new ReplicaRepositoryServiceAsyncImpl(), null);
		System.out.println("IHello RemoteService registered");
		*/
	}
	
	void unregisterHelloRemoteService() {
		if (repoSrvRegistration != null) {
			repoSrvRegistration.unregister();
			repoSrvRegistration = null;
		}
		// tell everyone
		System.out.println("Host: Hello Remote Service Unregistered");
	}
	
//	public IContainerManager getContainerManager() {
//		if(containerManagerTracker == null) {
//			containerManagerTracker = new ServiceTracker(context, IContainerManager.class.getName(), null);
//			containerManagerTracker.open();
//		}
//		return (IContainerManager) containerManagerTracker.getService();
//	}
	
	public IContainerManager getContainerManagerService() {
		if(containerManagerTracker == null) {
			containerManagerTracker = new ServiceTracker(context, IContainerManager.class.getName(), null);
			containerManagerTracker.open();
		}
		return (IContainerManager) containerManagerTracker.getService();
	}
	
	public CommManager getCommManager() {
		if(commManagerTracker == null) {
			commManagerTracker = new ServiceTracker(context, CommManager.class.getName(), null);
			commManagerTracker.open();
		}
		return (CommManager) commManagerTracker.getService();
	}
	
	public IIDStore getIDStore() {
		if(idStoreTracker == null) {
			idStoreTracker = new ServiceTracker(context, IIDStore.class.getName(), null);
			idStoreTracker.open();
		}
		return (IIDStore) idStoreTracker.getService();
	}
	
	public ISecurePreferences getSecurePreferences() {
		if(securePreferencesTracker == null) {
			securePreferencesTracker = new ServiceTracker(context, ISecurePreferences.class.getName(), null);
			securePreferencesTracker.open();
		}
		return (ISecurePreferences) securePreferencesTracker.getService();
	}
	
	public LogService getLogService() {
		if (logServiceTracker == null) {
			logServiceTracker = new ServiceTracker(this.context, LogService.class.getName(), null);
			logServiceTracker.open();
		}
		logService = (LogService) logServiceTracker.getService();
		if (logService == null)
			logService = new SystemLogService(PLUGIN_ID);
		return logService;
	}
	
	public BundleContext getContext() {
		return context;
	}
	
//	private void startLocalDiscoveryIfPresent() {
//		Bundle[] bundles = context.getBundles();
//		for(int i=0; i < bundles.length; i++) {
//			if (bundles[i].getSymbolicName().equals("org.eclipse.ecf.osgi.services.discovery.local")) {
//				try {
//					bundles[i].start();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

//	private Filter createRemoteFilter() throws InvalidSyntaxException {
//		// This filter looks for IHello instances that have the 
//		// 'service.imported' property set, as specified by OSGi 4.2
//		// remote services spec (Chapter 13)
//		return context.createFilter("(&("
//				+ org.osgi.framework.Constants.OBJECTCLASS + "="
//				+ Server.class.getName() + ")(service.imported=*))");
//	}
	
//	void registerHelloRemoteService() {
//		// Setup properties for remote service distribution, as per OSGi 4.2
//		// remote services
//		// specification (chap 13 in compendium spec)
//		Properties props = new Properties();
//		// add OSGi service property indicated export of all interfaces exposed
//		// by service (wildcard)
//		props.put(IDistributionConstants.SERVICE_EXPORTED_INTERFACES,
//				IDistributionConstants.SERVICE_EXPORTED_INTERFACES_WILDCARD);
//		// add OSGi service property specifying config
//		props.put(IDistributionConstants.SERVICE_EXPORTED_CONFIGS,
//				containerType);
//		// add ECF service property specifying container factory args
//		props.put(
//				IDistributionConstants.SERVICE_EXPORTED_CONTAINER_FACTORY_ARGUMENTS,
//				containerId);
//		// register remote service
//		helloRegistration = context.registerService(
//				Server.class.getName(), new Hello(), props);
//		// tell everyone
//		System.out.println("Host: Hello Service Registered");
//	}


}
