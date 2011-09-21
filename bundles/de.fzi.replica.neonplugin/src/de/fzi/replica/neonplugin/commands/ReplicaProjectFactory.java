package de.fzi.replica.neonplugin.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.neontoolkit.core.IOntologyProjectFactory;
import org.neontoolkit.core.NeOnCorePlugin;
import org.neontoolkit.core.project.IOntologyProject;
import org.semanticweb.owlapi.model.OWLOntologyFactory;

import de.fzi.replica.neonplugin.Activator;

public class ReplicaProjectFactory implements IOntologyProjectFactory {
	public static final String FACTORY_ID = "de.fzi.replica.neonplugin.commands.ReplicaProjectFactory"; //$NON-NLS-1$
    public static final String ONTOLOGY_LANGUAGE = "OWL2"; //$NON-NLS-1$
    
    private List<OWLOntologyFactory> thirdPartyFactories = new ArrayList<OWLOntologyFactory>();
    
	@Override
	public IOntologyProject createOntologyProject(IProject project) {
		Activator.getDefault().logInfo("createOntologyProject("+project+")");
		ReplicaProject ontologyProject = new ReplicaProject(project.getName());
		ontologyProject.setThirdPartyFactories(thirdPartyFactories);
        NeOnCorePlugin.getDefault().updateMarkers(ontologyProject);
		return ontologyProject;
	}
	
	@Override
	public String getIdentifier() {
		return FACTORY_ID;
	}
	
}
