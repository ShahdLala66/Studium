public record TelVerbindung(TelKnoten anfang, TelKnoten ende, int c) {

    @Override
    public String toString() {
        return "Anfangsknoten: " + anfang + " Endknoten: " + ende + " Kosten: " + c;
    }

}