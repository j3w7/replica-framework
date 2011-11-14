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

import java.util.Set;

import org.eclipse.ecf.remoteservice.IAsyncCallback;
import org.eclipse.ecf.remoteservice.IAsyncRemoteServiceProxy;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import de.fzi.replica.app.client.Client.AddException;
import de.fzi.replica.app.client.Client.FetchException;

/**
 * Replica Repository interface using ECF asynchronous remote services.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 05.10.2011
 *
 */
public interface ReplicaRepositoryServiceAsync extends IAsyncRemoteServiceProxy {
	
	/**
	 * Add the specified ontology to the pool of shared ontologies. The ontology
	 * will belong to no group. Use <code>addOWLOntology(OWLOntology, ID)</code>
	 * if you want to set the group to which the ontology belongs.
	 * 
	 * @param ontology
	 * @return
	 * @throws ReplicaOntologyAddException
	 * 
	 * @deprecated use addOWLOntology(OWLOntology, ID) instead
	 */
	// OWLOntology addOWLOntology(OWLOntology ontology) throws
	// ReplicaOntologyAddException;

	/**
	 * Add the specified ontology to the pool of shared ontologies in
	 * <code>groups</code>. If <code>groups</code> is null, then the ontology will
	 * belong to no group. If <code>groups</code> does not exist yet, it will be
	 * created.
	 * 
	 * @param ontology
	 * @param groups
	 * @param listener
	 * @throws AddException
	 */
	void addOWLOntologyAsync(OWLOntology ontology, Set<? extends Object> groups,
			IAsyncCallback<String> callback);
//			throws AddException;

	/**
	 * Fetches the ontology with the specified OWLOntologyID.
	 * 
	 * @param ontologyID
	 * @param listener
	 * @throws FetchException
	 */
	void getOWLOntologyAsync(OWLOntologyID ontologyID,
			IAsyncCallback<OWLOntology> callback);
//			throws FetchException;

	/**
	 * Returns IDs of all shared ontologies or an empty list if none found.
	 * 
	 * @deprecated use getOWLOntologies(String group) instead
	 */
	// List<OWLOntologyID> getOWLOntologies() throws
	// ReplicaOntologyFetchException;

	/**
	 * Returns IDs of all shared ontologies which belong to the specified groups.
	 * <br>
	 * If <code>groups</code> is null, IDs of all shared ontologies will be
	 * returned.<br>
	 * Beware that the specified set implementation has to be serializable.
	 * 
	 * @param groups
	 * @param listener
	 */
	void getOWLOntologyIDsAsync(Set<Object> groups,
			IAsyncCallback<String> callback);
//			throws FetchException;

	/**
	 * Returns a set of all group names currently registered.
	 * 
	 * @param listener
	 */
	void getGroupsAsync(IAsyncCallback<Set<Object>> callback);
//			throws FetchException;
	
}
