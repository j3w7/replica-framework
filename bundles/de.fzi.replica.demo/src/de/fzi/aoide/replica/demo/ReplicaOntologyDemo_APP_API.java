package de.fzi.aoide.replica.demo;

//import java.util.Properties;
//
//import javax.swing.Box;
//import javax.swing.BoxLayout;
//import javax.swing.JFrame;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
//
//import org.apache.commons.collections15.Factory;
//import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.model.AddAxiom;
//import org.semanticweb.owlapi.model.IRI;
//import org.semanticweb.owlapi.model.OWLClass;
//import org.semanticweb.owlapi.model.OWLDataFactory;
//import org.semanticweb.owlapi.model.OWLMutableOntology;
//import org.semanticweb.owlapi.model.OWLOntology;
//import org.semanticweb.owlapi.model.OWLOntologyCreationException;
//
//import de.fzi.aoide.common.graph.OWLClassHierarchyGraph;
//import de.fzi.aoide.common.graph.OWLClassHierarchyTreeBuilder;
//import de.fzi.aoide.common.graph.OWLClassNode;
//import de.fzi.aoide.common.graph.OWLClassRelation;
//import de.fzi.aoide.common.graph.OWLSubClassOfRelation;
//import de.fzi.aoide.common.graph.visualize.OWLClassHierarchyVisualizationViewerFactory;
//import de.fzi.aoide.replica.app.ReplicaOntologyClient;
//import de.fzi.aoide.replica.app.ReplicaOntologyClientImpl;
//import de.fzi.aoide.replica.app.ReplicaOntologyServer;
//import de.fzi.aoide.replica.app.ReplicaOntologyServerImpl;
//import de.fzi.aoide.replica.app.ReplicaOntologyApplication.StartupException;
//import de.fzi.aoide.replica.app.ReplicaOntologyClient.ConnectException;
//import de.fzi.aoide.replica.app.ReplicaOntologyClient.ReplicaOntologyAddException;
//import de.fzi.aoide.replica.app.ReplicaOntologyClient.ReplicaOntologyGetException;
//import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
//import edu.uci.ics.jung.visualization.VisualizationViewer;
//import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;

public class ReplicaOntologyDemo_APP_API {
	
//	private ReplicaOntologyServer server;
//	
//	public void start()
//			throws StartupException, InterruptedException, ConnectException, 
//				ReplicaOntologyAddException, ReplicaOntologyGetException, OWLOntologyCreationException {
//		final OWLDataFactory owlDFac = OWLManager.getOWLDataFactory();
//		OWLClassNode a = new OWLClassNode(owlDFac.getOWLClass(IRI.create("A")));
//		OWLClassNode b = new OWLClassNode(owlDFac.getOWLClass(IRI.create("B")));
//		
//		ReplicaOntologyDemo_APP_API demo = new ReplicaOntologyDemo_APP_API();
//		Holder clients = demo.setUpServerAndClients();
//		
//		demo.setUpFrame("client0", clients);
//		demo.setUpFrame("client1", clients);
//		
//		boolean run = true;
//		while(run) {
//			System.out.println("client0.onto="+clients.onto.getAxioms()+"\nclient1.onto="+clients.replica.getAxioms());
//			run = false;
//		}
//	}
//	
//	private VisualizationViewer<OWLClassNode, OWLClassRelation> setUpFrame(final String clientId, final Holder clients) {
//		OWLClassHierarchyVisualizationViewerFactory vvFac = new OWLClassHierarchyVisualizationViewerFactory();
//		VisualizationViewer<OWLClassNode, OWLClassRelation> vv = vvFac.create();
//		final OWLDataFactory owlDFac = OWLManager.getOWLDataFactory();
//		Factory<OWLClassNode> nodeFac = new Factory<OWLClassNode>() {
//			public OWLClassNode create() {
//				OWLClass clazz = owlDFac.getOWLClass(IRI.create("some_random_class" + Math.random()));
//				if("client0".equals(clientId)) {
//					System.out.println("primary change...");
//					AddAxiom addAx = new AddAxiom(clients.onto, owlDFac.getOWLSubClassOfAxiom(clazz, owlDFac.getOWLThing()));
//					System.out.println("changes: "+clients.onto.getOWLOntologyManager().applyChange(addAx));
//				} else if("client1".equals(clientId)) {
//					System.out.println("replica change...");
//					AddAxiom addAx = new AddAxiom(clients.replica, owlDFac.getOWLSubClassOfAxiom(clazz, owlDFac.getOWLThing()));
//					System.out.println("changes: "+clients.replica.getOWLOntologyManager().applyChange(addAx));
//				}
//				return new OWLClassNode(clazz);
//			}
//		};
//		Factory<OWLClassRelation> edgeFac = new Factory<OWLClassRelation>() {
//			public OWLClassRelation create() {
//				return new OWLSubClassOfRelation();
//			}
//		};
//		vv.setGraphMouse(new EditingModalGraphMouse<OWLClassNode, OWLClassRelation>(vv.getRenderContext(), nodeFac, edgeFac));
//		OWLClassHierarchyGraph graph = (OWLClassHierarchyGraph) vv.getGraphLayout().getGraph();
//		vv.setGraphLayout(vvFac.createFRLayout(graph));
//		
//		Box controls = new Box(BoxLayout.X_AXIS);
//		controls.add(vvFac.createVertexLabelCheckBox(vv));
//		controls.add(Box.createHorizontalStrut(8));
//		controls.add(vvFac.createEdgeLabelCheckBox(vv));
//		controls.add(((EditingModalGraphMouse) vv.getGraphMouse()).getModeComboBox());
//		
//		Box mainHolder = new Box(BoxLayout.Y_AXIS);
//		mainHolder.add(new GraphZoomScrollPane(vv));
//		mainHolder.add(Box.createVerticalStrut(8));
//		mainHolder.add(controls);
//		
//		JFrame frame = new JFrame("ReplicaOntology Demo - "+clientId);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().add(mainHolder);
//		frame.pack();
//		frame.setVisible(true);
//		
//		vv.addChangeListener(new ChangeListener() {
//
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				System.out.println("ChangeEvent: "+e);
//				
//			}
//			
//		});
//		
//		return vv;
//	}
//	
//	private Holder setUpServerAndClients()
//			throws StartupException, InterruptedException, ConnectException,
//				ReplicaOntologyAddException, ReplicaOntologyGetException, OWLOntologyCreationException {
//		// Create a server
//		server = new ReplicaOntologyServerImpl();
//		Properties configServer = new Properties();
//		configServer.put("containerType", "ecf.generic.server");
//		configServer.put("containerID", "ecftcp://localhost:5888/server");
//		server.setConfiguration(configServer);
//		server.start();
//		
//		// Create two clients, Interface not finished yet
//		ReplicaOntologyClient client0 = new ReplicaOntologyClientImpl();
//		ReplicaOntologyClient client1 = new ReplicaOntologyClientImpl();
//		
//		// Configuration for client 0
//		Properties configClient0 = new Properties();
//		configClient0.put("containerType", "ecf.generic.client");
//		configClient0.put("containerID", "client0");
//		configClient0.put("targetID", "ecftcp://localhost:5888/server");
//		Properties configClient1 = new Properties();
//		configClient1.putAll(configClient0);
//		configClient1.put("containerID", "client1");
//		
//		client0.setConfiguration(configClient0);
//		client1.setConfiguration(configClient1);
//
//		client0.start();
//		client0.connect();
//		client1.start();
//		client1.connect();
//		
//		OWLOntology onto = OWLManager.createOWLOntologyManager().createOntology(IRI.create("replica://myontology.org/"));
//		OWLDataFactory fac = onto.getOWLOntologyManager().getOWLDataFactory();
//		
//		client0.addOWLOntology(onto);
//		// Wait for the ontology to become active
//		Thread.sleep(1000);
//		OWLOntology replica = client1.getOWLOntology(onto.getOntologyID());
//		
//		System.out.println("-------------------");
//		AddAxiom addAx = new AddAxiom(onto, fac.getOWLSubClassOfAxiom(fac.getOWLClass(IRI.create("someClass")), fac.getOWLThing()));
//		((OWLMutableOntology) replica).applyChange(addAx);
//		System.out.println("-------------------");
//		
//		return new Holder(client0, onto, client1, replica);
//	}
//	
//	class Holder {
//		public Holder(ReplicaOntologyClient client0,
//				OWLOntology onto, ReplicaOntologyClient client1, OWLOntology replica) {
//			this.client0 = client0;
//			this.client1 = client1;
//			this.onto = onto;
//			this.replica = replica;
//		}
//		ReplicaOntologyClient client0;
//		ReplicaOntologyClient client1;
//		OWLOntology onto;
//		OWLOntology replica;
//	}
	
}
