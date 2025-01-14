import java.util.Objects;

public class TelVerbindung {

    final TelKnoten anfang;
    final TelKnoten ende;
    final int c;

    public TelVerbindung(TelKnoten anfang, TelKnoten ende, int c) {
        this.anfang = anfang;
        this.ende = ende;
        this.c = c;
    }


    @Override
    public String toString() {
        return "Anfangsknoten: " + anfang + " Endknoten: " + ende + " Kosten: " + c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelVerbindung that = (TelVerbindung) o;
        return c == that.c &&
                Objects.equals(anfang, that.anfang) &&
                Objects.equals(ende, that.ende);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anfang, ende, c);
    }
}
