// O. Bittel;
// 22.02.2017

package Aufgabe2.aufgabe2.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class StrongComponents<V> {
	// comp speichert jede Komponente die zughörigen Knoten.
	private final Map<Integer,Set<V>> comp = new TreeMap<>();

	// Anzahl der Komponenten:
	private int numberOfComp = 0;

	/**
	 * Ermittelt alle strengen Komponenten mit
	 * dem Kosaraju-Sharir Algorithmus.
	 * @param g gerichteter Graph.
	 */
	public StrongComponents(DirectedGraph<V> g) {
		// ...
		//a)
		DepthFirstOrder<V> p = new DepthFirstOrder<>(g);
		List<V> pinv = new LinkedList<>(p.postOrder());
		Collections.reverse(pinv);//funkzioniert


		//b)
		DirectedGraph<V> ginv = g.invert();//funkzioniert


		//c)
		int n = 0;
		Set<V> besucht = new TreeSet<>();
		for(V v : pinv){
			if(!besucht.contains(v)){
				Set<V> value = new TreeSet<>();
				visitDF(v,ginv, value, besucht);
				comp.put(n,value);
				numberOfComp++;
				n++;

			}
		}
	}
	private void visitDF(V v, DirectedGraph<V> g,Set<V> value,Set<V> besucht){
		besucht.add(v);
		value.add(v);


		for(V w : g.getSuccessorVertexSet(v)){

			if(!besucht.contains(w)){
				visitDF(w,g,value,besucht);
			}
		}


	}

	/**
	 *
	 * @return Anzahl der strengen Komponeneten.
	 */
	public int numberOfComp() {
		return numberOfComp;
	}

	@Override
	public String toString() {
		//To change body of generated methods, choose Tools | Templates.
		StringBuilder result = new StringBuilder();
		for (Map.Entry<Integer, Set<V>> entry : comp.entrySet()){
			result.append("Component ").append(entry.getKey()).append(": ");
			for (V element : entry.getValue()){
				result.append(element).append(", ");

			}
			result.append("\n");
		}
		return result.toString();
	}


	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(1,3);
		g.addEdge(2,1);
		g.addEdge(2,3);
		g.addEdge(3,1);

		g.addEdge(1,4);
		g.addEdge(5,4);

		g.addEdge(5,7);
		g.addEdge(6,5);
		g.addEdge(7,6);

		g.addEdge(7,8);
		g.addEdge(8,2);

		StrongComponents<Integer> sc = new StrongComponents<>(g);

		System.out.println(sc.numberOfComp());  // 4

		System.out.println(sc);
		// Component 0: 5, 6, 7,
		// Component 1: 8,
		// Component 2: 1, 2, 3,
		// Component 3: 4,
	}
}