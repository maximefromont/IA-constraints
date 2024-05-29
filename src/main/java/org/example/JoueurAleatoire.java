package org.example;

import org.example.enums.TEAM_COLOR;
import org.example.move.Move;
import org.example.move.RegularMove;

public class JoueurAleatoire implements IJoueur {

    //PRIVATE ATTRIBUTES
    private TEAM_COLOR playerColor;
    private EscampeBoard board; //This is a reference to the board that is instancied in the main


    //CONSTRUCTOR
    public JoueurAleatoire(EscampeBoard board) {
        this.board = board; //The board is instancied in the constructor, however it's important that the referee call the initJoueur method before actually starting to play
    }

    @Override
    public void initJoueur(int mycolour) {
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
        TEAM_COLOR ennemiColor = TEAM_COLOR.getOppositeTeamColor(playerColor);
        board.play(coup, TEAM_COLOR.getTeamColorStringFromTeamColor(ennemiColor));
    }

    @Override
    public String binoName() {
        return null;
    }
}
