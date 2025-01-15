import java.util.HashMap;
import java.util.Set;

public class UnionFind<T> {

    private final HashMap<T, T> parent;
    private final HashMap<T, Integer> rank;
    private int size;

    public UnionFind(Set<T> s) {
        parent = new HashMap<>();
        rank = new HashMap<>();
        for (T e : s) {
            parent.put(e, e);
            rank.put(e, 0);
        }
        size = s.size();
    }

    public T find(T e) {
        if (!parent.get(e).equals(e)) {
            parent.put(e, find(parent.get(e))); // Path compression
        }
        return parent.get(e);
    }

    public void union(T s1, T s2) {
        T root1 = find(s1);
        T root2 = find(s2);

        if (root1.equals(root2)) {
            return;
        }

        int rank1 = rank.get(root1);
        int rank2 = rank.get(root2);

        if (rank1 < rank2) {
            parent.put(root1, root2);
        } else if (rank1 > rank2) {
            parent.put(root2, root1);
        } else {
            parent.put(root2, root1);
            rank.put(root1, rank1 + 1);
        }
        size--;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        Set<Integer> s = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        UnionFind<Integer> uf = new UnionFind<>(s);

        System.out.println("Initial size: " + uf.size());
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