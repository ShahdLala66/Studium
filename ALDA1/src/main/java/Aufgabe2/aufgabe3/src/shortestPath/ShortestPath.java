// O. Bittel;
// 25.3.2021; jetzt mit IndexMinPq
// 30.06.2024; Anpassung auf ungerichtete Graphen

package shortestPath;

import sim.SYSimulation;
import undirectedGraph.UndirectedGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 30.06.2024
 */
public class ShortestPath<V> {

    SYSimulation sim = null;
    Map<V, Double> dist;            // Distanz für jeden Knoten
    Map<V, V> pred;                 // Vorgänger für jeden Knoten
    IndexMinPQ<V, Double> cand;     // Kandidaten als PriorityQueue PQ
    UndirectedGraph<V> graph;       // Der Graph
    Heuristic<V> heur;              // Heuristik
    private V targetNode;           // Zielknoten


    /**
     * Konstruiert ein Objekt, das im Graph g kürzeste Wege
     * nach dem A*-Verfahren berechnen kann.
     * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
     * Wird h = null gewählt, dann ist das Verfahren identisch
     * mit dem Dijkstra-Verfahren.
     *
     * @param g Gerichteter Graph
     * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
     *          dem Dijkstra-Verfahren gesucht.
     */

    public ShortestPath(UndirectedGraph<V> g, Heuristic<V> h) {
        dist = new HashMap<>();
        pred = new HashMap<>();
        cand = new IndexMinPQ<>();
        this.graph = g;
        this.heur = h;
    }


    /**
     * Diese Methode sollte nur verwendet werden,
     * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
     * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
     * <p>
     * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
     * <blockquote><pre>
     *    if (sim != null)
     *       sim.visitStation((Integer) v, Color.blue);
     * </pre></blockquote>
     *
     * @param sim SYSimulation-Objekt.
     */

    public void setSimulator(SYSimulation sim) {
        this.sim = sim;
    }


    /**
     * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
     * <p>
     * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
     * der als nächstes aus der Kandidatenliste besucht wird, animiert.
     *
     * @param s Startknoten
     * @param g Zielknoten
     */

    public void searchShortestPath(V s, V g) {
        targetNode = g; // Zielknoten speichern
        for (V v : graph.getVertexSet()) {
            dist.put(v, Double.POSITIVE_INFINITY);
            pred.put(v, null);
        }
        dist.put(s, 0.0);

        cand.add(s, 0.0 + (heur != null ? heur.estimatedCost(s, g) : 0.0));

        while (!cand.isEmpty()) {
            V v = cand.getMinKey(); // Knoten mit minimalem dist[v] + h(v, g)
            cand.removeMin();

            System.out.println("Besuche Knoten " + v + " mit d =  " + dist.get(v));

            if (v.equals(g)) {
                return; // Zielknoten erreicht
            }

            for (V w : graph.getNeighborSet(v)) {
                double newDist = dist.get(v) + graph.getWeight(v, w);

                if (dist.get(w) == Double.POSITIVE_INFINITY) {
                    // Knoten w noch nicht besucht
                    pred.put(w, v);
                    dist.put(w, newDist);
                    cand.add(w, newDist + (heur != null ? heur.estimatedCost(w, g) : 0.0));
                } else if (newDist < dist.get(w)) {
                    // Kürzerer Weg gefunden
                    pred.put(w, v);
                    dist.put(w, newDist);
                    cand.change(w, newDist + (heur != null ? heur.estimatedCost(w, g) : 0.0));
                }
            }
        }

        throw new IllegalArgumentException("Kein kürzester Weg gefunden!");
    }


    /**
     * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
     * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
     *
     * @return kürzester Weg als Liste von Knoten.
     * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
     */

    public List<V> getShortestPath() {
        if (targetNode == null || !pred.containsKey(targetNode) || pred.get(targetNode) == null) {
            throw new IllegalArgumentException("Kein kürzester Weg berechnet oder Zielknoten wurde nicht erreicht.");
        }

        LinkedList<V> path = new LinkedList<>();
        V current = targetNode;

        while (current != null) {
            path.addFirst(current);
            current = pred.get(current);
        }

        if (path.isEmpty() || !dist.containsKey(path.getFirst()) || dist.get(path.getFirst()) != 0.0) {
            throw new IllegalArgumentException("Kein gültiger Pfad gefunden.");
        }
        return path;
    }


    /**
     * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
     * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
     *
     * @return Länge eines kürzesten Weges.
     * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
     */
    public double getDistance() {
        if (targetNode == null || !dist.containsKey(targetNode)) {
            throw new IllegalArgumentException("Kein kürzester Weg berechnet.");
        }

        return dist.get(targetNode); // Distanz des Zielknotens zurückgeben
    }
}
