package de.fzi.replica.app.util;


// TODO
public class CLIStarter {
	
	private static final String DEFAULT_CONTAINER_TYPE = "ecf.generic.client";
	private static final String DEFAULT_CONTAINER_TARGET = "ecftcp://localhost:5888/server";
	private static final String DEFAULT_CONTAINER_ID = "client"+String.valueOf(Math.random());
	
	String containerType = DEFAULT_CONTAINER_TYPE;
	String targetID = DEFAULT_CONTAINER_TARGET;
	String containerID = DEFAULT_CONTAINER_ID;
	
	public void processArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-containerType")) {
				containerType = args[i + 1];
				i++;
			} else if (args[i].equals("-targetID")) {
				targetID = args[i + 1];
				i++;
			} else if (args[i].equals("-containerID")) {
				containerID = args[i + 1];
				i++;
			}
		}
	}
	
}
