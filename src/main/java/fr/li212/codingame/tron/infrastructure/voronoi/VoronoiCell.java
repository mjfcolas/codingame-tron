package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.domain.grid.port.Coordinate;

public interface VoronoiCell {
    boolean isVoronoiEligible(final int reductionFactor);
    Coordinate getCoordinate();
}
