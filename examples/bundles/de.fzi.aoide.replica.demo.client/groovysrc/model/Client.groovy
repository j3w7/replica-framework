package model

import org.osgi.framework.Filter
import org.osgi.framework.InvalidSyntaxException

import de.fzi.aoide.replica.comm.CommManager
import de.fzi.aoide.replica.comm.CommManagerImpl
import de.fzi.aoide.replica.comm.Connection

class Client {
	String name
	CommManager manager
	
	Client(String name) {
		this.name = name
		manager = new CommManagerImpl()
	}
	
	def connect(cType, target) {
		println "Connecting to "+target+", container type "+cType
		Properties p = new Properties()
		p.put "containerType", cType
		p.put "containerID", target
		Connection c = manager.createConnection(p)
		c.initialize()
		c.connect()
	}
}
