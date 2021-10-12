package fr.li212.codingame.tron.domain.providers;

import fr.li212.codingame.tron.domain.grid.AugmentedGrid;
import fr.li212.codingame.tron.domain.grid.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;

import java.util.Collection;

public interface AugmentedGridProvider {
    AugmentedGrid get(final Grid grid,
                      final Collection<PlayerContext> newPlayerContexts);
}
