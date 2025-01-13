package shortestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Klasse für Scotland Yard Heuristik.
 * Bietet eine Methode estimatedCost an, die
 * die Distanz zweier Knoten im Scotland-Yard-Spielplan schätzt.
 * Die Heuristik wird für A* benötigt.
 *
 * @author Oliver Bittel
 * @since 30.06.2024
 */
public class ScotlandYardHeuristic implements Heuristic<Integer> {
    private Map<Integer, Point> coord; // Ordnet jedem Knoten seine Koordinaten zu

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Der Konstruktor liest die (x,y)-Koordinaten (Pixelkoordinaten) aller Knoten
     * von der Datei ScotlandYard_Knoten.txt in eine Map ein.
     */
    public ScotlandYardHeuristic() throws FileNotFoundException {
        // Initialize the coord map
        coord = new HashMap<>();

        Scanner in = new Scanner(new File("data/ScotlandYard_Knoten.txt"));
        String line;

        while (in.hasNextLine()) {
            line = in.nextLine();
            String[] w = line.split("[\\t|\\s]+");

            Point p = new Point(Integer.parseInt(w[1]), Integer.parseInt(w[2]));

            coord.put(Integer.parseInt(w[0]), p);
        }
    }


    /**
     * Liefert einen skalierten Euklidischen Abstand zwischen Knoten u und v zurück.
     * Da die Koordinaten von x und y in Pixeleinheiten sind, wird
     * der Euklidische Abstand mit einem Faktor zwischen 0.02 bis 0.1 skaliert.
     *
     * @param u Knoten
     * @param v Knoten
     * @return skalierter Euklidischer Abstand als geschätze Kosten
     */
    public double estimatedCost(Integer u, Integer v) {
        ScotlandYardHeuristic.Point vP = coord.get(u);
        ScotlandYardHeuristic.Point wP = coord.get(v);

        int x = (vP.x - wP.x);
        int y = (vP.y - wP.y);

        double betrag = Math.sqrt((x * x) + (y * y));

        return betrag / 30;
    }
}
