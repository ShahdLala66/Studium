// O. Bittel;
// 22.02.2017

package main.java.Aufgabe2.aufgabe2.graph;

import java.util.*;

/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 22.02.2017
 */
public class StrongComponents<V> {
    private final Map<Integer, Set<V>> comp = new TreeMap<>();
    private int numberOfComp = 0;
    Set<V> visited = new HashSet<>();

    /**
     * Ermittelt alle strengen Komponenten mit
     * dem Kosaraju-Sharir Algorithmus.
     *
     * @param g gerichteter Graph.
     */
    public StrongComponents(DirectedGraph<V> g) {
        List<V> postOrder = reversePostOrder(g);
        DirectedGraph<V> gi = g.invert();

        for (V v : postOrder) {
            // wenn knoten noch nicht besucht dann neue komponente
            // und rekursiv alle knoten in komponente hinzufügen
            if (!visited.contains(v)) {
                comp.put(numberOfComp, new HashSet<>());
                comp.get(numberOfComp).add(v);
                visited.add(v);
                dfsR(gi, v, numberOfComp);
                numberOfComp++;
            }
        }
    }

    // für rekursive Tiefensuche, fügt Knoten zur Komponente hinzu
    public void dfsR(DirectedGraph<V> g, V v, int numberOfComp) {
        for (V w : g.getSuccessorVertexSet(v)) {
            if (!visited.contains(w)) {
                comp.get(numberOfComp).add(w);
                visited.add(w);
                dfsR(g, w, numberOfComp);
            }
        }
    }

    // gibt die umgekehrte Postorder zurück
    private List<V> reversePostOrder(DirectedGraph<V> g) {
        DepthFirstOrder<V> dfo = new DepthFirstOrder<>(g);
        List<V> postOrderList = new LinkedList<>(dfo.postOrder());
        Collections.reverse(postOrderList);
        return postOrderList;
    }

    /**
     * @return Anzahl der strengen Komponenten.
     */
    public int numberOfComp() {
        return numberOfComp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var v : comp.entrySet()) {
            sb.append("Component ").append(v.getKey()).append(": ");
            for (var e : v.getValue())
                sb.append(e.toString()).append(", ");
            sb.append("\n");
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        g.addEdge(1, 4);
        g.addEdge(5, 4);

        g.addEdge(5, 7);
        g.addEdge(6, 5);
        g.addEdge(7, 6);

        g.addEdge(7, 8);
        g.addEdge(8, 2);

        StrongComponents<Integer> sc = new StrongComponents<>(g);

        System.out.println(sc.numberOfComp());  // 4

        System.out.println(sc);
        // Component 0: 5, 6, 7,
        // Component 1: 8,
        // Component 2: 1, 2, 3,
        // Component 3: 4,
    }
}
