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

package de.fzi.replica.util;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version version, 31.08.2010
 *
 * @param <Source>
 * @param <Target>
 */
public abstract class OWLOntologyCopier<Source extends OWLOntology, Target extends OWLOntology> {
	
	// one day there may be filters and stuff here
	
	public abstract void copy(Set<Source> sources, Target target);
	
}
