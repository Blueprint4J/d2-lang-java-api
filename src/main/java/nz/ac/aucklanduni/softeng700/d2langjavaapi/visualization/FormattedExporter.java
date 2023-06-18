package nz.ac.aucklanduni.softeng700.d2langjavaapi.visualization;

import com.google.common.graph.ValueGraph;

import java.io.File;

import nz.ac.aucklanduni.softeng700.d2langjavaapi.graph.Relationship;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.graph.Component;

public interface FormattedExporter {
    File export(ValueGraph<Component, Relationship> graph, String fileName);
}
