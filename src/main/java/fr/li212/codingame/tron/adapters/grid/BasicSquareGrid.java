package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.Cell;
import fr.li212.codingame.tron.domain.grid.Coordinate;
import fr.li212.codingame.tron.domain.grid.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicSquareGrid implements Grid {
    private final int width;
    private final int height;
    private final SquareCell[][] cells;

    private final ArrayList[][] neighboursCache;

    public BasicSquareGrid(
            final int width,
            final int height) {
        this.width = width;
        this.height = height;
        neighboursCache = new ArrayList[width][height];
        cells = new SquareCell[width][height];
        IntStream.range(0, width).boxed()
                .forEach(x -> IntStream.range(0, height).boxed()
                        .forEach(y -> cells[x][y] = new SquareCell(new SquareCoordinate(x, y), null)));
    }

    public BasicSquareGrid(
            final int width,
            final int height,
            final SquareCell[][] cells) {
        this.width = width;
        this.height = height;
        neighboursCache = new ArrayList[width][height];
        this.cells = cells;
    }


    public BasicSquareGrid(
            final BasicSquareGrid initialGrid,
            final Collection<PlayerContext> playerContexts) {
        this.width = initialGrid.getWidth();
        this.height = initialGrid.getHeight();
        neighboursCache = new ArrayList[width][height];
        cells = new SquareCell[width][height];


        final Set<PlayerIdentifier> eliminatedPlayers = playerContexts.stream()
                .filter(PlayerContext::isEliminated)
                .map(PlayerContext::getPlayerIdentifier)
                .collect(Collectors.toSet());

        IntStream.range(0, width).boxed()
                .forEach(x -> IntStream.range(0, height).boxed()
                        .forEach(y -> {
                            final SquareCell cell = initialGrid.getCells()[x][y];

                            PlayerIdentifier playerOnCell;
                            if (eliminatedPlayers.contains(cell.getPlayerOnCell())) {
                                playerOnCell = null;
                            } else if (cell.getPlayerOnCell() != null) {
                                playerOnCell = cell.getPlayerOnCell();
                            } else {
                                final Optional<PlayerIdentifier> newPlayerOnCell = playerContexts.stream()
                                        .filter(playerContext -> playerContext.getStartCoordinate().equals(cell.getCoordinate()) || playerContext.getCurrentCoordinate().equals(cell.getCoordinate()))
                                        .map(PlayerContext::getPlayerIdentifier)
                                        .findAny();
                                playerOnCell = newPlayerOnCell.orElse(null);
                            }
                            this.cells[x][y] = new SquareCell(cell.getCoordinate(), playerOnCell);
                        }));
    }

    @Override
    public Cell getCell(final Coordinate coordinate) {
        if (coordinate.getX() < 0 || coordinate.getX() >= width || coordinate.getY() < 0 || coordinate.getY() >= height) {
            return null;
        }
        return cells[coordinate.getX()][coordinate.getY()];
    }

    @Override
    public List<Coordinate> getNeighbours(final Coordinate coordinate) {

        if (neighboursCache[coordinate.getX()][coordinate.getY()] == null) {
            final ArrayList<Coordinate> result = new ArrayList<>(4);
            if (coordinate.getX() - 1 >= 0) {
                if (cells[coordinate.getX() - 1][coordinate.getY()].isAccessible()) {
                    result.add(cells[coordinate.getX() - 1][coordinate.getY()].getCoordinate());
                }
            }
            if (coordinate.getX() + 1 < width) {
                if (cells[coordinate.getX() + 1][coordinate.getY()].isAccessible()) {
                    result.add(cells[coordinate.getX() + 1][coordinate.getY()].getCoordinate());
                }
            }
            if (coordinate.getY() - 1 >= 0) {
                if (cells[coordinate.getX()][coordinate.getY() - 1].isAccessible()) {
                    result.add(cells[coordinate.getX()][coordinate.getY() - 1].getCoordinate());
                }
            }
            if (coordinate.getY() + 1 < height) {
                if (cells[coordinate.getX()][coordinate.getY() + 1].isAccessible()) {
                    result.add(cells[coordinate.getX()][coordinate.getY() + 1].getCoordinate());
                }
            }
            neighboursCache[coordinate.getX()][coordinate.getY()] = result;
            return result;
        }
        return neighboursCache[coordinate.getX()][coordinate.getY()];
    }

    @Override
    public SquareCell[][] getCells() {
        return cells;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
