package org.example;

import org.example.enums.TEAM_COLOR;
import org.example.move.Move;
import org.example.move.PositionMove;
import org.example.move.RegularMove;

import java.util.ArrayList;

public class JoueurAleatoire implements IJoueur {

    //PRIVATE ATTRIBUTES
    private TEAM_COLOR playerColor;
    private EscampeBoard board; //This is a reference to the board that is instancied in the main

    private ArrayList<String> upperStart = new ArrayList<>() {{
        add("A1");
        add("B1");
        add("C1");
        add("D1");
        add("E1");
        add("F1");
        add("A2");
        add("B2");
        add("C2");
        add("D2");
        add("E2");
        add("F2");
    }};

    private ArrayList<String> lowerStart = new ArrayList<>() {{
        add("A5");
        add("B5");
        add("C5");
        add("D5");
        add("E5");
        add("F5");
        add("A6");
        add("B6");
        add("C6");
        add("D6");
        add("E6");
        add("F6");
    }};

    //CONSTRUCTOR
    public JoueurAleatoire() {
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
                for (String s : upperStart){
                    Coordinate c = new Coordinate(s);
                    if(!board.isFree(c, playerColor)){
                        System.out.println("upperStart is not free : "+ s);
                        goLower = true;
                        break;
                    }
                }
            }
            ArrayList<String> availablesCases = upperStart;
            if(goLower){
                availablesCases = lowerStart;
            }
            String move = "";

            for (int i = 0; i<6 ; i++){
                int place = (int) (Math.random() * availablesCases.size());
                move = move + availablesCases.get(place);
                availablesCases.remove(place);
                if(i != 5){
                    move = move + "/";
                }
            }
            board.play(move, TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
            return move;
        }
        String[] moveArray = board.possiblesMoves(TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
        //if possible moves is empty, or null return e
        if (moveArray == null || moveArray.length == 0) {
            return "E";
        }else if(moveArray.length == 1) {
            board.play(moveArray[0], TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
            return moveArray[0];
        }

         int randomIndex = (int) (Math.random() * moveArray.length);
         if(!moveArray[randomIndex].equals("E")) {
             board.play(moveArray[randomIndex], TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
         }
         return moveArray[randomIndex];
    }

    @Override
    public void declareLeVainqueur(int colour) {
        TEAM_COLOR winningCollor = TEAM_COLOR.getTeamColorFromInt(colour);
        if (winningCollor == playerColor) {
            System.out.println("What a beautiful day, I have zero problems in life \uD83E\uDD13 !");
        } else {
            System.out.println("What a terrible day, I have only problems in life \uD83D\uDE3E !");
        }
    }

    @Override
    public void mouvementEnnemi(String coup) {
        TEAM_COLOR ennemiColor = TEAM_COLOR.getOppositeTeamColor(playerColor);
        if(!coup.equals("E")) {
            board.play(coup, TEAM_COLOR.getTeamColorStringFromTeamColor(ennemiColor));
        }
    }

    @Override
    public String binoName() {
        return "Golmonisator" + " - " + Math.random() * 1000;
    }
}
