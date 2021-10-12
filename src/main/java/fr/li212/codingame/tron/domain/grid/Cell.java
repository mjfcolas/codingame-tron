package fr.li212.codingame.tron.domain.grid;

public interface Cell {
    boolean isAccessible();
    int getX();
    int getY();
    Coordinate getCoordinate();
}
