package nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.style;

public enum LayoutEngine {
    DAGRE("dagre"),
    ELK("elk"),
    TALA("tala");

    private final String d2Selector;

    LayoutEngine(String d2Selector) {
        this.d2Selector = d2Selector;
    }

    public String getSelector() {
        return d2Selector;
    }
}
