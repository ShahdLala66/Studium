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
	private final List<V> cycle = new LinkedList<>(); // cycle

    /**
	 * Führt eine Tiefensuche für g durch und prüft dabei auf Zyklen.
	 * Falls ein Zyklus erkannt wird, wird die Suche abgebrochen.
	 * @param g gerichteter Graph.
	 */
	public DirectedCycle(DirectedGraph<V> g) {
        Set<V> visited = new HashSet<>();
		// statt stack und set für nodeinpath und path
		LinkedHashSet<V> path = new LinkedHashSet<>();

		for (V v : g.getVertexSet()) {
			if (!visited.contains(v)) {
				searchDirectedCycle(v, g, visited, path);
			}
		}
	}

	private void searchDirectedCycle(V v, DirectedGraph<V> g, Set<V> visited, LinkedHashSet<V> path) {
		visited.add(v);
		path.add(v);

		// iterieren über alle knoten
		for (V w : g.getSuccessorVertexSet(v)) {
			// wenn zyklus gefunden, dann abbrechen
			if (hasCycle()) {
				return;
			} else if (!visited.contains(w)) {
				// wenn nicht besucht, dann rekursiv
				searchDirectedCycle(w, g, visited, path);
			} else if (path.contains(w)) {
				// wenn besucht und im pfad, dann zyklus gefunden
				boolean isCycleStart = false;
				for (V node : path) {
					if (node.equals(w)) {
						isCycleStart = true;
					}
					if (isCycleStart) {
						cycle.add(node);
					}
				}
				cycle.add(w);
				return;
			}
		}

		path.remove(v);
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
