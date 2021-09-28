package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import fr.li212.codingame.tron.infrastructure.astar.AStarManhattanPathFinder;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiCell;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiGrid;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicSquareGrid implements Grid, VoronoiGrid {
    private final int width;
    private final int height;
    private final SquareCell[][] cells;
    private final Map<StartAndDestKey, List<Coordinate>> cachedPaths = new HashMap<>();

    private final Map<Coordinate, List<Coordinate>> neighboursCache = new HashMap<>();

    static class StartAndDestKey {
        private final Coordinate start;
        private final Coordinate end;

        public StartAndDestKey(final Coordinate start, final Coordinate end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final StartAndDestKey that = (StartAndDestKey) o;
            return Objects.equals(start, that.start) && Objects.equals(end, that.end)
                    || Objects.equals(start, that.end) && Objects.equals(end, that.start);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }

    public BasicSquareGrid(
            final int width,
            final int height) {
        this.width = width;
        this.height = height;
        cells = new SquareCell[width][height];
        IntStream.range(0, width).boxed()
                .forEach(x -> IntStream.range(0, height).boxed()
                        .forEach(y -> cells[x][y] = new SquareCell(new SquareCoordinate(x, y), null)));
    }

    public BasicSquareGrid(
            final BasicSquareGrid initialGrid,
            final Collection<PlayerContext> playerContexts) {
        this.width = initialGrid.getWidth();
        this.height = initialGrid.getHeight();
        cells = new SquareCell[width][height];
        IntStream.range(0, width).boxed()
                .forEach(x -> IntStream.range(0, height).boxed()
                        .forEach(y -> {
                            final SquareCell cell = initialGrid.getCells()[x][y];
                            final Set<PlayerIdentifier> playerInGame = playerContexts.stream().map(PlayerContext::getPlayerIdentifier).collect(Collectors.toSet());
                            final PlayerIdentifier playerAlreadyOnCell = cell.getPlayerOnCell() != null
                                    && playerInGame.contains(cell.getPlayerOnCell()) ? cell.getPlayerOnCell() : null;
                            final PlayerIdentifier newPlayerOnCell = playerContexts.stream()
                                    .filter(playerContext -> playerContext.getStartCoordinate().equals(cell.getCoordinate()) || playerContext.getCurrentCoordinate().equals(cell.getCoordinate()))
                                    .map(PlayerContext::getPlayerIdentifier)
                                    .findAny()
                                    .orElse(null);
                            this.cells[x][y] = new SquareCell(cell.getCoordinate(), playerAlreadyOnCell != null ? playerAlreadyOnCell : newPlayerOnCell);
                        }));
    }

    @Override
    public Cell getCell(final Coordinate coordinate) {
        if(coordinate.getX() < 0 || coordinate.getX() >= width || coordinate.getY() < 0 || coordinate.getY() >= height){
            return null;
        }
        return cells[coordinate.getX()][coordinate.getY()];
    }

    @Override
    public List<Coordinate> path(final VoronoiCell start, final VoronoiCell end) {
        if (!(start instanceof SquareCell) || !(end instanceof SquareCell)) {
            System.err.println(start);
            System.err.println(end);
            throw new IllegalStateException("Only Square cell are allowed in this implementation");
        }
        final StartAndDestKey key = new StartAndDestKey(start.getCoordinate(), end.getCoordinate());
        if (cachedPaths.containsKey(key)) {
            return cachedPaths.get(key);
        }

        final List<Coordinate> path = AStarManhattanPathFinder.getNew().findPath(this, ((SquareCell) start).getCoordinate(), ((SquareCell) end).getCoordinate());

        cachedPaths.put(key, path);
        return path;
    }

    public List<Coordinate> getNeighbours(final Coordinate coordinate) {
        if(neighboursCache.containsKey(coordinate)){
            return neighboursCache.get(coordinate);
        }
        final List<Coordinate> result = new ArrayList<>(4);
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
        neighboursCache.put(coordinate, result);
        return result;
    }


    @Override
    public Collection<VoronoiCell> getVoronoiCells() {
        List<VoronoiCell> result = new ArrayList<>(width * height);
        for (final SquareCell[] cell : cells) {
            for (final SquareCell squareCell : cell) {
                if (squareCell.isAccessible()) {
                    result.add(squareCell);
                }
            }
        }
        return result;
    }

    public SquareCell[][] getCells() {
        return cells;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
