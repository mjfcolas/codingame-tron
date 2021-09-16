package fr.li212.codingame.tron.infrastructure.astar;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AStarNode implements Comparable<AStarNode> {

    private AStarNode predecessor;
    private final AStarGrid underlyingGrid;
    private final CellWithHeuristic underlyingNode;
    private final int underlyingNodeHeuristic;
    private final CellWithHeuristic goal;
    private Set<AStarNode> neighbours;
    private Integer distanceFromStartToNode = null;

    public AStarNode(
            final CellWithHeuristic underlyingNode,
            final AStarGrid underlyingGrid,
            final CellWithHeuristic goal) {
        this.underlyingNode = underlyingNode;
        this.underlyingGrid = underlyingGrid;
        this.goal = goal;
        this.underlyingNodeHeuristic = this.underlyingNode.getHeuristic(goal);
    }

    public int getHeuristic() {
        if (distanceFromStartToNode == null) {
            throw new IllegalStateException("No cost defined for this node. Impossible to compute its heuristic");
        }
        return this.underlyingNodeHeuristic + distanceFromStartToNode;
    }

    public CellWithHeuristic getUnderlyingNode() {
        return underlyingNode;
    }

    public Set<AStarNode> getNeighbours() {
        if (neighbours == null) {
            neighbours = this.underlyingGrid
                    .getNeighbours(this.getUnderlyingNode()).stream().map(neighbour -> new AStarNode(neighbour, underlyingGrid, goal))
                    .collect(Collectors.toSet());
        }
        return neighbours;
    }

    public Integer getDistanceFromStartToNode() {
        return distanceFromStartToNode;
    }

    public void setDistanceFromStartToNode(final Integer distanceFromStartToNode) {
        this.distanceFromStartToNode = distanceFromStartToNode;
    }

    public void setPredecessor(final AStarNode predecessor) {
        this.predecessor = predecessor;
    }

    public AStarNode getPredecessor() {
        return predecessor;
    }

    @Override
    public int compareTo(final AStarNode toCompare) {
        return this.getHeuristic() - toCompare.getHeuristic();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AStarNode starNode = (AStarNode) o;
        return Objects.equals(underlyingNode, starNode.underlyingNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingNode);
    }

    @Override
    public String toString() {
        return "AStarNode{" +
                "underlyingNode=" + underlyingNode + ", " +
                "heuristic=" + (distanceFromStartToNode == null ? "null" : getHeuristic()) +
                '}';
    }
}
