package org.example;

import org.example.enums.TEAM_COLOR;
import org.example.move.Move;
import org.example.move.RegularMove;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JoueurHumain implements IJoueur {

    //PRIVATE ATTRIBUTES
    private int id;
    private TEAM_COLOR playerColor;
    private EscampeBoard board; //This is a reference to the board that is instancied in the main


    //CONSTRUCTOR
    public JoueurHumain() {
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
        System.out.println("Enter your coup : ");
        String move = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            move = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!move.equals("E")) {
            board.play(move, TEAM_COLOR.getTeamColorStringFromTeamColor(playerColor));
        }
        return move;
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
        if(!coup.equals("E")) {
            board.play(coup, TEAM_COLOR.getTeamColorStringFromTeamColor(ennemiColor));
        }
    }

    @Override
    public String binoName() {
        return "Humain" + " - " + id;
    }
}
