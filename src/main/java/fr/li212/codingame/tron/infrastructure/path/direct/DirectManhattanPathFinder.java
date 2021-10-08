package fr.li212.codingame.tron.infrastructure.path.direct;

import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.infrastructure.path.Path;

import java.util.ArrayList;
import java.util.List;

public class DirectManhattanPathFinder {
    public Path findPath(final Cell[][] grid, final Coordinate start, final Coordinate goal) {

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
            return new Path(null, false);

        }

        return new Path(path, true);
    }
}
