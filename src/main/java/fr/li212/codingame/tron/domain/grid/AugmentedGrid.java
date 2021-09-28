package fr.li212.codingame.tron.domain.grid;

import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.player.PlayerContext;

public interface AugmentedGrid {
    float numberOfConflictualCellForMove(final Move move, final PlayerContext playerContext);
}
