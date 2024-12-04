// O. Bittel;
// 19.03.2018

package Aufgabe2.aufgabe2.graph;

import java.util.*;

/**
 * Implementierung von DirectedGraph mit einer doppelten TreeMap
 * für die Nachfolgerknoten und einer einer doppelten TreeMap
 * für die Vorgängerknoten.
 * <p>
 * Beachte: V muss vom Typ Comparable&lt;V&gt; sein.
 * <p>
 * Entspicht einer Adjazenzlisten-Implementierung
 * mit schnellem Zugriff auf die Knoten.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 19.03.2018
 */
public class AdjacencyListDirectedGraph<V extends Comparable<V>> implements DirectedGraph<V> {
    private final Map<V, Map<V, Double>> succ = new TreeMap<>();
    private final Map<V, Map<V, Double>> pred = new TreeMap<>();

    private int numberEdge = 0;

    @Override
    public boolean addVertex(V v) {
        if (!pred.containsKey(v)) { //entweder pred oder succ
            succ.put(v, new TreeMap<>());
            pred.put(v, new TreeMap<>());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addEdge(V v, V w, double weight) {
        addVertex(v); // Ensure vertex v is in the graph
        addVertex(w); // Ensure vertex w is in the graph

        if (!succ.get(v).containsKey(w)) {
            succ.get(v).put(w, weight);
            pred.get(w).put(v, weight);
            numberEdge++;
            return true;
        } else {
            if (!succ.get(v).get(w).equals(weight)) {
                succ.get(v).put(w, weight); // Update the weight
                pred.get(w).put(v, weight); // Update the weight
            }
            return false;
        }
    }

    @Override
    public boolean addEdge(V v, V w) {
        return addEdge(v, w, 1.0);
    }

    @Override
    public boolean containsVertex(V v) {
        return succ.containsKey(v);
    }

    @Override
    public boolean containsEdge(V v, V w) {
        if (succ.containsKey(v) && succ.get(v).containsKey(w)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public double getWeight(V v, V w) {
        if (containsEdge(v, w)) {
            return succ.get(v).get(w); //reutrn the weight, succ [v] [w]
        }
        return 0.0;
    }


    @Override
    public int getInDegree(V v) { //number of edges that go into the vertex
        return pred.get(v).size();
    }

    @Override
    public int getOutDegree(V v) {
        return succ.get(v).size(); //number of edges that go out of the vertex VERY IMPORTANT ITS SUCC NOT PRED
    }

    @Override
    public Set<V> getVertexSet() {
        return Collections.unmodifiableSet(succ.keySet()); // nicht modifizierbare Sicht
    }

    @Override
    public Set<V> getPredecessorVertexSet(V v) { //return the set of vertices that are the predecessors of v
        return Collections.unmodifiableSet(pred.get(v).keySet());
    }

    @Override
    public Set<V> getSuccessorVertexSet(V v) { // return the set of vertices that are the successors of v
        if (succ.containsKey(v)) {
            return Collections.unmodifiableSet(succ.get(v).keySet());
        }
        return null;
    }

    @Override
    public int getNumberOfVertexes() {
        return succ.size();
    }

    @Override
    public int getNumberOfEdges() {
        return numberEdge;
    }

    @Override
    public DirectedGraph<V> invert() {
        DirectedGraph<V> a = new AdjacencyListDirectedGraph<>();
        for (V v : getVertexSet()) {
            for (V w : getSuccessorVertexSet(v)) {
                a.addEdge(w, v, getWeight(v, w));
            }
        }
        return a;

    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (V c : getVertexSet()) {
            for (V d : getSuccessorVertexSet(c)) {
                s.append(c).append(" --> ").append(d).append
                        (" weight = ").append(getWeight(c, d)).append("\n");
            }//to do
        }
        return s.toString();
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


        System.out.println(g.getNumberOfVertexes());    // 7
        System.out.println(g.getNumberOfEdges());        // 8
        System.out.println(g.getVertexSet());    // 1, 2, ..., 7
        System.out.println(g);
        // 1 --> 2 weight = 1.0
        // 2 --> 5 weight = 1.0
        // 2 --> 6 weight = 1.0
        // 3 --> 7 weight = 1.0
        // ...

        System.out.println("");
        System.out.println(g.getOutDegree(2));                // 2
        System.out.println(g.getSuccessorVertexSet(2));    // 5, 6
        System.out.println(g.getInDegree(6));                // 2
        System.out.println(g.getPredecessorVertexSet(6));    // 2, 4

        System.out.println("");
        System.out.println(g.containsEdge(1, 2));    // true
        System.out.println(g.containsEdge(2, 1));    // false
        System.out.println(g.getWeight(1, 2));    // 1.0
        g.addEdge(1, 2, 5.0);
        System.out.println(g.getWeight(1, 2));    // 5.0

        System.out.println("");
        System.out.println(g.invert());
        // 1 --> 5 weight = 1.0
        // 2 --> 1 weight = 5.0
        // 3 --> 4 weight = 1.0
        // 4 --> 7 weight = 1.0
        // ...

        Set<Integer> s = new HashSet<>(g.getSuccessorVertexSet(2)); //this is a copy of the set
        System.out.println(s);
        s.remove(5); // This is now allowed, why was it before not allowed? Because the set was unmodifiable
        System.out.println(s); //modifying the set

    }
}
