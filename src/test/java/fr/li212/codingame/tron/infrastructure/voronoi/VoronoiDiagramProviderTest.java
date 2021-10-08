package fr.li212.codingame.tron.infrastructure.voronoi;

import fr.li212.codingame.tron.infrastructure.voronoi.dummy.DummyCell;
import fr.li212.codingame.tron.infrastructure.voronoi.dummy.DummyGrid;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class VoronoiDiagramProviderTest {

    /*
    0 = cell
    . = germ
    .00
    0.0
     */
    private final static VoronoiGrid GRID = new DummyGrid(30, 20);
    private final static Set<VoronoiGerm> GERMS = new HashSet<>(Arrays.asList(
            new VoronoiGerm(new DummyCell(1, 0)),
            new VoronoiGerm(new DummyCell(2, 0))
    ));

    @DisplayName("Given a grid and germs," +
            "when getting voronoi diagram," +
            "then expected diagram is retrieved")
    @Test
    void getDiagram() throws InterruptedException {
        final VoronoiDiagramProvider provider = new VoronoiDiagramProvider();
        provider.get(GRID, GERMS, 1);
    }

}
