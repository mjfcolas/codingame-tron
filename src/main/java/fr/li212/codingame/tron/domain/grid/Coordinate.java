package fr.li212.codingame.tron.domain.grid;

import fr.li212.codingame.tron.domain.move.Move;

public interface Coordinate {
    Coordinate adjacentCoordinate(final Move askedMove);
    int getX();
    int getY();
    int distance(final Coordinate startCoordinate);
}
