package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.infrastructure.astar.AStarGrid;
import fr.li212.codingame.tron.infrastructure.astar.AStarNode;
import fr.li212.codingame.tron.infrastructure.astar.AStarPathFinder;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiCell;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiGrid;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicSquareGrid implements Grid, AStarGrid, VoronoiGrid {
    private final int width;
    private final int height;
    private final Map<SquareCoordinate, SquareCell> cells;
    private final Map<StartAndDestKey, Integer> cachedDistances = new HashMap<>();

    class StartAndDestKey {
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
            return Objects.equals(start, that.start) && Objects.equals(end, that.end);
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
        cells = IntStream.range(0, width).boxed()
                .flatMap(x -> IntStream.range(0, height).boxed().map(y -> new SquareCoordinate(x, y)))
                .collect(Collectors.toMap(
                        coordinate -> coordinate,
                        coordinate -> new SquareCell(coordinate, true)
                ));
    }

    public BasicSquareGrid(
            final BasicSquareGrid initialGrid,
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
        final List<AStarNode> path = AStarPathFinder.getNew().findPath(this, (SquareCell) start, (SquareCell) end);
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
    public Set<AStarNode> getNeighbours(final AStarNode node) {
        if (!(node instanceof SquareCell)) {
            throw new IllegalStateException("Only Square cell are allowed in this implementation");
        }
        final SquareCell baseCell = (SquareCell) node;
        return Arrays.stream(Move.values())
                .map(move -> baseCell.getCoordinate().adjacentCoordinate(move))
                .map(cells::get)
                .filter(cell -> cell != null && cell.isAccessible())
                .collect(Collectors.toSet());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
