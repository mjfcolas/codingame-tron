package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.infrastructure.astar.AStarGrid;
import fr.li212.codingame.tron.infrastructure.astar.AStarPathFinder;
import fr.li212.codingame.tron.infrastructure.astar.CellWithHeuristic;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiCell;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiGrid;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicSquareAStarGrid implements Grid, AStarGrid, VoronoiGrid {
    private final int width;
    private final int height;
    private final Map<SquareCoordinate, SquareCell> cells;
    private final Map<StartAndDestKey, Integer> cachedDistances = new HashMap<>();
    private final Map<CellWithHeuristic, List<CellWithHeuristic>> neighboursCache = new HashMap<>();

    static class StartAndDestKey {
        private final VoronoiCell start;
        private final VoronoiCell end;

        public StartAndDestKey(final VoronoiCell start, final VoronoiCell end) {
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

    public BasicSquareAStarGrid(
            final int width,
            final int height) {
        this.width = width;
        this.height = height;
        cells = IntStream.range(0, width).boxed()
                .flatMap(x -> IntStream.range(0, height).boxed().map(y -> new SquareCoordinate(x, y)))
                .collect(Collectors.toMap(
                        coordinate -> coordinate,
                        coordinate -> new SquareCell(coordinate, true)
                ));
    }

    public BasicSquareAStarGrid(
            final BasicSquareAStarGrid initialGrid,
            final Collection<PlayerContext> playerContexts) {
        this.width = initialGrid.getWidth();
        this.height = initialGrid.getHeight();
        cells = initialGrid.getCells().stream().map(cell -> {
            final Set<Coordinate> walledCoordinates = playerContexts.stream()
                    .flatMap(playerContext -> new HashSet<>(Arrays.asList(playerContext.getCurrentCoordinate(), playerContext.getStartCoordinate())).stream())
                    .collect(Collectors.toSet());
            final boolean isAccessible = cell.isAccessible() && !walledCoordinates.contains(cell.getCoordinate());
            return new SquareCell(cell.getCoordinate(), isAccessible);
        }).collect(Collectors.toMap(
                SquareCell::getCoordinate,
                cell -> cell
        ));
    }

    private BasicSquareAStarGrid(
            final int width,
            final int height,
            Map<SquareCoordinate, SquareCell> cells) {
        this.width = width;
        this.height = height;
        this.cells = cells;
    }

    public BasicSquareAStarGrid reduce(final int reductionFactor) {
        if (this.width % reductionFactor != 0 || this.height % reductionFactor != 0) {
            throw new IllegalArgumentException("Reduction factor must divide height and width");
        }
        final int newWidth = this.width / reductionFactor;
        final int newHeight = this.height / reductionFactor;
        final Map<SquareCoordinate, SquareCell> newCells = IntStream.range(0, newWidth).boxed()
                .flatMap(x -> IntStream.range(0, newHeight).boxed().map(y -> new SquareCoordinate(x, y)))
                .map(targetCoordinate -> {
                    final boolean targetAccessible = this.cells.values().stream()
                            .filter(sourceCell -> sourceCell.getX() >= reductionFactor * targetCoordinate.getX()
                                    && sourceCell.getX() < targetCoordinate.getX() + 1
                                    && sourceCell.getY() >= reductionFactor * targetCoordinate.getY()
                                    && sourceCell.getY() < targetCoordinate.getY() + 1).allMatch(SquareCell::isAccessible);
                    return new SquareCell(targetCoordinate, targetAccessible);
                }).collect(Collectors.toMap(
                        SquareCell::getCoordinate,
                        squareCell -> squareCell
                ));
        return new BasicSquareAStarGrid(newWidth, newHeight, newCells);
    }

    @Override
    public Cell getCell(final Coordinate coordinate) {
        if (!(coordinate instanceof SquareCoordinate)) {
            throw new IllegalStateException("Only Square Coordinates are allowed in this implementation");
        }
        return cells.get(coordinate);
    }

    @Override
    public int distance(final VoronoiCell start, final VoronoiCell end) {
        if (!(start instanceof SquareCell) || !(end instanceof SquareCell)) {
            System.err.println(start);
            System.err.println(end);
            throw new IllegalStateException("Only Square cell are allowed in this implementation");
        }
        final StartAndDestKey key = new StartAndDestKey(start, end);
        if (cachedDistances.containsKey(key)) {
            return cachedDistances.get(key);
        }

        final List<CellWithHeuristic> path = AStarPathFinder.getNew().findPath(this, (SquareCell) start, (SquareCell) end);
        final int distance = path.size() - 1;
        cachedDistances.put(key, distance);
        return distance;
    }

    @Override
    public Collection<VoronoiCell> getVoronoiCells() {
        return this.getCells().stream()
                .filter(SquareCell::isAccessible)
                .map(squareCell -> (VoronoiCell) squareCell)
                .collect(Collectors.toSet());
    }

    public Collection<SquareCell> getCells() {
        return cells.values();
    }

    @Override
    public CellWithHeuristic[] getAStarCells() {
        final CellWithHeuristic[] cellArray = new CellWithHeuristic[this.cells.size()];
        int i = 0;
        for (CellWithHeuristic current : this.cells.values()) {
            cellArray[i] = current;
            i++;
        }
        return cellArray;
    }

    @Override
    public List<CellWithHeuristic> getNeighbours(final CellWithHeuristic node) {
        if (!(node instanceof SquareCell)) {
            throw new IllegalStateException("Only Square cell are allowed in this implementation");
        }
        final SquareCell baseCell = (SquareCell) node;
        if (!neighboursCache.containsKey(node)) {
            final List<CellWithHeuristic> neighbours = new ArrayList<>(4);
            for (Move move : Move.values()) {
                final SquareCell cell = this.cells.get(baseCell.getCoordinate().adjacentCoordinate(move));
                if (cell != null && cell.isAccessible()) {
                    neighbours.add(cell);
                }
            }
            neighboursCache.put(node, neighbours);
        }
        return neighboursCache.get(node);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
