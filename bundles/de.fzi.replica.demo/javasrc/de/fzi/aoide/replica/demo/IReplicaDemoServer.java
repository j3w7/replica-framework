package de.fzi.aoide.replica.demo;

/**
 * This is the basic interface for the Replica Framework Demo Server instance.
 * As the framework's capabilities are used when establishing a connection,
 * the demo server interface is only used to transmit IP address and port of an
 * actual framework server instance.
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 04.07.2011
 */
public interface IReplicaDemoServer {
	
	String getServerIP();
	
	String getServerPort();
	
}
