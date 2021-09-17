package fr.li212.codingame.tron.infrastructure.astar;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public class AStarPathFinder {

    public static AStarPathFinder getNew() {
        return new AStarPathFinder();
    }

    public List<CellWithHeuristic> findPath(final AStarGrid aStarGrid, final CellWithHeuristic start, final CellWithHeuristic goal) {

        final Map<CellWithHeuristic, AStarNode> copiedGrid = new HashMap<>();

        final CellWithHeuristic[] cellArray = aStarGrid.getAStarCells();
        for(CellWithHeuristic current: cellArray){
            copiedGrid.put(current, new AStarNode(current, goal));
        }

        final Map<AStarNode, AStarNode[]> cachedNeighbours = new HashMap<>();

        AStarNode startNode = copiedGrid.get(start);
        AStarNode goalNode = copiedGrid.get(goal);

        final Queue<AStarNode> openSet = new PriorityBlockingQueue<>();
        final Map<AStarNode, AStarNode> closedMap = new HashMap<>();

        startNode.setDistanceFromStartToNode(0);
        openSet.add(startNode);
        closedMap.put(startNode, startNode);
        while (!openSet.isEmpty()) {
            AStarNode currentNode = openSet.remove();
            if (currentNode.equals(goalNode)) {
                return computePath(currentNode);
            }
            AStarNode[] neighbours = getNeighbours(currentNode, cachedNeighbours, copiedGrid, aStarGrid);
            for (AStarNode neighbour : neighbours) {
                final int predictedDistanceToStartNode = currentNode.getDistanceFromStartToNode() + neighbour.getUnderlyingNode().getPriceToGo();
                final Optional<AStarNode> nodeInClosedMap = Optional.ofNullable(closedMap.get(neighbour));
                if (!nodeInClosedMap.isPresent() || (nodeInClosedMap.get().getDistanceFromStartToNode() > predictedDistanceToStartNode)) {
                    neighbour.setPredecessor(currentNode);
                    neighbour.setDistanceFromStartToNode(predictedDistanceToStartNode);
                    closedMap.put(neighbour, neighbour);
                    openSet.add(neighbour);
                }
            }

        }
        throw new IllegalStateException("No path found");
    }

    private List<CellWithHeuristic> computePath(final AStarNode startNode) {
        final List<AStarNode> path = new ArrayList<>();
        path.add(startNode);
        AStarNode currentNode = startNode;
        while (currentNode.getPredecessor() != null) {
            currentNode = currentNode.getPredecessor();
            path.add(currentNode);
        }
        return path.stream().map(AStarNode::getUnderlyingNode).collect(Collectors.toList());
    }


    private AStarNode[] getNeighbours(
            final AStarNode node,
            final Map<AStarNode, AStarNode[]> cachedNeighbours,
            final Map<CellWithHeuristic, AStarNode> nodePool,
            final AStarGrid grid) {
        if (!cachedNeighbours.containsKey(node)) {
            List<CellWithHeuristic> rawNeighbours = grid.getNeighbours(node.getUnderlyingNode());
            final AStarNode[] neighbours = new AStarNode[rawNeighbours.size()];

            for (int i = 0; i < rawNeighbours.size(); i++) {
                neighbours[i] = nodePool.get(rawNeighbours.get(i));
            }
            cachedNeighbours.put(node, neighbours);
        }
        return cachedNeighbours.get(node);
    }
}
