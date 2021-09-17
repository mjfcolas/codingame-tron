package fr.li212.codingame.tron.domain.grid;

import fr.li212.codingame.tron.domain.grid.port.Grid;
import fr.li212.codingame.tron.domain.player.PlayerContext;

public interface AugmentedGrid {
    Grid getUnderlyingGrid();
    float voronoiScore(final PlayerContext playerContext);
}
