package fr.li212.codingame.tron.domain.port;

import fr.li212.codingame.tron.domain.move.Move;

public interface OutputTurn {
    void play(final Move move);
}
