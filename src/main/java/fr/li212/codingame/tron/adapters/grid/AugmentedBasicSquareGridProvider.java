package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.providers.AugmentedGridProvider;

import java.util.Collection;

public class AugmentedBasicSquareGridProvider implements AugmentedGridProvider {

    public AugmentedBasicSquareGridProvider() {
    }

    @Override
    public AugmentedGrid get(final Grid grid, final Collection<PlayerContext> newPlayerContexts) {
        if (!(grid instanceof BasicSquareGrid)) {
            throw new IllegalStateException("Only Basic square grid are allowed in this implementation");
        }
        return new AugmentedBasicSquareGrid(
                (BasicSquareGrid) grid,
                newPlayerContexts);
    }
}
