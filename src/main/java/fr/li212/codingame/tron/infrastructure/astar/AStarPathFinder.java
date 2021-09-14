package fr.li212.codingame.tron.infrastructure.astar;

import java.util.*;

public class AStarPathFinder {

    private static Long numberOfcalls = 0L;
    private static Long cumulatedTime = 0L;

    private final Map<AStarNode, NodeStatus> openSet = new HashMap<>();
    private final Map<AStarNode, NodeStatus> closeSet = new HashMap<>();

    private NodeStatus currentNode;
    private final Map<AStarNode, NodeStatus> nodeStatuses = new HashMap<>();

    public static AStarPathFinder getNew(){
        return new AStarPathFinder();
    }

    public List<AStarNode> findPath(final AStarGrid aStarGrid, final AStarNode start, final AStarNode goal) {
        final long startTime = System.currentTimeMillis();
        numberOfcalls++;
        nodeStatuses.putIfAbsent(start, new NodeStatus(start));
        this.openSet.put(start, nodeStatuses.get(start));
        nodeStatuses.get(start).setG(0);

        while (!openSet.isEmpty()) {
            Optional<NodeStatus> optionalCurrentNode = this.getLowestDistanceNode(goal);
            currentNode = null;

            if (optionalCurrentNode.isPresent()) {
                currentNode = optionalCurrentNode.get();
            } else {
                break;
            }
            if (currentNode.getUnderlyingNode().equals(goal)) {
                break;
            }

            Set<AStarNode> successors = aStarGrid.getNeighbours(currentNode.getUnderlyingNode());
            for (AStarNode successor : successors) {
                this.processSuccesorNode(successor);
            }
            this.closeSet.put(currentNode.getUnderlyingNode(), currentNode);
        }

        List<AStarNode> path = new ArrayList<>();
        path.add(goal);
        final NodeStatus status = nodeStatuses.get(goal);
        AStarNode current = status.getParent();
        while (current != null) {
            path.add(current);
            current = nodeStatuses.get(current).getParent();
        }

        cumulatedTime += System.currentTimeMillis() - startTime;
        if(numberOfcalls%100 == 0) {
            System.err.println("A* CALL: " + numberOfcalls + " CUMULATED TIME: " + cumulatedTime);
        }
        return path;
    }

    private void processSuccesorNode(AStarNode successor) {
        int priceToGo = successor.getPriceToGo();
        nodeStatuses.putIfAbsent(successor, new NodeStatus(successor));
        nodeStatuses.get(successor).setG(currentNode.getG() + priceToGo);
        NodeStatus nodeInOpenSet = this.findNodeInOpenSet(successor);
        if (nodeInOpenSet != null && nodeStatuses.get(successor).getG() >= nodeInOpenSet.getG()) {
            return;
        }
        NodeStatus nodeInClosedSet = this.findNodeInClosedSet(successor);
        if (nodeInClosedSet != null && nodeStatuses.get(successor).getG() >= nodeInClosedSet.getG()) {
            return;
        }

        closeSet.remove(nodeStatuses.get(successor));
        nodeStatuses.get(successor).setParent(currentNode.getUnderlyingNode());

        openSet.put(successor, nodeStatuses.get(successor));
    }

    private Optional<NodeStatus> getLowestDistanceNode(AStarNode goal) {
        return this.openSet.values().stream().filter(x -> !this.closeSet.containsKey(x.getUnderlyingNode())).min(Comparator.comparing(x -> x.getF(goal)));
    }

    private NodeStatus findNodeInOpenSet(AStarNode cell) {
        return this.openSet.get(cell);

    }

    private NodeStatus findNodeInClosedSet(AStarNode cell) {
        return this.closeSet.get(cell);

    }
}
