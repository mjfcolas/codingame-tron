package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.domain.grid.Coordinate;

public class VoronoiNode implements Comparable<VoronoiNode> {

    private final Coordinate underlyingCoordinates;
    private Integer distance;
    private boolean isClosed = false;

    public VoronoiNode(final Coordinate underlyingCoordinates) {
        this.underlyingCoordinates = underlyingCoordinates;
    }

    public Coordinate getUnderlyingCoordinates() {
        return underlyingCoordinates;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(final Integer distance) {
        this.distance = distance;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed() {
        isClosed = true;
    }

    @Override
    public int compareTo(final VoronoiNode that) {
        return this.distance.compareTo(that.getDistance());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VoronoiNode that = (VoronoiNode) o;
        return underlyingCoordinates.equals(that.underlyingCoordinates);
    }

    @Override
    public int hashCode() {
        return underlyingCoordinates.hashCode();
    }
}
