package nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram;

import java.util.Date;

/**
 * Extra information to store alongside the diagram (e.g. graph title).
 */
public record Metadata(String title, String description) {

    public Metadata(String title) {
        this(title, new Date() + " (auto-generated)");
    }

}
