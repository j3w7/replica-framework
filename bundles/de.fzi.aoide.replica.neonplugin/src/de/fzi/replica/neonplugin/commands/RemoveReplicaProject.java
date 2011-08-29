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

import org.neontoolkit.core.NeOnCorePlugin;
import org.neontoolkit.core.command.CommandException;
import org.neontoolkit.core.command.LoggedCommand;
import org.neontoolkit.core.exception.NeOnCoreException;
import org.neontoolkit.core.project.IOntologyProject;

/*
 * Created on 13.06.2008
 * Created by Dirk Wenke
 * 
 * Modified on 03.02.2011
 * Modified by Jan Novacek
 *
 * Function: 
 * Keywords: 
 */
public class RemoveReplicaProject extends LoggedCommand {
	
	public RemoveReplicaProject(String project, boolean removeFromDatamodel) {
		super(project, removeFromDatamodel);
	}

	/* (non-Javadoc)
	 * @see org.neontoolkit.core.command.LoggedCommand#perform()
	 */
	@Override
	protected void perform() throws CommandException {
	    try {
    		String projectName = (String)getArgument(0);
    		boolean removeFromDatamodel = (Boolean)getArgument(1);
    		IOntologyProject project = NeOnCorePlugin.getDefault().getOntologyProject(projectName);
    		if (project != null) {
    		    project.dispose(removeFromDatamodel);
    		}
	    } catch (NeOnCoreException e) {
	        throw new CommandException(e);
	    }
	}
}
