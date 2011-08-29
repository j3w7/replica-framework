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

import org.neontoolkit.core.command.CommandException;
import org.neontoolkit.core.command.LoggedCommand;
import org.neontoolkit.core.project.IOntologyProject;
import org.neontoolkit.core.project.OntologyProjectManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.OWLReplicaOntologyFactoryImpl;
import de.fzi.replica.neonplugin.Activator;

/* 
 * Created on: 29.07.2005
 * Created by: Dirk Wenke
 * 
 * Modified on 03.02.2011
 * Modified by Jan Novacek
 *
 * Keywords: UI, Command
 */
/**
 * Command for creating an ontology development project.
 */
public class CreateReplicaProject extends LoggedCommand {

	public CreateReplicaProject(String projectName, String ontologyLanguageId,
			Properties configProperties) {
		this(projectName, ontologyLanguageId, propertiesToStringArray(configProperties));
	}
	
    public CreateReplicaProject(String projectName, String ontologyLanguageId,
    		String[][] configProperties) {
        super(projectName, ontologyLanguageId, configProperties);
    }

    @Override
	protected void checkArguments() throws IllegalArgumentException {
	    if (isNull(0) || isNull(1) || isNull(2)) {
	        throw new IllegalArgumentException(NULL_ARGUMENT_MESSAGE);
	    }
	}
    
	/*
	 * (non-Javadoc)
	 * @see org.neontoolkit.core.command.LoggedCommand#perform()
	 */
	@Override
	public void perform() throws CommandException {
		Activator.getDefault().logInfo("CreateReplicaOntologyProject.perform()");
	    String projectName = (String)getArgument(0);
	    String ontologyLanguageId = (String)getArgument(1);
	    Properties configProperties = stringArrayToProperties((String[][])getArgument(2));
		try {
		    IOntologyProject project = OntologyProjectManager.getDefault().
		    	createOntologyProject(projectName, ontologyLanguageId, configProperties);
		    Activator.getDefault().logInfo("project="+project);
		    // Add ReplicaOntologyFactory
		    OWLOntologyManager owlOntologyManager = project.getAdapter(OWLOntologyManager.class);
		    Activator.getDefault().logInfo("owlOntologyManager="+owlOntologyManager);
		    owlOntologyManager.addOntologyFactory(new OWLReplicaOntologyFactoryImpl());
		} catch (Exception ce) {
		    //Problem while creating datamodel
		    //remove project
		    new RemoveReplicaProject(projectName, true).perform();
			throw new CommandException(ce);
		}
	}
}
