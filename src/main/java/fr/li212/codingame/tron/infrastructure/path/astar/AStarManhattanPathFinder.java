package fr.li212.codingame.tron.infrastructure.path.astar;

import fr.li212.codingame.tron.adapters.grid.BasicSquareGrid;
import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.infrastructure.path.Path;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class AStarManhattanPathFinder {
    private final int width;
    private final int height;
    private final BasicSquareGrid gridContainer;
    final AStarNode[][] copiedGrid;

    public AStarManhattanPathFinder(final BasicSquareGrid gridContainer) {
        this.gridContainer = gridContainer;
        this.width = gridContainer.getWidth();
        this.height = gridContainer.getHeight();
        this.copiedGrid = initialize();
    }

    public Path findPath(final Coordinate start, final Coordinate goal) {

        this.resetCopiedGrid(goal);

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
            final Collection<AStarNode> neighbours = this.getNeighbours(currentNode);
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

    private void resetCopiedGrid(final Coordinate goal) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                copiedGrid[x][y].reset(Math.abs(x - goal.getX()) + Math.abs(y - goal.getY()));
            }
        }
    }

    private AStarNode[][] initialize() {
        final Cell[][] grid = gridContainer.getCells();
        final AStarNode[][] copiedGrid = new AStarNode[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                copiedGrid[x][y] = new AStarNode(grid[x][y].getCoordinate());
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

    private List<AStarNode> getNeighbours(final AStarNode currentNode) {
        final List<Coordinate> neighbours = gridContainer.getNeighbours(currentNode.getUnderlyingCoordinate());
        List<AStarNode> result = new ArrayList<>(4);
        for (Coordinate coordinate : neighbours) {
            result.add(copiedGrid[coordinate.getX()][coordinate.getY()]);
        }
        return result;
    }
}
