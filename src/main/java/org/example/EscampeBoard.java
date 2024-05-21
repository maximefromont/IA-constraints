package org.example;

public class EscampeBoard implements Partie1 {

    //PRIVATE ATTRIBUTES
    private Case[][] board;

    //CONSTRUCTOR
    public EscampeBoard() {
        this.board = new Case[BOARD_SIZE][BOARD_SIZE];

        //Initialise cases (and their values)
        
    }

    @Override
    public void setFromFile(String fileName) {

    }

    @Override
    public void saveToFile(String fileName) {

    }

    @Override
    public boolean isValidMove(String move, String player) {
        return false;
    }

    @Override
    public String[] possiblesMoves(String player) {
        return new String[0];
    }

    @Override
    public void play(String move, String player) {

    }

    @Override
    public boolean gameOver() {
        return false;
    }

    //PRIVATE CONSTANTS
    private static final int BOARD_SIZE = 6;
}
