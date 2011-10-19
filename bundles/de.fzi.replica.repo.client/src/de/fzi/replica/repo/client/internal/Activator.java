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

package de.fzi.replica.repo.client.internal;

import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.fzi.replica.repo.ReplicaRepositoryServiceAsync;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 17.10.2011
 *
 */
public class Activator implements BundleActivator, IDistributionConstants,
		ServiceTrackerCustomizer {

	private BundleContext context;
	private ReplicaRepositoryServiceAsync srv;
	private ServiceReference serviceReference;
	
	private ServiceTracker serviceTracker;
	
	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		this.context = context;
		
//		service tracker
//		serviceReference = context.
//			getServiceReference(ReplicaRepositoryServiceAsync.class.getName());
//		srv = 
//			(ReplicaRepositoryServiceAsync) context.getService(serviceReference);
		
//		serviceTracker = new ServiceTracker(context, createRemoteFilter(), this);
//		serviceTracker.open();
		
		// try to receive groups
//		srv.getGroupsAsync(new IAsyncCallback<Set<Object>>() {
//			@Override
//			public void onSuccess(Set<Object> result) {
//				System.out.println("success, result="+result);
//			}
//			@Override
//			public void onFailure(Throwable exception) {
//				System.out.println("failure, exception="+exception);
//			}
//		});
		
//		startLocalDiscoveryIfPresent();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		context.ungetService(serviceReference);
		context = null;
	}
	
	private Filter createRemoteFilter() throws InvalidSyntaxException {
		// This filter looks for IHello instances that have the 
		// 'service.imported' property set, as specified by OSGi 4.2
		// remote services spec (Chapter 13)
		return context.createFilter("(&("
				+ org.osgi.framework.Constants.OBJECTCLASS + "="
				+ ReplicaRepositoryServiceAsync.class.getName()
				+ ")(" + SERVICE_IMPORTED + "=*))");
	}

	@Override
	public Object addingService(ServiceReference reference) {
		System.out.println("addingService("+reference+")");
		return null;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
	}
	
	private void startLocalDiscoveryIfPresent() {
		Bundle[] bundles = context.getBundles();
		for(int i=0; i < bundles.length; i++) {
			if (bundles[i].getSymbolicName().equals(
					"org.eclipse.ecf.osgi.services.discovery.local")) {
				try {
					bundles[i].start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
