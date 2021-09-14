package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.infrastructure.voronoi.printer.PrintVoronoiDiagram;

import java.util.*;

public class VoronoiDiagramProvider {
    public VoronoiDiagram get(final VoronoiGrid grid, Collection<VoronoiGerm> germs) {
        final Map<VoronoiGerm, Set<VoronoiCellWithDistance>> distancesByGerm = new HashMap<>();
        final Map<VoronoiCell, Set<VoronoiCellWithDistance>> distancesByCell = new HashMap<>();

        for (VoronoiGerm germ : germs) {
            if (!distancesByGerm.containsKey(germ)) {
                distancesByGerm.put(germ, new HashSet<>());
            }
            for (VoronoiCell cell : grid.getVoronoiCells()) {
                if (!distancesByCell.containsKey(cell)) {
                    distancesByCell.put(cell, new HashSet<>());
                }
                final VoronoiCellWithDistance newDistance = new VoronoiCellWithDistance(germ, cell, grid.distance(germ.getCell(), cell));
                distancesByGerm.get(germ)
                        .add(newDistance);
                distancesByCell.get(cell)
                        .add(newDistance);
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
        PrintVoronoiDiagram.print(result);
        return result;
    }
}
