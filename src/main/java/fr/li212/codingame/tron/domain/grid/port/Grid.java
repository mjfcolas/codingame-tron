package fr.li212.codingame.tron.domain.grid.port;

import java.util.Collection;
import java.util.List;

public interface Grid {
    int getWidth();
    int getHeight();

    Cell getCell(final Coordinate coordinate);

    Cell[][] getCells();

    List<Coordinate> path(final Cell start, final Cell end);

    Collection<Cell> getAccessibleCellsFromStartingPoint(final Cell startingPoint);
}
