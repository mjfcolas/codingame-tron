package fr.li212.codingame.tron.adapters.grid;

import fr.li212.codingame.tron.domain.grid.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;
import fr.li212.codingame.tron.domain.providers.GridProvider;

import java.util.Collection;

public class BasicSquareGridProvider implements GridProvider {

    @Override
    public Grid get(final Grid currentGrid, final Collection<PlayerContext> playerContexts) {
        if(!(currentGrid instanceof BasicSquareGrid)){
            throw new IllegalStateException("Only BasicSquareGrid are allowed");
        }
        return new BasicSquareGrid((BasicSquareGrid)currentGrid, playerContexts);
    }
}
