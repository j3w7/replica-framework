/*
   Copyright 2012 Jan Novacek

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

package de.fzi.replica.app.test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import de.fzi.replica.OWLReplicaOntology;
import de.fzi.replica.app.client.Client.AddException;
import de.fzi.replica.app.client.Client.AsyncMethodCallback.Result;
import de.fzi.replica.app.client.Client.FetchException;
import de.fzi.replica.app.client.Client.OnGroupsReceivedListener;
import de.fzi.replica.app.client.Client.OnOntologyAddedListener;
import de.fzi.replica.app.client.Client.OnOntologyIDsReceivedListener;
import de.fzi.replica.app.client.Client.OnOntologyReceivedListener;

/**
 * Tests all methods of the <code>Client</code> interface.
 * 
 * @see de.fzi.replica.app.client.Client
 * 
 * @author Jan Novacek novacek@fzi.de, jannvck@gmail.com
 * @version 0.2, 25.01.2012
 */
public class ClientTestCase extends AbstractApplicationTestCase {

	// just for convenience
	private static final String A = "A";
	private static final String B = "B";

	private OWLOntologyID ontologyID0 = new OWLOntologyID(
			IRI.create("replica://myTestOntology.org/0")); // TODO IRI scheme
															// rewrite
	private OWLOntologyID ontologyID1 = new OWLOntologyID(
			IRI.create("replica://myTestOntology.org/1")); // TODO IRI scheme
															// rewrite

	@Before
	public void setUp() throws Exception {
		super.setUp();
		createServer();
		startServer();
		createClients(A, B);
		startAllClients();
		connectAllClients();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		// disconnectAllClients(); stopping implies disconnecting
		stopAllClients();
		stopServer();
	}

	private class Holder {
		Result result0;
		Result result1;
		Object[] objs;
	}

	final Holder h = new Holder();

	class AddOntoThread extends Thread {
		@Override
		public void run() {
			try {
				OWLOntology onto0 = createTestOWLOntology(ontologyID0);
				Set<String> groups = new HashSet<String>();
				groups.add("testgroup0");
				groups.add("testgroup1");
				groups.add("testgroup2");
				getClient(A).addOWLOntology(onto0, groups,
						new OnOntologyAddedListener() {
							@Override
							public void onOntologyAdded(Result result) {
								h.result0 = result;
							}
						});
				Thread.sleep(3000);
				OWLOntology onto1 = createTestOWLOntology(ontologyID1);
				getClient(B).addOWLOntology(onto1, groups,
						new OnOntologyAddedListener() {
							@Override
							public void onOntologyAdded(Result result) {
								h.result1 = result;
							}
						});
			} catch (AddException e) {
				e.printStackTrace();
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testAddOntology() throws OWLOntologyCreationException,
			AddException, InterruptedException {
		System.out.println("------testAddOntology()------start");
		AddOntoThread t = new AddOntoThread();
		t.start();
		Thread.sleep(8000);
		assertEquals(Result.OK, h.result0);
		assertEquals(Result.OK, h.result1);
		System.out.println("------testAddOntology()------end\n");
	}

	class AddOntoAndModifyThread extends Thread {
		@Override
		public void run() {
			try {
				// clear the holder
				h.result0 = null;
				h.result1 = null;
				h.objs = new Object[2];
				h.objs[0] = null;
				h.objs[1] = null;

				final OWLOntology onto0 = createTestOWLOntology(ontologyID0);
				onto0.getOWLOntologyManager().removeAxioms(onto0,
						onto0.getAxioms());
				Set<String> groups = new HashSet<String>();
				groups.add("testgroup0");
				groups.add("testgroup1");
				groups.add("testgroup2");
				getClient(A).addOWLOntology(onto0, groups,
						new OnOntologyAddedListener() {
							@Override
							public void onOntologyAdded(Result result) {
								h.result0 = result;
								h.objs[0] = onto0;
							}
						});
				Thread.sleep(500);
				getClient(B).getOWLOntology(ontologyID0,
						new OnOntologyReceivedListener() {
							@Override
							public void onOntologyReceived(Result result,
									OWLOntology onto) {
								h.result1 = result;
								h.objs[1] = onto;
							}
						});

				while (h.objs[1] == null) {
					// wait
				}

				/*
				 * Modify the ontology
				 */

				OWLOntologyManager man0 = onto0.getOWLOntologyManager();
				man0.addOntologyChangeListener(new OWLOntologyChangeListener() {
					@Override
					public void ontologiesChanged(
							List<? extends OWLOntologyChange> changes)
							throws OWLException {
						System.out.println("onto0 changes: " + changes);
					}
				});

				((OWLOntology) h.objs[0]).getOWLOntologyManager()
						.addOntologyChangeListener(
								new OWLOntologyChangeListener() {
									@Override
									public void ontologiesChanged(
											List<? extends OWLOntologyChange> changes)
											throws OWLException {
										System.out.println("onto1 changes: "
												+ changes);
									}
								});

				// Now modifiy ontology 0, later watch out for changes on onto 1
				OWLDataFactory fac = man0.getOWLDataFactory();
				OWLClass clA = fac.getOWLClass(IRI.create("classA"));
				OWLClass clB = fac.getOWLClass(IRI.create("classB"));
				man0.applyChange(new AddAxiom(onto0, fac.getOWLSubClassOfAxiom(
						clA, clB)));
			} catch (AddException e) {
				e.printStackTrace();
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			} catch (FetchException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testAddOntologyAndModify()
		throws OWLOntologyCreationException,
			AddException, InterruptedException {
		System.out.println("------testAddOntologyAndModify()------start");
		AddOntoAndModifyThread t = new AddOntoAndModifyThread();
		t.start();
		Thread.sleep(8000);
		assertEquals(Result.OK, h.result0);
		assertEquals(Result.OK, h.result1);
		assertEquals(((OWLOntology) h.objs[0]).getAxioms(),
				((OWLOntology) h.objs[1]).getAxioms());
		System.out.println("------testAddOntologyAndModify()------end\n");
	}

	class AddOntoAndCheckGroupsThread extends Thread {
		@Override
		public void run() {
			try {
				// clear the holder
				h.result0 = null;
				h.result1 = null;
				h.objs = new Object[2];

				final OWLOntology onto0 = createTestOWLOntology(ontologyID0);
				onto0.getOWLOntologyManager().removeAxioms(onto0,
						onto0.getAxioms());
				Set<String> groups = new HashSet<String>();
				groups.add("testgroup0");
				groups.add("testgroup1");
				groups.add("testgroup2");
				getClient(A).addOWLOntology(onto0, groups,
						new OnOntologyAddedListener() {
							@Override
							public void onOntologyAdded(Result result) {
								h.result0 = result;
								h.objs[0] = onto0;
							}
						});
				getClient(B).getGroups(new OnGroupsReceivedListener() {
					@Override
					public void onGroupsGot(Result result, Set<String> groups) {
						h.result1 = result;
						h.objs[1] = groups;
					}
				});
				// getClient(B).getOWLOntology(ontologyID0,
				// new OnOntologyReceivedListener() {
				// @Override
				// public void onOntologyReceived(Result result,
				// OWLOntology onto) {
				// h.result1 = result;
				// h.objs[1] = onto;
				// }
				// });

				while (h.objs[1] == null) {
					// wait
				}

			} catch (AddException e) {
				e.printStackTrace();
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			} catch (FetchException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testAddOntologyAndCheckGroups()
			throws OWLOntologyCreationException, AddException,
			InterruptedException {
		System.out.println("------testAddOntologyAndModify()------start");
		AddOntoAndCheckGroupsThread t = new AddOntoAndCheckGroupsThread();
		t.start();
		Thread.sleep(8000);
		assertEquals(Result.OK, h.result0);
		assertEquals(Result.OK, h.result1);
		// assertEquals(((OWLOntology) h.objs[0]).getAxioms(),
		// ((OWLOntology) h.objs[1]).getAxioms());
		System.out.println("------testAddOntologyAndModify()------end\n");
	}

//	class GetGroupsThread extends Thread {
//		@Override
//		public void run() {
//			try {
//				clearHolder();
//				h.objs = new Object[2];
//				getClient(A).getGroups(new OnGroupsReceivedListener() {
//					@Override
//					public void onGroupsGot(Result result, Set<String> groups) {
//						h.result0 = result;
//						h.objs[0] = groups;
//					}
//				});
//				Thread.sleep(100);
//				getClient(B).getGroups(new OnGroupsReceivedListener() {
//					@Override
//					public void onGroupsGot(Result result, Set<String> groups) {
//						h.result1 = result;
//						h.objs[1] = groups;
//					}
//				});
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (FetchException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetGroups() throws FetchException {
		System.out.println("------testGetGroups()------start");
		clearHolder();
		h.objs = new Object[2];
		h.objs[0] = null;
		h.objs[1] = null;
		OWLOntology onto0 = null;
		try {
			onto0 = createTestOWLOntology(ontologyID0);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		onto0.getOWLOntologyManager().removeAxioms(onto0,
				onto0.getAxioms());
		Set<String> groups0 = new HashSet<String>();
		groups0.add("test groups0 0");
		groups0.add("test groups0 1");
		groups0.add("test groups0 2");
		Set<String> groups1 = new HashSet<String>();
		groups1.add("test groups1 0");
		groups1.add("test groups1 1");
		groups1.add("test groups1 2");
		try {
			getClient(A).addOWLOntology(onto0,
					groups0, new OnOntologyAddedListener() {
				@Override
				public void onOntologyAdded(Result result) {
					assertEquals(result, Result.OK);
				}
			});
			Thread.sleep(500);
			getClient(B).addOWLOntology(onto0,
					groups1, new OnOntologyAddedListener() {
				@Override
				public void onOntologyAdded(Result result) {
					assertEquals(result, Result.OK);
				}
			});
			Thread.sleep(500);
			getClient(A).getGroups(new OnGroupsReceivedListener() {
				@Override
				public void onGroupsGot(Result result, Set<String> groups) {
					h.result0 = result;
					h.objs[0] = groups;
				}
			});
			Thread.sleep(500);
			getClient(B).getGroups(new OnGroupsReceivedListener() {
				@Override
				public void onGroupsGot(Result result, Set<String> groups) {
					h.result1 = result;
					h.objs[1] = groups;
				}
			});
		} catch (AddException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		sleep(200);
		assertEquals(Result.OK, h.result0);
		assertEquals(Result.OK, h.result1);
		assertNotNull(h.objs[0]);
		assertNotNull(h.objs[1]);
		for(String s : groups0) {
			assertTrue(((Set<String>) h.objs[0]).contains(s));
		}
		for(String s : groups1) {
			assertTrue(((Set<String>) h.objs[1]).contains(s));
		}
		System.out.println("h.objs[0]=" + h.objs[0]);
		System.out.println("h.objs[1]=" + h.objs[1]);
		System.out.println("------testGetGroups()------end\n");
	}

	class GetOntoThread extends Thread {
		@Override
		public void run() {
			try {
				getClient(A).getOWLOntology(ontologyID0,
						new OnOntologyReceivedListener() {
							@Override
							public void onOntologyReceived(Result result,
									OWLOntology onto) {
								h.result0 = result;
								h.objs[0] = onto;
							}
						});
				Thread.sleep(100);
				getClient(B).getOWLOntology(ontologyID0,
						new OnOntologyReceivedListener() {
							@Override
							public void onOntologyReceived(Result result,
									OWLOntology onto) {
								h.result1 = result;
								h.objs[1] = onto;
							}
						});
			} catch (FetchException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testGetOntologyAndFail() throws InterruptedException,
			FetchException {
		System.out.println("------testGetOntologyAndFail()------start");
		clearHolder();
		h.objs = new Object[2];
		GetOntoThread getOntoThread = new GetOntoThread();
		getOntoThread.start();
		Thread.sleep(300);
		assertEquals(Result.NULL, h.result0);
		assertEquals(Result.NULL, h.result1);
		System.out.println("------testGetOntologyAndFail()------end\n");
	}

	@Test
	public void testGetOntology() throws InterruptedException, FetchException,
			OWLOntologyCreationException, AddException {
		System.out.println("------testGetOntology()------start");
		testAddOntology();
		clearHolder();
		h.objs = new Object[2];
		GetOntoThread getOntoThread = new GetOntoThread();
		getOntoThread.start();
		Thread.sleep(3000);
		assertEquals(Result.OK, h.result0);
		assertEquals(Result.OK, h.result1);
		OWLReplicaOntology o0 = (OWLReplicaOntology) h.objs[0];
		OWLReplicaOntology o1 = (OWLReplicaOntology) h.objs[1];
		assertNotNull(o0);
		assertNotNull(o1);
		o0.containsClassInSignature(IRI.create("http://blub.org/#classA"));
		o0.containsClassInSignature(IRI.create("http://blub.org/#classB"));
		o0.containsClassInSignature(IRI.create("http://blub.org/#classC"));
		o1.containsClassInSignature(IRI.create("http://blub.org/#classA"));
		o1.containsClassInSignature(IRI.create("http://blub.org/#classB"));
		o1.containsClassInSignature(IRI.create("http://blub.org/#classC"));
		System.out.println("h.objs[0]=" + h.objs[0]);
		System.out.println("h.objs[1]=" + h.objs[1]);
		System.out.println("------testGetOntology()------end\n");
	}

	class GetOntoIDsThread extends Thread {
		@Override
		public void run() {
			try {
				System.out.println("creating test ontology, ontologyID="
						+ ontologyID0);
				OWLOntology onto0 = createTestOWLOntology(ontologyID0);
				Set<String> groups = new HashSet<String>();
				groups.add("testgroup0");
				groups.add("testgroup1");
				groups.add("testgroup2");
				getClient(A).addOWLOntology(onto0, groups,
						new OnOntologyAddedListener() {
							@Override
							public void onOntologyAdded(Result result) {
								Assert.assertEquals(Result.OK, result);
								System.out.println("onOntologyAdded(" + result
										+ ")");
							}
						});
				Thread.sleep(1000);
				OWLOntology onto1 = createTestOWLOntology(ontologyID1);
				getClient(B).addOWLOntology(onto1, groups,
						new OnOntologyAddedListener() {
							@Override
							public void onOntologyAdded(Result result) {
								Assert.assertEquals(Result.OK, result);
								System.out.println("onOntologyAdded(" + result
										+ ")");
							}
						});
				Thread.sleep(2000);
				getClient(A).getOWLOntologyIDs(groups,
						new OnOntologyIDsReceivedListener() {
							@Override
							public void onOntologyIDsGot(Result result,
									Map<Object, Set<OWLOntologyID>> ids) {
								System.out.println("onOntologyIDsGot(" + result
										+ ", " + ids + ")");
								h.result0 = result;
								h.objs[0] = ids;
							}
						});
				Thread.sleep(5000);
				getClient(B).getOWLOntologyIDs(groups,
						new OnOntologyIDsReceivedListener() {
							@Override
							public void onOntologyIDsGot(Result result,
									Map<Object, Set<OWLOntologyID>> ids) {
								h.result1 = result;
								h.objs[1] = ids;
							}
						});
			} catch (FetchException e) {
				e.printStackTrace();
			} catch (AddException e) {
				e.printStackTrace();
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testGetOntologyIDs() throws InterruptedException,
			FetchException, OWLOntologyCreationException, AddException {
		System.out.println("------testGetOntologyIDs()------start");
		clearHolder();
		h.objs = new Object[2];
		GetOntoIDsThread getOntoIDsThread = new GetOntoIDsThread();
		getOntoIDsThread.start();
		Thread.sleep(20000);
		assertEquals(Result.OK, h.result0);
		assertEquals(Result.OK, h.result1);
		assertNotNull(h.objs[0]);
		assertNotNull(h.objs[1]);
		System.out.println("h.objs[0]=" + h.objs[0]);
		System.out.println("h.objs[1]=" + h.objs[1]);
		System.out.println("------testGetOntologyIDs()------end\n");
	}

	private void clearHolder() {
		h.objs = null;
		h.result0 = null;
		h.result1 = null;
	}

	// @Test
	// public void testAddEmptySharedOWLOntology()
	// throws OWLOntologyCreationException, InterruptedException,
	// ReplicaOntologyAddException, ReplicaOntologyFetchException {
	// // Create a standard OWLAPI ontology
	// OWLOntologyID ontologyID = new
	// OWLOntologyID(IRI.create("replica://mySharedOntology/")); // TODO any iri
	// should work
	// OWLOntology onto = createOWLReplicaOntology(ontologyID);
	//
	// // Add it at client A
	// // getClient(A).addOWLOntology(onto, null); // TODO set group or create
	// test for groups
	// // getClient(A).addOWLOntology(onto, "groupA"); // TODO set group or
	// create test for groups
	// // Wait for the object to become active
	// // Thread.sleep(1000);
	// // // B should get a reference to the replica now
	// // OWLOntology replica = getClient(B).getOWLOntology(ontologyID);
	// //
	// // assertNotNull(replica);
	// // assertNotSame(onto, replica);
	// // assertEquals(onto.getOntologyID(), replica.getOntologyID());
	// }

	// @Test
	// public void testAddEmptySharedOWLOntologyAndFill()
	// throws OWLOntologyCreationException, ReplicaOntologyAddException,
	// InterruptedException, ReplicaOntologyGetException {
	// // Create a standard OWLAPI ontology
	// OWLOntologyID ontologyID = new
	// OWLOntologyID(IRI.create("replica://mySharedOntology/")); // TODO any iri
	// should work
	// OWLOntology onto = createOWLOntology(ontologyID);
	// // Add it at client A
	// getClient(A).addOWLOntology(onto);
	// // Wait for the object to become active
	// Thread.sleep(1000);
	// // B should get a reference to the replica now
	// OWLOntology replica = getClient(B).getOWLOntology(ontologyID);
	//
	// assertNotNull(replica);
	// assertNotSame(onto, replica);
	// assertEquals(onto.getOntologyID(), replica.getOntologyID());
	//
	// // Now fill the ontology at client A's side
	// IRI pizzaOntologyIRI =
	// IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
	// OWLOntology pizzaOnto =
	// OWLManager.createOWLOntologyManager().loadOntology(pizzaOntologyIRI);
	// new
	// OWLOntologyToOWLReplicaOntologyCopier().copy(Collections.singleton(pizzaOnto),
	// onto);
	// }

	// @Test
	// public void testAddEmptySharedOWLOntologyAndFill()
	// throws SharedObjectAddException, OWLOntologyCreationException,
	// InterruptedException {
	// OWLOntologyID ontoID = new
	// OWLOntologyID(IRI.create("replica://mySharedOntology/"));
	// ID sharedObjID = IDFactory.getDefault().createGUID();
	//
	// OWLReplicaOntology sharedOnto = createOWLReplicaOntology(ontoID);
	//
	// // A adds an empty SharedOWLOntology
	// getClientSharedObjectManager(A).addSharedObject(sharedObjID, sharedOnto,
	// null);
	//
	// // Wait for the object to be added
	// Thread.sleep(1000);
	//
	// // B should get a reference to the replica now
	// OWLReplicaOntology replica = (OWLReplicaOntology)
	// getClientSharedObjectManager(B).getSharedObject(sharedObjID);
	// assertNotNull(replica);
	// assertNotSame(sharedOnto, replica);
	//
	// // Load the pizza ontology and copy all axioms into our shared ontology
	// IRI pizzaOntologyIRI =
	// IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
	// OWLOntology pizzaOnto =
	// OWLManager.createOWLOntologyManager().loadOntology(pizzaOntologyIRI);
	// new
	// OWLOntologyToOWLReplicaOntologyCopier().copy(Collections.singleton(pizzaOnto),
	// sharedOnto);
	//
	// // Wait for change promotion
	// Thread.sleep(1000);
	// assertNotSame(pizzaOnto, sharedOnto);
	// assertEquals(pizzaOnto.getAxioms(), sharedOnto.getAxioms());
	// assertEquals(pizzaOnto.getAxioms(), replica.getAxioms());
	// }

}
