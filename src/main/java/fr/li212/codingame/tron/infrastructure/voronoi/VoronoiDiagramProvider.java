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

    public VoronoiDiagram get(final VoronoiGrid grid, final Collection<VoronoiGerm> germs, final int reductionFactor)  throws InterruptedException{

        final Map<VoronoiCell, Queue<VoronoiCellWithDistance>> distancesByCell = new HashMap<>();

        this.getDistances(grid, germs, reductionFactor, distancesByCell);
        final VoronoiDiagram result = this.getDiagram(germs, distancesByCell);
        //PrintVoronoiDiagram.print(grid, result);
        return result;
    }

    private void getDistances(
            final VoronoiGrid grid,
            final Collection<VoronoiGerm> germs,
            final int reductionFactor,
            final Map<VoronoiCell, Queue<VoronoiCellWithDistance>> distancesByCell) throws InterruptedException{
        final Map<StartAndDestKey, Integer> cachedDistances = new HashMap<>();

        for (VoronoiGerm germ : germs) {
            final Collection<VoronoiCell> cellsAccessibleFromGerm = grid.getVoronoiCellsAccessibleFromGerm(germ);
            for (VoronoiCell cell : cellsAccessibleFromGerm) {
                if(Thread.interrupted()){
                    throw new InterruptedException();
                }
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
                distancesByCell.get(cell)
                        .add(cellWithDistance);
            }
        }
    }

    private VoronoiDiagram getDiagram(
            final Collection<VoronoiGerm> germs,
            final Map<VoronoiCell, Queue<VoronoiCellWithDistance>> distancesByCell) throws InterruptedException {
        final Map<VoronoiGerm, Set<VoronoiCell>> diagram = new HashMap<>();
        germs.forEach(germ -> diagram.put(germ, new HashSet<>()));

        for(Queue<VoronoiCellWithDistance> voronoiCellWithDistances : distancesByCell.values()){
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            final VoronoiCellWithDistance minimumDistance = voronoiCellWithDistances.remove();
            final VoronoiCellWithDistance secondMinimumDistance = voronoiCellWithDistances.isEmpty() ? null : voronoiCellWithDistances.remove();
            if (secondMinimumDistance == null || minimumDistance.getDistance() < secondMinimumDistance.getDistance()) {

                diagram.get(minimumDistance.getGerm()).add(minimumDistance.getCell());
            }
        }
        return new VoronoiDiagram(diagram);
    }
}
