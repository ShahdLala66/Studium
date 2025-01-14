import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UnionFind<T> {

    private Map<T, T> parent;
    private Map<T, Integer> rank;
    private int size;

    public UnionFind(Set<T> elements) {
        parent = new HashMap<>();
        rank = new HashMap<>();
        for (T element : elements) {
            parent.put(element, element);
            rank.put(element, 0);
        }
        size = elements.size();
    }

    public T find(T element) {
        if (!parent.get(element).equals(element)) {
            parent.put(element, find(parent.get(element))); // Path compression
        }
        return parent.get(element);
    }

    public void union(T element1, T element2) {
        T root1 = find(element1);
        T root2 = find(element2);

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
        Set<Integer> elements = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        UnionFind<Integer> uf = new UnionFind<>(elements);

        System.out.println("Initial size: " + uf.size());
        uf.union(0, 1);
        System.out.println("Size after union(0, 1): " + uf.size());
        uf.union(2, 3);
        System.out.println("Size after union(2, 3): " + uf.size());
        uf.union(0, 2);
        System.out.println("Size after union(0, 2): " + uf.size());
        System.out.println("Find(3): " + uf.find(3));
        System.out.println("Find(1): " + uf.find(1));
    }
}