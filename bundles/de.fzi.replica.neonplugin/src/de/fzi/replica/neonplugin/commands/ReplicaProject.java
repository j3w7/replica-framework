package de.fzi.replica.neonplugin.commands;

import org.neontoolkit.core.command.CommandException;
import org.neontoolkit.core.exception.InternalNeOnException;
import org.neontoolkit.core.exception.NeOnCoreException;

import com.ontoprise.ontostudio.owl.model.OWLManchesterProject;

import de.fzi.replica.neonplugin.Activator;

public class ReplicaProject extends OWLManchesterProject {

	public ReplicaProject(String name) {
		super(name);
	}
	
	@Override
    public String retrieveOntologyUri(String physicalUri) throws NeOnCoreException {
        return "http://ReplicaProject/defaultOntologyUri";
    }

    @Override
    public void createOntology(String ontologyURI, String defaultNamespace) throws NeOnCoreException {
    	Activator.getDefault().logInfo("createOntology("+ontologyURI+", "+defaultNamespace+")");
        try {
            new CreateReplicaOntology(getName(), ontologyURI, defaultNamespace, getNameFromUri(ontologyURI)).run();
            setOntologyDirty(ontologyURI, true);
        } catch (CommandException e) {
            throw new InternalNeOnException(e);
        }
    }

}
