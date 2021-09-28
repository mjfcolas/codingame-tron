package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.move.Move;

import java.util.Objects;
import java.util.function.Function;

public class SquareCoordinate implements Coordinate {
    private final int x;
    private final int y;

    public SquareCoordinate(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public SquareCoordinate adjacentCoordinate(final Move askedMove) {
        return SquareCoordinateMoves.fromDomain(askedMove).computeMove(this);
    }

    @Override
    public int distance(final Coordinate startCoordinate) {
        return Math.abs(this.getX() - startCoordinate.getX()) + Math.abs(this.getY() - startCoordinate.getY());
    }

    private enum SquareCoordinateMoves {
        UP(sourceCoordinate -> new SquareCoordinate(sourceCoordinate.getX(), sourceCoordinate.getY() - 1)),
        DOWN(sourceCoordinate -> new SquareCoordinate(sourceCoordinate.getX(), sourceCoordinate.getY() + 1)),
        LEFT(sourceCoordinate -> new SquareCoordinate(sourceCoordinate.getX() - 1, sourceCoordinate.getY())),
        RIGHT(sourceCoordinate -> new SquareCoordinate(sourceCoordinate.getX() + 1, sourceCoordinate.getY()));

        final Function<SquareCoordinate, SquareCoordinate> computeMoveFunction;

        SquareCoordinateMoves(final Function<SquareCoordinate, SquareCoordinate> computeMoveFunction) {
            this.computeMoveFunction = computeMoveFunction;
        }

        SquareCoordinate computeMove(final SquareCoordinate sourceCoordinate) {
            return this.computeMoveFunction.apply(sourceCoordinate);
        }

        public static SquareCoordinateMoves fromDomain(final Move domainMove) {
            return SquareCoordinateMoves.valueOf(domainMove.name());
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SquareCoordinate that = (SquareCoordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "SquareCoordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
