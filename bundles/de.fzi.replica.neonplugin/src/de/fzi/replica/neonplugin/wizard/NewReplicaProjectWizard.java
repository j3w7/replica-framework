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

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.neontoolkit.core.command.CommandException;
import org.neontoolkit.gui.NeOnUIPlugin;
import org.neontoolkit.gui.SharedImages;
import org.neontoolkit.gui.exception.NeonToolkitExceptionHandler;

import com.ontoprise.ontostudio.owl.gui.Messages;

import de.fzi.replica.comm.CommManager;
import de.fzi.replica.comm.CommManagerImpl;
import de.fzi.replica.comm.Connection;
import de.fzi.replica.neonplugin.Activator;
import de.fzi.replica.neonplugin.commands.CreateReplicaProject;
import de.fzi.replica.neonplugin.commands.ReplicaProjectFactory;

/* 
 * Created on: 06.11.2004
 * Created by: Dirk Wenke
 * 
 * Modified on 03.02.2011
 * Modified by Jan Novacek
 *
 * Function: UI, Project, Wizard
 */
/**
 * This class provides the wizard for the creation of new ontology projects.
 */
public class NewReplicaProjectWizard extends Wizard implements INewWizard {

    private NewReplicaProjectWizardPage _defaultPage;
    private ISelection _selection;

    /**
     * Constructor for SampleNewWizard.
     */
    public NewReplicaProjectWizard() {
        super();
        Activator.getDefault().logInfo("NewReplicaProjectWizard constructor");
        setDefaultPageImageDescriptor(NeOnUIPlugin.getDefault().getImageRegistry().getDescriptor(SharedImages.NEW_ONTOLOGY_PROJECT_WIZ));
        setWindowTitle(Messages.NewOntologyProjectWizard_1); 
        setNeedsProgressMonitor(true);
    }

	/**
     * Adding the page to the wizard.
     */
    @Override
    public void addPages() {
        _defaultPage = new NewReplicaProjectWizardPage(_selection);
        addPage(_defaultPage);
    }
    
    /**
     * This method is called when 'Finish' button is pressed in the wizard. We
     * will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {
        final String projectName = _defaultPage.getProjectName();
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(projectName, monitor);
                } catch (CommandException e) {
                    throw new InvocationTargetException(e, e.getMessage());
                } catch (RuntimeException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            new NeonToolkitExceptionHandler().handleException(realException.getMessage(), realException, getShell());
            return false;
        }
        return true;
    }
    
	/**
     * The worker method. It will find the container, create the file if missing
     * or just replace its contents, and open the editor on the newly created
     * file.
     * @param projectProperties 
     */

    private void doFinish(String projectName, IProgressMonitor monitor) throws CommandException {

        monitor.beginTask(Messages.NewOntologyProjectWizard_2 + projectName + "\".", 1); //$NON-NLS-1$ 

        new CreateReplicaProject(projectName, ReplicaProjectFactory.FACTORY_ID, new Properties()).run();

        monitor.worked(1);
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize
     * from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this._selection = selection;
    }

	public NewReplicaProjectWizardPage getLastPage() {
		return _defaultPage;
	}
	
}
