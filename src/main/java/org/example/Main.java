package org.example;

public class Main {
    public static void main(String[] args) {
        EscampeBoard escampeBoard = new EscampeBoard();
        escampeBoard.printBoard();
        escampeBoard.setFromFile("src/demo1_board.txt");
        escampeBoard.printBoardWithPion();
        escampeBoard.saveToFile("src/demo2_board.txt");
    }
}