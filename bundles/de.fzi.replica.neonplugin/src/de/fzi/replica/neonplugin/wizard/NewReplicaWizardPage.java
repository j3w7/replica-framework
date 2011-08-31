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
import org.neontoolkit.core.command.ontology.ExistsOntology;
import org.neontoolkit.core.exception.NeOnCoreException;
import org.neontoolkit.core.project.OntologyProjectManager;
import org.neontoolkit.gui.NeOnUIPlugin;
import org.neontoolkit.gui.navigator.elements.IProjectElement;
import org.neontoolkit.gui.util.URIUtils;

import com.ontoprise.ontostudio.owl.gui.Messages;

import de.fzi.replica.neonplugin.commands.CreateUniqueReplicaUri;


/* 
 * Created on: 12.11.2004
 * Created by: Dirk Wenke
 * 
 * Modified on 03.02.2011
 * Modified by Jan Novacek
 *
 * Function: UI, Ontology, Wizard
 */
/**
 * This class provides the wizard page that is displayed in the NewOntologyWizard.
 */
public class NewReplicaWizardPage extends WizardPage {
	
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

    private IStructuredSelection _selection;
    private Composite _container;
    private Combo _projectCombo;
    private Button _createButton;
    private Text _ontologyUriText;
    private Text _namespaceText;
    // Replica specific settings
    private Text _containerID;
    private Text _containerType;
    private Button _isClientCheckbox;
    
    protected IInputValidator _uriValidator = getInputValidator();

    public NewReplicaWizardPage(IStructuredSelection selection) {
        super("NewReplicaOntologyWizardPage"); //$NON-NLS-1$
//        setTitle(Messages.NewOntologyWizardPage_1); 
//        setDescription(Messages.NewOntologyWizardPage_2);
        setTitle("Create New ReplicaOntology"); 
      setDescription("Creates a new empty ReplicaOntology");
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
    public void createControl(Composite parent) {
        _container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(3, false);
        _container.setLayout(layout);
        layout.verticalSpacing = 9;
        Label label = new Label(_container, SWT.NULL);
        label.setText(Messages.NewOntologyWizardPage_3); 

        _ontologyUriText = new Text(_container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        _ontologyUriText.setLayoutData(gd);
        _ontologyUriText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
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

            public void modifyText(ModifyEvent e) {
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

            public void widgetSelected(SelectionEvent e) {
                updateStatus();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
//        gd = new GridData(GridData.FILL_HORIZONTAL);
//        Label dummy5 = new Label(_container, SWT.NONE);
//        dummy5.setVisible(false); 
//        dummy5.setLayoutData(gd);

        _createButton = new Button(_container, SWT.PUSH);
        _createButton.setText(Messages.NewOntologyWizardPage_11); 
        _createButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                NewReplicaProjectWizard wizard = new NewReplicaProjectWizard();
                wizard.init(PlatformUI.getWorkbench(), null);
        		Shell parent = getShell();
        		WizardDialog dialog = new WizardDialog(parent, wizard);
        		dialog.create();
//        		WorkbenchHelp.setHelp(dialog.getShell(), org.eclipse.ui.internal.IHelpContextIds.NEW_WIZARD_SHORTCUT);
        		dialog.open();
        		initialize();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
            
        });
        _createButton.setVisible(false);
        gd = new GridData(GridData.GRAB_HORIZONTAL);
        _createButton.setLayoutData(gd);
                
//        GridData gd3 = new GridData(GridData.FILL_HORIZONTAL);
//        Label dummy3 = new Label(_container, SWT.NONE);
//        dummy3.setVisible(false);
//        dummy3.setLayoutData(gd3);
        
        /*
         * ReplicaOntology specific stuff
         */
        
        label = new Label(_container, SWT.NULL);
        label.setText("Replica server ID");
        
        _containerID = new Text(_container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.grabExcessHorizontalSpace = true;
        _containerID.setLayoutData(gd);
//        _containerID.addModifyListener(new ModifyListener() {
//        	@Override
//            public void modifyText(ModifyEvent e) {
//                updateStatus();
//            }
//        });
        _containerID.setText(targetID);
        
        
        gd = new GridData(GridData.FILL_HORIZONTAL);
        Label dummy3 = new Label(_container, SWT.NONE);
        dummy3.setVisible(false);
        dummy3.setLayoutData(gd);
        
        
        label = new Label(_container, SWT.NULL);
        label.setText("Container type");
        
        _containerType = new Text(_container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.grabExcessHorizontalSpace = true;
        _containerType.setLayoutData(gd);
//        _containerID.addModifyListener(new ModifyListener() {
//        	@Override
//            public void modifyText(ModifyEvent e) {
//                updateStatus();
//            }
//        });
        _containerType.setText(containerTypeClient);
        
        
        gd = new GridData(GridData.FILL_HORIZONTAL);
        Label dummy4 = new Label(_container, SWT.NONE);
        dummy4.setVisible(false);
        dummy4.setLayoutData(gd);
        
        _isClientCheckbox = new Button(_container, SWT.CHECK);
        _isClientCheckbox.setText("Start local server");
        
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

            //set the current selected item
            if (_selection != null) {
                Object selection = _selection.getFirstElement();
                if (selection instanceof IProjectElement) {
                    _projectCombo.select(_projectCombo.indexOf(((IProjectElement) selection).getProjectName()));
                }
            }
            //set the initial values for the id and namespace
            //String newId = new CreateUniqueOntologyUri(getProjectName()).getOntologyUri();
            String newId = new CreateUniqueReplicaUri(getProjectName()).getOntologyUri();
            _ontologyUriText.setText(newId);
            if (newId.equals("")) { //$NON-NLS-1$
                _namespaceText.setText(""); //$NON-NLS-1$
            } else {
            	_namespaceText.setText(newId+"#"); //$NON-NLS-1$
            }
            _container.layout(true);
        } catch (Exception e) {
        	e.printStackTrace();
            NeOnUIPlugin.getDefault().logError("", e); //$NON-NLS-1$
        }
    }

    private void updateStatus() {
        if (_projectCombo.getItemCount() <= 0) {
            updateStatus(Messages.NewOntologyWizardPage_6); 
            return;
        } else {
            try {
            	String projectName = getProjectName();
                if (NeOnCorePlugin.getDefault().getOntologyProject(projectName) != null) {
                	String ontologyUri = _ontologyUriText.getText();
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
        
        String ontologyUri = _ontologyUriText.getText();
        String namespace = _namespaceText.getText();

        if ("".equals(ontologyUri)) { //$NON-NLS-1$
            updateStatus(Messages.NewOntologyWizardPage_8); 
            return;
        }

        if ("".equals(namespace)) { //$NON-NLS-1$
            updateStatus(Messages.NewOntologyWizardPage_9); 
            return;
        }
 
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
        return _ontologyUriText.getText();
    }

    public String getDefaultNamespace() {
  		return _namespaceText.getText();
    }
    
    @Override
    public void performHelp() {
        String helpContextId = org.neontoolkit.gui.IHelpContextIds.OWL_CREATE_ONTOLOGY;
        PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpContextId);
    }
    
    private Properties createConnectionProperties() {
    	Properties connectionProperties = new Properties();
    	if(true) {
//    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeClient);
//    		connectionProperties.put(CONFIG_KEYWORD_TARGET_ID, targetID);
//    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, containerIDClient);    		
    	} else {
//    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_TYPE, containerTypeServer);
//    		connectionProperties.put(CONFIG_KEYWORD_CONTAINER_ID, containerIDServer);    		
    	}
    	return connectionProperties;
    }
}