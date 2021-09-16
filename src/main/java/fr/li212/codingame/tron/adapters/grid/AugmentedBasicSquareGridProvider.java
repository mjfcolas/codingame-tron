package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.providers.AugmentedGridProvider;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagramProvider;

import java.util.Collection;

public class AugmentedBasicSquareGridProvider implements AugmentedGridProvider {

    private final VoronoiDiagramProvider voronoiDiagramProvider;

    public AugmentedBasicSquareGridProvider(
            final VoronoiDiagramProvider voronoiDiagramProvider) {
        this.voronoiDiagramProvider = voronoiDiagramProvider;
    }

    @Override
    public AugmentedGrid get(final Grid grid, final Collection<PlayerContext> newPlayerContexts) {
        if (!(grid instanceof BasicSquareAStarGrid)) {
            throw new IllegalStateException("Only Basic square grid are allowed in this implementation");
        }
        return new AugmentedBasicSquareGrid(
                voronoiDiagramProvider,
                (BasicSquareAStarGrid) grid,
                newPlayerContexts);
    }
}
