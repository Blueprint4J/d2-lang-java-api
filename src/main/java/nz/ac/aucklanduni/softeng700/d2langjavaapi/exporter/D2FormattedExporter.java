package nz.ac.aucklanduni.softeng700.d2langjavaapi.exporter;

import com.google.common.graph.Graphs;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.Traverser;
import com.google.common.graph.ValueGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import nz.ac.aucklanduni.softeng700.d2langjavaapi.graph.Relationship;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.graph.Component;

public class D2FormattedExporter implements FormattedExporter {
    public File export(ValueGraph<Component, Relationship> graph, String filename) {
        convertToNestedSyntaxGraph(graph);

        try {
            new File("temp").mkdir();
            File file = new File("temp", filename + ".d2");

            FileWriter writer = new FileWriter(file);

            writer.append("direction: down\n");

            // Write nodes (composition relations are embedded inside)
            for (Component node : graph.nodes()) {
                writer.append(node.getLabel()).append("\n");
            }

            // Write dependency edges
            for (var edge : graph.edges()) {
                Relationship relationship = graph.edgeValue(edge).get();
                if (relationship.type() == Relationship.EdgeType.DEPENDENCY) {
                    writer.append(String.format("%s -> %s\n", edge.source().getLabel(), edge.target().getLabel()));
                }
            }

            writer.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException("Could not create export file");
        }
    }

    private static Optional<Component> getParent(ValueGraph<Component, Relationship> graph, Component component) {
        return graph.predecessors(component).stream().findFirst();
    }

    private void convertToNestedSyntaxGraph(ValueGraph<Component, Relationship> graph) {
        // Create copy of graph to make a composition view
        MutableValueGraph<Component, Relationship> compositionTree = Graphs.copyOf(graph);
        graph.edges().stream()
                .filter(it -> graph.edgeValue(it).get().type() == Relationship.EdgeType.DEPENDENCY)
                .forEach(compositionTree::removeEdge);

        // Returns all root composition nodes: nodes with only children
        Set<Component> rootCompositionComponents = compositionTree.nodes().stream()
                .filter(it -> compositionTree.inDegree(it) == 0)
                .collect(Collectors.toSet());

        Traverser.forTree(compositionTree).breadthFirst(rootCompositionComponents)
                .forEach(it -> {
                    Optional<Component> parent = getParent(compositionTree, it);
                    parent.ifPresent(component -> it.setCompositionParent(component.getLabel()));
                });
    }
}
