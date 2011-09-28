//import groovy.util.BuilderSupport
//import prefuse.Constants
//import prefuse.Display
//import prefuse.Visualization
//import prefuse.action.ActionList
//import prefuse.action.RepaintAction
//import prefuse.action.assignment.ColorAction
//import prefuse.action.assignment.DataColorAction
//import prefuse.action.layout.graph.ForceDirectedLayout
//import prefuse.activity.Activity
//import prefuse.controls.DragControl
//import prefuse.controls.PanControl
//import prefuse.controls.ZoomControl
//import prefuse.data.Graph
//import prefuse.data.Node
//import prefuse.render.DefaultRendererFactory
//import prefuse.render.LabelRenderer
//import prefuse.util.ColorLib
//import prefuse.visual.VisualItem
//
//class PrefuseBuilder extends BuilderSupport {
//	
//	def graph
//	def visualization
//	
//	void setParent(Object parent, Object child) {
//		if (parent instanceof Node && child instanceof Node) {
//			graph.addEdge(parent, child)
//		}
//	}
//		
//	Object createNode(Object name) {
//		return createNode(name, null, null)
//	}
//	
//	Object createNode(Object name, Object value) {
//		return createNode(name, null, value)
//	}
//	
//	Object createNode(Object name, Map attributes) {
//		return createNode(name, attributes, null)
//	}
//	
//	Object createNode(Object name, Map attributes, Object value) {
//		def node = null
//		if (name == 'node') {
//			node = graph.addNode()
//			node.setString("name", value)
//			visualization.run("color")
//		}
//		if (name == 'graph') {
//			graph = new Graph()
//			graph.addColumn("name", String.class)
//			visualization = new Visualization()
//			visualization.add("graph", graph)
//			def labelRenderer = new LabelRenderer("name")
//			labelRenderer.setRoundedCorner(8, 8)
//			visualization.setRendererFactory(new DefaultRendererFactory(labelRenderer))
//			def fill = new ColorAction("graph.nodes", VisualItem.FILLCOLOR, ColorLib.rgb(190,190,255))
//			def text = new ColorAction("graph.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0))
//			def edges = new ColorAction("graph.edges", VisualItem.STROKECOLOR, ColorLib.gray(200))
//			def color = new ActionList()
//			color.add(fill)
//			color.add(text)
//			color.add(edges)
//			def layout = new ActionList(Activity.INFINITY)
//			layout.add(new ForceDirectedLayout("graph", true))
//			layout.add(new RepaintAction())
//			visualization.putAction("color", color)
//			visualization.putAction("layout", layout)
//			def display = new Display(visualization)
//			display.setSize(720, 500)
//			display.addControlListener(new DragControl())
//			display.addControlListener(new PanControl())
//			display.addControlListener(new ZoomControl())
//			display.setHighQuality(true)
//			visualization.run("color")
//			visualization.run("layout")
//			node = display
//		}
//		return node
//	}
//	
//}