package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.Cell;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;

public class SquareCell implements Cell {
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

    public PlayerIdentifier getPlayerOnCell() {
        return playerOnCell;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SquareCell cell = (SquareCell) o;
        return coordinate.equals(cell.coordinate);
    }

    @Override
    public int hashCode() {
        return coordinate.hashCode();
    }

    @Override
    public String toString() {
        return "SquareCell{" +
                "coordinate=" + coordinate +
                ", playerOnCell=" + playerOnCell +
                '}';
    }
}
