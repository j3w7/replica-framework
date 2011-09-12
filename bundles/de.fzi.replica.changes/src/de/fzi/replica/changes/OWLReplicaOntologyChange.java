/*
   Copyright 2011 Jan Novacek

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package de.fzi.replica.changes;

import java.util.List;

import org.eclipse.ecf.core.identity.ID;
import org.semanticweb.owlapi.model.OWLOntologyChange;

/**
 * Beware: an OWLReplicaOntologyChange can consist of many OWLOntologyChanges.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 09.02.2011
 *
 */
public interface OWLReplicaOntologyChange {
	
	List<OWLOntologyChange> getChanges();
	
	ID getModifierID();
	
	long getModificationTimestamp();
	
	String getModificationComment();
	
	//public OWLReplicaOntology getOWLReplicaOntology();
	
}