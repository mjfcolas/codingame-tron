package fr.li212.codingame.tron.infrastructure.newvoronoi;

import fr.li212.codingame.tron.domain.grid.port.Coordinate;

public class GermWithDistance {
    private Coordinate germ;
    private final Integer distance;

    public GermWithDistance(final Coordinate germ, final Integer distance) {
        this.germ = germ;
        this.distance = distance;
    }

    public Coordinate getGerm() {
        return germ;
    }

    public void setGerm(final Coordinate germ) {
        this.germ = germ;
    }

    public Integer getDistance() {
        return distance;
    }
}
