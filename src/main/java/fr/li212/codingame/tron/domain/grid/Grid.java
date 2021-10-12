package fr.li212.codingame.tron.domain.grid;

import java.util.List;

public interface Grid {
    int getWidth();

    int getHeight();

    Cell getCell(final Coordinate coordinate);

    List<Coordinate> getNeighbours(final Coordinate coordinate);

    Cell[][] getCells();
}
