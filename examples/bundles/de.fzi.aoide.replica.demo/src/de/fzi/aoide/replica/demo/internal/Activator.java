package de.fzi.aoide.replica.demo.internal;

import java.util.Arrays;
import java.util.Comparator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.application.ApplicationDescriptor;
import org.osgi.service.application.ApplicationHandle;
import org.osgi.util.tracker.ServiceTracker;

import de.fzi.aoide.replica.demo.ReplicaOntologyDemo;

public class Activator implements BundleActivator {
	
	private static BundleContext context;
	
	private static ServiceTracker appDescriptors;
	private static ServiceTracker appHandles;
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Replica Framework Demo bundle started");
		appDescriptors = new ServiceTracker(context, context.
				createFilter("(&(objectClass=" + ApplicationDescriptor.class.getName() +
						")(eclipse.application.type=any.thread))"), null);
		appDescriptors.open();
		appHandles = new ServiceTracker(context, context.
				createFilter("(&(objectClass=" + ApplicationHandle.class.getName() + 
						")(eclipse.application.type=any.thread))"), null);
		appHandles.open();
		Activator.context = context;
		
		new ReplicaOntologyDemo().start();
	}

	public void stop(BundleContext context) throws Exception {
		if (appDescriptors != null) {
			appDescriptors.close();
		}
		if (appHandles != null) {
			appHandles.close();
		}
		appDescriptors = null;
		appHandles = null;
		Activator.context = null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ApplicationDescriptor[] getApplications() {
		ServiceTracker apps = appDescriptors;
		if (apps == null) {
			return new ApplicationDescriptor[0];
		}
		Object[] objs = apps.getServices();
		if (objs == null) {
			return new ApplicationDescriptor[0];
		}
		ApplicationDescriptor[] results = new ApplicationDescriptor[objs.length];
		System.arraycopy(objs, 0, results, 0, objs.length);
		Arrays.sort(results, new Comparator() {
			public int compare(Object o1, Object o2) {
				ApplicationDescriptor app1 = (ApplicationDescriptor) o1;
				ApplicationDescriptor app2 = (ApplicationDescriptor) o2;
				return app1.getApplicationId().compareTo(app2.getApplicationId());
			}
		});
		return results;
	}

	public static BundleContext getBundleContext() {
		return context;
	}
	
}
