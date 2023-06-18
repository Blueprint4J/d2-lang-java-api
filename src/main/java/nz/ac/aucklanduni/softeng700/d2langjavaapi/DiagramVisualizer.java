package nz.ac.aucklanduni.softeng700.d2langjavaapi;

import com.google.common.graph.ValueGraph;

import java.io.File;

import nz.ac.aucklanduni.softeng700.d2langjavaapi.driver.D2Executor;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.exporter.D2FormattedExporter;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.graph.Component;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.graph.Relationship;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.exporter.FormattedExporter;

public class DiagramVisualizer {

    private final FormattedExporter exporter;

    // Add configuration here
    public DiagramVisualizer(FormattedExporter exporter) {
        this.exporter = exporter;
    }

    /**
     * Generates a visual, file representation of the diagram.
     */
    public void generateDiagram(ValueGraph<Component, Relationship> diagram, String outputFilename) {
        File d2file = exporter.export(diagram, outputFilename);

        if (exporter instanceof D2FormattedExporter) {
            D2Executor.runBuildCommand(d2file.getName(), outputFilename);
        }
    }
}
