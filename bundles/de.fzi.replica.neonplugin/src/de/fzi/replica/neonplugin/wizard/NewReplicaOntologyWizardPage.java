/*****************************************************************************
 * Copyright (c) 2009 ontoprise GmbH.
 *
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package de.fzi.replica.neonplugin.wizard;

import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_ID;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_TYPE;
import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_TARGET_ID;

import java.util.Properties;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.neontoolkit.core.NeOnCorePlugin;
import org.neontoolkit.core.command.CommandException;
import org.neontoolkit.core.command.ontology.CreateUniqueOntologyUri;
import org.neontoolkit.core.command.ontology.ExistsOntology;
import org.neontoolkit.core.exception.NeOnCoreException;
import org.neontoolkit.core.project.OntologyProjectManager;
import org.neontoolkit.gui.NeOnUIPlugin;
import org.neontoolkit.gui.navigator.elements.IProjectElement;
import org.neontoolkit.gui.util.URIUtils;

import com.ontoprise.ontostudio.owl.gui.Messages;

import de.fzi.replica.app.Application.StartupException;
import de.fzi.replica.app.server.DefaultServerFactory;
import de.fzi.replica.app.server.Server;
import de.fzi.replica.neonplugin.Activator;

/* 
 * Created on: 12.11.2004
 * Created by: Dirk Wenke
 *
 * Function: UI, Ontology, Wizard
 */
/**
 * This class provides the wizard page that is displayed in the NewReplicaOntologyWizard.
 * @author Nico Stieler
 */
public class NewReplicaOntologyWizardPage extends WizardPage {
	
	public static final String DEFAULT_CONTAINER_TYPE_CLIENT = "ecf.generic.client";
	public static final String DEFAULT_CONTAINER_TYPE_SERVER = "ecf.generic.server";
	public static final String DEFAULT_CONTAINER_ID_SERVER = "ecftcp://localhost:10000/server"; // Set to interface IP
	
    private IStructuredSelection _selection;
    private Composite _container;
    private Combo _projectCombo;
    protected boolean _projectComboFixed = false;
    private Button _createButton;
    private Text _ontologyText;
    private Text _namespaceText;
    protected IInputValidator _uriValidator = getInputValidator();
    private String _ontologyURI;
    private String _projectName;
    private boolean _initDone;
	private Combo _containerID;
	private Combo _containerType;

    public NewReplicaOntologyWizardPage(IStructuredSelection selection) {
        super("NewReplicaOntologyWizardPage"); //$NON-NLS-1$
//        setTitle(Messages.NewReplicaOntologyWizardPage_1);
//        setDescription(Messages.NewReplicaOntologyWizardPage_2);
        setTitle("title");
        setDescription("description");
        _selection = selection;
    }
    
    protected IInputValidator getInputValidator() {
        return URIUtils.getOntologyUriValidator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        _container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(3, false);
        _container.setLayout(layout);
        layout.verticalSpacing = 9;
        Label label = new Label(_container, SWT.NULL);
        label.setText(Messages.NewOntologyWizardPage_3); 

        _ontologyText = new Text(_container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        _ontologyText.setLayoutData(gd);
        _ontologyText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if(_initDone)
                    updateStatus();
            }
        });

        gd = new GridData(GridData.FILL_HORIZONTAL);
        Label dummy1 = new Label(_container, SWT.NONE);
        dummy1.setVisible(false);
        dummy1.setLayoutData(gd);
        
        label = new Label(_container, SWT.NONE);
        label.setText(Messages.NewOntologyWizardPage_4); 

        _namespaceText = new Text(_container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.grabExcessHorizontalSpace = true;
        _namespaceText.setLayoutData(gd);
        _namespaceText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if(_initDone)
                    updateStatus();
            }
        });

        gd = new GridData(GridData.FILL_HORIZONTAL);
        Label dummy2 = new Label(_container, SWT.NONE);
        dummy2.setVisible(false);
        dummy2.setLayoutData(gd);

        label = new Label(_container, SWT.NONE);
        label.setText(Messages.NewOntologyWizardPage_5); 

        _projectCombo = new Combo(_container, SWT.BORDER | SWT.READ_ONLY);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.grabExcessHorizontalSpace = true;
        _projectCombo.setLayoutData(gd);
        _projectCombo.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(_initDone)
                    updateStatus();
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        _createButton = new Button(_container, SWT.PUSH);
        _createButton.setText(Messages.NewOntologyWizardPage_11); 
        _createButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                NewReplicaOntologyWizard wizard =
                	new NewReplicaOntologyWizard();
                wizard.init(PlatformUI.getWorkbench(), null);
        		Shell parent = getShell();
        		WizardDialog dialog = new WizardDialog(parent, wizard);
        		dialog.create();
//        		WorkbenchHelp.setHelp(dialog.getShell(), org.eclipse.ui.internal.IHelpContextIds.NEW_WIZARD_SHORTCUT);
        		dialog.open();
        		initialize();
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
            
        });
        _createButton.setVisible(false);
        gd = new GridData(GridData.GRAB_HORIZONTAL);
        _createButton.setLayoutData(gd);
        
        /*
         * ReplicaOntology specific stuff
         */
        
        Label l = new Label(_container, SWT.NONE);
        l.setText("Container Type:");
        
        _containerType = new Combo(_container, SWT.NONE);
        _containerType.add(DEFAULT_CONTAINER_TYPE_CLIENT);
        _containerType.add(DEFAULT_CONTAINER_TYPE_SERVER);
        _containerType.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				System.out.println("modify event="+arg0);
//				containerType=(String) arg0.data;
			}
		});
        
		GridData gd3 = new GridData(GridData.FILL_HORIZONTAL);
		Label dummy3 = new Label(_container, SWT.NONE);
		dummy3.setVisible(false);
		dummy3.setLayoutData(gd3);
        
        Label l2 = new Label(_container, SWT.NONE);
        l2.setText("Target ID:");
        
        _containerID = new Combo(_container, SWT.NONE);
        _containerID.add(DEFAULT_CONTAINER_ID_SERVER);
        _containerID.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				System.out.println("modify event="+arg0);
//				containerType=(String) arg0.data;
			}
		});
        
        initialize();
        updateStatus();
        setControl(_container);
    }

    private void initialize() {
        //initialize project combo
        String[] projects;
        try {
            projects = OntologyProjectManager.getDefault().getOntologyProjects();
            _projectCombo.setItems(projects);
            if (projects.length == 0) {
                _createButton.setVisible(true);
            } else if (projects.length == 1) {
                _projectCombo.select(_projectCombo.indexOf(projects[0]));
            }
            //set the initial values for the id and namespace
            if(_projectComboFixed){
                _ontologyText.setText(_ontologyURI);
                _ontologyText.setEnabled(false);
                _namespaceText.setText(_ontologyURI+"#"); //$NON-NLS-1$
                //set the current selected item
                if(_projectName != null && !_projectName.equals("")) //$NON-NLS-1$
                    _projectCombo.select(_projectCombo.indexOf(_projectName));
            }else{
                //set the current selected item
                if (_selection != null) {
                    Object selection = _selection.getFirstElement();
                    if (selection instanceof IProjectElement) {
                        _projectCombo.select(_projectCombo.indexOf(((IProjectElement) selection).getProjectName()));
                    }
                }
                String newId = new CreateUniqueOntologyUri(getProjectName()).getOntologyUri();
                _ontologyText.setText(newId);
                if (newId.equals("")) { //$NON-NLS-1$
                    _namespaceText.setText(""); //$NON-NLS-1$
                } else {
                    _namespaceText.setText(newId+"#"); //$NON-NLS-1$
                }
            }
            //set the current selected item
            if(_projectName != null && !_projectName.equals("")) //$NON-NLS-1$
                _projectCombo.select(_projectCombo.indexOf(_projectName));
            _projectCombo.setEnabled(!_projectComboFixed);
            _container.layout(true);
        } catch (Exception e) {
        	e.printStackTrace();
            NeOnUIPlugin.getDefault().logError("", e); //$NON-NLS-1$
        }
        _initDone = true;
    }

    private void updateStatus() {
        if (_projectCombo.getItemCount() <= 0) {
            updateStatus(Messages.NewOntologyWizardPage_6); 
            return;
        } else {
            try {
            	String projectName = getProjectName();
                if (NeOnCorePlugin.getDefault().getOntologyProject(projectName) != null) {
                	String ontologyUri = _ontologyText.getText();
                	String errorMsg = _uriValidator.isValid(ontologyUri);
                	if (errorMsg != null) {
                	    updateStatus(errorMsg);
                	    return;
                	}
                	errorMsg = validateNamespace(_namespaceText.getText());
                    if (errorMsg != null) {
                        updateStatus(errorMsg);
                        return;
                    }
                    if (new ExistsOntology(projectName, ontologyUri).exists()) {
                        updateStatus(Messages.NewOntologyWizardPage_15); 
                        return;
                    }
                }
            } catch (CommandException e) {
                NeOnUIPlugin.getDefault().logError("", e); //$NON-NLS-1$
            } catch (NeOnCoreException e) {
            	NeOnUIPlugin.getDefault().logError(Messages.NewOntologyWizardPage_14, e); 
            }
        }

        if (_projectCombo.getSelectionIndex() == -1) {
            updateStatus(Messages.NewOntologyWizardPage_7); 
            return;
        }
        
        String ontologyUri = _ontologyText.getText();
        String namespace = _namespaceText.getText();

        if ("".equals(ontologyUri)) { //$NON-NLS-1$
            updateStatus(Messages.NewOntologyWizardPage_8); 
            return;
        }

        if ("".equals(namespace)) { //$NON-NLS-1$
            updateStatus(Messages.NewOntologyWizardPage_9); 
            return;
        }
        
        /*
         *  Replica specific stuff
         */
        
//        containerIDClient = _containerID.getText();
//        containerIDServer = _containerID.getText();
//        containerTypeClient = _containerType.getText();
//        containerTypeServer = _containerType.getText();
//        
//        Activator.getDefault().logInfo("status updated, "+
//        		"containerIDClient="+containerIDClient+"\n"+
//        		"containerIDServer="+containerIDServer+"\n"+
//        		"containerTypeClient="+containerTypeClient+"\n"+
//        		"containerTypeServer="+containerTypeServer+"\n");
        
        updateStatus(null);
    }

    protected void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    private String validateNamespace(String input) {
        return URIUtils.validateNamespace(input, _uriValidator);
    }

    public String getProjectName() {
    	int index = _projectCombo.getSelectionIndex();
    	if (index != -1) {
            return _projectCombo.getItem(index);
    	}
    	return ""; //$NON-NLS-1$
    }

    public String getOntologyUri() {
        return _ontologyText.getText();
    }

    public String getDefaultNamespace() {
  		return _namespaceText.getText();
    }
    
    public String getContainerType() {
    	return _containerType.getText();
    }
    
    public String getContainerID() {
    	return _containerID.getText();
    }
    
    @Override
    public void performHelp() {
        String helpContextId = org.neontoolkit.gui.IHelpContextIds.OWL_CREATE_ONTOLOGY;
        PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpContextId);
    }
    
    /**
     * @param projectName
     */
    public void setFixed(String projectName, String ontologyURI) {
        _projectName = projectName;
        _ontologyURI = ontologyURI;
        _projectComboFixed = true;
    }
    
//    public static Properties createConnectionProperties(boolean server) {
////    	String info = "";
//    	Properties connectionProperties = new Properties();
//    	int instance = Integer.parseInt(System.getenv("INSTANCE").toString());
//    	if(!server) { // client
////    		info = "\t client instance\n"+
////				Integer.parseInt(System.getenv("INSTANCE").toString())+"\n"+
////				"containerTypeClient="+containerTypeClient+"\n"+
////				"targetID="+targetID+"\n"+
////				"client"+instance;
//    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
//    		connectionProperties.put(CONFIG_KEYWORD_TARGET_ID, targetID);
////    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, containerIDClient);
//			if(instance == 0) {
//				connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, "client0");
//			} else {
//				connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, "client1");
//			}
//    	} else { // server
////    		info = "\t server instance"+
////				Integer.parseInt(System.getenv("INSTANCE").toString())+"\n"+
////				"containerTypeServer="+containerTypeServer+"\n"+
////				"containerIDServer="+containerIDServer;
//    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeServer);
//    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, containerIDServer);    		
//    	}
//    	Activator.getDefault().logInfo("connectionProperties="+
//    			connectionProperties);
//    	return connectionProperties;
//    }
    
//    protected Properties createServerConfig() {
//		Properties connectionConfig = new Properties();
//		connectionConfig.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeServer);
//		connectionConfig.put(CONFIG_KEYWORD_CONTAINER_ID, containerIDServer);
////		connectionConfig.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_TYPE, containerTypeServer);
////		connectionConfig.put(CONFIG_KEYWORD_DEFAULT_CONTAINER_ID, containerIDServer);
//		return connectionConfig;
//	}
    
//    private void startServer() {
//		try {
////			CommManager mgr = new CommManagerImpl();
////			Connection connection = mgr.createConnection(
////					createConnectionProperties(true));
////			connection.connect();
////			connection.getSharedObjectContainer().addListener(
////					new IContainerListener() {
////						@Override
////						public void handleEvent(IContainerEvent event) {
////							Activator.getDefault().logInfo(
////									"event="+event+"\n");
////						}
////					});
//			
//			Server s = new DefaultServerFactory().createServer(createServerConfig());
//			s.start();
//			
//			/*
//			 * Debug output
//			 */
//			
////			System.out.println("");
////			Activator.getDefault().logInfo("connection==null ?   "+
////					(connection!=null?false:true));
//			int instance = Integer.parseInt(System.getenv("INSTANCE").toString());
//	//		if(instance == 0) {
//	//			INST0_SERVER_STARTED = true;
//	//			INST1_SERVER_STARTED = false;
//	//		} else {
//	//			// server is started on instance 1
//	//			INST0_SERVER_STARTED = false;
//	//			INST1_SERVER_STARTED = true;
//	//		}
//			Activator.getDefault().logInfo("Server started on instance "+instance);
//		} catch (StartupException e) {
//			e.printStackTrace();
//		}
//  	}
    
//    class ServerThread extends Thread {
//    	@Override
//    	public void run() {
//    		startServer();
//    		System.out.println("Replica server started");
//    	}
//    }
}


//package de.fzi.replica.neonplugin.wizard;
//
//import java.util.Properties;
//
//import org.eclipse.ecf.core.ContainerConnectException;
//import org.eclipse.jface.dialogs.IInputValidator;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.jface.wizard.WizardDialog;
//import org.eclipse.jface.wizard.WizardPage;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.SelectionListener;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Combo;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.ui.PlatformUI;
//import org.neontoolkit.core.NeOnCorePlugin;
//import org.neontoolkit.core.command.CommandException;
//import org.neontoolkit.core.command.ontology.CreateUniqueOntologyUri;
//import org.neontoolkit.core.command.ontology.ExistsOntology;
//import org.neontoolkit.core.exception.NeOnCoreException;
//import org.neontoolkit.core.project.OntologyProjectManager;
//import org.neontoolkit.gui.NeOnUIPlugin;
//import org.neontoolkit.gui.navigator.elements.IProjectElement;
//import org.neontoolkit.gui.util.URIUtils;
//
//import com.ontoprise.ontostudio.owl.gui.Messages;
//
//import de.fzi.replica.comm.CommManager;
//import de.fzi.replica.comm.CommManagerImpl;
//import de.fzi.replica.comm.Connection;
//import de.fzi.replica.neonplugin.Activator;
//import de.fzi.replica.neonplugin.commands.CreateUniqueReplicaUri;
//
//import static de.fzi.replica.comm.CommManager.*;
//import static de.fzi.replica.comm.Connection.*;
//
//import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_ID;
//import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_CONTAINER_TYPE;
//import static de.fzi.replica.comm.Connection.CONFIG_KEYWORD_TARGET_ID;
//
///* 
// * Created on: 12.11.2004
// * Created by: Dirk Wenke
// * 
// * Modified on: 05.12.2012
// * Modified by: Jan Novacek
// *
// * Function: UI, Ontology, Wizard
// */
///**
// * This class provides the wizard page that is displayed in the NewOntologyWizard.
// * @author Nico Stieler
// */
//public class NewReplicaOntologyWizardPage extends WizardPage {
//	
//	public static boolean IS_CLIENT = true;
//	public static boolean INST0_SERVER_STARTED = true;
//	public static boolean INST1_SERVER_STARTED = true;
//	// One client adds a shared ontology, another one retrieves it.
//	private static final boolean IS_ADDING_SHARED_OBJECT = true;
//	
//	private static final String DEFAULT_CONTAINER_TYPE_CLIENT = "ecf.generic.client";
//	private static final String DEFAULT_CONTAINER_TYPE_SERVER = "ecf.generic.server";
//	private static final String DEFAULT_CONTAINER_ID_SERVER = "ecftcp://192.168.178.100:10000/server"; // Set to interface IP
//	private static final String DEFAULT_CONTAINER_ID_CLIENT = "client1";
//	
//	// Use for a client connection
//	public static String containerTypeClient = DEFAULT_CONTAINER_TYPE_CLIENT;
//	public static String containerIDClient = DEFAULT_CONTAINER_ID_CLIENT;
//	public static String targetID = DEFAULT_CONTAINER_ID_SERVER;
//	// Use for a server connection, uncomment previous
//	public static String containerTypeServer = DEFAULT_CONTAINER_TYPE_SERVER;
//	public static String containerIDServer = DEFAULT_CONTAINER_ID_SERVER;
//
//    private IStructuredSelection _selection;
//    private Composite _container;
//    private Combo _projectCombo;
//    protected boolean _projectComboFixed = false;
//    private Button _createButton;
//    private Text _ontologyText;
//    private Text _namespaceText;
//    protected IInputValidator _uriValidator = getInputValidator();
//    private String _ontologyURI;
//    private String _projectName;
//    private boolean _initDone;
//
//    public NewReplicaOntologyWizardPage(IStructuredSelection selection) {
//        super("NewOntologyWizardPage"); //$NON-NLS-1$
//        setTitle(Messages.NewOntologyWizardPage_1); 
//        setDescription(Messages.NewOntologyWizardPage_2); 
//        _selection = selection;
//        
//        // Start a server
//        if(Integer.parseInt(System.getenv("INSTANCE").toString()) == 0) {
//        	Activator.getDefault().logInfo("Server started");
//        	startServer();
//        }
//    }
//
//    protected IInputValidator getInputValidator() {
//        return URIUtils.getOntologyUriValidator();
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
//     */
//    @Override
//    public void createControl(Composite parent) {
//        _container = new Composite(parent, SWT.NULL);
//        GridLayout layout = new GridLayout(3, false);
//        _container.setLayout(layout);
//        layout.verticalSpacing = 9;
//        Label label = new Label(_container, SWT.NULL);
//        label.setText(Messages.NewOntologyWizardPage_3); 
//
//        _ontologyText = new Text(_container, SWT.BORDER | SWT.SINGLE);
//        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//        _ontologyText.setLayoutData(gd);
//        _ontologyText.addModifyListener(new ModifyListener() {
//            @Override
//            public void modifyText(ModifyEvent e) {
//                if(_initDone)
//                    updateStatus();
//            }
//        });
//
//        gd = new GridData(GridData.FILL_HORIZONTAL);
//        Label dummy1 = new Label(_container, SWT.NONE);
//        dummy1.setVisible(false);
//        dummy1.setLayoutData(gd);
//        
//        label = new Label(_container, SWT.NONE);
//        label.setText(Messages.NewOntologyWizardPage_4); 
//
//        _namespaceText = new Text(_container, SWT.BORDER | SWT.SINGLE);
//        gd = new GridData(GridData.FILL_HORIZONTAL);
//        gd.grabExcessHorizontalSpace = true;
//        _namespaceText.setLayoutData(gd);
//        _namespaceText.addModifyListener(new ModifyListener() {
//            @Override
//            public void modifyText(ModifyEvent e) {
//                if(_initDone)
//                    updateStatus();
//            }
//        });
//
//        gd = new GridData(GridData.FILL_HORIZONTAL);
//        Label dummy2 = new Label(_container, SWT.NONE);
//        dummy2.setVisible(false);
//        dummy2.setLayoutData(gd);
//
//        label = new Label(_container, SWT.NONE);
//        label.setText(Messages.NewOntologyWizardPage_5); 
//
//        _projectCombo = new Combo(_container, SWT.BORDER | SWT.READ_ONLY);
//        gd = new GridData(GridData.FILL_HORIZONTAL);
//        gd.grabExcessHorizontalSpace = true;
//        _projectCombo.setLayoutData(gd);
//        _projectCombo.addSelectionListener(new SelectionListener() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//                if(_initDone)
//                    updateStatus();
//            }
//            @Override
//            public void widgetDefaultSelected(SelectionEvent e) {
//            }
//        });
//
//        _createButton = new Button(_container, SWT.PUSH);
//        _createButton.setText(Messages.NewOntologyWizardPage_11); 
//        _createButton.addSelectionListener(new SelectionListener() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//                NewReplicaProjectWizard wizard =
//                	new NewReplicaProjectWizard();
//                wizard.init(PlatformUI.getWorkbench(), null);
//        		Shell parent = getShell();
//        		WizardDialog dialog = new WizardDialog(parent, wizard);
//        		dialog.create();
////        		WorkbenchHelp.setHelp(dialog.getShell(), org.eclipse.ui.internal.IHelpContextIds.NEW_WIZARD_SHORTCUT);
//        		dialog.open();
//        		initialize();
//            }
//            @Override
//            public void widgetDefaultSelected(SelectionEvent e) {
//            }
//            
//        });
//        _createButton.setVisible(false);
//        gd = new GridData(GridData.GRAB_HORIZONTAL);
//        _createButton.setLayoutData(gd);
//                
//        GridData gd3 = new GridData(GridData.FILL_HORIZONTAL);
//        Label dummy3 = new Label(_container, SWT.NONE);
//        dummy3.setVisible(false);
//        dummy3.setLayoutData(gd3);
//
//        
//        initialize();
//        updateStatus();
//        setControl(_container);
//    }
//
//    private void initialize() {
//        //initialize project combo
//        String[] projects;
//        try {
//            projects = OntologyProjectManager.getDefault().getOntologyProjects();
//            _projectCombo.setItems(projects);
//            if (projects.length == 0) {
//                _createButton.setVisible(true);
//            } else if (projects.length == 1) {
//                _projectCombo.select(_projectCombo.indexOf(projects[0]));
//            }
//            //set the initial values for the id and namespace
//            if(_projectComboFixed){
//                _ontologyText.setText(_ontologyURI);
//                _ontologyText.setEnabled(false);
//                _namespaceText.setText(_ontologyURI+"#"); //$NON-NLS-1$
//                //set the current selected item
//                if(_projectName != null && !_projectName.equals("")) //$NON-NLS-1$
//                    _projectCombo.select(_projectCombo.indexOf(_projectName));
//            }else{
//                //set the current selected item
//                if (_selection != null) {
//                    Object selection = _selection.getFirstElement();
//                    if (selection instanceof IProjectElement) {
//                        _projectCombo.select(_projectCombo.indexOf(((IProjectElement) selection).getProjectName()));
//                    }
//                }
//                String newId = new CreateUniqueOntologyUri(getProjectName()).getOntologyUri();
//                _ontologyText.setText(newId);
//                if (newId.equals("")) { //$NON-NLS-1$
//                    _namespaceText.setText(""); //$NON-NLS-1$
//                } else {
//                    _namespaceText.setText(newId+"#"); //$NON-NLS-1$
//                }
//            }
//            //set the current selected item
//            if(_projectName != null && !_projectName.equals("")) //$NON-NLS-1$
//                _projectCombo.select(_projectCombo.indexOf(_projectName));
//            _projectCombo.setEnabled(!_projectComboFixed);
//            _container.layout(true);
//        } catch (Exception e) {
//        	e.printStackTrace();
//            NeOnUIPlugin.getDefault().logError("", e); //$NON-NLS-1$
//        }
//        _initDone = true;
//    }
//
//    private void updateStatus() {
//        if (_projectCombo.getItemCount() <= 0) {
//            updateStatus(Messages.NewOntologyWizardPage_6); 
//            return;
//        } else {
//            try {
//            	String projectName = getProjectName();
//                if (NeOnCorePlugin.getDefault().getOntologyProject(projectName) != null) {
//                	String ontologyUri = _ontologyText.getText();
//                	String errorMsg = _uriValidator.isValid(ontologyUri);
//                	if (errorMsg != null) {
//                	    updateStatus(errorMsg);
//                	    return;
//                	}
//                	errorMsg = validateNamespace(_namespaceText.getText());
//                    if (errorMsg != null) {
//                        updateStatus(errorMsg);
//                        return;
//                    }
//                    if (new ExistsOntology(projectName, ontologyUri).exists()) {
//                        updateStatus(Messages.NewOntologyWizardPage_15); 
//                        return;
//                    }
//                }
//            } catch (CommandException e) {
//                NeOnUIPlugin.getDefault().logError("", e); //$NON-NLS-1$
//            } catch (NeOnCoreException e) {
//            	NeOnUIPlugin.getDefault().logError(Messages.NewOntologyWizardPage_14, e); 
//            }
//        }
//
//        if (_projectCombo.getSelectionIndex() == -1) {
//            updateStatus(Messages.NewOntologyWizardPage_7); 
//            return;
//        }
//        
//        String ontologyUri = _ontologyText.getText();
//        String namespace = _namespaceText.getText();
//
//        if ("".equals(ontologyUri)) { //$NON-NLS-1$
//            updateStatus(Messages.NewOntologyWizardPage_8); 
//            return;
//        }
//
//        if ("".equals(namespace)) { //$NON-NLS-1$
//            updateStatus(Messages.NewOntologyWizardPage_9); 
//            return;
//        }
// 
//        updateStatus(null);
//    }
//
//    protected void updateStatus(String message) {
//        setErrorMessage(message);
//        setPageComplete(message == null);
//    }
//    
//    private String validateNamespace(String input) {
//        return URIUtils.validateNamespace(input, _uriValidator);
//    }
//
//    public String getProjectName() {
//    	int index = _projectCombo.getSelectionIndex();
//    	if (index != -1) {
//            return _projectCombo.getItem(index);
//    	}
//    	return ""; //$NON-NLS-1$
//    }
//
//    public String getOntologyUri() {
//        return _ontologyText.getText();
//    }
//
//    public String getDefaultNamespace() {
//  		return _namespaceText.getText();
//    }
//    
//    @Override
//    public void performHelp() {
//        String helpContextId = org.neontoolkit.gui.IHelpContextIds.OWL_CREATE_ONTOLOGY;
//        PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpContextId);
//    }
//    /**
//     * @param projectName
//     */
//    public void setFixed(String projectName, String ontologyURI) {
//        _projectName = projectName;
//        _ontologyURI = ontologyURI;
//        _projectComboFixed = true;
//    }
//    
//    private void startServer() {
//		try {
//			CommManager mgr = new CommManagerImpl();
//			Connection connection = mgr.createConnection(createConnectionProperties(true));
//			connection.connect();
//			Activator.getDefault().logInfo("connection==null ?   "+
//					(connection!=null?false:true));
//			int instance = Integer.parseInt(System.getenv("INSTANCE").toString());
////			if(instance == 0) {
////				INST0_SERVER_STARTED = true;
////				INST1_SERVER_STARTED = false;
////			} else {
////				// server is started on instance 1
////				INST0_SERVER_STARTED = false;
////				INST1_SERVER_STARTED = true;
////			}
//			Activator.getDefault().logInfo("Server started on instance "+instance);
//		} catch (ContainerConnectException e) {
//			e.printStackTrace();
//		}
//    }
//    
//    
//}
