package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiCell;
import fr.li212.codingame.tron.infrastructure.voronoi.printer.PrintableVoronoiCell;

import java.util.Objects;

public class SquareCell implements Cell, VoronoiCell, PrintableVoronoiCell {
    private final SquareCoordinate coordinate;
    private final PlayerIdentifier playerOnCell;

    public SquareCell(final SquareCoordinate coordinate, final PlayerIdentifier playerOnCell) {
        this.playerOnCell = playerOnCell;
        this.coordinate = coordinate;
    }

    public SquareCoordinate getCoordinate() {
        return coordinate;
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
        return this.playerOnCell == null;
    }

    @Override
    public boolean isVoronoiEligible(final int reductionFactor) {
        return (this.getX() + this.getY()) % reductionFactor == 0;
    }

    public PlayerIdentifier getPlayerOnCell() {
        return playerOnCell;
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
        return 17 * coordinate.getX() + 31 * coordinate.getY();
    }

    @Override
    public String toString() {
        return "SquareCell{" +
                "coordinate=" + coordinate +
                ", playerOnCell=" + playerOnCell +
                '}';
    }
}
