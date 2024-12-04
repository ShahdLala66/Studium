package Aufgabe2.aufgabe2.graph;

import java.util.*;

public class DirectedCycle<V> {
	private final List<V> cycle = new LinkedList<>(); // a cycle, if present
	private final DirectedGraph<V> myGraph;

	public DirectedCycle(DirectedGraph<V> g) {
		myGraph = g;
		Set<V> visited = new HashSet<>();
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

		for (V w : g.getSuccessorVertexSet(v)) {
			if (hasCycle()) {
				return;
			} else if (!visited.contains(w)) {
				searchDirectedCycle(w, g, visited, path);
			} else if (path.contains(w)) {
				System.out.println("Cycle detected");
				// Construct the cycle using LinkedHashSet
				boolean isCycleStart = false;
				for (V node : path) {
					if (node.equals(w)) {
						isCycleStart = true;
					}
					if (isCycleStart) {
						cycle.add(node);
					}
				}
				cycle.add(w); // Close the cycle
				return;
			}
		}

		path.remove(v);
	}

	public List<V> getCycle() {
		return cycle.isEmpty() ? null : Collections.unmodifiableList(cycle);
	}

	public boolean hasCycle() {
		return !cycle.isEmpty();
	}

	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1, 2);
		g.addEdge(2, 5);
		g.addEdge(5, 1);
		g.addEdge(2, 6);
		g.addEdge(3, 7);
		g.addEdge(4, 3);
		g.addEdge(4, 6);
		g.addEdge(7, 4);

		DirectedCycle<Integer> dc = new DirectedCycle<>(g);
		System.out.println(dc.hasCycle());
		System.out.println(dc.getCycle());
	}
}
