package nz.ac.aucklanduni.softeng700.d2langjavaapi.exporter;

import com.google.common.graph.Graphs;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.Traverser;
import com.google.common.graph.ValueGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.Metadata;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.Relationship;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.diagram.Component;

public class D2FormattedExporter implements FormattedExporter {

    /**
     * Creates the meta-text block for title/description.
     */
    private String[] createMeta(Metadata data) {
        if (data != null) {
            return new String[] {
                    "explanation: |md",
                    String.format("  # %s", data.title()),
                    String.format("  %s", data.description()),
                    "| { near: top-center }",
                    "",
                    "direction: down"
            };
        } else {
            return new String[] {
                    "direction: down",
            };
        }
    }

    private String[] createGraph(ValueGraph<Component, Relationship> graph) {
        List<String> lines = new ArrayList<>();
        convertToNestedSyntaxGraph(graph);

        Map<Component, Component> twoWayRelationships = new HashMap<>();

        // Write nodes (composition relations are embedded inside)
        for (Component node : graph.nodes()) {
            lines.add(node.getLabel());
        }

        // Write dependency edges
        for (var edge : graph.edges()) {
            Relationship relationship = graph.edgeValue(edge).get();
            if (relationship.type() == Relationship.EdgeType.DEPENDENCY) {

                // Check if dependency is two-way
                boolean twoWay = graph.edgeValue(edge.target(), edge.source())
                        .map(Relationship::type)
                        .filter(it -> it.equals(Relationship.EdgeType.DEPENDENCY))
                        .isPresent();

                if (twoWay && twoWayRelationships.get(edge.source()) == edge.target()) {
                    continue;
                } else if (twoWay) {
                    twoWayRelationships.put(edge.target(), edge.source());
                }

                lines.add(String.format("%s %s %s",
                                        edge.source().getLabel(),
                                        twoWay ? "<->" : "->",
                                        edge.target().getLabel()));
            }
        }

        return lines.toArray(new String[0]);
    }

    public File export(Metadata data, ValueGraph<Component, Relationship> graph, String filename) {
        try {
            new File("temp").mkdir();
            File file = new File("temp", filename + ".d2");
            FileWriter writer = new FileWriter(file);

            String[] titleBlock = createMeta(data);
            String[] graphBlock = createGraph(graph);
            List<String> joined = Stream.concat(
                    Arrays.stream(titleBlock),
                    Arrays.stream(graphBlock))
                    .collect(Collectors.toList());

            for (String s : joined) {
                writer.append(String.format("%s\n", s));
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
