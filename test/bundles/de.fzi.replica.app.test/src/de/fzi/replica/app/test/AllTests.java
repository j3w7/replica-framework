package de.fzi.replica.app.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ClientTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
