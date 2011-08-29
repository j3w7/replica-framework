package de.fzi.replica.app.internal;

import org.eclipse.ecf.core.util.SystemLogService;
import org.eclipse.ecf.storage.IIDStore;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import de.fzi.replica.comm.CommManager;

public class Activator implements BundleActivator {
	
	private static final String PLUGIN_ID = "de.fzi.aoide.replica.app";

	private static Activator instance;
	
	private BundleContext context; // plugin is a singleton anyway, so could be static
	
	private LogService logService = null;
	
//	private ServiceTracker containerManagerTracker;
	private ServiceTracker commManagerTracker;
	private ServiceTracker idStoreTracker;
	private ServiceTracker securePreferencesTracker;
	private ServiceTracker logServiceTracker;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		this.context = context;
		instance = this;
		System.out.println("ReplicaOntology Application bundle started");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		context = null;
		if (logServiceTracker != null) {
			logServiceTracker.close();
			logServiceTracker = null;
			logService = null;
		}
		System.out.println("ReplicaOntology Application bundle stopped");
	}
	
	public static Activator getDefault() {
		return instance;
	}
	
//	public IContainerManager getContainerManager() {
//		if(containerManagerTracker == null) {
//			containerManagerTracker = new ServiceTracker(context, IContainerManager.class.getName(), null);
//			containerManagerTracker.open();
//		}
//		return (IContainerManager) containerManagerTracker.getService();
//	}
	
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

}
