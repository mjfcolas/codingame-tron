package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagram;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagramProvider;

import java.util.Collection;
import java.util.stream.Collectors;

public class AugmentedBasicSquareGrid implements AugmentedGrid {


    private final BasicSquareGrid underlyingGrid;
    private final VoronoiDiagram voronoiDiagram;
    private final PlayerContext predictedCurrentPlayerContext;

    public AugmentedBasicSquareGrid(
            final VoronoiDiagramProvider voronoiDiagramProvider,
            final BasicSquareGrid underlyingGrid,
            final Collection<PlayerContext> playerContexts,
            final PlayerContext predictedCurrentPlayerContext) {

        this.underlyingGrid = underlyingGrid;
        this.predictedCurrentPlayerContext = predictedCurrentPlayerContext;
        this.voronoiDiagram = voronoiDiagramProvider
                .get(underlyingGrid,
                        playerContexts.stream()
                                .map(this::germFromPlayerContext)
                                .collect(Collectors.toSet()));
    }


    @Override
    public float voronoiScore() {
        return (float) numberOfVoronoiCellsForPlayer(predictedCurrentPlayerContext);
    }

    @Override
    public int numberOfLibertiesAfter() {
        return underlyingGrid.getNeighbours(this.predictedCurrentPlayerContext.getCurrentCoordinate()).size();
    }

    private int numberOfVoronoiCellsForPlayer(final PlayerContext playerContext) {
        return voronoiDiagram.getVoronoiSpaces().get(this.germFromPlayerContext(playerContext)).size();
    }

    private Cell germFromPlayerContext(final PlayerContext playerContext) {
        return underlyingGrid.getCell(playerContext.getCurrentCoordinate());
    }
}
