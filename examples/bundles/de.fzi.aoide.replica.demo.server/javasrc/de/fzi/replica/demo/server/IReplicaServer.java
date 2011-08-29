package de.fzi.replica.demo.server;

public interface IReplicaServer {
	
	String getClientName(String host);
	
	String getClientContainerType();
	
	String getTargetID();
	
}
