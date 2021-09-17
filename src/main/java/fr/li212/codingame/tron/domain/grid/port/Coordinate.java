package fr.li212.codingame.tron.domain.grid.port;

import fr.li212.codingame.tron.domain.move.Move;

public interface Coordinate {
    Coordinate adjacentCoordinate(final Move askedMove);
    int distance(final Coordinate startCoordinate);
    Coordinate reduce(final int reductionFactor);
}
