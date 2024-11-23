// O. Bittel;
// 30.7.2024

package main.java.Aufgabe2.aufgabe2.graph;

import java.util.*;

/**
 * Klasse zur Ermittlung von gerichteten Zyklen.
 * @author Oliver Bittel
 * @since 30.7.2024
 * @param <V> Knotentyp.
 */
public class DirectedCycle<V> {
	private final List<V> cycle = new LinkedList<>(); // a cycle, if present
	private final DirectedGraph<V> myGraph;
	private final Set<V> visited = new HashSet<>();
	private final Deque<V> path = new ArrayDeque<>();
	private final Set<V> nodeInPath = new HashSet<>();

	/**
	 * Führt eine Tiefensuche für g durch und prüft dabei auf Zyklen.
	 * Falls ein Zyklus erkannt wird, wird die Suche abgebrochen.
	 * @param g gerichteter Graph.
	 */
	public DirectedCycle(DirectedGraph<V> g) {
		myGraph = g;
		for (V v : g.getVertexSet()) {
			if (!visited.contains(v)) {
				searchDirectedCycle(v);
				if (!cycle.isEmpty()) {
					break;
				}
			}
		}
	}

	private void searchDirectedCycle(V v) {
		visited.add(v);
		path.push(v);
		nodeInPath.add(v);

		for (V w : myGraph.getSuccessorVertexSet(v)) {
			if (!visited.contains(w)) {
				searchDirectedCycle(w);
				if (!cycle.isEmpty()) {
					return;
				}
			} else if (nodeInPath.contains(w)) {
				for (V node : path) {
					cycle.add(node);
					if (node.equals(w)) {
						break;
					}
				}
				cycle.add(w);
				Collections.reverse(cycle);
				return;
			}
		}

		path.pop();
		nodeInPath.remove(v);
	}

	
	/**
	 * Liefert einen Zyklus zurück, falls ein Zyklus vorhanden ist.
	 * @return Zyklus falls vorhanden, sonst null.
	 */
	public List<V> getCycle(){
		return cycle.isEmpty()? null : Collections.unmodifiableList(cycle);
	}
	
	/**
	 * Prüft ob Zyklus vorhanden ist.
	 * @return true, falls Zyklus vorhanden ist, sonst false.
	 */
	public boolean hasCycle(){
		return !cycle.isEmpty();
	}

	
	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(2,5);
		g.addEdge(5,1);
		g.addEdge(2,6);
		g.addEdge(3,7);
		g.addEdge(4,3);
		g.addEdge(4,6);
		g.addEdge(7,4);
		
		DirectedCycle<Integer> dc = new DirectedCycle<>(g);
		System.out.println(dc.hasCycle());
		System.out.println(dc.getCycle());
		
	}
}
