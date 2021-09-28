package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.move.Move;

import java.util.*;
import java.util.stream.Collectors;

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

        final Map<VoronoiGerm, Set<VoronoiCellWithDistanceAndDirectionToGo>> distancesByGerm = new HashMap<>();
        final Map<VoronoiCell, Set<VoronoiCellWithDistanceAndDirectionToGo>> distancesByCell = new HashMap<>();

        for (VoronoiGerm germ : germs) {
            final Map<StartAndDestKey, VoronoiCellWithDistanceAndDirectionToGo> cachedDistances = new HashMap<>();
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
                            final Move direction = Arrays.stream(Move.values())
                                    .filter(move -> path.size() > 2 && path.get(maxPathSize - 1).adjacentCoordinate(move).equals(path.get(maxPathSize - 2)))
                                    .findFirst()
                                    .orElse(null);
                            final VoronoiCellWithDistanceAndDirectionToGo cellWithDistanceAndDirection
                                    = new VoronoiCellWithDistanceAndDirectionToGo(germ, cell, maxPathSize - positionInPath, direction);
                            cachedDistances.put(currentDistanceKey, cellWithDistanceAndDirection);
                        }
                    } catch (final IllegalStateException e) {
                        cachedDistances.put(distanceCacheKey, new VoronoiCellWithDistanceAndDirectionToGo(germ, cell, Integer.MAX_VALUE, null));
                    }
                }
                final VoronoiCellWithDistanceAndDirectionToGo cellWithDistance = cachedDistances.get(distanceCacheKey);
                distancesByGerm.get(germ)
                        .add(cellWithDistance);
                distancesByCell.get(cell)
                        .add(cellWithDistance);
            }
        }

        final Map<VoronoiGerm, Set<VoronoiCellWithDistanceAndDirectionToGo>> conflictualCellsByGerm = new HashMap<>();

        for (VoronoiGerm voronoiGerm : germs) {
            if (!conflictualCellsByGerm.containsKey(voronoiGerm)) {
                conflictualCellsByGerm.put(voronoiGerm, new HashSet<>());
            }
            final Set<VoronoiCellWithDistanceAndDirectionToGo> currentDistancesForGerm = distancesByGerm.get(voronoiGerm);
            for (VoronoiCellWithDistanceAndDirectionToGo currentDistance : currentDistancesForGerm) {
                final Set<VoronoiCellWithDistanceAndDirectionToGo> conflictualCells = distancesByCell.get(currentDistance.getCell())
                        .stream().filter(otherGermDistance -> otherGermDistance.getDistance().equals(currentDistance.getDistance())
                                || otherGermDistance.getDistance() == currentDistance.getDistance() - 1)
                        .collect(Collectors.toSet());

                conflictualCellsByGerm.get(voronoiGerm).addAll(conflictualCells);
            }
        }

        final VoronoiDiagram result = new VoronoiDiagram(conflictualCellsByGerm);
        //PrintVoronoiDiagram.print(grid, result);
        return result;
    }
}
