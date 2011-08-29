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

package de.fzi.replica.demo.server.internal;

import java.io.InputStream;

import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.jruby.embed.jsr223.JRubyEngineFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.fzi.replica.util.ScriptRunner;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 22.06.2011
 */
public class Activator implements BundleActivator {
	
	private static BundleContext context;
	
	static BundleContext getContext() {
		return context;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		// Run ruby script
		InputStream is0 = getClass().getResourceAsStream("/rubysrc/DemoServer.rb");
		JRubyEngineFactory jRubyEngineFactory = new JRubyEngineFactory();
		new ScriptRunner(jRubyEngineFactory).run(is0);
		// Run groovy script
		InputStream is1 = getClass().getResourceAsStream("/groovysrc/DemoServer.groovy");
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
	
}
