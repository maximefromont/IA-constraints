package org.example;

public class Main {
    public static void main(String[] args) {
        EscampeBoard escampeBoard = new EscampeBoard();
        escampeBoard.printBoard();
        escampeBoard.setFromFile("/home/kevin/Cour/résolution de contrainte/projet/demo1_board.txt");
        escampeBoard.printBoardWithPion();
        escampeBoard.saveToFile("/home/kevin/Cour/résolution de contrainte/projet/demo2_board.txt");
    }
}