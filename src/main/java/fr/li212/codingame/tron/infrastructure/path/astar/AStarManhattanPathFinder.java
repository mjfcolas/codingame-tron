package fr.li212.codingame.tron.infrastructure.path.astar;

import fr.li212.codingame.tron.adapters.grid.BasicSquareGrid;
import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.infrastructure.path.Path;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class AStarManhattanPathFinder {

    public Path findPath(final BasicSquareGrid gridContainer, final Coordinate start, final Coordinate goal) {

        final AStarNode[][] copiedGrid = this.initialize(gridContainer, goal);

        AStarNode startNode = copiedGrid[start.getX()][start.getY()];
        AStarNode goalNode = copiedGrid[goal.getX()][goal.getY()];

        final Queue<AStarNode> openSet = new PriorityBlockingQueue<>();

        startNode.setDistanceFromStartToNode(0);
        openSet.add(startNode);
        startNode.setClosed();
        while (!openSet.isEmpty()) {
            AStarNode currentNode = openSet.remove();
            if (currentNode.equals(goalNode)) {
                return computePath(currentNode);
            }
            final Collection<AStarNode> neighbours = this.getNeighbours(currentNode, gridContainer, copiedGrid);
            for (AStarNode neighbour : neighbours) {
                this.manageNeighbour(neighbour, currentNode, openSet);
            }

        }
        return new Path(null, false);
    }

    private void manageNeighbour(final AStarNode neighbour, final AStarNode currentNode, final Queue<AStarNode> openSet) {
        final int predictedDistanceToStartNode = currentNode.getDistanceFromStartToNode() + 1;
        if (!neighbour.isClosed() || (neighbour.getDistanceFromStartToNode() > predictedDistanceToStartNode)) {
            neighbour.setPredecessor(currentNode);
            neighbour.setDistanceFromStartToNode(predictedDistanceToStartNode);
            neighbour.setClosed();
            openSet.add(neighbour);
        }
    }

    private AStarNode[][] initialize(final BasicSquareGrid gridContainer, final Coordinate goal) {
        final Cell[][] grid = gridContainer.getCells();
        final AStarNode[][] copiedGrid = new AStarNode[grid.length][grid[0].length];
        final int width = gridContainer.getWidth();
        final int height = gridContainer.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                copiedGrid[x][y] = new AStarNode(grid[x][y].getCoordinate(), goal);
            }
        }
        return copiedGrid;
    }

    private Path computePath(final AStarNode endNode) {
        final List<Coordinate> path = new ArrayList<>();
        path.add(endNode.getUnderlyingCoordinate());
        AStarNode currentNode = endNode;
        while (currentNode.getPredecessor() != null) {
            currentNode = currentNode.getPredecessor();
            path.add(currentNode.getUnderlyingCoordinate());
        }
        Collections.reverse(path);
        return new Path(path, true);
    }

    private List<AStarNode> getNeighbours(final AStarNode currentNode, final BasicSquareGrid grid, final AStarNode[][] copiedGrid) {
        final List<Coordinate> neighbours = grid.getNeighbours(currentNode.getUnderlyingCoordinate());
        List<AStarNode> result = new ArrayList<>(4);
        for (Coordinate coordinate : neighbours) {
            result.add(copiedGrid[coordinate.getX()][coordinate.getY()]);
        }
        return result;
    }
}
