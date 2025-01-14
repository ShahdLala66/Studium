import java.awt.*;
import java.util.*;
import java.util.List;

public class TelNet {
    private final int maxDistance; // Maximum allowed distance between nodes
    private final Map<TelKnoten, Integer> nodeIds; // Maps nodes to their unique IDs
    private int nextId; // Next available ID for nodes
    private final List<TelVerbindung> minSpanningTree; // Stores the minimal spanning tree edges

    public TelNet(int maxDistance) {
        this.maxDistance = maxDistance;
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
        // Create sets for UnionFind
        Set<Integer> nodes = new HashSet<>(nodeIds.values());
        UnionFind<Integer> unionFind = new UnionFind<>(nodes);

        // Create priority queue for edges
        PriorityQueue<TelVerbindung> edges = new PriorityQueue<>(
                Comparator.comparingInt(e -> e.c)
        );

        // Add all valid edges to priority queue
        for (TelKnoten node1 : nodeIds.keySet()) {
            for (TelKnoten node2 : nodeIds.keySet()) {
                if (node1 == node2) continue;

                int cost = calculateDistance(node1, node2);
                if (cost <= maxDistance) {
                    edges.add(new TelVerbindung(node1, node2, cost));
                }
            }
        }

        // Kruskal's algorithm
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

        // Check if we found a valid spanning tree
        return unionFind.size() == 1;
    }

    private int calculateDistance(TelKnoten a, TelKnoten b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
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

    public void drawOptTelNet(int xMax, int yMax) {
        StdDraw.setCanvasSize(512, 512);
        StdDraw.setXscale(0, xMax + 1);
        StdDraw.setYscale(0, yMax + 1);

        // Draw grid
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        for (int i = 0; i <= xMax; i++) {
            StdDraw.line(i, 0, i, yMax);
        }
        for (int i = 0; i <= yMax; i++) {
            StdDraw.line(0, i, xMax, i);
        }

        // Draw edges
        StdDraw.setPenColor(StdDraw.RED);
        for (TelVerbindung edge : minSpanningTree) {
            StdDraw.line(edge.anfang.x, edge.anfang.y, edge.ende.x, edge.ende.y);
        }

        // Draw nodes
        StdDraw.setPenColor(StdDraw.BLUE);
        for (TelKnoten node : nodeIds.keySet()) {
            StdDraw.filledCircle(node.x, node.y, 0.3);
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

    public static void main(String[] args) {
        TelNet telNet = new TelNet(7);

        // Add the 7 nodes from the example image
        // Node coordinates from grid:
        telNet.addTelKnoten(1, 1); // Node at (2,1)
        telNet.addTelKnoten(3, 1); // Node at (1,2)
        telNet.addTelKnoten(4, 2); // Node at (3,2)
        telNet.addTelKnoten(3, 4); // Node at (4,3)
        telNet.addTelKnoten(2, 6); // Node at (6,5)
        telNet.addTelKnoten(4, 7); // Node at (5,6)
        telNet.addTelKnoten(7, 6); // Node at (7,6)

        // Compute optimal network
        boolean success = telNet.computeOptTelNet();

        // Print results
        System.out.println("Network creation successful: " + success);
        System.out.println("Number of nodes: " + telNet.size());
        System.out.println("Total cost: " + telNet.getOptTelNetKosten());

        // Print all connections in the minimal spanning tree
        System.out.println("\nConnections in minimal spanning tree:");
        for (TelVerbindung connection : telNet.getOptTelNet()) {
            System.out.printf("(%d,%d) to (%d,%d) with cost %d%n",
                    connection.anfang.x, connection.anfang.y,
                    connection.ende.x, connection.ende.y,
                    connection.c);
        }

        // Draw the network
        telNet.drawOptTelNet(7, 7);
    }


}