package fr.li212.codingame.tron.infrastructure.astar;

import java.util.Collection;

public interface AStarGrid {
    Collection<CellWithHeuristic> getNeighbours(final CellWithHeuristic node);

    Collection<CellWithHeuristic> getAStarCells();
}
