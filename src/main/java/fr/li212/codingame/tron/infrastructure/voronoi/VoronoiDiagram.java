package fr.li212.codingame.tron.infrastructure.voronoi;

import java.util.Map;
import java.util.Set;

public class VoronoiDiagram {
    private final Map<VoronoiGerm, Set<VoronoiCell>> voronoiSpaces;

    public VoronoiDiagram(final Map<VoronoiGerm, Set<VoronoiCell>> voronoiSpaces) {
        this.voronoiSpaces = voronoiSpaces;
    }

    public Map<VoronoiGerm, Set<VoronoiCell>> getVoronoiSpaces() {
        return voronoiSpaces;
    }
}
