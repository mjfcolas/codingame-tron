package fr.li212.codingame.tron.infrastructure.astar;

import java.util.Set;

public interface AStarGrid {
    Set<AStarNode> getNeighbours(final AStarNode node);
}
