/*****************************************************************************
 * Copyright (c) 2009 ontoprise GmbH.
 *
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package de.fzi.replica.neonplugin.commands;

import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.neontoolkit.core.NeOnCorePlugin;
import org.neontoolkit.core.command.CommandException;
import org.neontoolkit.core.command.DatamodelCommand;
import org.neontoolkit.core.exception.NeOnCoreException;
import org.neontoolkit.core.project.IOntologyProject;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.ontoprise.ontostudio.owl.model.OWLManchesterProjectFactory;
import com.ontoprise.ontostudio.owl.model.OWLNamespaces;

import de.fzi.replica.OWLReplicaOntologyFactoryImpl;
import de.fzi.replica.comm.CommManager;
import de.fzi.replica.comm.Connection;
import de.fzi.replica.comm.DefaultCommManagerFactory;
import de.fzi.replica.neonplugin.Activator;

/**
 * @author diwe
 *
 */
public class CreateReplicaOntology extends DatamodelCommand {
	
	Properties connectionProperties;
    
    public CreateReplicaOntology(String project, String ontologyUri, String defaultNamespace, String fileName) {
        super(project, ontologyUri, defaultNamespace, fileName);
        Activator.getDefault().logInfo("CreateReplicaOntology("+project+", "+ontologyUri+", "+defaultNamespace+", "+fileName);
    }

    @Override
    protected void perform() throws CommandException {
    	Activator.getDefault().logInfo("CreateReplicaOntology.perform()");
        try {        	
            IOntologyProject ontologyProject = NeOnCorePlugin.getDefault().getOntologyProject(getProjectName());
            String ontologyURI = (String)getArgument(1);
            String defaultNamespace = (String)getArgument(2);
            String fileName = (String)getArgument(3);
            
            // TODO migration: hack to make the adapter work for the moment... 
            // will most likely not work for the export since the filename is missing
            if (!OWLManchesterProjectFactory.FACTORY_ID.equals(ontologyProject.getProjectFactoryId())) {
            	Activator.getDefault().logInfo("CreateReplicaOntology.perform(): KAON2 context");
                // KAON2 context
                ontologyProject.createOntology(ontologyURI, defaultNamespace);
                getOntologyProject().readAndDispatchWhileWaitingForEvents();
            } else {
            	Activator.getDefault().logInfo("CreateReplicaOntology.perform(): Manchester context");
                // Manchester context
                IProject project = NeOnCorePlugin.getDefault().getProject(getProjectName());
                if (fileName == null || "".equals(fileName)) { //$NON-NLS-1$
                    fileName = ontologyProject.getNewOntologyFilenameFromURI(ontologyURI, null);
                }
                String physicalUri = project.getFile(fileName).getLocationURI().toString();
                // MAPI remove OWLOntologyManager
                OWLOntologyManager manager = ontologyProject.getAdapter(OWLOntologyManager.class);
                manager.addOntologyFactory(new OWLReplicaOntologyFactoryImpl());
                // Check factories
                for(OWLOntologyFactory fac : manager.getOntologyFactories()) {
                	Activator.getDefault().logInfo("CreateReplicaOntology.perform(): fac="+fac);
                }
                OWLOntology ontology = manager.createOntology(IRI.create(ontologyURI));
                manager.setOntologyDocumentIRI(ontology, IRI.create(physicalUri));
                ontologyProject.readAndDispatchWhileWaitingForEvents();
                
                // The manchester implementation has no event for new created ontologies, we need to update manually.
                ontologyProject.addOntology(ontologyURI);

                try {
                    if (defaultNamespace != null && !"".equals(defaultNamespace)) { //$NON-NLS-1$
                        ontologyProject.setDefaultNamespace(ontologyURI, defaultNamespace);
                    }
                    ontologyProject.setNamespacePrefix(ontologyURI, "owl", OWLNamespaces.OWL_NS); //$NON-NLS-1$
                    ontologyProject.setNamespacePrefix(ontologyURI, "owl",OWLNamespaces.OWL_NS); //$NON-NLS-1$
                    ontologyProject.setNamespacePrefix(ontologyURI, "owl2",org.semanticweb.owlapi.vocab.Namespaces.OWL2.toString()); //$NON-NLS-1$
                    ontologyProject.setNamespacePrefix(ontologyURI, "swrl",OWLNamespaces.SWRL_NS); //$NON-NLS-1$
                    ontologyProject.setNamespacePrefix(ontologyURI, "swrlb",OWLNamespaces.SWRLB_NS); //$NON-NLS-1$
                    ontologyProject.setNamespacePrefix(ontologyURI, "swrlx",OWLNamespaces.SWRLX_NS); //$NON-NLS-1$
                    ontologyProject.setNamespacePrefix(ontologyURI, "rdf",OWLNamespaces.RDF_NS); //$NON-NLS-1$
                    ontologyProject.setNamespacePrefix(ontologyURI, "rdfs",OWLNamespaces.RDFS_NS); //$NON-NLS-1$
                    ontologyProject.setNamespacePrefix(ontologyURI, "xsd",OWLNamespaces.XSD_NS); //$NON-NLS-1$
                } catch (NeOnCoreException e) {
                    throw new CommandException(e);
                }
            }
            
        } catch (OWLOntologyCreationException e) {
            throw new CommandException(e);
        } catch (NeOnCoreException nce) {
            throw new CommandException(nce);
        }
    }
    
    
    
    private void shareOntology(OWLOntology ontology)
			throws ContainerConnectException, SharedObjectAddException {
		// Create and open a connection
		CommManager commManager = new DefaultCommManagerFactory().createCommManager();
		Connection connection = commManager.createConnection(connectionProperties);
		connection.connect();
		// Add the shared ontology
		ID sharedObjectID = IDFactory.getDefault().createStringID("NeOnReplicaOntology");
		connection.getSharedObjectContainer().getSharedObjectManager().
			addSharedObject(sharedObjectID, (ISharedObject) ontology, null);
		// Debugging
		System.out.print("Added Shared object, IDs:");
		for(ID soID : connection.getSharedObjectContainer().getSharedObjectManager().getSharedObjectIDs()) {
			System.out.print(" sharedObjectID="+soID);
		}
		System.out.println();
	}

	private OWLOntology retrieveOntology() throws ContainerConnectException {
		// Create and open a connection
		CommManager commManager = new DefaultCommManagerFactory().createCommManager();
		Connection connection = commManager.createConnection(connectionProperties);
		connection.connect();
		try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
		System.out.print("connectedID="+connection.getSharedObjectContainer().getConnectedID()+" ");
		for(ID soID : connection.getSharedObjectContainer().getSharedObjectManager().getSharedObjectIDs()) {
			System.out.print("sharedObjectID="+soID+" ");
		}
		System.out.println();
		// Get the shared ontology
		ID sharedObjectID = IDFactory.getDefault().createStringID("NeOnReplicaOntology");
		ISharedObject sharedObject = connection.getSharedObjectContainer().
			getSharedObjectManager().getSharedObject(sharedObjectID);
		return (OWLOntology) sharedObject;
	}
}
