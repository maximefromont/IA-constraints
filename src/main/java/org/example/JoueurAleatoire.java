package org.example;

import org.example.enums.TEAM_COLOR;
import org.example.move.Move;
import org.example.move.RegularMove;

import java.util.ArrayList;

public class JoueurAleatoire implements IJoueur {

    //PRIVATE ATTRIBUTES
    private int id;
    private TEAM_COLOR playerColor;
    private EscampeBoard board; //This is a reference to the board that is instancied in the main


    //CONSTRUCTOR
    public JoueurAleatoire() {
    }

    @Override
    public void initJoueur(int mycolour) {
        this.id = (int) (Math.random() * 1000);
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
        System.out.println(board.playedCoups());
        if(board.playedCoups() == 0){
            ArrayList<String> availablesCases = new ArrayList<>();
            String move = "";
            availablesCases.add("A1");
            availablesCases.add("B1");
            availablesCases.add("C1");
            availablesCases.add("D1");
            availablesCases.add("E1");
            availablesCases.add("F1");
            availablesCases.add("A2");
            availablesCases.add("B2");
            availablesCases.add("C2");
            availablesCases.add("D2");
            availablesCases.add("E2");
            availablesCases.add("F2");
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
        } else if (board.playedCoups() == 1) {
            ArrayList<String> availablesCases = new ArrayList<>();
            String move = "";
            availablesCases.add("A5");
            availablesCases.add("B5");
            availablesCases.add("C5");
            availablesCases.add("D5");
            availablesCases.add("E5");
            availablesCases.add("F5");
            availablesCases.add("A6");
            availablesCases.add("B6");
            availablesCases.add("C6");
            availablesCases.add("D6");
            availablesCases.add("E6");
            availablesCases.add("F6");
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
         int randomIndex = (int) (Math.random() * moveArray.length);
         board.play(moveArray[randomIndex], TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
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
        System.out.println("Coup ennemi : " + coup);
        TEAM_COLOR ennemiColor = TEAM_COLOR.getOppositeTeamColor(playerColor);
        board.play(coup, TEAM_COLOR.getTeamColorStringFromTeamColor(ennemiColor));
    }

    @Override
    public String binoName() {
        return "Golmonisator" + " - " + id;
    }
}
