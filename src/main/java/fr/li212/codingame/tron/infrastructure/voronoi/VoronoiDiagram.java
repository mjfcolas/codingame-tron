package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.domain.grid.port.Cell;

import java.util.Map;
import java.util.Set;

public class VoronoiDiagram {
    private final Map<Cell, Set<Cell>> voronoiSpaces;

    public VoronoiDiagram(final Map<Cell, Set<Cell>> voronoiSpaces) {
        this.voronoiSpaces = voronoiSpaces;
    }

    public Map<Cell, Set<Cell>> getVoronoiSpaces() {
        return voronoiSpaces;
    }
}
