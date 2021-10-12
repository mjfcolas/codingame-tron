package fr.li212.codingame.tron.domain.move;

import fr.li212.codingame.tron.domain.grid.Cell;

public class CellWithMove {
    private final Cell cell;
    private final Move move;

    public CellWithMove(final Cell cell, final Move move) {
        this.cell = cell;
        this.move = move;
    }

    public Cell getCell() {
        return cell;
    }

    public Move getMove() {
        return move;
    }
}
