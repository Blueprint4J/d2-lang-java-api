package nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.style;

public enum ColourTheme {

    DEFAULT(0),
    COOL_CLASSICS(4),
    GRAPE_SODA(6),
    COLOURBLIND_CLEAR(8),
    TERMINAL(300);

    private final int id;

    ColourTheme(int id) {
        this.id = id;
    }

    public String getSelector() {
        return "" + id;
    }
}
