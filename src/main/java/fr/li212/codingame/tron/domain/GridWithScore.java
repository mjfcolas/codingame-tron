package fr.li212.codingame.tron.domain;

import fr.li212.codingame.tron.domain.grid.Grid;
import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.player.PlayerContext;

public class GridWithScore {
    private final Grid grid;
    private final PlayerContext sourcePlayer;
    private final Move sourceMove;
    private final Integer score;

    public GridWithScore(final Grid grid,
                         final PlayerContext sourcePlayer,
                         final Move sourceMove,
                         final Integer score) {
        this.grid = grid;
        this.sourcePlayer = sourcePlayer;
        this.sourceMove = sourceMove;
        this.score = score;
    }

    public PlayerContext getSourcePlayer() {
        return sourcePlayer;
    }

    public Grid getGrid() {
        return grid;
    }

    public Move getSourceMove() {
        return sourceMove;
    }

    public Integer getScore() {
        return score;
    }
}
