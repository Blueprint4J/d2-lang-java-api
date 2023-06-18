package nz.ac.aucklanduni.softeng700.d2langjavaapi;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import nz.ac.aucklanduni.softeng700.d2langjavaapi.graph.Component;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.graph.Relationship;
import nz.ac.aucklanduni.softeng700.d2langjavaapi.exporter.D2FormattedExporter;

public class DiagramVisualizerTest {

    @Test
    public void test_A1Graph_NoGrouping() {
        var graph = buildExampleA1Graph();
        DiagramVisualizer visualizer = new DiagramVisualizer(new D2FormattedExporter());
        visualizer.generateDiagram(graph, "example_a1_graph");
    }

    @Test
    public void test_A1Graph_Grouping() {
        var graph = buildExampleA1GraphComposed();
        DiagramVisualizer visualizer = new DiagramVisualizer(new D2FormattedExporter());
        visualizer.generateDiagram(graph, "example_a1_graph_clustered");
    }

    // Build baseline
    private static MutableValueGraph<Component, Relationship> buildExampleA1Graph() {
        MutableValueGraph<Component, Relationship> graph = ValueGraphBuilder.directed().build();

        // Add nodes
        String[] nodeLabels = {
                "PersistenceManager",
                "SubscriptionManager",
                "SecurityUtil",
                "FlightsResource",
                "BookingsResource",
                "UserResource",
                "Flight",
                "Airport",
                "User",
                "FlightBooking",
                "AircraftType",
                "SeatPricing",
                "Seat",
                "SeatingZone",
                "CabinClass"
        };
        Map<String, Component> nodes = Arrays.stream(nodeLabels).collect(Collectors.toMap(it -> it, Component::new));
        nodes.values().forEach(graph::addNode);

        String[] edges = {
                "FlightsResource -> Airport",
                "FlightsResource -> Flight",
                "BookingsResource -> Flight",
                "BookingsResource -> FlightBooking",
                "BookingsResource -> User",
                "UserResource -> User",
                "FlightsResource -> SubscriptionManager",
                "FlightsResource -> PersistenceManager",
                "FlightsResource -> SecurityUtil",
                "BookingsResource -> PersistenceManager",
                "BookingsResource -> SecurityUtil",
                "UserResource -> PersistenceManager",
                "UserResource -> SecurityUtil",
                "Flight -> FlightBooking",
                "Flight -> Airport",
                "Flight -> Seat",
                "Flight -> AircraftType",
                "FlightBooking -> Flight",
                "FlightBooking -> User",
                "FlightBooking -> Seat",
                "SeatPricing -> CabinClass",
                "AircraftType -> SeatingZone",
                "AircraftType -> CabinClass",
                "SeatingZone -> CabinClass",
        };
        Arrays.stream(edges).forEach(it -> {
            String[] split = it.split(" -> ");
            graph.putEdgeValue(nodes.get(split[0]), nodes.get(split[1]), new Relationship());
        });

        return graph;
    }

    // Build composition
    private static ValueGraph<Component, Relationship> buildExampleA1GraphComposed() {
        MutableValueGraph<Component, Relationship> graph = ValueGraphBuilder.directed().build();

        // Add nodes
        String[] nodeLabels = {
                "PersistenceManager",
                "SubscriptionManager",
                "SecurityUtil",
                "FlightsResource",
                "BookingsResource",
                "UserResource",
                "Flight",
                "Airport",
                "User",
                "FlightBooking",
                "AircraftType",
                "SeatPricing",
                "Seat",
                "SeatingZone",
                "CabinClass",
                "Domain",
                "Resources",
                "Infra"
        };
        Map<String, Component> nodes = Arrays.stream(nodeLabels).collect(Collectors.toMap(it -> it, Component::new));
        nodes.values().forEach(graph::addNode);

        String[] dependencies = {
                "FlightsResource -> Airport",
                "FlightsResource -> Flight",
                "BookingsResource -> Flight",
                "BookingsResource -> FlightBooking",
                "BookingsResource -> User",
                "UserResource -> User",
                "Resources -> Infra",
                "Resources -> Domain",
                "Flight -> FlightBooking",
                "Flight -> Airport",
                "Flight -> Seat",
                "Flight -> AircraftType",
                "FlightBooking -> Flight",
                "FlightBooking -> User",
                "FlightBooking -> Seat",
                "SeatPricing -> CabinClass",
                "AircraftType -> SeatingZone",
                "AircraftType -> CabinClass",
                "SeatingZone -> CabinClass",
        };
        Arrays.stream(dependencies).forEach(it -> {
            String[] split = it.split(" -> ");
            graph.putEdgeValue(nodes.get(split[0]), nodes.get(split[1]), new Relationship(Relationship.EdgeType.DEPENDENCY));
        });

        // Composition relationships
        String[] compositions = {
                "Resources -> FlightsResource",
                "Resources -> BookingsResource",
                "Resources -> UserResource",
                "Infra -> PersistenceManager",
                "Infra -> SecurityUtil",
                "Infra -> SubscriptionManager",
                "Domain -> Flight",
                "Domain -> Airport",
                "Domain -> User",
                "Domain -> FlightBooking",
                "Domain -> AircraftType",
                "Domain -> SeatPricing",
                "Domain -> Seat",
                "Domain -> SeatingZone",
                "Domain -> CabinClass"
        };
        Arrays.stream(compositions).forEach(it -> {
            String[] split = it.split(" -> ");
            graph.putEdgeValue(nodes.get(split[0]), nodes.get(split[1]), new Relationship(Relationship.EdgeType.COMPOSITION));
        });

        return graph;
    }
}


