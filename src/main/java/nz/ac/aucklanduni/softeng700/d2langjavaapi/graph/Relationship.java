package nz.ac.aucklanduni.softeng700.d2langjavaapi.graph;

/**
 * Represents a relationship between two components of our diagram.
 */
public record Relationship(EdgeType type) {

    // Default
    public Relationship() {
        this(EdgeType.DEPENDENCY);
    }

    public enum EdgeType {
        COMPOSITION,
        DEPENDENCY
    }
}
