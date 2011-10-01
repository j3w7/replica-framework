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

package de.fzi.replica.app;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Interface to global information about an application environment.
 * Also provides means for managing the state of an application for
 * implementing a state machine.
 * Depending on the situation an the complexity of the application,
 * using a global state can be an anti-pattern. It does not fit in
 * all situations.<br>
 * The framework also provides this mechanism at the connection level,
 * so it is also possible to implement a state machine which is more
 * connection-centric; have a look at
 * {@link de.fzi.aoide.replica.comm.Connection Connection}.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.2, 20.07.2011
 */
public interface ApplicationContext {
	
	void setConfiguration(Properties config);
	
	Properties getConfiguration();
	
	void addService(Object srv);
	
	Object getService(Class<?> service);
	
//	void setApplicationStates(
//			Map<ApplicationState, Set<ApplicationState>> stateMap,
//			ApplicationState initialState);
//	
//	ApplicationState setState(ApplicationState newState);
//	
//	ApplicationState getState();
	
	void setApplicationStates(
			Map<Enum<?>, Set<Enum<?>>> stateMap,
			Enum<?> initialState);
	
	Enum<?> setState(Enum<?> newState);
	
	Enum<?> getState();
	
	void registerApplicationStateChangeListener(ApplicationStateChangeListener listener);
	
	void unregisterApplicationStateChangeListener(ApplicationStateChangeListener listener);
	
	/**
	 * Can be used for implementing state machines. Beware that
	 * using a global state is also seen as anti-pattern by many people
	 * for practical reasons. 
	 */
//	interface ApplicationState {
//		
//		Object getID();	// TODO consider using Enums for ApplicationStates
//		
//	}
	
//	interface ApplicationStateChangeEvent {
//		
//		ApplicationState getNewState();
//		
//		ApplicationState getPreviousState();
//		
//	}
	
	interface ApplicationStateChangeEvent {
		
		Enum<?> getNewState();
		
		Enum<?> getPreviousState();
		
	}
	
	interface ApplicationStateChangeListener {
		
		void onApplicationStateChanged(ApplicationStateChangeEvent event);
		
	}

}
