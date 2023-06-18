package nz.ac.aucklanduni.softeng700.d2langjavaapi.exporter;

import com.google.common.graph.ValueGraph;

import java.io.File;

import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.Metadata;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.Relationship;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.Component;

public interface FormattedExporter {
    File export(Metadata data, ValueGraph<Component, Relationship> graph, String fileName);
}
