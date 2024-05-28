package org.example.heuristics;

import org.example.EscampeBoard;

public class Heuristics {
    public static int mobilityHeuristic(EscampeBoard board, String player){
        String opponent = "noir";
        if(player.equals("noir")){
            opponent = "blanc";
        }
        return board.possiblesMoves(player).length - board.possiblesMoves(opponent).length;
    }
}
