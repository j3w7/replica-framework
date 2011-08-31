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

package de.fzi.replica.fragments;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.fzi.replica.OWLReplicaManager;

public class Activator implements BundleActivator {

	private static BundleContext context;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		OWLReplicaManager.addOWLReplicaOntologyFactory(
				new OWLReplicaOntologyFragmentFactoryImpl());
		System.out.println("Replica Framework Fragments bundle started");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		System.out.println("Replica Framework Fragments bundle stopped");
	}
	
	static BundleContext getContext() {
		return context;
	}
	
	/**
	 * Call this method from other plugins to initiate plugin loading.
	 */
	public static void activate() {}

}
