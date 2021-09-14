package fr.li212.codingame.tron.adapters.io;

import fr.li212.codingame.tron.domain.move.Move;
import fr.li212.codingame.tron.domain.port.OutputTurn;

public class OutputCodingameTurn implements OutputTurn {

    @Override
    public void play(final Move move) {
        System.out.println(move);
    }
}
