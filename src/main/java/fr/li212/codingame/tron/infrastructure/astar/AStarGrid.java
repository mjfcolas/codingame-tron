package fr.li212.codingame.tron.infrastructure.astar;

import java.util.List;

public interface AStarGrid {
    List<CellWithHeuristic> getNeighbours(final CellWithHeuristic node);

    CellWithHeuristic[] getAStarCells();
}
