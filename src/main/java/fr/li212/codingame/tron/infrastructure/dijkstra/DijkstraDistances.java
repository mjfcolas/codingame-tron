package fr.li212.codingame.tron.infrastructure.dijkstra;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DijkstraDistances {
    public int get(
            final Collection<NodeWithNeighbours> nodeWithNeighbours,
            final Vertex source,
            final Vertex target) {
        final Set<Edge> edges = new HashSet<>();
        for (NodeWithNeighbours node : nodeWithNeighbours) {
            edges.addAll(node.getNeighbours().stream()
                    .map(neighbour -> new Edge(node.getSource(), neighbour, 1))
                    .collect(Collectors.toSet()));
        }
        final Graph graph = new Graph(edges);
        final DijkstraAlgorithm dijkstraDistances = new DijkstraAlgorithm(graph);
        dijkstraDistances.execute(source);
        return dijkstraDistances.getPath(target).size() -1;
    }
}
