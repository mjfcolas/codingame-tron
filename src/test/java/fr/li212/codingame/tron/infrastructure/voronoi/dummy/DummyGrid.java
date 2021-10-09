package fr.li212.codingame.tron.infrastructure.voronoi.dummy;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.grid.port.Grid;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DummyGrid implements Grid {
    private final int width;
    private final int height;
    private final Cell[][] cells;


    public DummyGrid(final int width, final int heigt) {
        this.width = width;
        this.height = heigt;
        this.cells = new Cell[width][heigt];
        IntStream.range(0, width).boxed()
                .forEach(x -> IntStream.range(0, heigt).boxed()
                        .forEach(y -> cells[x][y] = new DummyCell(x, y)));
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Cell getCell(final Coordinate coordinate) {
        return this.cells[coordinate.getX()][coordinate.getY()];
    }

    @Override
    public Cell[][] getCells() {
        return this.cells;
    }

    @Override
    public List<Coordinate> path(final Cell start, final Cell end) {
        return Arrays.asList(start.getCoordinate(), end.getCoordinate());
    }


    @Override
    public Collection<Cell> getAccessibleCellsFromStartingPoint(final Cell startingPoint) {
        return Arrays.stream(this.cells).flatMap(Arrays::stream).collect(Collectors.toList());
    }
}
