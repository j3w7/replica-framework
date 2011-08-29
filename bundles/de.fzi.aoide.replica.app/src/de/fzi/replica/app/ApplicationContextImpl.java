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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Jan Novacek novacek@fzi.de
 * @version 0.2, 21.07.2011
 */
public class ApplicationContextImpl implements ApplicationContext {

	private Map<Class<?>, Object> services;

	private Properties config;

	private Map<Enum<?>, Set<Enum<?>>> stateMap;

	private Enum<?> currentState;

	private List<ApplicationStateChangeListener> stateListeners;

	public ApplicationContextImpl() {
		services = new HashMap<Class<?>, Object>();
		stateListeners = new ArrayList<ApplicationStateChangeListener>();
		stateMap = new HashMap<Enum<?>, Set<Enum<?>>>();
	}

	@Override
	public void setConfiguration(Properties config) {
		this.config = config;
	}

	@Override
	public Properties getConfiguration() {
		return config;
	}

	@Override
	public void addService(Object srv) {
		services.put(srv.getClass(), srv);
	}

	@Override
	public Object getService(Class<?> service) {
		return services.get(service);
	}

	@Override
	public void setApplicationStates(
			Map<Enum<?>, Set<Enum<?>>> stateMap,
			Enum<?> initialState) {
		this.stateMap = stateMap;
		currentState = initialState;
	}

	@Override
	public Enum<?> setState(final Enum<?> newState) {
		if (!isReachable(newState)) {
			throw new IllegalArgumentException(
					"Unreachable connection activity state!");
		}
		final Enum<?> previousState = currentState;
		currentState = newState;
		final ApplicationStateChangeEvent changeEvent = new ApplicationStateChangeEvent() {
			@Override
			public Enum<?> getNewState() {
				return newState;
			}
			@Override
			public Enum<?> getPreviousState() {
				return previousState;
			}
		};
		for (final ApplicationStateChangeListener listener : stateListeners) {
			listener.onApplicationStateChanged(changeEvent);
		}
		return previousState;
	}

	@Override
	public Enum<?> getState() {
		return currentState;
	}

	@Override
	public void registerApplicationStateChangeListener(
			ApplicationStateChangeListener listener) {
		stateListeners.add(listener);
	}

	@Override
	public void unregisterApplicationStateChangeListener(
			ApplicationStateChangeListener listener) {
		stateListeners.remove(listener);
	}

	private boolean isReachable(Enum<?> newState) {
		return stateMap.get(currentState).contains(newState);
	}

}
