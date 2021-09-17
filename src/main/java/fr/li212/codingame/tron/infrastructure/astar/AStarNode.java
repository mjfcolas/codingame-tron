package fr.li212.codingame.tron.infrastructure.astar;

import java.util.Objects;

public class AStarNode implements Comparable<AStarNode> {

    private final CellWithHeuristic underlyingNode;
    private final int underlyingNodeHeuristic;
    private AStarNode predecessor;
    private Integer distanceFromStartToNode = null;

    public AStarNode(
            final CellWithHeuristic underlyingNode,
            final CellWithHeuristic goal) {
        this.underlyingNode = underlyingNode;
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
