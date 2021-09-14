package fr.li212.codingame.tron.infrastructure.astar;

public class NodeStatus {

    private AStarNode parent;

    private final AStarNode underlyingNode;
    private int g = 0;

    public NodeStatus(final AStarNode underlyingNode) {
        this.underlyingNode = underlyingNode;
    }

    public int getF(AStarNode destNode) {
        return this.underlyingNode.getH(destNode) + this.g;
    }

    public void setG(final int g) {
        this.g = g;
    }

    public int getG() {
        return g;
    }

    public void setParent(AStarNode parent) {
        this.parent = parent;
    }

    public AStarNode getParent() {
        return parent;
    }

    public AStarNode getUnderlyingNode() {
        return underlyingNode;
    }
}
