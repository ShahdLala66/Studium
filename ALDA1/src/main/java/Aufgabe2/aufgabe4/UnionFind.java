import java.util.HashMap;
import java.util.Set;

public class UnionFind<T> {

    private final HashMap<T, T> parent;
    private final HashMap<T, Integer> rank;
    private int size;

    /**
     * Legt eine neue Union-Find-Struktur mit allen 1-elementigen Teilmengen von s an.
     * s ist die Grundmenge der Partionierung.
     * @param s - Grundmenge
     */
    public UnionFind(Set<T> s) {
        parent = new HashMap<>();
        rank = new HashMap<>();
        for (T e : s) {
            parent.put(e, e);
            rank.put(e, 0);
        }
        size = s.size();
    }

    /**
     * Liefert den Repräsentanten der Menge zurück, zu der e gehört.
     * @param e - Element
     * @return Repräsentant der Menge, zu der e gehört.
     */
    public T find(T e) {
        if (!parent.get(e).equals(e)) {             // ist das e der repräsentant?
            parent.put(e, find(parent.get(e)));     // wenn nein rekuriv nach oben gehen
        }
        return parent.get(e);
    }


    /**
     * Vereinigt die beiden Menge s1 und s2. s1 und s2 müssen Repräsentanten der jeweiligen Menge sein.
     * Die Vereinigung wird nur durchgeführt, falls s1 und s2 unterschiedlich sind.
     * Es wird union-by-height durchgeführt.
     *
     * @param s1 - Element, das eine Menge repräsentiert.
     * @param s2 - Element, das eine Menge repräsentiert.
     */
    public void union(T s1, T s2) {
        T root1 = find(s1);
        T root2 = find(s2);

        if (root1.equals(root2)) {  // falls s1 und s2 schon in der gleichen menge sind
            return;
        }

        int rank1 = rank.get(root1);    // Höhe baum abfragen
        int rank2 = rank.get(root2);

        if (rank1 < rank2) {            // niedrigeren baum an höheren baum hängen
            parent.put(root1, root2);
        } else if (rank1 > rank2) {
            parent.put(root2, root1);
        } else {                        // höhe der bäume gleich
            parent.put(root2, root1);
            rank.put(root1, rank1 + 1);
        }
        size--;                         // zwei Mengen zu einer vereinigt
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        Set<Integer> s = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        UnionFind<Integer> uf = new UnionFind<>(s);

        System.out.println("Size: " + uf.size());
        uf.union(0, 1);
        System.out.println("Size after union(0, 1): " + uf.size());
        uf.union(2, 3);
        System.out.println("Size after union(2, 3): " + uf.size());
        uf.union(0, 2);
        System.out.println("Size after union(0, 2): " + uf.size());
        System.out.println("Find(9): " + uf.find(9));
        System.out.println("Find(1): " + uf.find(1));
    }
}