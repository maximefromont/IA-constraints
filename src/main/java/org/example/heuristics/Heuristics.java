package org.example.heuristics;

import org.example.util.Node;
import org.example.EscampeBoard;

public class Heuristics {
    public static int mobilityHeuristic(EscampeBoard board, String player){
        String opponent = "noir";
        if(player.equals("noir")){
            opponent = "blanc";
        }
        return board.possiblesMoves(player).length - board.possiblesMoves(opponent).length;
    }

    public static int directWinHeuristic(Node node){

        if(node.getDepth() == 1 && node.getBoard().gameOver()){
            return 1000;
        }
        return 0;

    }
    public static int directLoseHeuristic(Node node){
        if(node.getDepth() == 2 && node.getBoard().gameOver()){
            return 1000;
        }
        return 0;
    }

    public static int Win(Node node){
        if(node.getDepth() >= 3 && node.getBoard().gameOver() && node.getBoard().getWinner().equals(node.getFriend())){
            return 100;
        }
        return 0;
    }

    public static int Lose(Node node){
        if(node.getDepth() >= 3 && node.getBoard().gameOver() && node.getBoard().getWinner().equals(node.getEnnemy())){
            return 100;
        }
        return 0;
    }


    public static  int HeuristicValue(Node node, String player){
        return directWinHeuristic(node) + directLoseHeuristic(node) + Win(node) + Lose(node) + mobilityHeuristic(node.getBoard(), player);
    }
}
