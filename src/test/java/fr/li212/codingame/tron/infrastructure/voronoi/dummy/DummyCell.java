package fr.li212.codingame.tron.infrastructure.voronoi.dummy;

import fr.li212.codingame.tron.adapters.grid.SquareCoordinate;
import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;

public class DummyCell implements Cell {

    private final int x;
    private final int y;

    public DummyCell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int distance(final DummyCell other) {
        return Math.abs(this.getX() - other.getX()) + Math.abs(this.getY() - other.getY());
    }

    @Override
    public String toString() {
        return "DummyCell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public Coordinate getCoordinate() {
        return new SquareCoordinate(x, y);
    }
}
