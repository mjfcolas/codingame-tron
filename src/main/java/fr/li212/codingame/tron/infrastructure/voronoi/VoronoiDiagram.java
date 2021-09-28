package fr.li212.codingame.tron.infrastructure.voronoi;

import java.util.Map;
import java.util.Set;

public class VoronoiDiagram {
    private final Map<VoronoiGerm, Set<VoronoiCellWithDistanceAndDirectionToGo>> conflictualCells;

    public VoronoiDiagram(final Map<VoronoiGerm, Set<VoronoiCellWithDistanceAndDirectionToGo>> conflictualCells) {
        this.conflictualCells = conflictualCells;
    }

    public Map<VoronoiGerm, Set<VoronoiCellWithDistanceAndDirectionToGo>> getConflictualCells() {
        return conflictualCells;
    }
}
