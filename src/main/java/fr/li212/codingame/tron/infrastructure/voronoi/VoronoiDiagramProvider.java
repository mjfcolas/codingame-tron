package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.domain.grid.port.Coordinate;
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
            return Objects.equals(start, that.start) && Objects.equals(end, that.end)
                    || Objects.equals(start, that.end) && Objects.equals(end, that.start);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end) + Objects.hash(end, start);
        }
    }

    public VoronoiDiagram get(final VoronoiGrid grid, final Collection<VoronoiGerm> germs, final int reductionFactor) {
        final Map<StartAndDestKey, Integer> cachedDistances = new HashMap<>();

        final Map<VoronoiGerm, Set<VoronoiCellWithDistance>> distancesByGerm = new HashMap<>();
        final Map<VoronoiCell, Set<VoronoiCellWithDistance>> distancesByCell = new HashMap<>();

        for (VoronoiGerm germ : germs) {
            if (!distancesByGerm.containsKey(germ)) {
                distancesByGerm.put(germ, new HashSet<>());
            }
            for (VoronoiCell cell : grid.getVoronoiCells()) {
                if (!cell.isVoronoiEligible(reductionFactor)) {
                    continue;
                }
                if (!distancesByCell.containsKey(cell)) {
                    distancesByCell.put(cell, new HashSet<>());
                }
                final StartAndDestKey distanceCacheKey = new StartAndDestKey(
                        germ.getCell().getCoordinate(), cell.getCoordinate()
                );
                if (!cachedDistances.containsKey(distanceCacheKey)) {
                    try {
                        final List<Coordinate> path = grid.path(germ.getCell(), cell);
                        final int maxPathSize = path.size();
                        for (int positionInPath = maxPathSize - 1; positionInPath >= 0; positionInPath--) {
                            final StartAndDestKey currentDistanceKey = new StartAndDestKey(
                                    path.get(maxPathSize - 1), path.get(positionInPath)
                            );
                            cachedDistances.put(currentDistanceKey, maxPathSize - positionInPath);
                        }
                    } catch (final IllegalStateException e) {
                        cachedDistances.put(distanceCacheKey, Integer.MAX_VALUE);
                    }
                }
                final int distance = cachedDistances.get(distanceCacheKey);
                final VoronoiCellWithDistance cellWithDistance = new VoronoiCellWithDistance(germ, cell, distance);
                distancesByGerm.get(germ)
                        .add(cellWithDistance);
                distancesByCell.get(cell)
                        .add(cellWithDistance);
            }
        }

        final Map<VoronoiGerm, Set<VoronoiCell>> diagram = new HashMap<>();

        for (VoronoiGerm voronoiGerm : germs) {
            if (!diagram.containsKey(voronoiGerm)) {
                diagram.put(voronoiGerm, new HashSet<>());
            }
            final Set<VoronoiCellWithDistance> currentDistancesForGerm = distancesByGerm.get(voronoiGerm);
            for (VoronoiCellWithDistance currentDistance : currentDistancesForGerm) {
                final int minimumDistance = distancesByCell.get(currentDistance.getCell())
                        .stream().mapToInt(VoronoiCellWithDistance::getDistance).min()
                        .orElseThrow(() -> new IllegalStateException("Distance should have been found"));

                final long numberOfDistanceInferiorToMinimum = distancesByCell.get(currentDistance.getCell())
                        .stream().filter(voronoiCellWithDistance -> voronoiCellWithDistance.getDistance() <= minimumDistance)
                        .count();

                if (numberOfDistanceInferiorToMinimum <= 1 && currentDistance.getDistance().equals(minimumDistance)) {
                    diagram.get(voronoiGerm).add(currentDistance.getCell());
                }
            }
        }

        final VoronoiDiagram result = new VoronoiDiagram(diagram);
        //PrintVoronoiDiagram.print(grid, result);
        return result;
    }
}
