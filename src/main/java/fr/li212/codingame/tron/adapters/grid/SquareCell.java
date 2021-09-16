package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.infrastructure.astar.CellWithHeuristic;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiCell;
import fr.li212.codingame.tron.infrastructure.voronoi.printer.PrintableVoronoiCell;

import java.util.Objects;

public class SquareCell implements Cell, CellWithHeuristic, VoronoiCell, PrintableVoronoiCell {
    private final SquareCoordinate coordinate;
    private final boolean isAccessible;

    public SquareCell(final SquareCoordinate coordinate, final boolean isAccessible) {
        this.isAccessible = isAccessible;
        this.coordinate = coordinate;
    }

    public SquareCoordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public int getHeuristic(final CellWithHeuristic goal) {
        if (!(goal instanceof SquareCell)) {
            throw new IllegalStateException("Only Square cell are allowed in this implementation");
        }
        return this.coordinate.distance(((SquareCell) goal).getCoordinate());
    }

    @Override
    public int getPriceToGo() {
        return 1;
    }

    @Override
    public int getX() {
        return this.coordinate.getX();
    }

    @Override
    public int getY() {
        return this.coordinate.getY();
    }

    @Override
    public boolean isAccessible() {
        return this.isAccessible;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SquareCell cell = (SquareCell) o;
        return Objects.equals(coordinate, cell.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }

    @Override
    public String toString() {
        return "SquareCell{" +
                "coordinate=" + coordinate +
                ", isAccessible=" + isAccessible +
                '}';
    }
}
