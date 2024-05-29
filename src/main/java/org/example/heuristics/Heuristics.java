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

    public static int directWinHeuristic(Node node, String player){

        if(node.getDepth() == 1 && node.getBoard().gameOver()){
            return 1000;
        }
        return 0;

    }
}
