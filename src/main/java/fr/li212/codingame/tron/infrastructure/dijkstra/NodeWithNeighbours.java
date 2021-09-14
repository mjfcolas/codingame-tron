package fr.li212.codingame.tron.infrastructure.dijkstra;

import java.util.Set;

public class NodeWithNeighbours {
    private final Vertex source;
    private final Set<Vertex> neighbours;

    public NodeWithNeighbours(final Vertex source, final Set<Vertex> neighbours) {
        this.source = source;
        this.neighbours = neighbours;
    }

    public Vertex getSource() {
        return source;
    }

    public Set<Vertex> getNeighbours() {
        return neighbours;
    }
}
