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

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method is a proxy method.<br>
 * Proxy methods delegate their invocation to a method of another object,
 * eventually modifying the parameters.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.0, 24.08.2010
 */
@Target(ElementType.METHOD)
public @interface ProxyMethod {

}
