// O. Bittel;
// 2.8.2023

package main.java.Aufgabe2.aufgabe2.graph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;


/**
 * Klasse zur Analyse von Web-Sites.
 *
 * @author Oliver Bittel
 * @since 30.10.2023
 */
public class AnalyzeWebSite {
    public static void main(String[] args) throws IOException {
        // Graph aus Website erstellen und ausgeben:
        //DirectedGraph<String> webSiteGraph = buildGraphFromWebSite("src/main/java/Aufgabe2/aufgabe2/data/WebSiteKlein");
        DirectedGraph<String> webSiteGraph = buildGraphFromWebSite("src/main/java/Aufgabe2/aufgabe2/data/WebSiteGross");
        System.out.println("Anzahl Seiten: \t" + webSiteGraph.getNumberOfVertexes());
        System.out.println("Anzahl Links: \t" + webSiteGraph.getNumberOfEdges());
        //System.out.println(webSiteGraph);

        // Starke Zusammenhangskomponenten berechnen und ausgeben
        StrongComponents<String> sc = new StrongComponents<>(webSiteGraph);
        System.out.println("Anzahl starke Zusammenhangskomponenten: " + sc.numberOfComp());
        //System.out.println(sc);

        // Page Rank ermitteln und Top-100 ausgeben
        pageRank(webSiteGraph);
    }

    /**
     * Liest aus dem Verzeichnis dirName alle Web-Seiten und
     * baut aus den Links einen gerichteten Graphen.
     *
     * @param dirName Name eines Verzeichnisses
     * @return gerichteter Graph mit Namen der Web-Seiten als Knoten und Links als gerichtete Kanten.
     */
    private static DirectedGraph buildGraphFromWebSite(String dirName) throws IOException {
        File webSite = new File(dirName);
        DirectedGraph<String> webSiteGraph = new AdjacencyListDirectedGraph();

        for (File f : webSite.listFiles()) {
            String from = f.getName();
            LineNumberReader in = new LineNumberReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("href")) {
                    String[] s_arr = line.split("\"");
                    String to = s_arr[1];
                    webSiteGraph.addEdge(from, to);
                }
            }
        }
        return webSiteGraph;
    }

    /**
     * pageRank ermittelt Gewichte (Ranks) von Web-Seiten
     * aufgrund ihrer Link-Struktur und gibt sie aus.
     *
     * @param g gerichteter Graph mit Web-Seiten als Knoten und Links als Kanten.
     */
    private static <V> void pageRank(DirectedGraph<V> g) {
        int nI = 10;            //number of iterations
        double alpha = 0.5;

        // Definiere und initialisiere rankTable:
        Map<V, Double> rankTable = new HashMap<>();
        for (V vertex : g.getVertexSet()) {
            rankTable.put(vertex, 1.0);
        }

        // Iteration:
        for (int iteration = 0; iteration < nI; iteration++) {
            Map<V, Double> newRankTable = new HashMap<>();
            double maxDelta = 0.0; // Unterschied zur Konvergenzkontrolle

            for (V vertex : g.getVertexSet()) {
                double rankSum = 0.0;

                // Berechne den Rank-Beitrag der Vorgänger
                for (V predecessor : g.getPredecessorVertexSet(vertex)) {
                    rankSum += rankTable.get(predecessor) / g.getOutDegree(predecessor);
                }

                // Aktualisiere den Rank für den aktuellen Knoten
                double newRank = (1 - alpha) + alpha * rankSum;
                newRankTable.put(vertex, newRank);

                // Berechne die maximale Änderung (Delta) für die Konvergenzprüfung
                maxDelta = Math.max(maxDelta, Math.abs(newRank - rankTable.get(vertex)));
            }

            // Aktualisiere die Rank-Tabelle
            rankTable = newRankTable;


        }


        // Rank Table ausgeben (nur für data/WebSiteKlein):

        //for (Map.Entry<V, Double> entry : rankTable.entrySet()) {
        //System.out.println("Seite: " + entry.getKey() + ", Rank: " + entry.getValue());
        //}

        // Nach Ranks sortieren Top 100 ausgeben (nur für data/WebSiteGross):

        List<Map.Entry<V, Double>> sortedRanks = rankTable.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .toList();

        System.out.println("\nTop 100 Seiten:");
        int count = 0;
        for (Map.Entry<V, Double> entry : sortedRanks) {
            System.out.println("\t" + entry.getKey() + ", Rank: " + entry.getValue());
            count++;
            if (count >= 100) break;
        }

        // Top-Seite mit ihren Vorgängern und Ranks ausgeben (nur für data/WebSiteGross):
        if (!sortedRanks.isEmpty()) {
            Map.Entry<V, Double> topPage = sortedRanks.get(0);
            V topVertex = topPage.getKey();

            System.out.println("\nTop-Seite: " + topVertex + ", Rank: " + topPage.getValue());
            System.out.println("\nVorgänger:");

            for (V predecessor : g.getPredecessorVertexSet(topVertex)) {
                double predecessorRank = rankTable.get(predecessor);
                System.out.println("\t" + predecessor + ", Rank: " + predecessorRank);
            }
        }
    }
}
