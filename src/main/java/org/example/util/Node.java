package org.example.util;
import org.example.Printinator;
import org.example.heuristics.Heuristics;
import org.example.EscampeBoard;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node {
    private ArrayList<Node> children;
    private int values;
    private final String move;
    private int depth = 0;
    private String player ;
    private String friend;
    private String ennemy;
    private final EscampeBoard board;

    public Node(String move, int depth, int maxDepth, EscampeBoard board, String friend) {

        this.ennemy = friend.equals("blanc") ? "noir" : "blanc";
        if (depth%2 == 0) {
            this.player = friend;
        }else {
            this.player = ennemy;
        }

        this.friend = friend;
        this.children = new ArrayList<Node>();
        this.move = move;
        this.depth = depth;
        this.board = board;
        if(depth < maxDepth && !board.gameOver()) {
            // this should be allowed moves within the max depth
            //display possible moves
            if(board.possiblesMoves(player).length == 0){
                children.add(new Node("e", depth+1, maxDepth, board, friend));
            }else {
                for (String nodeMove : board.possiblesMoves(player)) {
                    //decalre childre as arraylist
                    EscampeBoard tempBoard = board.clone();
                    tempBoard.play(nodeMove, player);
                    children.add(new Node(nodeMove, depth + 1, maxDepth, tempBoard, friend));
                }
            }
            values = value(children, player);
        } else {

            values = Heuristics.HeuristicValue(this, player);
        }

        // Printinator.printBoardWithPion(board.getBoardArray(), null);
        if (depth == 1) {
            System.out.print("Move : " + move);
            System.out.print(" node created  ");
            //print number of coup played in the board
            System.out.print("Depth : " + depth);
            System.out.print(" Nombre de coup  : " + this.board.playedCoups());
            System.out.println(" Value : " + values);
        }


    }

    private int value (ArrayList<Node> childrens, String player){
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for(Node child : childrens){
            if(child.getValues()>max){
                max = child.getValues();
            }
            if(child.getValues()<min){
                min = child.getValues();
            }
        }
        if(player.equals(friend)){
            return max;
        }
        return min;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public int getValues() {
        return values;
    }

    public String getMove() {
        return move;
    }

    public int getDepth() {
        return depth;
    }

    public String getBestMove(){
        //get the move in the children with the same value as the node
        for(Node child : children){
            if(child.getValues() == values){
                return child.getMove();
            }
        }
        return "e";
    }

    public EscampeBoard getBoard() {
        return board;
    }

    //get friend and ennemy

    public String getFriend() {
        return friend;
    }

    public String getEnnemy() {
        return ennemy;
    }




    //create a  main method
    public static void main(String[] args) {
        EscampeBoard escampeBoard = new EscampeBoard();
        Printinator.printBoard(escampeBoard.getBoardArray(), "Lisere map :");
        Printinator.printLineSpace();
        escampeBoard.setFromFile("src/demo1_board.txt");
        Printinator.printBoardWithPion(escampeBoard.getBoardArray(), null);
        Node node = new Node("e", 0, 3, escampeBoard, "noir");
        System.out.println(node.getBestMove());
    }
}
