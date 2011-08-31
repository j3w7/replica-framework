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

import java.io.Serializable;
import java.util.Arrays;

import org.eclipse.ecf.core.identity.ID;

import de.fzi.replica.OWLReplicaOntology;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 03.02.2011
 *
 */
public class OWLReplicaOntologyChangePropagationImpl implements
		OWLReplicaOntologyChangePropagation, Serializable {
	
	private static final long serialVersionUID = -7831081370028883047L;
	
	protected String name;
	protected Object[] args;
	protected ID[] targets;
	protected OWLReplicaOntology ontology;
	
	protected OWLReplicaOntologyChangePropagationImpl(
			String name,
			Object[] args,
			ID[] targets,
			OWLReplicaOntology ontology) {
		this.name = name;
		this.args = args;
		this.targets = targets;
		this.ontology = ontology;
	}

	@Override
	public Object[] getArgs() {
		return args;
	}

	@Override
	public ID[] getTargets() {
		return targets;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public OWLReplicaOntology getOWLReplicaOntology() {
		return ontology;
	}

	@Override
	public String toString() {
		return "OWLReplicaOntologyChangePropagationImpl [name=" + name
				+ ", args=" + Arrays.toString(args) + ", targets="
				+ Arrays.toString(targets) + ", ontology.getOntologyID()=" + ontology.getOntologyID() + "]";
	}

}
