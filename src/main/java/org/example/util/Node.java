package org.example.util;

public class Node {
    private Node[] children;
    private int[] values;
    private String move;
    private int depth;

    public Node(Node[] children, int[] values, String move, int depth, int maxDepth) {
        if(depth < maxDepth) {
            this.children = children; // this should be allowed moves within the max depth
        }
        this.values = values; // Valeurs de nos heuristiques
        this.move = move;
        this.depth = depth;
    }

    public Node(Node[] children, int[] values, String move, int maxDepth) {
        if(depth < maxDepth) {
            this.children = children; // this should be allowed moves within the max depth
        }
        this.values = values; // Valeurs de nos heuristiques
        this.move = move;
        this.depth = 0;
    }

    public Node[] getChildren() {
        return children;
    }

    public int[] getValues() {
        return values;
    }

    public String getMove() {
        return move;
    }

    public int getDepth() {
        return depth;
    }
}
