package fr.li212.codingame.tron.domain.providers;

import fr.li212.codingame.tron.domain.grid.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;

import java.util.Collection;

public interface GridProvider {
    Grid get(final Grid currentGrid, final Collection<PlayerContext> playerContexts);
}
