package de.fzi.aoide.replica.demo.client;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObject;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
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

/**
 * @author Jan Novacek novacek@fzi.de, jannvck@mail.com
 * @version 0.1, 30.06.2011
 */
public class ReplicaOntologyDemo {
	
	static int CFG_CLIENT;
	static String CFG_SERVER_HOST;
	static String CFG_SERVER_PORT;
	static {
		ResourceBundle cfgBundle = null;
		try {
			cfgBundle = ResourceBundle.getBundle("config");
			CFG_CLIENT = Integer.valueOf(cfgBundle.getString("client"));
			CFG_SERVER_HOST = cfgBundle.getString("ip");
			CFG_SERVER_PORT = cfgBundle.getString("port");
//			System.out.println("clientID="+CFG_CLIENT+"\n"+
//					"ip="+CFG_SERVER_HOST+"\n"+
//					"port="+CFG_SERVER_PORT);
		} catch (Exception e) {
			System.out.println("Could not read config.properties, cause: "+
					e.getCause());
			e.printStackTrace();
		}
	}
	private static final int CLIENT = CFG_CLIENT;
	private static final String SERVER_HOST = CFG_SERVER_HOST;
	private static final String SERVER_PORT = CFG_SERVER_PORT;
	private static final String client0ID = "client0";
	private static final String client1ID = "client1";
	private static final int TREE_DEPTH = 100;
	private static final int SYNC_DELAY = 1000;
	private CommManager server;
	private VisualizationViewer<OWLClassNode, OWLClassRelation> vv;
	private JFrame frame;
	
	public void start()
			throws InterruptedException, OWLOntologyCreationException,
			SharedObjectAddException, ContainerCreateException, ContainerConnectException {
		ReplicaOntologyDemo demo = new ReplicaOntologyDemo();
		switch(CLIENT) {
		case 0: {
			OWLOntology holder = demo.setUpServerAndClient(client0ID);
			demo.setUpFrame(client0ID, holder);
			break;
			}
		case 1: {
			OWLOntology holder = demo.setUpServerAndClient(client1ID);
			demo.setUpFrame(client1ID, holder);
			break;
			}
		default:
			break;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private VisualizationViewer<OWLClassNode, OWLClassRelation> setUpFrame(
			final String clientId, final OWLOntology onto) {
		final JFrame frame = new JFrame("ReplicaOntology Demo - "+clientId);
		final OWLClassHierarchyVisualizationViewerFactory vvFac = 
			new OWLClassHierarchyVisualizationViewerFactory();
		vv = vvFac.create();
		final OWLClassHierarchyGraph graph = (OWLClassHierarchyGraph) vv.getGraphLayout().getGraph();
		final OWLDataFactory owlDFac = OWLManager.getOWLDataFactory();
		Factory<OWLClassNode> nodeFac = new Factory<OWLClassNode>() {
			public OWLClassNode create() {
				String clsIRI = createClassDialog(frame);
				OWLClass clazz = owlDFac.getOWLClass(IRI.create(clsIRI));
				OWLClass superCls = selectSuperClassDialog(frame, onto);
				AddAxiom addAx = new AddAxiom(onto, owlDFac.getOWLSubClassOfAxiom(clazz, superCls));
				AddAxiom aAx = new AddAxiom(onto, createOWLAnnotationAxiomOf(clazz.getIRI(), onto));
				List<AddAxiom> axioms = new LinkedList<AddAxiom>();
				axioms.add(addAx);
				axioms.add(aAx);
				onto.getOWLOntologyManager().applyChanges(axioms);
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						syncOntoFromGraph(graph, onto);
						vv.repaint();
					}
				}, SYNC_DELAY);
				OWLClassNode n = new OWLClassNode(clazz);
				n.setClient(CLIENT);
				return n;
			}
		};
		Factory<OWLClassRelation> edgeFac = new Factory<OWLClassRelation>() {
			public OWLClassRelation create() {
				System.out.println("creating OWLSubClassOfRelation");
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						syncOntoFromGraph(graph, onto);
						vv.repaint();
					}
				}, SYNC_DELAY);
				return new OWLSubClassOfRelation();
			}
		};
		vv.setGraphMouse(new EditingModalGraphMouse<OWLClassNode, OWLClassRelation>(
				vv.getRenderContext(), nodeFac, edgeFac));
		vv.setGraphLayout(vvFac.createFRLayout(graph));
		vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<OWLClassNode, Paint>() {
			@Override
			public Paint transform(OWLClassNode node) {
				return getNodeColor(node.getOWLClass(), onto);
			}
		});
		
		Box controls = new Box(BoxLayout.X_AXIS);
		controls.add(vvFac.createVertexLabelCheckBox(vv));
		controls.add(Box.createHorizontalStrut(8));
		controls.add(vvFac.createEdgeLabelCheckBox(vv));
		controls.add(((EditingModalGraphMouse) vv.getGraphMouse()).getModeComboBox());
		controls.setBorder(BorderFactory.createTitledBorder("Controls"));
		
		JLabel client = createColoredLabel("Client "+CLIENT, getClientColor(CLIENT), new Point(0,0));
		JLabel address = new JLabel("Address: "+SERVER_HOST+":"+SERVER_PORT);
		
		Box iBox = new Box(BoxLayout.X_AXIS);
		iBox.add(client);
		iBox.add(Box.createHorizontalStrut(20));
		iBox.add(address);
		iBox.setBorder(BorderFactory.createTitledBorder("Information"));
		
		Box mHolder = new Box(BoxLayout.Y_AXIS);
		mHolder.add(new GraphZoomScrollPane(vv));
		mHolder.add(Box.createVerticalStrut(8));
		mHolder.add(iBox);
		mHolder.add(Box.createVerticalStrut(8));
		mHolder.add(controls);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mHolder);
		frame.pack();
		frame.setVisible(true);
		
		final OWLClassHierarchyGraphBuilder treeBuilder = new OWLClassHierarchyGraphBuilder();
		switch(CLIENT) {
		case 0: {
			onto.getOWLOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
				@Override
				public void ontologiesChanged(List<? extends OWLOntologyChange> changes)
						throws OWLException {
					OWLClass thing = onto.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
					OWLClassHierarchyGraph graph = 
						treeBuilder.buildGraph(Collections.singleton(thing), TREE_DEPTH, null, onto);
					vv.setGraphLayout(vvFac.createFRLayout(graph));
					vv.repaint();
				}
			});
			break;
		}
		case 1: {
				onto.getOWLOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
					@Override
					public void ontologiesChanged(List<? extends OWLOntologyChange> changes)
							throws OWLException {
						OWLClass thing = onto.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
						OWLClassHierarchyGraph graph = 
							treeBuilder.buildGraph(Collections.singleton(thing), TREE_DEPTH, null, onto);
						vv.setGraphLayout(vvFac.createFRLayout(graph));
						vv.repaint();
					}
				});
				break;
			}
		default:
			break;
		}
		
		// Add OWL:Thing
		graph.addVertex(new OWLClassNode(owlDFac.getOWLThing()));
		vv.repaint();
		
		this.frame = frame;
		
//		graph.addVertex(new OWLClassNode((OWLClass) null));
//		graph.addVertex(new OWLClassNode((OWLClass) null));
//		graph.addVertex(new OWLClassNode((OWLClass) null));
//		graph.addVertex(new OWLClassNode((OWLClass) null));
		
//		PrefuseGraph<OWLClassNode, OWLClassRelation> jg = 
//			PrefuseConverter.getPrefuseGraph(graph);
//		show(jg);
		
//		PApplet.main(new String[] { "--present", "de.fzi.aoide.replica.demo.MyProcessingSketch" });
		
		return vv;
	}
	
	private Color getClientColor(int client) {
		switch(client) {
		case 0:
			return Color.RED;
		case 1:
			return Color.GREEN;
		default:
			return null;
		}
	}
	
	private Color getNodeColor(OWLClass c, OWLOntology o) {
		OWLAnnotationProperty fc = OWLManager.getOWLDataFactory().
			getOWLAnnotationProperty(IRI.create("fromClient"));
		for(OWLAnnotationAssertionAxiom a : o.getAnnotationAssertionAxioms(c.getIRI())) {
//			System.out.println("assertionAxiom subject="+a.getSubject()+
//					", value="+((OWLLiteral) a.getValue()).getLiteral());
			if(fc.equals(a.getProperty())) {
				String l = ((OWLLiteral) a.getValue()).getLiteral();
				if("0".equals(l)) {
					return Color.RED;
				} else if("1".equals(l)) {
					return Color.GREEN;
				}
			}
		}
		return null;
	}
	
	private OWLAnnotationAssertionAxiom createOWLAnnotationAxiomOf(OWLAnnotationSubject s,
			OWLOntology onto) {
		OWLDataFactory fac = OWLManager.getOWLDataFactory();
		OWLAnnotationProperty fC = fac.getOWLAnnotationProperty(IRI.create("fromClient"));
		OWLAnnotation a = fac.getOWLAnnotation(fC, fac.getOWLLiteral(CLIENT));
		return fac.getOWLAnnotationAssertionAxiom(s, a);
	}
	
    private JLabel createColoredLabel(String text,
                                      Color color,
                                      Point origin) {
        JLabel label = new JLabel(text);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.black);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setBounds(origin.x, origin.y, 50, 250);
        return label;
    }
	
//	 protected void show(JungGraph jg) {
//		// create a new item registry
//		//  the item registry stores all the visual
//		//  representations of different graph elements
//		ItemRegistry registry  = new ItemRegistry(jg);
//		
//		// create a new display component to show the data
//		Display display = new Display(registry);
//		display.setSize(400,400);
//		// lets users drag nodes around on screen
//		display.addControlListener(new DragControl());
//		
//		// create a new action list that
//		// (a) filters visual representations from the original graph
//		// (b) performs a random layout of graph nodes
//		// (c) calls repaint on displays so that we can see the result
//		ActionList actions = new ActionList(registry, -1, 15);
//		actions.add(new GraphFilter());
//		actions.add(new ForceDirectedLayout(true));
//		actions.add(new RepaintAction());
//		
//		frame.getContentPane().add(display);
//        
//		// now execute the actions to visualize the graph
//		actions.runNow();
//    }
	 
//	protected void show(PrefuseGraph g1) {
//		Table table = new Table();
//		table.addColumn("blub", String.class);
//		Graph g = new Graph();
////		Graph g = null;
////		try {
////			g = new GraphMLReader().readGraph("socialnet.xml");
////		} catch (DataIOException e) {
////			e.printStackTrace();
////		}
//		
//		
////		Node n0 = g.addNode();
//////		n0.setString("blub", "blub1");
////		Node n1 = g.addNode();
//////		n1.setString("blub", "blub2");
////		g.addEdge(n0, n1);
//		
//		// add the graph to the visualization as the data group "graph"
//		// nodes and edges are accessible as "graph.nodes" and "graph.edges"
//		Visualization vis = new Visualization();
//		vis.addGraph("graph", g);
//		
//		
//		// draw the "name" label for NodeItems
//		LabelRenderer r = new LabelRenderer("name");
//		r.setRoundedCorner(8, 8); // round the corners
//
//		// create a new default renderer factory
//		// return our name label renderer as the default for all non-EdgeItems
//		// includes straight line edges for EdgeItems by default
//		vis.setRendererFactory(new DefaultRendererFactory(r));
//		// create our nominal color palette
//		// pink for females, baby blue for males
//		int[] palette = new int[] {
//		    ColorLib.rgb(255,180,180), ColorLib.rgb(190,190,255)
//		};
//		
//		
//		// map nominal data values to colors using our provided palette
//		DataColorAction fill = new DataColorAction("graph.nodes", "gender",
//		    Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
//		// use black for node text
//		ColorAction text = new ColorAction("graph.nodes",
//		    VisualItem.TEXTCOLOR, ColorLib.gray(0));
//		// use light grey for edges
//		ColorAction edges = new ColorAction("graph.edges",
//		    VisualItem.STROKECOLOR, ColorLib.gray(200));
//			
//		// create an action list containing all color assignments
//		ActionList color = new ActionList();
//		color.add(fill);
//		color.add(text);
//		color.add(edges);
//		
//		
//		// create an action list with an animated layout
//		// the INFINITY parameter tells the action list to run indefinitely
//		ActionList layout = new ActionList(Activity.INFINITY);
//		layout.add(new ForceDirectedLayout("graph"));
//		layout.add(new RepaintAction());
//		
//		
//		// add the actions to the visualization
//		vis.putAction("color", color);
//		vis.putAction("layout", layout);
//		
//		
//		// create a new Display that pull from our Visualization
//		Display display = new Display(vis);
//		display.setSize(720, 500); // set display size
//		display.addControlListener(new DragControl()); // drag items around
//		display.addControlListener(new PanControl());  // pan with background left-drag
//		display.addControlListener(new ZoomControl()); // zoom with vertical right-drag
//		
//		// create a new window to hold the visualization
//		JFrame frame = new JFrame("prefuse example");
//		// ensure application exits when window is closed
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.add(display);
//		frame.pack();           // layout components in window
//		frame.setVisible(true); // show the window
//
//		vis.run("color");  // assign the colors
//		vis.run("layout"); // start up the animated layout
//		vis.run("repaint");
//	}
	
	private OWLOntology setUpServerAndClient(String clientId)
			throws InterruptedException, OWLOntologyCreationException,
			SharedObjectAddException, ContainerCreateException, ContainerConnectException {
		DefaultCommManagerFactory comFac = new DefaultCommManagerFactory();
		
		// Create a server which will live on client0
		if(CLIENT == 0) {	
			server = new DefaultCommManagerFactory().createCommManager();
			Properties configServer = new Properties();
			configServer.put("containerType", "ecf.generic.server");
			configServer.put("containerID", "ecftcp://"+SERVER_HOST+":"+SERVER_PORT+"/server");
			Connection conSrv = server.createConnection(configServer);
			conSrv.initialize();
			conSrv.connect();
		}
		
		// Create two clients, Interface not finished yet
		CommManager client = comFac.createCommManager();
		
		// Configuration for client 0
		Properties configClient = new Properties();
		configClient.put("containerType", "ecf.generic.client");
		configClient.put("containerID", "client"+CLIENT);
		configClient.put("targetID", "ecftcp://"+SERVER_HOST+":"+SERVER_PORT+"/server");
		
		client.setConfiguration(configClient);
		client.initialize();
		
		System.out.println("Connecting...");
		Connection connection = client.createConnection(configClient);
		connection.initialize();
		connection.connect();
		System.out.println("Connection established");
		
		// Use same ID for the ontology ID and the shared object ID
		String ontoID = "replica://myontology.org/";
		ID sharedObjID = IDFactory.getDefault().createStringID(ontoID);
		
		OWLOntology onto = null;
		switch(CLIENT) {
		case 0: {
			onto = OWLReplicaManager.createOWLOntologyManager().
				createOntology(IRI.create(ontoID));
			JOptionPane.showMessageDialog(frame, "Click OK when client1 has connected");
			connection.getSharedObjectContainer().getSharedObjectManager().
				addSharedObject(sharedObjID, (ISharedObject) onto, null);
			break;
			}
		case 1: {
			JOptionPane.showMessageDialog(frame, "Click OK when client0 has continued");
			onto = (OWLOntology) connection.getSharedObjectContainer().
				getSharedObjectManager().getSharedObject(sharedObjID);
			if(onto == null) {
				throw new RuntimeException("Could not retrieve shared ontology!");
			}
			break;
			}
		default:
			break;
		}
		
		return onto;
	}
	
	private List<OWLOntologyChange> syncOntoFromGraph(OWLClassHierarchyGraph graph,
			OWLOntology onto) {
		System.out.println("syncOntoFromGraph(..) graph.getVertices()="+graph.getVertices());
		OWLClassHierarchyFromOWLClassHierarchyGraphBuilder builder = 
			new OWLClassHierarchyFromOWLClassHierarchyGraphBuilder();
		List<OWLOntologyChange> changes = null;
		OWLClass thing = onto.getOWLOntologyManager().getOWLDataFactory().getOWLThing();
		changes = builder.buildHierarchy(graph, thing, onto);
		System.out.println("changes="+changes);
		onto.getOWLOntologyManager().applyChanges(changes);
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
				Connection client,
				OWLOntology onto) {
			this.client = client;
			this.onto = onto;
		}
		Connection client;
		OWLOntology onto;
	}
	
}
