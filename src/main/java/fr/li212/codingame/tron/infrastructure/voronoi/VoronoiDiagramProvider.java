package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.infrastructure.voronoi.printer.PrintVoronoiDiagram;

import java.util.*;

public class VoronoiDiagramProvider {

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
            return start.equals(that.start) && end.equals(that.end) || start.equals(that.end) && end.equals(that.start);
        }

        @Override
        public int hashCode() {
            return start.hashCode() + 397 * end.hashCode() + 397 * start.hashCode() + end.hashCode();
        }
    }

    public VoronoiDiagram get(final Grid grid, final Collection<Cell> germs, final int reductionFactor) {

        final Map<Cell, Queue<CellWithDistance>> distancesByCell = new HashMap<>();

        this.getDistances(grid, germs, reductionFactor, distancesByCell);
        final VoronoiDiagram result = this.getDiagram(germs, distancesByCell);
        PrintVoronoiDiagram.print(grid, result);
        return result;
    }

    private void getDistances(
            final Grid grid,
            final Collection<Cell> germs,
            final int reductionFactor,
            final Map<Cell, Queue<CellWithDistance>> distancesByCell) {
        final Map<StartAndDestKey, Integer> cachedDistances = new HashMap<>();

        for (Cell germ : germs) {
            final Collection<Cell> cellsAccessibleFromGerm = grid.getAccessibleCellsFromStartingPoint(germ);
            for (Cell cell : cellsAccessibleFromGerm) {
                if (!cell.isEligibleForComputation(reductionFactor)) {
                    continue;
                }
                if (!distancesByCell.containsKey(cell)) {
                    distancesByCell.put(cell, new PriorityQueue<>());
                }
                final StartAndDestKey distanceCacheKey = new StartAndDestKey(
                        germ.getCoordinate(), cell.getCoordinate()
                );
                if (!cachedDistances.containsKey(distanceCacheKey)) {
                    try {
                        final List<Coordinate> path = grid.path(germ, cell);
                        final int maxPathSize = path.size();
                        for (int positionInPath = 0; positionInPath < maxPathSize; positionInPath++) {
                            final StartAndDestKey currentDistanceKey = new StartAndDestKey(
                                    path.get(0), path.get(positionInPath)
                            );
                            cachedDistances.put(currentDistanceKey, positionInPath);
                        }
                    } catch (final IllegalStateException e) {
                        cachedDistances.put(distanceCacheKey, Integer.MAX_VALUE);
                    }
                }
                final Integer distance = cachedDistances.get(distanceCacheKey);

                final CellWithDistance cellWithDistance = new CellWithDistance(germ, cell, distance);
                distancesByCell.get(cell)
                        .add(cellWithDistance);
            }
        }
    }

    private VoronoiDiagram getDiagram(
            final Collection<Cell> germs,
            final Map<Cell, Queue<CellWithDistance>> distancesByCell) {
        final Map<Cell, Set<Cell>> diagram = new HashMap<>();
        germs.forEach(germ -> diagram.put(germ, new HashSet<>()));

        for (Queue<CellWithDistance> cellWithDistances : distancesByCell.values()) {
            final CellWithDistance minimumDistance = cellWithDistances.remove();
            final CellWithDistance secondMinimumDistance = cellWithDistances.isEmpty() ? null : cellWithDistances.remove();
            if (secondMinimumDistance == null || minimumDistance.getDistance() < secondMinimumDistance.getDistance()) {

                diagram.get(minimumDistance.getGerm()).add(minimumDistance.getCell());
            }
        }
        return new VoronoiDiagram(diagram);
    }
}
