package de.fzi.aoide.replica.demo;

public interface IReplicaServer {
	
	String getClientName(String host);
	
	String getClientContainerType();
	
	String getTargetID();
	
}
