package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.player.PlayerIdentifier;
import fr.li212.codingame.tron.infrastructure.path.Path;
import fr.li212.codingame.tron.infrastructure.path.astar.AStarManhattanPathFinder;
import fr.li212.codingame.tron.infrastructure.path.direct.DirectManhattanPathFinder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicSquareGrid implements Grid {
    private final int width;
    private final int height;
    private final SquareCell[][] cells;
    private final Map<StartAndDestKey, List<Coordinate>> cachedPaths = new HashMap<>();
    private final AStarManhattanPathFinder aStarManhattanPathFinder;
    private final DirectManhattanPathFinder directManhattanPathFinder = new DirectManhattanPathFinder();

    private final ArrayList[][] neighboursCache;

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
        neighboursCache = new ArrayList[width][height];
        cells = new SquareCell[width][height];
        IntStream.range(0, width).boxed()
                .forEach(x -> IntStream.range(0, height).boxed()
                        .forEach(y -> cells[x][y] = new SquareCell(new SquareCoordinate(x, y), null)));
        aStarManhattanPathFinder = new AStarManhattanPathFinder(this);
    }

    public BasicSquareGrid(
            final int width,
            final int height,
            final SquareCell[][] cells) {
        this.width = width;
        this.height = height;
        neighboursCache = new ArrayList[width][height];
        this.cells = cells;
        aStarManhattanPathFinder = new AStarManhattanPathFinder(this);
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

        aStarManhattanPathFinder = new AStarManhattanPathFinder(this);
        //BasicSquareGridPrinter.print(this, playerContexts);
    }

    @Override
    public Cell getCell(final Coordinate coordinate) {
        if (coordinate.getX() < 0 || coordinate.getX() >= width || coordinate.getY() < 0 || coordinate.getY() >= height) {
            return null;
        }
        return cells[coordinate.getX()][coordinate.getY()];
    }

    @Override
    public List<Coordinate> path(final Cell start, final Cell end) {
        final StartAndDestKey key = new StartAndDestKey(start.getCoordinate(), end.getCoordinate());
        if (cachedPaths.containsKey(key)) {
            return cachedPaths.get(key);
        }
        Path path;
        path = this.directPath(start, end);
        if (!path.exists()) {
            path = this.aStarPath(start, end);
        }
        cachedPaths.put(key, path.getPath());
        return path.getPath();
    }

    private Path directPath(final Cell start, final Cell end) {
        return directManhattanPathFinder.findPath(this.getCells(), start.getCoordinate(), end.getCoordinate());
    }

    private Path aStarPath(final Cell start, final Cell end) {
        return aStarManhattanPathFinder.findPath(start.getCoordinate(), end.getCoordinate());
    }

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
    public Collection<Cell> getAccessibleCellsFromStartingPoint(final Cell startingPoint) {
        final Set<Coordinate> accessibleCoordinates = new HashSet<>(width * height);
        Stack<Coordinate> workStack = new Stack<>();
        final Coordinate startCoordinate = startingPoint.getCoordinate();
        workStack.push(startCoordinate);
        while (!workStack.isEmpty()) {
            Coordinate currentCoordinate = workStack.pop();
            Collection<Coordinate> neighbours = this.getNeighbours(currentCoordinate);
            for (Coordinate coordinate : neighbours) {
                if (!accessibleCoordinates.contains(coordinate)) {
                    accessibleCoordinates.add(coordinate);
                    workStack.push(coordinate);
                }
            }
        }
        final List<Cell> result = new ArrayList<>(width * height);
        for (Coordinate coordinate : accessibleCoordinates) {
            result.add(this.cells[coordinate.getX()][coordinate.getY()]);
        }
        return result;
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
