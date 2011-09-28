//package de.fzi.aoide.replica.demo;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import prefuse.data.tuple.TableNode;
//import edu.uci.ics.jung.graph.Graph;
//
//public class PrefuseConverter<V,E> {
//
//	public static class PrefuseGraph<V,E> extends prefuse.data.Graph {
//
//		public final Graph<V,E> jungGraph;
//
//		public PrefuseGraph(Graph<V,E> g, boolean directed) {
//			super(directed);
//			this.jungGraph = g;
//		}
//
//	}
//
//	public static class JungNode<V> extends TableNode {
//
//		public JungNode(V v) {
//			super();
//			this.jungVertex = v;
//		}
//
//		public final V jungVertex;
//	}
//
//	public static <V,E> PrefuseGraph<V,E> getPrefuseGraph(Graph<V,E> jungGraph) {		
//		Map<V,JungNode<V>> jungToPrefuseVertices = new HashMap<V,JungNode<V>>();
//		Collection<V> jungVertices = jungGraph.getVertices();
//		for (Iterator<V> iter = jungVertices.iterator(); iter.hasNext();) {
//			V v = (V) iter.next();
//			jungToPrefuseVertices.put(v, new JungNode<V>(v));
//		}
//		PrefuseGraph<V,E> g = new PrefuseGraph<V,E>(jungGraph, true);
//		for(Iterator<E> iter = jungGraph.getEdges().iterator(); iter.hasNext();) {
//			E e = (E) iter.next();
//			JungNode<V> jn0 = (JungNode<V>) jungToPrefuseVertices.get(
//					jungGraph.getEndpoints(e).getFirst());
//			JungNode<V> jn1 = (JungNode<V>) jungToPrefuseVertices.get(
//					jungGraph.getEndpoints(e).getSecond());
//			g.addEdge(jn0, jn1);
//		}
//		return g;
//	}
//
//}
