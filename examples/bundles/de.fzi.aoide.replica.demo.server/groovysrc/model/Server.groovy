package model

import org.eclipse.ecf.core.events.IContainerConnectedEvent
import org.eclipse.ecf.core.events.IContainerEvent

import org.eclipse.ecf.core.IContainerListener

import de.fzi.replica.comm.CommManager
import de.fzi.replica.comm.CommManagerImpl
import de.fzi.replica.comm.Connection

class Server {
	CommManager manager

	Server() {
		manager = new CommManagerImpl()
	}

	def start(cType, contId) {
		println "Starting server of type "+cType+" with ID "+contId
		Properties p = new Properties()
		p.put "containerType", cType
		p.put "containerID", contId
		Connection c = manager.createConnection(p)
		c.initialize()
		c.connect()
	}
}
