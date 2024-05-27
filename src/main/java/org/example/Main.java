package org.example;

public class Main {
    public static void main(String[] args) {
        EscampeBoard escampeBoard = new EscampeBoard();

        //Debugging prints
        Printinator.printBoard(escampeBoard.getBoardArray(), null);

        escampeBoard.setFromFile("src/demo1_board.txt");

        //Debugging prints
        Printinator.printBoardWithPion(escampeBoard.getBoardArray(), null);

        escampeBoard.saveToFile("src/demo2_board.txt");
    }
}