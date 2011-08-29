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

package de.fzi.replica;

import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.semanticweb.owlapi.model.OWLMutableOntology;

/**
 * 
 * The OWLReplicaOntology interface combines functionalities of OWLMutableOntology
 * and IShared object to bridge between the OWLAPI ontology representation model
 * and the ECF shared object.<br>
 * <br>
 * As in the OWLAPI clients should not use this interface directly but OWLOntology
 * instead to avoid problems when applying ontology changes. Using the
 * <code>applyChange</code> and <code>applyChanges<code> methods will bypass
 * change listener notification registered in the ontology's
 * <code>OWLOntologyManager</code>.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 22.07.2010
 * 
 */
public interface OWLReplicaOntology extends OWLMutableOntology, ISharedObject {

	/*
	 * TODO Add applyChanges methods which return applied changes lists that are
	 * filled asynchronously.
	 */

}
