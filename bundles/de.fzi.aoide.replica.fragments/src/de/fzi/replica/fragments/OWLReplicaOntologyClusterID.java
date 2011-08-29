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

package de.fzi.replica.fragments;

import java.io.Serializable;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 17.03.2011
 */
public class OWLReplicaOntologyClusterID implements Serializable {
	
	private static final long serialVersionUID = 5406215805352217579L;
	
	private IRI clusterIRI;
	
	public OWLReplicaOntologyClusterID(IRI clusterIRI) {
		this.setClusterIRI(clusterIRI);
	}

	public void setClusterIRI(IRI clusterIRI) {
		this.clusterIRI = clusterIRI;
	}

	public IRI getClusterIRI() {
		return clusterIRI;
	}

	@Override
	public String toString() {
		return "OWLReplicaOntologyClusterID [clusterIRI=" + clusterIRI + "]";
	}
	
}
