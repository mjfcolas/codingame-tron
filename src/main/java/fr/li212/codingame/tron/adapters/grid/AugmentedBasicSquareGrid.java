package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagram;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiDiagramProvider;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiGerm;

import java.util.Collection;
import java.util.stream.Collectors;

public class AugmentedBasicSquareGrid implements AugmentedGrid {

    private final BasicSquareGrid underlyingGrid;
    private final VoronoiDiagram voronoiDiagram;

    public AugmentedBasicSquareGrid(
            final VoronoiDiagramProvider voronoiDiagramProvider,
            final BasicSquareGrid underlyingGrid,
            final Collection<PlayerContext> playerContexts,
            final int voronoiReductionFactor) {
        this.underlyingGrid = underlyingGrid;
        this.voronoiDiagram = voronoiDiagramProvider
                .get(underlyingGrid,
                        playerContexts.stream()
                                .map(this::germFromPlayerContext)
                                .collect(Collectors.toSet()),
                        voronoiReductionFactor);
    }

    @Override
    public float numberOfConflictualCellForMove(final Move move, final PlayerContext playerContext) {
        return (float)voronoiDiagram.getConflictualCells()
                .get(this.germFromPlayerContext(playerContext)).stream()
                .filter(conflictualCell -> move.equals(conflictualCell.getFirstMoveToGo()))
                .count();
    }

    private VoronoiGerm germFromPlayerContext(final PlayerContext playerContext) {
        return new VoronoiGerm((SquareCell) underlyingGrid.getCell(playerContext.getCurrentCoordinate()));
    }
}
