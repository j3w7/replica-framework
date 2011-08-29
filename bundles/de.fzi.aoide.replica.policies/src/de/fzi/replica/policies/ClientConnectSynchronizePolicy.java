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

package de.fzi.replica.policies;

import org.eclipse.ecf.core.IContainerListener;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerEvent;
import org.eclipse.ecf.core.sharedobject.events.ISharedObjectActivatedEvent;
import org.eclipse.ecf.core.sharedobject.events.SharedObjectActivatedEvent;

import de.fzi.replica.comm.Connection;

public class ClientConnectSynchronizePolicy extends AbstractPolicy {

	public ClientConnectSynchronizePolicy(Connection connection) {
		super(connection);
	}

	@Override
	void setPolicy(final Connection connection) {
		connection.getSharedObjectContainer().addListener(new IContainerListener() {
			@Override
			public void handleEvent(IContainerEvent event) {
				if(event instanceof ISharedObjectActivatedEvent) {
					System.out.println("shared object activated: "+event);
				} else if(event instanceof IContainerConnectedEvent) {
					System.out.println("target connected: "+
							((IContainerConnectedEvent) event).getTargetID());
					System.out.println("shared object IDs: "+
							connection.getSharedObjectContainer().
						getSharedObjectManager().getSharedObjectIDs());
				} else if(event instanceof SharedObjectActivatedEvent) {
					System.out.println("activated ID: "+
							((SharedObjectActivatedEvent) event).getActivatedID());
				}
			}
		});
	}

}
