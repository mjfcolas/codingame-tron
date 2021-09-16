package fr.li212.codingame.tron.infrastructure.astar;

public interface CellWithHeuristic {
    int getHeuristic(CellWithHeuristic goal);

    int getPriceToGo();
}
