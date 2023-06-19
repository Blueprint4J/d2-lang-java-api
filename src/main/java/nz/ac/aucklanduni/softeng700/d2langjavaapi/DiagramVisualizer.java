package nz.ac.aucklanduni.softeng700.d2langjavaapi;

import com.google.common.graph.ValueGraph;

import java.io.File;

import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.Metadata;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.driver.D2Executor;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.exporter.D2FormattedExporter;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.Component;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.Relationship;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.exporter.FormattedExporter;

public class DiagramVisualizer {

    private final FormattedExporter exporter;
    private final D2Executor executor;

    public DiagramVisualizer(FormattedExporter exporter) {
        this.executor = new D2Executor();
        this.exporter = exporter;
    }

    /**
     * Generates a visual, file representation of the diagram.
     */
    public void generateDiagram(Metadata data, ValueGraph<Component, Relationship> diagram, String outputFilename) {
        System.out.printf("Starting diagram generation: %s\n", outputFilename);
        File d2file = exporter.export(data, diagram, outputFilename);
        System.out.printf("Finished intermediary file export: %s\n", d2file.getPath());

        if (exporter instanceof D2FormattedExporter) {
            String outPath = executor.runBuildCommand(d2file, outputFilename);
            System.out.printf("Finished final file export: %s\n", outPath);
        }
    }

}
