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

/**
 * Will be used for tracing and implementing access restrictions.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.0, 11.09.2010
 *
 */
public aspect OWLReplicaOntologyProxy {
	
//	before(OWLReplicaOntologyImpl sharedOnto): execution(@ProxyMethod * *(..)) && target(sharedOnto) {
////		System.out.println(thisJoinPoint.getSignature().getName()+"() invoked"+
////				" on class "+thisJoinPoint.getSignature().getDeclaringTypeName());
//	}
	
}
