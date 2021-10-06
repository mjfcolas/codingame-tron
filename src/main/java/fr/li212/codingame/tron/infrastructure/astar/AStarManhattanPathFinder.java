package fr.li212.codingame.tron.infrastructure.astar;

import fr.li212.codingame.tron.adapters.grid.BasicSquareGrid;
import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class AStarManhattanPathFinder {

    public static AStarManhattanPathFinder getNew() {
        return new AStarManhattanPathFinder();
    }

    public List<Coordinate> findPath(final BasicSquareGrid gridContainer, final Coordinate start, final Coordinate goal) {

        final Cell[][] grid = gridContainer.getCells();
        final AStarNode[][] copiedGrid = new AStarNode[grid.length][grid[0].length];

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                copiedGrid[x][y] = new AStarNode(grid[x][y].getCoordinate(), goal);
            }
        }

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
                final int predictedDistanceToStartNode = currentNode.getDistanceFromStartToNode() + 1;
                if (!neighbour.isClosed() || (neighbour.getDistanceFromStartToNode() > predictedDistanceToStartNode)) {
                    neighbour.setPredecessor(currentNode);
                    neighbour.setDistanceFromStartToNode(predictedDistanceToStartNode);
                    neighbour.setClosed();
                    openSet.add(neighbour);
                }
            }

        }
        throw new IllegalStateException("No path found");
    }

    private List<Coordinate> computePath(final AStarNode endNode) {
        final List<Coordinate> path = new ArrayList<>();
        path.add(endNode.getUnderlyingCoordinate());
        AStarNode currentNode = endNode;
        while (currentNode.getPredecessor() != null) {
            currentNode = currentNode.getPredecessor();
            path.add(currentNode.getUnderlyingCoordinate());
        }
        Collections.reverse(path);
        return path;
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
