package fr.li212.codingame.tron.infrastructure.voronoi;

import java.util.Collection;

public interface VoronoiGrid {
    int distance(final VoronoiCell start, final VoronoiCell end);
    Collection<VoronoiCell> getVoronoiCells();
}
