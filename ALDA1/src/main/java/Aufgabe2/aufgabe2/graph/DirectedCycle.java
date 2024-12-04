package Aufgabe2.aufgabe2.graph;

import java.util.*;

public class DirectedCycle<V> {
	private final List<V> cycle = new LinkedList<>(); // a cycle, if present
	private final DirectedGraph<V> myGraph;

	public DirectedCycle(DirectedGraph<V> g) {
		myGraph = g;
		Set<V> visited = new HashSet<>();
		Stack<V> path = new Stack<>();
		Set<V> nodeInPath = new HashSet<>();

		for (V v : g.getVertexSet()) {
			if (!visited.contains(v)) {
				searchDirectedCycle(v, g, visited, path, nodeInPath);
			}
		}
	}

	private void searchDirectedCycle(V v, DirectedGraph<V> g, Set<V> visited, Stack<V> path, Set<V> nodeInPath) {
		visited.add(v);
		path.push(v);
		nodeInPath.add(v);

		for (V w : g.getSuccessorVertexSet(v)) {
			if (hasCycle()) {
				return;
			} else if (!visited.contains(w)) {
				searchDirectedCycle(w, g, visited, path, nodeInPath);
			} else if (nodeInPath.contains(w)) {
				// Construct the cycle using the stack
				while (!path.isEmpty() && !path.peek().equals(w)) {
					cycle.add(path.pop());
				}
				cycle.add(w); // Add the detected node (start of cycle)
				Collections.reverse(cycle); // Reverse the cycle
				cycle.add(w); // Add the start node again to close the loop
				return;
			}
		}

		path.pop();
		nodeInPath.remove(v);
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
