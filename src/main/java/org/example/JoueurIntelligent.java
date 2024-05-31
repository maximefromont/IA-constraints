package org.example;

import org.example.enums.TEAM_COLOR;
import org.example.move.Move;
import org.example.move.PositionMove;
import org.example.move.RegularMove;
import org.example.util.Node;

import java.util.ArrayList;
import java.util.Arrays;

public class JoueurIntelligent implements IJoueur {

    //PRIVATE ATTRIBUTES
    private TEAM_COLOR playerColor;
    private EscampeBoard board; //This is a reference to the board that is instancied in the main

    private final String lowerStart = "C6/B5/C5/D5/E5/F5";

    private final String upperStart = "C1/B2/C2/D2/E2/F2";

    //CONSTRUCTOR
    public JoueurIntelligent() {
    }

    @Override
    public void initJoueur(int mycolour) {
        this.board = new EscampeBoard();
        //this.board.setFromFile("src/demo1_board.txt");
        this.playerColor = TEAM_COLOR.getTeamColorFromInt(mycolour);
    }

    @Override
    public int getNumJoueur() {
        if (playerColor == TEAM_COLOR.WHITE_TEAM) {
            return TEAM_COLOR.WHITE_TEAM_INT;
        } else if (playerColor == TEAM_COLOR.BLACK_TEAM){
            return TEAM_COLOR.BLACK_TEAM_INT;
        }

        return 0; //By default 0 = error
    }

    @Override
    public String choixMouvement() {
        if(board.playedCoups()<=1){
            boolean goLower;
            if(playerColor == TEAM_COLOR.BLACK_TEAM){
                goLower = Math.random() < 0.5;
            }
            else{
                // check if all the cases in upperStart are empty
                goLower = false;
                ArrayList<String> upperCases = new ArrayList<>(Arrays.asList("A1", "B1", "C1", "D1", "E1", "F1", "A2", "B2", "C2", "D2", "E2", "F2"));
                for (String s : upperCases){
                    Coordinate c = new Coordinate(s);
                    if(!board.isFree(c)){
                        System.out.println("upperStart is not free : "+ s);
                        goLower = true;
                        break;
                    }
                }
            }
            if(goLower){
                board.play(lowerStart, TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
                return lowerStart;
            }
            board.play(upperStart, TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
            return upperStart;
        }
        System.out.println("Current lisere before coup : " + board.getLastLisere());
        Node state = new Node("E", 0, 3, board, TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
        String move = state.getBestMove();
        if(!move.equals("E")) {
            board.play(move, TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
            board.setLastLisere(board.getLisereValue(new Coordinate(move.substring(3, 5))));

        }

        System.out.println("Played Coup : " + move);
        System.out.println("Current lisere after coup : " + board.getLastLisere());
        System.out.println("Current board :" );
        Printinator.printBoardWithPion(board.getBoardArray(),null);
        System.out.println("-------------------");
        return move;
    }

    @Override
    public void declareLeVainqueur(int colour) {
        TEAM_COLOR winningCollor = TEAM_COLOR.getTeamColorFromInt(colour);
        if (winningCollor == playerColor) {
            System.out.println("(Smart player) What a beautiful day, I have zero problems in life \uD83E\uDD13 !");
        } else {
            System.out.println("(Smart player) What a terrible day, I have only problems in life \uD83D\uDE3E !");
        }
    }

    @Override
    public void mouvementEnnemi(String coup) {
        System.out.println("Coup ennemi : " + coup);

        TEAM_COLOR ennemiColor = TEAM_COLOR.getOppositeTeamColor(playerColor);
        if(!coup.equals("E")) {

            board.play(coup, TEAM_COLOR.getTeamColorStringFromTeamColor(ennemiColor));

            //test the number of played coups
            if(board.playedCoups() >= 3){
                System.out.println(coup.substring(3, 5));
                System.out.println("liseré de fin du coup                                          :"+board.getLisereValue(new Coordinate(coup.substring(3, 5))));
                board.setLastLisere(board.getLisereValue(new Coordinate(coup.substring(3, 5))));
            }
        }
                System.out.println("Current lisere after adverse coup                               :" + board.getLastLisere());
        System.out.println("Current board after adverse coup :");
        Printinator.printBoardWithPion(board.getBoardArray(),null);

        System.out.println("-------------------");
    }

    @Override
    public String binoName() {
        return "Intello" + " - " + Math.random() * 1000;
    }
}
