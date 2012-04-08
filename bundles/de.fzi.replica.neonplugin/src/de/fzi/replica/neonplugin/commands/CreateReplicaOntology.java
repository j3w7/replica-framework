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

import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_ID;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_TYPE;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_TARGET_ID;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.ecf.core.ContainerConnectException;
import org.neontoolkit.core.NeOnCorePlugin;
import org.neontoolkit.core.command.CommandException;
import org.neontoolkit.core.command.DatamodelCommand;
import org.neontoolkit.core.exception.NeOnCoreException;
import org.neontoolkit.core.project.IOntologyProject;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFactory;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.ontoprise.ontostudio.owl.model.OWLManchesterProjectFactory;
import com.ontoprise.ontostudio.owl.model.OWLNamespaces;

import de.fzi.replica.app.Application.StartupException;
import de.fzi.replica.app.client.Client;
import de.fzi.replica.app.client.Client.AddException;
import de.fzi.replica.app.client.Client.ConnectException;
import de.fzi.replica.app.client.Client.FetchException;
import de.fzi.replica.app.client.Client.OnOntologyAddedListener;
import de.fzi.replica.app.client.Client.OnOntologyIDsReceivedListener;
import de.fzi.replica.app.client.Client.OnOntologyReceivedListener;
import de.fzi.replica.app.client.DefaultClientFactory;
import de.fzi.replica.neonplugin.Activator;
import de.fzi.replica.neonplugin.wizard.NewReplicaOntologyWizardPage;

/**
 * @author diwe, novacek
 *
 */
public class CreateReplicaOntology extends DatamodelCommand {
	
	private Client client;
	
//	private OWLOntologyID ontoID = new OWLOntologyID(
//			IRI.create("replica://ontology.com/version"));
	
	protected final String DEFAULT_CONTAINER_TYPE_CLIENT = "ecf.generic.client";
	protected final String DEFAULT_CONTAINER_TYPE_SERVER = "ecf.generic.server";
//	protected final String DEFAULT_CONTAINER_ID_SERVER = "ecftcp://localhost:{0}/server";
	protected final String DEFAULT_CONTAINER_ID_SERVER = "ecftcp://localhost:10000/server";
	
	protected String containerTypeClient = DEFAULT_CONTAINER_TYPE_CLIENT;
	protected String containerTypeServer = DEFAULT_CONTAINER_TYPE_SERVER;
	protected String containerIDServer = DEFAULT_CONTAINER_ID_SERVER;
    
    public CreateReplicaOntology(String project, String ontologyUri,
    		String defaultNamespace, String fileName) {
        super(project, ontologyUri, defaultNamespace, fileName);
        Activator.getDefault().logInfo("CreateReplicaOntology("+project+", "+ontologyUri+", "+defaultNamespace+", "+fileName);
        
        Properties c = createClientConfig("client"+
				Integer.parseInt(System.getenv("INSTANCE").toString()));
        client = new DefaultClientFactory().createClient(c); // FIXME in factory
        client.setConfiguration(c);
        try {
        	client.start();
			client.connect();
			Thread.sleep(5000);
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (StartupException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private Properties createClientConfig(String containerId) {
    	Properties configuration = new Properties();
    	System.out.println("creating config...");
    	String t = NewReplicaOntologyWizardPage.containerType;
//    	if(DEFAULT_CONTAINER_TYPE_CLIENT.equals(t)) {
    		configuration.put(CONFIG_KEYWORD_CONTAINER_TYPE, t);
    		configuration.put(CONFIG_KEYWORD_TARGET_ID, containerIDServer);    		
//    	} else {
//    		configuration.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
//    		configuration.put(CONFIG_KEYWORD_TARGET_ID, containerIDServer);
//    	}
		configuration.put(CONFIG_KEYWORD_CONTAINER_ID, containerId);
		return configuration;
    }
    
//	protected void createClients(String... ids) {
////		clients = new HashMap<String, CommManager>();
////		CommManagerFactory fac = new DefaultCommManagerFactory();
////		for(int i = 0; i < ids.length; i++) {
////			Properties configuration = new Properties();
////			configuration.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
////			configuration.put(CONFIG_KEYWORD_TARGET_ID, containerIDServer);
////			configuration.put(CONFIG_KEYWORD_CONTAINER_ID, ids[i]);
////			CommManager client = fac.createCommManager();
////			client.setConfiguration(configuration);
////			clients.put(ids[i], client);
////		}
//		clients = new HashMap<String, Client>();
//		ClientFactory fac = new DefaultClientFactory();
//		for(int i = 0; i < ids.length; i++) {
//			Properties configuration = new Properties();
//			configuration.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
//			configuration.put(CONFIG_KEYWORD_TARGET_ID, containerIDServer);
//			configuration.put(CONFIG_KEYWORD_CONTAINER_ID, ids[i]);
//			// TODO about to come: config defaults
////			configuration.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_TYPE, containerTypeClient);
////			configuration.put(CONFIG_KEYWORD_DEFAULT_TARGET_ID, containerIDServer);
////			configuration.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_ID, ids[i]);
//			Client client = fac.createClient(configuration);
////			client.setConfiguration(configuration);
//			clients.put(ids[i], client);
//		}
//	}


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
                
                
//                manager.addOntologyFactory(new OWLReplicaOntologyFactoryImpl());
                
                // Check factories
                for(OWLOntologyFactory fac : manager.getOntologyFactories()) {
                	Activator.getDefault().logInfo("CreateReplicaOntology.perform(): fac="+fac);
                }
                OWLOntology ontology = manager.createOntology(IRI.create(ontologyURI));
                manager.setOntologyDocumentIRI(ontology, IRI.create(physicalUri));
                Activator.getDefault().logInfo("ontology="+ontology);
                
                // Activate shared ontology by adding it to the pool
                // of shared ontologies
                try {
                	Activator.getDefault().logInfo(
                			"this is instance "+
                				Integer.parseInt(System.getenv("INSTANCE").toString()));
                	if(Integer.parseInt(System.getenv("INSTANCE").toString()) == 0) {
                		// Server
                		shareOntology(ontology);
                		
                		try {
							Thread.sleep(2000);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
                		
                		// try to receive it
//                		Activator.getDefault().logInfo("retrieved="+retrieveOntology());
                		
//                		CommManager commManager = new DefaultCommManagerFactory().createCommManager();
//                		Properties cp = NewReplicaOntologyWizardPage.createConnectionProperties(false);
//                		cp.remove(CONFIG_KEYWORD_CONTAINER_ID);
//                		cp.put(CONFIG_KEYWORD_CONTAINER_ID, "client100");
//                		Connection connection = commManager.createConnection(cp); // get server propertiesTODO: server/client?!
//                		connection.connect();
//                		
//                		// Get the shared ontology
//                		ID sharedObjectID = IDFactory.getDefault().createStringID("NeOnReplicaOntology");
//                		ISharedObject sharedObject = connection.getSharedObjectContainer().
//                			getSharedObjectManager().getSharedObject(sharedObjectID);
//                		System.out.println("sharedObject="+sharedObject);
                	} else if(Integer.parseInt(System.getenv("INSTANCE").toString()) == 1) {
                		// Client
                		Activator.getDefault().logInfo("Client - retrieving ontology");
                		ontology = retrieveOntology();
                	}
				} catch (ContainerConnectException e) {
					e.printStackTrace();
				}
                
                Activator.getDefault().logInfo(
                		"this.getArgument(0)="+this.getArgument(0) + 
                		"this.getArgument(1)="+this.getArgument(1) + 
                		"this.getArgument(2)="+this.getArgument(2) );
				
				// Register a change listener
				ontology.getOWLOntologyManager().addOntologyChangeListener(
						new OWLOntologyChangeListener() {
							@Override
							public void ontologiesChanged(
									List<? extends OWLOntologyChange> changes)
									throws OWLException {
								System.out.println("changes="+changes);
							}
						});
                
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
                    // finally share the ontology
//                    shareOntology(ontology);
                } catch (NeOnCoreException e) {
                    throw new CommandException(e);
                }
//                catch (ContainerConnectException e) {
//					e.printStackTrace();
//				} catch (SharedObjectAddException e) {
//					e.printStackTrace();
//				}
            }
        } catch (OWLOntologyCreationException e) {
            throw new CommandException(e);
        } catch (NeOnCoreException nce) {
            throw new CommandException(nce);
        }
    }
    
//    // Use for a client connection
//	private String containerTypeClient = DEFAULT_CONTAINER_TYPE_CLIENT;
//	private String containerIDClient = DEFAULT_CONTAINER_ID_CLIENT;
//	private String targetID = DEFAULT_CONTAINER_ID_SERVER;
//	// Use for a server connection, uncomment previous
//	private String containerTypeServer = DEFAULT_CONTAINER_TYPE_SERVER;
//	private String containerIDServer = DEFAULT_CONTAINER_ID_SERVER;
//	
////	Properties connectionProperties = createConnectionProperties();
//    
//	private static final String DEFAULT_CONTAINER_TYPE_CLIENT = "ecf.generic.client";
//	private static final String DEFAULT_CONTAINER_TYPE_SERVER = "ecf.generic.server";
//	private static final String DEFAULT_CONTAINER_ID_SERVER = "ecftcp://192.168.123.35:10000/server"; // Set to interface IP
//	private static final String DEFAULT_CONTAINER_ID_CLIENT = "client1";
//    
//	private static final String CONFIG_KEYWORD_CONNECTION_ID = "connectionID";
//	private static final String CONFIG_KEYWORD_CONTAINER_TYPE = "containerType";
//	private static final String CONFIG_KEYWORD_CONTAINER_ID = "containerID";
//	private static final String CONFIG_KEYWORD_TARGET_ID = "targetID";
//	private static final String CONFIG_KEYWORD_SHAREDOBJECTS = "sharedObjects";
//	private static final String CONFIG_KEYWORD_CHANNELS = "channels";
    
//    private Properties createConnectionProperties() {
//    	Properties connectionProperties = new Properties();
////    	if(NewReplicaWizardPage.SERVER_STARTED) {
////    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_TYPE, NewReplicaWizardPage.containerTypeClient);
////    		connectionProperties.put(CONFIG_KEYWORD_TARGET_ID, NewReplicaWizardPage.targetID);
////    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, NewReplicaWizardPage.containerIDClient);    		
////    	} else {
////    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_TYPE, NewReplicaWizardPage.containerTypeServer);
////    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, NewReplicaWizardPage.containerIDServer);    		
////    	}
//    	Activator.getDefault().logInfo(
//    			this.getClass()+".connectionProperties="+connectionProperties);
//    	return connectionProperties;
//    }
    
//    Connection connection;
    
    private void shareOntology(final OWLOntology ontology) {
    	System.out.println("shareOntology(), ID="+ontology.getOntologyID());
    	try {
			client.addOWLOntology(ontology, Collections.singleton("neon"), 
				new OnOntologyAddedListener() {
					@Override
					public void onOntologyAdded(Result result) {
						System.out.println("onOntologyAdded(), result="+result);
					}
			});
			Thread.sleep(8000);
		} catch (AddException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("shareOntology() finished, ontology="+ontology);
    }
    
    private OWLOntology retrieveOntology()
			throws ContainerConnectException {
    	System.out.println("retrieveOntology()");
    	
    	// TODO get onto ID from a dialog
    	OWLOntologyID ontoID = new OWLOntologyID(
    			IRI.create("replica://www.NewOnto1.org/ontology1"));
    	
    	final OntoHolder h = new OntoHolder();
    	try {
    		client.getOWLOntologyIDs(Collections.singleton("neon"),
    				new OnOntologyIDsReceivedListener() {
				@Override
				public void onOntologyIDsGot(Result result,
						Map<Object, Set<OWLOntologyID>> ids) {
					System.out.println("onOntologyIDsGot("+
							result+", ids="+ids);
				}
			});
    		Thread.sleep(5000);
    		System.out.println("client.getOWLOntology");
			client.getOWLOntology(ontoID, new OnOntologyReceivedListener() {
				@Override
				public void onOntologyReceived(Result result, OWLOntology onto) {
					System.out.println("onOntologyReceived(), result="+result+
							", onto="+onto);
					h.onto = onto;
				}
			});
			while(h.onto == null) {
				// wait
				Thread.sleep(1000);
			}
		} catch (FetchException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
		System.out.println("retrieveOntology() finished, h.onto="+h.onto);
		return h.onto;
    }
    
    class OntoHolder {
    	OWLOntology onto;
    }
    
    
    
    
    
    
    
    
    /*
     * 	 	shareOntology and retrieveOntology without replica.app
     */
    
//    
//    
//    private void shareOntology(final OWLOntology ontology)
//			throws ContainerConnectException, SharedObjectAddException {
//		Activator.getDefault().logInfo("CreateReplicaOntology.shareOntology("+ontology+")");
//		// Create and open a connection
//		CommManager commManager = new DefaultCommManagerFactory().createCommManager();
//		Connection connection = commManager.createConnection(
//				NewReplicaOntologyWizardPage.createConnectionProperties(false)); // get client propertiesTODO: server/client?!
////		connection = commManager.createConnection(
////				NewReplicaOntologyWizardPage.createConnectionProperties(false)); // get client propertiesTODO: server/client?!
//		connection.connect();
//		
//		// Add the shared ontology
//		final ID sharedObjectID = IDFactory.getDefault().createStringID("NeOnReplicaOntology");
//		connection.getSharedObjectContainer().getSharedObjectManager().
//			addSharedObject(sharedObjectID, (ISharedObject) ontology, null);
//		// Debugging
//		System.out.print("\tAdded Shared object, IDs:");
//		for(ID soID : connection.getSharedObjectContainer().getSharedObjectManager().getSharedObjectIDs()) {
//			System.out.print("\tsharedObjectID="+soID);
//		}
//		System.out.println();
//		
//		ISharedObject is = connection.getSharedObjectContainer().
//			getSharedObjectManager().getSharedObject(
//					IDFactory.getDefault().createStringID("NeOnReplicaOntology"));
//		System.out.println("shareOntology(..) finished is="+is);
//		
//		/*
//		 * reactivate shared object, when client 2 connects
//		 */
//		final ISharedObjectContainer c = connection.getSharedObjectContainer();
//		c.addListener(
//			new IContainerListener() {
//				@Override
//				public void handleEvent(IContainerEvent event) {
//					System.out.println("event="+event);
//					if(event instanceof ContainerConnectedEvent) {
//						System.out.println("container connected: "+
//								event.getLocalContainerID());
//						System.out.println("reactivating shared object");
//						try {
//							// Wait for client 1 connection...
//							Thread.sleep(1000);
//							c.getSharedObjectManager().
//								removeSharedObject(sharedObjectID);
//							System.out.println("shared object removed");
//							Thread.sleep(1000);
//							System.out.println("about to add shared object");
//							c.getSharedObjectManager().
//								addSharedObject(sharedObjectID,
//									(ISharedObject) ontology, null);
//							System.out.println("shared object added");
////							((OWLReplicaOntologyImpl) ontology).
////								replicateToRemoteContainersPub();
////							System.out.println("shared object replicated");
//						} catch (SharedObjectAddException e) {
//							e.printStackTrace();
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//		
////		connection.dispose();
////		commManager.dispose();
//	}
//	
//	private OWLOntology retrieveOntology()
//			throws ContainerConnectException {
//		Activator.getDefault().logInfo("CreateReplicaOntology.retrieveOntology()");
//		// Create and open a connection
//		CommManager commManager = new DefaultCommManagerFactory().createCommManager();
//		Connection connection = commManager.createConnection(
//				NewReplicaOntologyWizardPage.createConnectionProperties(false)); // get server propertiesTODO: server/client?!
//		connection.connect();
//		// Wait
////		try { Thread.sleep(4000); } catch (InterruptedException e) { e.printStackTrace(); }
////		Activator.getDefault().logInfo(
////				"\tconnectedID="+
////				connection.getSharedObjectContainer().getConnectedID()+" ");
////		for(ID soID : connection.getSharedObjectContainer().getSharedObjectManager().getSharedObjectIDs()) {
////			Activator.getDefault().logInfo("\tfound sharedObjectID="+soID+" ");
////		}
//		
//		// Wait for shared object reactivation
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		// Get the shared ontology
//		ID sharedObjectID = IDFactory.getDefault().createStringID("NeOnReplicaOntology");
//		ISharedObject sharedObject = connection.getSharedObjectContainer().
//			getSharedObjectManager().getSharedObject(sharedObjectID);
//		
//		Activator.getDefault().logInfo("retrieveOntology(..) finished "+
//				"(OWLOntology) sharedObject="+(OWLOntology) sharedObject);
////		connection.dispose();
////		commManager.dispose();
//		
//		connection.getSharedObjectContainer().addListener(
//			new IContainerListener() {
//				@Override
//				public void handleEvent(IContainerEvent event) {
//					System.out.println("event="+event);
//				}
//			});
//		
//		
////		Object sharedObject = null;
//		return (OWLOntology) sharedObject;
//	}
//	
//	

}
