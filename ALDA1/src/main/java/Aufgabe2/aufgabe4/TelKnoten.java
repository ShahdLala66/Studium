public record TelKnoten(int x, int y) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TelKnoten) {
            return this.x == ((TelKnoten) obj).x && this.y == ((TelKnoten) obj).y;
        }
        return false;
    }

    @Override
    public String toString() {
        return ("x: " + this.x + " y: " + this.y);
    }

}