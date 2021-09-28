package fr.li212.codingame.tron.domain.grid;

import fr.li212.codingame.tron.domain.player.PlayerContext;

public interface AugmentedGrid {
    float voronoiScore(final PlayerContext playerContext);
}
