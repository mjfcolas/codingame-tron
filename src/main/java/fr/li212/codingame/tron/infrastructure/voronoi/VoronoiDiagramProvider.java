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
            return start.equals(that.start) && end.equals(that.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }

    public VoronoiDiagram get(final VoronoiGrid grid, final Collection<VoronoiGerm> germs, final int reductionFactor) {

        final Map<VoronoiGerm, Set<VoronoiCellWithDistance>> distancesByGerm = new HashMap<>();
        final Map<VoronoiCell, Queue<VoronoiCellWithDistance>> distancesByCell = new HashMap<>();

        this.getDistances(grid, germs, reductionFactor, distancesByGerm, distancesByCell);
        final VoronoiDiagram result = this.getDiagram(germs, distancesByGerm, distancesByCell);
        PrintVoronoiDiagram.print(grid, result);
        return result;
    }

    private void getDistances(
            final VoronoiGrid grid,
            final Collection<VoronoiGerm> germs,
            final int reductionFactor,
            final Map<VoronoiGerm, Set<VoronoiCellWithDistance>> distancesByGerm,
            final Map<VoronoiCell, Queue<VoronoiCellWithDistance>> distancesByCell) {
        final Map<StartAndDestKey, Integer> cachedDistances = new HashMap<>();

        for (VoronoiGerm germ : germs) {
            if (!distancesByGerm.containsKey(germ)) {
                distancesByGerm.put(germ, new HashSet<>());
            }
            for (VoronoiCell cell : grid.getVoronoiCellsAccessibleFromGerm(germ)) {
                if (!cell.isVoronoiEligible(reductionFactor)) {
                    continue;
                }
                if (!distancesByCell.containsKey(cell)) {
                    distancesByCell.put(cell, new PriorityQueue<>());
                }
                final StartAndDestKey distanceCacheKey = new StartAndDestKey(
                        germ.getCell().getCoordinate(), cell.getCoordinate()
                );
                if (!cachedDistances.containsKey(distanceCacheKey)) {
                    try {
                        final List<Coordinate> path = grid.path(germ.getCell(), cell);
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
                final int distance = cachedDistances.get(distanceCacheKey);

                final VoronoiCellWithDistance cellWithDistance = new VoronoiCellWithDistance(germ, cell, distance);
                distancesByGerm.get(germ)
                        .add(cellWithDistance);
                distancesByCell.get(cell)
                        .add(cellWithDistance);
            }
        }
    }

    private VoronoiDiagram getDiagram(
            final Collection<VoronoiGerm> germs,
            final Map<VoronoiGerm, Set<VoronoiCellWithDistance>> distancesByGerm,
            final Map<VoronoiCell, Queue<VoronoiCellWithDistance>> distancesByCell) {
        final Map<VoronoiGerm, Set<VoronoiCell>> diagram = new HashMap<>();

        distancesByCell.forEach((cell, voronoiCellWithDistances) -> {
            final VoronoiCellWithDistance minimumDistance = voronoiCellWithDistances.remove();
            final VoronoiCellWithDistance secondMinimumDistance = voronoiCellWithDistances.remove();
            if(minimumDistance.getDistance() < secondMinimumDistance.getDistance()){
                if (!diagram.containsKey(minimumDistance.getGerm())) {
                    diagram.put(minimumDistance.getGerm(), new HashSet<>());
                }
                diagram.get(minimumDistance.getGerm()).add(minimumDistance.getCell());
            }
        });

//        for (VoronoiGerm voronoiGerm : germs) {
//            if (!diagram.containsKey(voronoiGerm)) {
//                diagram.put(voronoiGerm, new HashSet<>());
//            }
//            final Set<VoronoiCellWithDistance> currentDistancesForGerm = distancesByGerm.get(voronoiGerm);
//            for (VoronoiCellWithDistance currentDistance : currentDistancesForGerm) {
//                final int minimumDistance = distancesByCell.get(currentDistance.getCell())
//                        .stream().mapToInt(VoronoiCellWithDistance::getDistance).min()
//                        .orElseThrow(() -> new IllegalStateException("Distance should have been found"));
//
//                final long numberOfDistanceInferiorToMinimum = distancesByCell.get(currentDistance.getCell())
//                        .stream().filter(voronoiCellWithDistance -> voronoiCellWithDistance.getDistance() <= minimumDistance)
//                        .count();
//
//                if (numberOfDistanceInferiorToMinimum <= 1 && currentDistance.getDistance().equals(minimumDistance)) {
//                    diagram.get(voronoiGerm).add(currentDistance.getCell());
//                }
//            }
//        }
        return new VoronoiDiagram(diagram);
    }
}
