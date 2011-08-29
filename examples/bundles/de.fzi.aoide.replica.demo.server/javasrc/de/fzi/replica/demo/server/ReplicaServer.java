package de.fzi.replica.demo.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

/**
 * @deprecated Server implementations in script languages for rapid prototyping.
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.1, 01.07.2011
 */
public class ReplicaServer implements IReplicaServer {
	
	public static final String CLIENT_CONTAINER_TYPE = "ecf.generic.client";
	public static final String DEFAULT_SERVER_PORT = "5888";
	
	private List<String> clientNames;
	private String serverHost;
	private String serverPort;
	private String targetId = "ecftcp://"+serverHost+":"+serverPort+"/server";
	
	public ReplicaServer() {
		clientNames = new LinkedList<String>();
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			serverHost = localHost.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			serverHost = "127.0.0.1";
		}
		serverPort = DEFAULT_SERVER_PORT;
	}

	@Override
	public String getClientName(String host) {
		String clientName = "Client"+clientNames.size();
		clientNames.add(clientName);
		return clientName;
	}

	@Override
	public String getClientContainerType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTargetID() {
		return targetId;
	}

}
