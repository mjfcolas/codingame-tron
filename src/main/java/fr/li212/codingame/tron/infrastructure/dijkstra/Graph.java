package fr.li212.codingame.tron.infrastructure.dijkstra;

import java.util.Set;

class Graph {
    private final Set<Edge> edges;

    public Graph(final Set<Edge> edges) {
        this.edges = edges;
    }

    public Set<Edge> getEdges() {
        return edges;
    }
}
