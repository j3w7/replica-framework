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

package de.fzi.replica.app.client;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import de.fzi.replica.app.Application;

/**
 * This is the main interface for a client instance.
 * <br>
 * The methods of this interface make extensive use of listeners which is
 * required by the asynchronous messaging concept client implementations rely
 * on.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version version 0.3, 22.07.2011
 * 
 */
public interface Client extends Application {

	/**
	 * Connect to the server specified in the configuration. If this client
	 * instance has not been started yet it will be started when calling this
	 * method.
	 * 
	 * @throws ConnectException
	 */
	void connect() throws ConnectException;

	/**
	 * Disconnect from the server.
	 */
	void disconnect();

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
	 * <code>group</code>. If <code>group</code> is null, then the ontology will
	 * belong to no group. If <code>group</code> does not exist yet, it will be
	 * created.
	 * 
	 * @param ontology
	 * @param group
	 * @param listener
	 * @throws AddException
	 */
	void addOWLOntology(OWLOntology ontology, String group, OnOntologyAddedListener listener)
			throws AddException;

	/**
	 * Fetches the ontology with the specified OWLOntologyID.
	 * 
	 * @param ontologyID
	 * @param listener
	 * @throws FetchException
	 */
	void getOWLOntology(OWLOntologyID ontologyID,
			OnOntologyReceivedListener listener)
			throws FetchException;

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
	void getOWLOntologyIDs(Set<Object> groups, OnOntologyIDsReceivedListener listener)
			throws FetchException;

	/**
	 * Returns a list of all group names currently registered.
	 * 
	 * @param listener
	 */
	void getGroups(OnGroupsReceivedListener listener) throws FetchException;

	/*
	 * Listeners
	 */
	
	interface AsyncMethodCallback {
		
		public enum Result {
			OK,
			NULL,
			EXCEPTION
		}
		
	}
	
	interface OnOntologyAddedListener extends AsyncMethodCallback {
		
		void onOntologyAdded(Result result);
		
	}

	interface OnOntologyReceivedListener extends AsyncMethodCallback {

		void onOntologyReceived(Result result, OWLOntology onto);

	}

	interface OnOntologyIDsReceivedListener extends AsyncMethodCallback {

		void onOntologyIDsGot(Result result, Map<Object, Set<OWLOntologyID>> ids);

	}

	interface OnGroupsReceivedListener extends AsyncMethodCallback {

		void onGroupsGot(Result result, Set<String> groups);

	}

	/*
	 * Exceptions
	 */

	class FetchException extends Exception {
		private static final long serialVersionUID = -2590780077175974375L;

		public FetchException(String string, Exception e) {
			super(string, e);
		}
	}

	class AddException extends Exception {
		private static final long serialVersionUID = -92865417812721874L;
		
		public AddException(String string, Exception e) {
			super(string, e);
		}
	}

	class ConnectException extends Exception {
		private static final long serialVersionUID = 3963872181898792177L;

		public ConnectException(String msg, Exception e) {
			super(msg, e);
		}
	}

}
