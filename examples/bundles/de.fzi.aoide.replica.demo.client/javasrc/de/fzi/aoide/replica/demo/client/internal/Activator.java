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

package de.fzi.aoide.replica.demo.client.internal;

import java.io.InputStream;

import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.jruby.embed.jsr223.JRubyEngineFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.fzi.aoide.replica.demo.IReplicaDemoServer;
import de.fzi.aoide.replica.util.ScriptRunner;


/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 04.07.2011
 */
public class Activator implements BundleActivator, ServiceTrackerCustomizer {
	
	private static BundleContext context;
	
	private ServiceTracker demoServiceTracker;
	
	private static final String SERVICE_IMPORTED = "service.imported";
	
	static BundleContext getContext() {
		return context;
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		// Create service tracker to track IReplicaDemoServer instances
		demoServiceTracker = new ServiceTracker(bundleContext,
				createRemoteFilter(), this);
		demoServiceTracker.open();
		// Run ruby script
		InputStream is0 = getClass().getResourceAsStream("/rubysrc/DemoClient.rb");
		JRubyEngineFactory jRubyEngineFactory = new JRubyEngineFactory();
		new ScriptRunner(jRubyEngineFactory).run(is0);
		// Run groovy script
		InputStream is1 = getClass().getResourceAsStream("/groovysrc/DemoClient.groovy");
		GroovyScriptEngineFactory groovyEngineFactory = new GroovyScriptEngineFactory();
		new ScriptRunner(groovyEngineFactory).run(is1);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	private Filter createRemoteFilter() throws InvalidSyntaxException {
		// This filter looks for IHello instances that have the
		// 'service.imported' property set, as specified by OSGi 4.2
		// remote services spec (Chapter 13)
		return context.createFilter("(&("
				+ org.osgi.framework.Constants.OBJECTCLASS + "="
				+ IReplicaDemoServer.class.getName() + ")(" + SERVICE_IMPORTED + "=*))");
	}
	
	/**
	 * Called when a REMOTE IReplicaDemoServer instance is registered.
	 */
	public Object addingService(ServiceReference reference) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void modifiedService(ServiceReference arg0, Object arg1) {
		
	}


	@Override
	public void removedService(ServiceReference arg0, Object arg1) {
		
	}
	
}
