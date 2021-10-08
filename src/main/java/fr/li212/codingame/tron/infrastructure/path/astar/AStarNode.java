package fr.li212.codingame.tron.infrastructure.path.astar;

import fr.li212.codingame.tron.domain.grid.port.Coordinate;

import java.util.Objects;

public class AStarNode implements Comparable<AStarNode> {

    private final Coordinate underlyingCoordinate;
    private final int underlyingNodeHeuristic;
    private AStarNode predecessor;
    private Integer distanceFromStartToNode = null;
    private boolean isClosed = false;

    public AStarNode(
            final Coordinate underlyingCoordinate,
            final Coordinate goal) {
        this.underlyingCoordinate = underlyingCoordinate;
        this.underlyingNodeHeuristic = underlyingCoordinate.distance(goal);
    }

    public int getHeuristic() {
        if (distanceFromStartToNode == null) {
            throw new IllegalStateException("No cost defined for this node. Impossible to compute its heuristic");
        }
        return this.underlyingNodeHeuristic + distanceFromStartToNode;
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

    public Coordinate getUnderlyingCoordinate() {
        return underlyingCoordinate;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed() {
        isClosed = true;
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
        return Objects.equals(underlyingCoordinate, starNode.underlyingCoordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingCoordinate);
    }

    @Override
    public String toString() {
        return "AStarNode{" +
                "underlyingCoordinate=" + underlyingCoordinate + ", " +
                "heuristic=" + (distanceFromStartToNode == null ? "null" : getHeuristic()) +
                '}';
    }
}
