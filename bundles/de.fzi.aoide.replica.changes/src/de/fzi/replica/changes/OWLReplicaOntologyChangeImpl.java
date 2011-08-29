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
import java.util.List;

import org.eclipse.ecf.core.identity.ID;
import org.semanticweb.owlapi.model.OWLOntologyChange;

/**
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 03.02.2011
 *
 */
public class OWLReplicaOntologyChangeImpl
		implements OWLReplicaOntologyChange, Serializable {
	
	private static final long serialVersionUID = 5320217377780174617L;
	
	private List<OWLOntologyChange> changes;
	private ID modifierId;
	private long timestamp;
	private String comment;

	protected OWLReplicaOntologyChangeImpl(List<OWLOntologyChange> changes,
			ID modifierId, long timestamp, String comment) {
		super();
		this.changes = changes;
		this.modifierId = modifierId;
		this.timestamp = timestamp;
		this.comment = comment;
	}

	@Override
	public List<OWLOntologyChange> getChanges() {
		return changes;
	}

	@Override
	public ID getModifierID() {
		return modifierId;
	}

	@Override
	public long getModificationTimestamp() {
		return timestamp;
	}

	@Override
	public String getModificationComment() {
		return comment;
	}

	@Override
	public String toString() {
		return "OWLReplicaOntologyChangeImpl [changes=" + changes
				+ ", modifierId=" + modifierId + ", timestamp=" + timestamp
				+ ", comment=" + comment + "]";
	}

}
