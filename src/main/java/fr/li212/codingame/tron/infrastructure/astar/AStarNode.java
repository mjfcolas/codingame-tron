package fr.li212.codingame.tron.infrastructure.astar;

public interface AStarNode {
    int getH(AStarNode destNode);

    int getPriceToGo();
}
