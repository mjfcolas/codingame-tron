package fr.li212.codingame.tron.infrastructure.voronoi.dummy;

import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiCell;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiGrid;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DummyGrid implements VoronoiGrid {

    private final Collection<VoronoiCell> voronoiCells;

    public DummyGrid(final int width, final int heigt) {
        this.voronoiCells = IntStream.range(0, width).boxed()
                .flatMap(x -> IntStream.range(0, heigt).boxed()
                        .map(y -> new DummyCell(x, y)))
                .collect(Collectors.toSet());
    }

    @Override
    public int distance(final VoronoiCell start, final VoronoiCell end) {
        return ((DummyCell) start).distance((DummyCell) end);
    }

    @Override
    public Collection<VoronoiCell> getVoronoiCells() {
        return this.voronoiCells;
    }
}
