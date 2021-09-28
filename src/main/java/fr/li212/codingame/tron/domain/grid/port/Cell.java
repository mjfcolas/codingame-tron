package fr.li212.codingame.tron.domain.grid.port;

public interface Cell {
    boolean isAccessible();
    int getX();
    int getY();
    Coordinate getCoordinate();
}
