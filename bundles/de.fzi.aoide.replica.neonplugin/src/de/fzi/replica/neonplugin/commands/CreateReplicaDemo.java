package de.fzi.replica.neonplugin.commands;

import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_ID;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_TYPE;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_TARGET_ID;

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
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.ontoprise.ontostudio.owl.model.OWLManchesterProjectFactory;
import com.ontoprise.ontostudio.owl.model.OWLNamespaces;

import de.fzi.replica.OWLReplicaOntologyFactoryImpl;
import de.fzi.replica.comm.CommManager;
import de.fzi.replica.comm.Connection;
import de.fzi.replica.comm.DefaultCommManagerFactory;
import de.fzi.replica.neonplugin.Activator;

public class CreateReplicaDemo extends DatamodelCommand {
	
	private static final boolean IS_CLIENT = true;
	// One client adds a shared ontology, another one retrieves it.
	private static final boolean IS_ADDING_SHARED_OBJECT = true;
	
	private static final String DEFAULT_CONTAINER_TYPE_CLIENT = "ecf.generic.client";
	private static final String DEFAULT_CONTAINER_TYPE_SERVER = "ecf.generic.server";
	private static final String DEFAULT_CONTAINER_ID_SERVER = "ecftcp://192.168.123.35:10000/server"; // Set to interface IP
	private static final String DEFAULT_CONTAINER_ID_CLIENT = "client1";
	
	// Use for a client connection
	private String containerTypeClient = DEFAULT_CONTAINER_TYPE_CLIENT;
	private String containerIDClient = DEFAULT_CONTAINER_ID_CLIENT;
	private String targetID = DEFAULT_CONTAINER_ID_SERVER;
	// Use for a server connection, uncomment previous
	private String containerTypeServer = DEFAULT_CONTAINER_TYPE_SERVER;
	private String containerIDServer = DEFAULT_CONTAINER_ID_SERVER;

	public CreateReplicaDemo(String project, String ontologyUri, String defaultNamespace, String fileName) {
        super(project, ontologyUri, defaultNamespace, fileName);
    }

    @Override
    protected void perform() throws CommandException {
        try {
            IOntologyProject ontologyProject = NeOnCorePlugin.getDefault().getOntologyProject(getProjectName());
            String ontologyURI = (String)getArgument(1);
            String defaultNamespace = (String)getArgument(2);
            String fileName = (String)getArgument(3);
            
            Activator.getDefault().logInfo("About to create ReplicaOntology (CreateReplicaOntology), ontologyProject="+ontologyProject);
            
            // TODO migration: hack to make the adapter work for the moment... 
            // will most likely not work for the export since the filename is missing
            if (!OWLManchesterProjectFactory.FACTORY_ID.equals(ontologyProject.getProjectFactoryId())) {
                
                // KAON2 context
                ontologyProject.createOntology(ontologyURI, defaultNamespace);
                getOntologyProject().readAndDispatchWhileWaitingForEvents();
            } else {
                
                // Manchester context
                IProject project = NeOnCorePlugin.getDefault().getProject(getProjectName());
                if (fileName == null || "".equals(fileName)) { //$NON-NLS-1$
                    fileName = ontologyProject.getNewOntologyFilenameFromURI(ontologyURI, null);
                }
                String physicalUri = project.getFile(fileName).getLocationURI().toString();
                // MAPI remove OWLOntologyManager
                OWLOntologyManager manager = ontologyProject.getAdapter(OWLOntologyManager.class);
                logInfo("manager="+manager);
                // Add the ontology factory for creating the replica instance
                manager.addOntologyFactory(new OWLReplicaOntologyFactoryImpl());
                logInfo("OWLReplicaOntologyFactory has been added");
                OWLOntology ontology = manager.createOntology(IRI.create(ontologyURI));
                logInfo("Ontology has been created, ontology.getClass()="+ontology.getClass());
                shareOrSetOntology(ontology);
                manager.setOntologyDocumentIRI(ontology, IRI.create(physicalUri));
                ontologyProject.readAndDispatchWhileWaitingForEvents();
                // The manchester implementation has no event for new created ontologies, we need to update manually.
                ontologyProject.addOntology(ontologyURI);
                logInfo("Ontology has been added");
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
        } catch (ContainerConnectException e) {
        	throw new CommandException(e);
		} catch (SharedObjectAddException e) {
			System.out.println("SharedObjectAddException, cause: "+e.getCause());
			throw new CommandException(e);
		}
    }
    
    private void shareOrSetOntology(OWLOntology ontology)
			throws ContainerConnectException, SharedObjectAddException {
		if(IS_ADDING_SHARED_OBJECT) {
			shareOntology(ontology);
			logInfo("Ontology now shared");
		} else { // This client retrieves it
			ontology = retrieveOntology();
			logInfo("Shared ontology received, ontology="+ontology);
		}
	}
    
    private Properties createConnectionProperties() {
    	Properties connectionProperties = new Properties();
    	if(IS_CLIENT) {
    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
    		connectionProperties.put(CONFIG_KEYWORD_TARGET_ID, targetID);
    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, containerIDClient);    		
    	} else {
    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeServer);
    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, containerIDServer);    		
    	}
    	return connectionProperties;
    }
    
    private void shareOntology(OWLOntology ontology)
    		throws ContainerConnectException, SharedObjectAddException {
    	// Create and open a connection
    	CommManager commManager = new DefaultCommManagerFactory().createCommManager();
    	Connection connection = commManager.createConnection(createConnectionProperties());
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
    	Connection connection = commManager.createConnection(createConnectionProperties());
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
    
    private void logInfo(String msg) {
    	// Most useful method
    	Activator.getDefault().logInfo(msg);
    }
    
}
