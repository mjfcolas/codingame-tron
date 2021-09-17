package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagram;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagramProvider;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiGerm;

import java.util.Collection;
import java.util.stream.Collectors;

public class AugmentedBasicSquareGrid implements AugmentedGrid {

    private final int voronoiReductionFactor;
    private final BasicSquareAStarGrid underlyingGrid;
    private final VoronoiDiagram voronoiDiagram;

    public AugmentedBasicSquareGrid(
            final VoronoiDiagramProvider voronoiDiagramProvider,
            final BasicSquareAStarGrid underlyingGrid,
            final Collection<PlayerContext> playerContexts,
            final int voronoiReductionFactor) {
        this.voronoiReductionFactor = voronoiReductionFactor;
        this.underlyingGrid = underlyingGrid;
        this.voronoiDiagram = voronoiDiagramProvider
                .get(underlyingGrid,
                        playerContexts.stream()
                                .map(this::germFromPlayerContext)
                                .collect(Collectors.toSet()),
                        voronoiReductionFactor);
    }

    @Override
    public Grid getUnderlyingGrid() {
        return this.underlyingGrid;
    }

    @Override
    public float voronoiScore(final PlayerContext playerContext) {
        return (float) numberOfVoronoiCellsForPlayer(playerContext) / voronoiReductionFactor;
    }

    private int numberOfVoronoiCellsForPlayer(final PlayerContext playerContext) {
        return voronoiDiagram.getVoronoiSpaces().get(this.germFromPlayerContext(playerContext)).size();
    }

    private VoronoiGerm germFromPlayerContext(final PlayerContext playerContext) {
        return new VoronoiGerm((SquareCell) underlyingGrid.getCell(playerContext.getCurrentCoordinate()));
    }
}
