package fr.li212.codingame.tron.domain.move;

import fr.li212.codingame.tron.domain.grid.Coordinate;

public enum Move {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public Coordinate computeMove(final Coordinate sourceCoordinate) {
        return sourceCoordinate.adjacentCoordinate(this);
    }
}
