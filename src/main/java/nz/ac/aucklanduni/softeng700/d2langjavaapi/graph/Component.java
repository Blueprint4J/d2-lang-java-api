package nz.ac.aucklanduni.softeng700.d2langjavaapi.graph;

import java.util.Objects;

public class Component {

    public String compositionParent = null;
    private final String label;

    public Component(String label) {
        this.label = label;
    }

    public void setCompositionParent(String parentString) {
        this.compositionParent = parentString;
    }

    public String getLabel() {
        if (compositionParent != null) {
            return compositionParent + "." + label;
        }
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(label, component.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}