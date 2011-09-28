package de.fzi.aoide.replica.demo;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.collections15.Factory;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import de.fzi.aoide.common.graph.OWLClassHierarchyFromOWLClassHierarchyGraphBuilder;
import de.fzi.aoide.common.graph.OWLClassHierarchyGraph;
import de.fzi.aoide.common.graph.OWLClassHierarchyGraphBuilder;
import de.fzi.aoide.common.graph.OWLClassNode;
import de.fzi.aoide.common.graph.OWLClassRelation;
import de.fzi.aoide.common.graph.OWLSubClassOfRelation;
import de.fzi.aoide.common.graph.visualize.OWLClassHierarchyVisualizationViewerFactory;
import de.fzi.aoide.replica.OWLReplicaManager;
import de.fzi.aoide.replica.comm.CommManager;
import de.fzi.aoide.replica.comm.Connection;
import de.fzi.aoide.replica.comm.DefaultCommManagerFactory;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;

public class ReplicaOntologyDemoSingleHost {
	
	private static final int CLIENT = 0;
	private static final String SERVER_HOST = "localhost";
	private static final String SERVER_PORT = "5888";
	
	private static final String client0ID = "client0";
	private static final String client1ID = "client1";
	private static final int TREE_DEPTH = 100;
	private CommManager server;
	VisualizationViewer<OWLClassNode, OWLClassRelation> vv;
	
	public void start()
			throws InterruptedException, OWLOntologyCreationException,
			SharedObjectAddException, ContainerCreateException, ContainerConnectException {
		ReplicaOntologyDemoSingleHost demo = new ReplicaOntologyDemoSingleHost();
		
		switch(CLIENT) {
		case 0: {
			Holder holder = demo.setUpServerAndClient(client0ID);
			demo.setUpFrame(client0ID, holder);
			break;
			}
		case 1: {
			Holder holder = demo.setUpServerAndClient(client1ID);
			demo.setUpFrame(client1ID, holder);
			break;
			}
		default:
			break;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private VisualizationViewer<OWLClassNode, OWLClassRelation> setUpFrame(
			final String clientId, final Holder holder) {
		final JFrame frame = new JFrame("ReplicaOntology Demo - "+clientId);
		final OWLOntology primary = holder.primary;
		final OWLOntology replica = holder.replica;
		final OWLClassHierarchyVisualizationViewerFactory vvFac = 
			new OWLClassHierarchyVisualizationViewerFactory();
		vv = vvFac.create();
		final OWLClassHierarchyGraph graph = (OWLClassHierarchyGraph) vv.getGraphLayout().getGraph();
		final OWLDataFactory owlDFac = OWLManager.getOWLDataFactory();
		Factory<OWLClassNode> nodeFac = new Factory<OWLClassNode>() {
			public OWLClassNode create() {
				String clsIRI = createClassDialog(frame);
				OWLClass clazz = owlDFac.getOWLClass(IRI.create(clsIRI));
				if(client0ID.equals(clientId)) {
					System.out.println("primary change...");
					OWLClass superCls = selectSuperClassDialog(frame, primary);
					AddAxiom addAx = new AddAxiom(primary, owlDFac.getOWLSubClassOfAxiom(clazz, superCls));
					System.out.println("changes: "+primary.getOWLOntologyManager().applyChange(addAx));
				} else if(client1ID.equals(clientId)) {
					System.out.println("replica change...");
					OWLClass superCls = selectSuperClassDialog(frame, replica);
					AddAxiom addAx = new AddAxiom(replica, owlDFac.getOWLSubClassOfAxiom(clazz, superCls));
					System.out.println("changes: "+replica.getOWLOntologyManager().applyChange(addAx));
				}
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						System.out.println("rebuilding graph...");
						syncOntoFromGraph(graph, client0ID.equals(clientId) ? primary : replica);
						vv.repaint();
					}
				}, 1000);
				return new OWLClassNode(clazz);
			}
		};
		Factory<OWLClassRelation> edgeFac = new Factory<OWLClassRelation>() {
			public OWLClassRelation create() {
				System.out.println("creating edge...");
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						System.out.println("rebuilding graph...");
						syncOntoFromGraph(graph, client0ID.equals(clientId) ? primary : replica);
						vv.repaint();
					}
				}, 1000);
				return new OWLSubClassOfRelation();
			}
		};
		vv.setGraphMouse(new EditingModalGraphMouse<OWLClassNode, OWLClassRelation>(
				vv.getRenderContext(), nodeFac, edgeFac));
		vv.setGraphLayout(vvFac.createFRLayout(graph));
		
		Box controls = new Box(BoxLayout.X_AXIS);
		controls.add(vvFac.createVertexLabelCheckBox(vv));
		controls.add(Box.createHorizontalStrut(8));
		controls.add(vvFac.createEdgeLabelCheckBox(vv));
		controls.add(((EditingModalGraphMouse) vv.getGraphMouse()).getModeComboBox());
		
		Box mainHolder = new Box(BoxLayout.Y_AXIS);
		mainHolder.add(new GraphZoomScrollPane(vv));
		mainHolder.add(Box.createVerticalStrut(8));
		mainHolder.add(controls);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainHolder);
		frame.pack();
		frame.setVisible(true);
		
		final OWLClassHierarchyGraphBuilder treeBuilder = new OWLClassHierarchyGraphBuilder();
		if(client0ID.equals(clientId)) {
			primary.getOWLOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
				@Override
				public void ontologiesChanged(List<? extends OWLOntologyChange> changes)
						throws OWLException {
					System.out.println("OntologyChange on primary");
					OWLClass thing = primary.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
					OWLClassHierarchyGraph graph = 
						treeBuilder.buildGraph(Collections.singleton(thing), TREE_DEPTH, null, primary);
					System.out.println("graph.getVertices()="+graph.getVertices());
					vv.setGraphLayout(vvFac.createFRLayout(graph));
					vv.repaint();
				}
			});
		} else if(client1ID.equals(clientId)) {
			replica.getOWLOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
				@Override
				public void ontologiesChanged(List<? extends OWLOntologyChange> changes)
						throws OWLException {
					System.out.println("OntologyChange on replica");
					OWLClass thing = replica.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
					OWLClassHierarchyGraph graph = 
						treeBuilder.buildGraph(Collections.singleton(thing), TREE_DEPTH, null, replica);
					vv.setGraphLayout(vvFac.createFRLayout(graph));
					vv.repaint();
				}
			});
		}
		
		// Add OWL:Thing
		graph.addVertex(new OWLClassNode(owlDFac.getOWLThing()));
		vv.repaint();
		return vv;
	}
	
	private Holder setUpServerAndClient(String clientId)
			throws InterruptedException, OWLOntologyCreationException,
			SharedObjectAddException, ContainerCreateException, ContainerConnectException {
		DefaultCommManagerFactory comFac = new DefaultCommManagerFactory();
		
		// Create a server which will live on both clients
		server = new DefaultCommManagerFactory().createCommManager();
		Properties configServer = new Properties();
		configServer.put("containerType", "ecf.generic.server");
		configServer.put("containerID", "ecftcp://localhost:5888/server");
		Connection conSrv = server.createConnection(configServer);
		conSrv.initialize();
		conSrv.connect();
		
		// Create two clients, Interface not finished yet
		CommManager client0 = comFac.createCommManager();
		CommManager client1 = comFac.createCommManager();
		
		// Configuration for client 0
		Properties configClient0 = new Properties();
		configClient0.put("containerType", "ecf.generic.client");
		configClient0.put("containerID", "client0");
		configClient0.put("targetID", "ecftcp://"+SERVER_HOST+":"+SERVER_PORT+"/server");
		Properties configClient1 = new Properties();
		configClient1.putAll(configClient0);
		configClient1.put("containerID", "client1");
		
		client0.setConfiguration(configClient0);
		client1.setConfiguration(configClient1);
		client0.initialize();
		client1.initialize();
		
		Connection conClient0 = client0.createConnection(configClient0);
		Connection conClient1 = client1.createConnection(configClient1);
		conClient0.initialize();
		conClient0.connect();
		conClient1.initialize();
		conClient1.connect();
		
		// Use same ID for the ontology ID and the shared object ID
		String ontoID = "replica://myontology.org/";
		ID sharedObjID = IDFactory.getDefault().createStringID(ontoID);
		OWLOntology primary = OWLReplicaManager.createOWLOntologyManager().
			createOntology(IRI.create(ontoID));
		
		conClient0.getSharedObjectContainer().getSharedObjectManager().
			addSharedObject(sharedObjID, (ISharedObject) primary, null);
		// Wait for the ontology to become active
		Thread.sleep(1000);
		OWLOntology replica = (OWLOntology) conClient1.getSharedObjectContainer().
			getSharedObjectManager().getSharedObject(sharedObjID);
		
//		// Add some axiom
//		System.out.println("-------------------");
//		OWLDataFactory fac = primary.getOWLOntologyManager().getOWLDataFactory();
//		AddAxiom addAx = new AddAxiom(primary, fac.getOWLSubClassOfAxiom(
//				fac.getOWLClass(IRI.create("someClass")), fac.getOWLThing()));
//		((OWLMutableOntology) replica).applyChange(addAx);
//		System.out.println("-------------------");
		
		return new Holder(conClient0, conClient1, primary, replica);
	}
	
	private List<OWLOntologyChange> syncOntoFromGraph(OWLClassHierarchyGraph graph,
			OWLOntology onto) {
		OWLClassHierarchyFromOWLClassHierarchyGraphBuilder builder = 
			new OWLClassHierarchyFromOWLClassHierarchyGraphBuilder();
		List<OWLOntologyChange> changes = null;
		OWLClass thing = onto.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
		changes = builder.buildHierarchy(graph, thing, onto);
		onto.getOWLOntologyManager().applyChanges(changes);
		System.out.println("changes="+changes);
		return changes;
	}
	
	private String createClassDialog(JFrame parent) {
		String clsName = (String) JOptionPane.showInputDialog(
                parent,
                "Enter new class name:\n",
                "New class name",
                JOptionPane.PLAIN_MESSAGE,
                (Icon) null, // icon
                null,
                "<enter class name>");
		return clsName;
	}
	
	private OWLClass selectSuperClassDialog(JFrame parent, OWLOntology onto) {
		Set<OWLClass> classes = onto.getClassesInSignature();
		OWLClass thing = OWLManager.getOWLDataFactory().getOWLThing();
		classes.add(thing);
		Object[] possibilities = classes.toArray();
		OWLClass clazz = (OWLClass) JOptionPane.showInputDialog(
                parent,
                "Select super class:\n",
                "Super class selection",
                JOptionPane.PLAIN_MESSAGE,
                (Icon) null, // icon
                possibilities,
                thing);
		return clazz;
	}
	
	class Holder {
		public Holder(
				Connection client0,
				Connection client1,
				OWLOntology primary,
				OWLOntology replica) {
			this.client0 = client0;
			this.client1 = client1;
			this.primary = primary;
			this.replica = replica;
		}
		Connection client0;
		Connection client1;
		OWLOntology primary;
		OWLOntology replica;
	}
	
}
