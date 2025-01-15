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

    public boolean addTelKnoten(int x, int y) {
        TelKnoten newNode = new TelKnoten(x, y);
        if (!nodeIds.containsKey(newNode)) {
            nodeIds.put(newNode, nextId++);
            return true;
        }
        return false;
    }

    public boolean computeOptTelNet() {
        Set<Integer> nodes = new HashSet<>(nodeIds.values());
        UnionFind<Integer> unionFind = new UnionFind<>(nodes);

        PriorityQueue<TelVerbindung> edges = new PriorityQueue<>(
                Comparator.comparingInt(e -> e.c)
        );

        for (TelKnoten node1 : nodeIds.keySet()) {
            for (TelKnoten node2 : nodeIds.keySet()) {
                if (node1 == node2) continue;

                int cost = calculateDistance(node1, node2);
                if (cost <= lbg) {
                    edges.add(new TelVerbindung(node1, node2, cost));
                }
            }
        }

        minSpanningTree.clear();
        while (!edges.isEmpty() && minSpanningTree.size() < nodeIds.size() - 1) {
            TelVerbindung edge = edges.poll();
            int id1 = nodeIds.get(edge.anfang);
            int id2 = nodeIds.get(edge.ende);

            if (!unionFind.find(id1).equals(unionFind.find(id2))) {
                unionFind.union(id1, id2);
                minSpanningTree.add(edge);
            }
        }

        return unionFind.size() == 1;
    }

    private int calculateDistance(TelKnoten a, TelKnoten b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    public void generateRandomTelNet(int n, int xMax, int yMax) {
        Random rand = new Random();
        int attempts = 0;
        int added = 0;

        while (added < n && attempts < n * 10) {
            int x = rand.nextInt(xMax + 1);
            int y = rand.nextInt(yMax + 1);
            if (addTelKnoten(x, y)) {
                added++;
            }
            attempts++;
        }
    }

    public void drawOptTelNet(int xMax, int yMax, boolean drawGrid) {
        StdDraw.setCanvasSize(2048, 1024);
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
            TelKnoten start = edge.anfang;
            TelKnoten end = edge.ende;
            StdDraw.line(start.x() - 0.5, start.y() - 0.5, start.x() - 0.5, end.y() - 0.5);
            StdDraw.line(start.x() - 0.5, end.y() - 0.5, end.x() - 0.5, end.y() - 0.5);
        }
        StdDraw.show();
    }

    public List<TelVerbindung> getOptTelNet() {
        return new ArrayList<>(minSpanningTree);
    }

    public int getOptTelNetKosten() {
        return minSpanningTree.stream()
                .mapToInt(edge -> edge.c)
                .sum();
    }

    public int size() {
        return nodeIds.size();
    }

    private static void teilA() {
        StdDraw.setPenRadius(0.01);
        StdDraw.line(0.25, 0, 0.75, 0); // Shifted right by 0.25
        StdDraw.line(0.25, 0, 0.5, 0.5); // Shifted right by 0.25
        StdDraw.line(0.5, 0.5, 0.75, 0); // Shifted right by 0.25
        StdDraw.circle(0.18, 0.1, 0.1); // Shifted right by 0.25
        StdDraw.circle(0.82, 0.1, 0.1); // Shifted right by 0.25
    }
    private static void teilB() {
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
        teilA();
    }
}