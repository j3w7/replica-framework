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

package de.fzi.replica.neonplugin;

import org.neontoolkit.core.LoggingPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends LoggingPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.fzi.replica.neonplugin";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		logInfo("ReplicaOntology NeOn Plugin started");
		
//		OntologyProjectManager projectManager = OntologyProjectManager.getDefault();
//		String[] projectNames = projectManager.getOntologyProjects();
//		for(String projectName : projectNames) {
//			IOntologyProject project = projectManager.getOntologyProject(projectName);
//			OWLOntologyManager manager = project.getAdapter(OWLOntologyManager.class);
//			manager.addOntologyFactory(new OWLReplicaOntologyFactory());
//		}
//		OWLOntologyInfo ontoInfo = OWLFileUtilities.getOntologyInfo(IRI.create(new File("file:///home/pnx/Desktop/pizza.owl")));
//		logInfo("OntologyID="+ontoInfo.getOntologyID());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
    	if (plugin == null) {
    		plugin = new Activator();
    	}
        return plugin;
	}

}
