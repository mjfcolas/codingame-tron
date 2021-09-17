package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.adapters.parameters.GlobalParameters;
import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagram;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagramProvider;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiGerm;

import java.util.Collection;
import java.util.stream.Collectors;

public class AugmentedBasicSquareGrid implements AugmentedGrid {

    private final BasicSquareAStarGrid underlyingGrid;
    private final BasicSquareAStarGrid reducedGrid;
    private final VoronoiDiagram voronoiDiagram;

    public AugmentedBasicSquareGrid(
            final VoronoiDiagramProvider voronoiDiagramProvider,
            final BasicSquareAStarGrid underlyingGrid,
            final Collection<PlayerContext> playerContexts) {
        this.underlyingGrid = underlyingGrid;
        this.reducedGrid = underlyingGrid.reduce(GlobalParameters.REDUCTION_FACTOR);
        this.voronoiDiagram = voronoiDiagramProvider
                .get(reducedGrid,
                        playerContexts.stream()
                                .map(this::germFromPlayerContext)
                                .collect(Collectors.toSet()));
    }

    @Override
    public Grid getUnderlyingGrid() {
        return this.underlyingGrid;
    }

    @Override
    public float voronoiScore(final PlayerContext playerContext) {
        return (float) numberOfVoronoiCellsForPlayer(playerContext) / (reducedGrid.getWidth() * reducedGrid.getHeight());
    }

    private int numberOfVoronoiCellsForPlayer(final PlayerContext playerContext) {
        return voronoiDiagram.getVoronoiSpaces().get(this.germFromPlayerContext(playerContext)).size();
    }

    private VoronoiGerm germFromPlayerContext(final PlayerContext playerContext) {
        return new VoronoiGerm((SquareCell) underlyingGrid
                .getCell(playerContext.getCurrentCoordinate().reduce(GlobalParameters.REDUCTION_FACTOR)));
    }
}
