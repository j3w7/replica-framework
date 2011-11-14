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

package de.fzi.replica.app.server.rs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.remoteservice.IAsyncCallback;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Replica repository implementation.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 05.10.2011
 *
 */
public class ReplicaRepositoryServiceAsyncImpl
		implements ReplicaRepositoryServiceAsync {
	
//	private List<ID> clientIDs;
	private Map<Object, Set<ID>> groups;
	// TODO use bidimap
	private Map<OWLOntologyID, ID> ontoSoid;
	private Map<ID, OWLOntologyID> soidOnto;
	
	public ReplicaRepositoryServiceAsyncImpl() {
		groups = new HashMap<Object, Set<ID>>();
		ontoSoid = new HashMap<OWLOntologyID, ID>();
		soidOnto = new HashMap<ID, OWLOntologyID>();
		
		groups.put("devgroup0", Collections.singleton(
				IDFactory.getDefault().createStringID("onto0")));
		groups.put("devgroup1", Collections.singleton(
				IDFactory.getDefault().createStringID("onto1")));
		groups.put("devgroup2", Collections.singleton(
				IDFactory.getDefault().createStringID("onto2")));
	}
	
	@Override
	public void addOWLOntologyAsync(OWLOntology ontology,
			Set<? extends Object> groups, IAsyncCallback<String> callback) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getOWLOntologyAsync(OWLOntologyID ontologyID,
			IAsyncCallback<OWLOntology> callback) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getOWLOntologyIDsAsync(Set<Object> groups,
			IAsyncCallback<String> callback) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getGroupsAsync(IAsyncCallback<Set<Object>> callback) {
		// TODO Auto-generated method stub
		callback.onSuccess(groups.keySet());
	}
	
}
