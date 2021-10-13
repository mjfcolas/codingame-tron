package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.Coordinate;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.infrastructure.voronoi.VoronoiSpaceProvider;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AugmentedBasicSquareGrid implements AugmentedGrid {

    private final BasicSquareGrid underlyingGrid;
    private final Map<Coordinate, Set<Coordinate>> voronoiSpaces;

    public AugmentedBasicSquareGrid(
            final BasicSquareGrid underlyingGrid,
            final Collection<PlayerContext> playerContexts) {
        this.underlyingGrid = underlyingGrid;
        this.voronoiSpaces = VoronoiSpaceProvider.get(underlyingGrid)
                .get(playerContexts.stream()
                        .map(PlayerContext::getCurrentCoordinate)
                        .collect(Collectors.toSet()));
    }


    @Override
    public float voronoiScore(final PlayerContext playerContext) {
        return (float) numberOfVoronoiCellsForPlayer(playerContext);
    }

    @Override
    public int numberOfLibertiesAfter(final PlayerContext playerContext) {
        return underlyingGrid.getNeighbours(playerContext.getCurrentCoordinate()).size();
    }

    private int numberOfVoronoiCellsForPlayer(final PlayerContext playerContext) {
        return voronoiSpaces.get(playerContext.getCurrentCoordinate()).size();
    }

}
