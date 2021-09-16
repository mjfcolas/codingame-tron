package fr.li212.codingame.tron.infrastructure.astar;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public class AStarPathFinder {

    private static Long numberOfcalls = 0L;
    private static Long cumulatedTime = 0L;

    public static AStarPathFinder getNew() {
        return new AStarPathFinder();
    }

    public List<CellWithHeuristic> findPath(final AStarGrid aStarGrid, final CellWithHeuristic start, final CellWithHeuristic goal) {
        final long startTime = System.currentTimeMillis();
        numberOfcalls++;

        final Set<AStarNode> copiedGrid = aStarGrid.getAStarCells().stream()
                .map(cellWithHeuristic -> new AStarNode(cellWithHeuristic, aStarGrid, goal))
                .collect(Collectors.toSet());

        AStarNode startNode = copiedGrid.stream().filter(node -> node.getUnderlyingNode().equals(start)).findAny().orElseThrow(() -> new IllegalStateException("Start not found in grid"));
        AStarNode goalNode = copiedGrid.stream().filter(node -> node.getUnderlyingNode().equals(goal)).findAny().orElseThrow(() -> new IllegalStateException("Goal not found in grid"));

        final Queue<AStarNode> openSet = new PriorityBlockingQueue<>();
        final Map<AStarNode, AStarNode> closedMap = new HashMap<>();

        startNode.setDistanceFromStartToNode(0);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            AStarNode currentNode = openSet.remove();
            if (currentNode.equals(goalNode)) {
                cumulatedTime += System.currentTimeMillis() - startTime;
                if (numberOfcalls % 100 == 0) {
                    System.err.println("A* CALL: " + numberOfcalls + " CUMULATED TIME: " + cumulatedTime);
                }
                return computePath(currentNode);
            }
            Set<AStarNode> neighbours = currentNode.getNeighbours();
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

    private List<CellWithHeuristic> computePath(final AStarNode startNode){
        final List<AStarNode> path = new ArrayList<>();
        path.add(startNode);
        AStarNode currentNode = startNode;
        while (currentNode.getPredecessor() != null) {
            currentNode = currentNode.getPredecessor();
            path.add(currentNode);
        }
        return path.stream().map(AStarNode::getUnderlyingNode).collect(Collectors.toList());
    }
}
