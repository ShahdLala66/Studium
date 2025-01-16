import java.util.*;

public class TelNet {
    private final int lbg;                  // Leistungsbegrenzungswert
    private final Map<TelKnoten, Integer> nodeIds;
    private int nextId;
    private final List<TelVerbindung> minSpanningTree;

    public TelNet(int lbg) {
        this.lbg = lbg;
        this.nodeIds = new HashMap<>();
        this.nextId = 0;
        this.minSpanningTree = new ArrayList<>();
    }


    /**
     * Fügt einen neuen Telefonknoten mit Koordinate (x,y) dazu.
     * @param x - x-Koordinate.
     * @param y - y-Koordinate.
     * @return true, falls die Koordinate neu ist, sonst false.
     */
    public boolean addTelKnoten(int x, int y) {
        TelKnoten newNode = new TelKnoten(x, y);
        if (!nodeIds.containsKey(newNode)) {
            nodeIds.put(newNode, nextId++);
            return true;
        }
        return false;
    }


    /**
     * Berechnet ein optimales Telefonnetz als minimal aufspannenden Baum mit dem Algorithmus von Kruskal.
     * @return true, falls es einen minimal aufspannenden Baum gibt, sonst false.
     */
    public boolean computeOptTelNet() {
        Set<Integer> nodes = new HashSet<>(nodeIds.values());
        UnionFind<Integer> unionFind = new UnionFind<>(nodes);

        // alle möglichen Verbindungen in PQ mit Comparator
        PriorityQueue<TelVerbindung> edges = new PriorityQueue<>(
                Comparator.comparingInt(e -> e.c())
        );

        // alle möglichen Verbindungen aka <= lbg speichern
        for (TelKnoten node1 : nodeIds.keySet()) {
            for (TelKnoten node2 : nodeIds.keySet()) {
                if (node1 == node2) continue;

                // Manhattan-Distanz
                int cost = Math.abs(node1.x() - node2.x()) + Math.abs(node1.y() - node2.y());

                if (cost <= lbg) {
                    edges.add(new TelVerbindung(node1, node2, cost));
                }
            }
        }

        // Kruskal
        while (!edges.isEmpty() && minSpanningTree.size() < nodeIds.size() - 1) {   // solange es noch Kanten gibt und nicht alle Knoten verbunden sind
            TelVerbindung edge = edges.poll();  // kürzeste Verbindung
            int id1 = nodeIds.get(edge.anfang());
            int id2 = nodeIds.get(edge.ende());

            if (!unionFind.find(id1).equals(unionFind.find(id2))) { // wenn nicht in der gleichen Menge
                unionFind.union(id1, id2);                          // vereinigen
                minSpanningTree.add(edge);
            }
        }

        return unionFind.size() == 1;
    }


    /**
     * Fügt n zufällige Telefonknoten zum Netz dazu mit x-Koordinate aus [0,xMax] und y-Koordinate aus [0,yMax].
     *
     * @param n - Anzahl Telefonknoten
     * @param xMax - Intervallgrenz für x-Koordinate.
     * @param yMax - Intervallgrenz für y-Koordinate.
     */
    public void generateRandomTelNet(int n, int xMax, int yMax) {
        Random rand = new Random();
        int added = 0;

        while (added < n) {
            int x = rand.nextInt(xMax + 1);
            int y = rand.nextInt(yMax + 1);
            if (addTelKnoten(x, y)) {
                added++;
            }
        }
    }


    /**
     * Zeichnet das gefundene optimale Telefonnetz mit der Größe xMax*yMax in ein Fenster.
     *
     * @param xMax - Maximale x-Größe.
     * @param yMax - Maximale y-Größe.
     * @param drawGrid - Gitter zeichnen.
     * @throws IllegalStateException - falls nicht zuvor computeOptTelNet() erfolgreich durchgeführt wurde.
     */
    public void drawOptTelNet(int xMax, int yMax, boolean drawGrid) throws IllegalStateException {
        if (minSpanningTree.isEmpty()) {
            throw new IllegalStateException("computeOptTelNet() first");
        }

        StdDraw.setCanvasSize(512, 512);
        StdDraw.setXscale(0, xMax);
        StdDraw.setYscale(0, yMax);

        if (drawGrid) {
            StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            for (int i = 0; i <= xMax; i++) {
                StdDraw.line(i, 0, i, yMax);
            }
            for (int i = 0; i <= yMax; i++) {
                StdDraw.line(0, i, xMax, i);
            }
        }

        StdDraw.setPenColor(StdDraw.BLUE);
        for (TelKnoten node : nodeIds.keySet()) {
            StdDraw.filledSquare(node.x() - 0.5, node.y() - 0.5, 0.5);
            StdDraw.setPenColor(StdDraw.RED);

            StdDraw.filledCircle(node.x() - 0.5, node.y() - 0.5, 0.1);
            StdDraw.setPenColor(StdDraw.BLUE);

        }

        StdDraw.setPenColor(StdDraw.RED);
        for (TelVerbindung edge : minSpanningTree) {
            TelKnoten start = edge.anfang();
            TelKnoten end = edge.ende();

            StdDraw.line(start.x() - 0.5, start.y() - 0.5, start.x() - 0.5, end.y() - 0.5);
            StdDraw.line(start.x() - 0.5, end.y() - 0.5, end.x() - 0.5, end.y() - 0.5);

            StdDraw.setPenColor(StdDraw.BLACK);
            if (drawGrid)
                StdDraw.text((double) (start.x() + end.x()) / 2 - 0.5, (double) (start.y() + end.y()) / 2 - 0.5, Integer.toString(edge.c()));
            StdDraw.setPenColor(StdDraw.RED);
        }
        StdDraw.show();
    }


    /**
     * Liefert ein optimales Telefonnetz als Liste von Telefonverbindungen zurück.
     *
     * @return Liste von Telefonverbindungen.
     * @throws IllegalStateException - falls nicht zuvor computeOptTelNet() erfolgreich durchgeführt wurde.
     */
    public List<TelVerbindung> getOptTelNet() throws IllegalStateException {
        if (minSpanningTree.isEmpty()) {
            throw new IllegalStateException("computeOptTelNet() first");
        }
        return new ArrayList<>(minSpanningTree);
    }


    /**
     * Liefert die Gesamtkosten eines optimalen Telefonnetzes zurück.
     * @return Gesamtkosten eines optimalen Telefonnetzes.
     * @throws IllegalStateException - falls nicht zuvor computeOptTelNet() erfolgreich durchgeführt wurde.
     */
    public int getOptTelNetKosten() throws IllegalStateException {
        if (minSpanningTree.isEmpty()) {
            throw new IllegalStateException("computeOptTelNet() first");
        }
        return minSpanningTree.stream()
                .mapToInt(TelVerbindung::c)
                .sum();
    }

    public int size() {
        return nodeIds.size();
    }

    private static void a3() {
        TelNet telNet = new TelNet(7);

        telNet.addTelKnoten(1, 1);
        telNet.addTelKnoten(3, 1);
        telNet.addTelKnoten(4, 2);
        telNet.addTelKnoten(3, 4);
        telNet.addTelKnoten(2, 6);
        telNet.addTelKnoten(4, 7);
        telNet.addTelKnoten(7, 6);

        boolean success = telNet.computeOptTelNet();

        System.out.println("Netzwerk erfolgreich erstellt: " + success);
        System.out.println("Anzahl der Knoten: " + telNet.size());
        System.out.println("Gesamtkosten: " + telNet.getOptTelNetKosten());

        System.out.println("\nVerbindungen im minimalen Spannbaum:");
        for (TelVerbindung connection : telNet.getOptTelNet()) {
            System.out.printf("(%d,%d) zu (%d,%d) mit Kosten %d%n",
                    connection.anfang().x(), connection.anfang().y(),
                    connection.ende().x(), connection.ende().y(),
                    connection.c());
        }

        telNet.drawOptTelNet(7, 7, true);
    }

    private static void a4() {
        int n = 1000;
        int xMax = 1000;
        int yMax = 1000;
        int lbg = 100;

        TelNet telNet = new TelNet(lbg);

        telNet.generateRandomTelNet(n, xMax, yMax);

        boolean success = telNet.computeOptTelNet();

        System.out.println("Netzwerk erfolgreich erstellt: " + success);
        System.out.println("Anzahl der Knoten: " + telNet.size());
        System.out.println("Gesamtkosten: " + telNet.getOptTelNetKosten());

        telNet.drawOptTelNet(xMax, yMax, false);
    }

    public static void main(String[] args) {
        a3();
    }
}