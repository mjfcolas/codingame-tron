package fr.li212.codingame.tron.infrastructure.astar;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class DirectManhattanPathFinder {
    public static DirectManhattanPathFinder getNew() {
        return new DirectManhattanPathFinder();
    }

    public List<Coordinate> findPath(final Cell[][] grid, final Coordinate start, final Coordinate goal) {

        Coordinate currentCoordinate = start;

        final List<Coordinate> path = new ArrayList<>(50);
        path.add(start);
        while (currentCoordinate != goal) {
            final int stepXDirection = Integer.compare(goal.getX(), currentCoordinate.getX());
            final int stepYDirection = Integer.compare(goal.getY(), currentCoordinate.getY());

            if (stepXDirection != 0) {
                final Cell potentialNextCell = grid[currentCoordinate.getX() + stepXDirection][currentCoordinate.getY()];
                if (potentialNextCell.isAccessible()) {
                    path.add(potentialNextCell.getCoordinate());
                    currentCoordinate = potentialNextCell.getCoordinate();
                    continue;
                }
            }
            if(stepYDirection != 0){
                final Cell potentialNextCell = grid[currentCoordinate.getX()][currentCoordinate.getY() + stepYDirection];
                if (potentialNextCell.isAccessible()) {
                    path.add(potentialNextCell.getCoordinate());
                    currentCoordinate = potentialNextCell.getCoordinate();
                    continue;
                }
            }
            throw new IllegalStateException("No direct path found");

        }

        return path;
    }
}
