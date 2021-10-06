package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.domain.grid.port.Coordinate;

import java.util.Collection;
import java.util.List;

public interface VoronoiGrid {
    List<Coordinate> path(final VoronoiCell start, final VoronoiCell end);
    Collection<VoronoiCell> getVoronoiCells();
    Collection<VoronoiCell> getVoronoiCellsAccessibleFromGerm(final VoronoiGerm germ);
}
