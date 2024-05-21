package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EscampeBoard implements Partie1 {

    //PRIVATE ATTRIBUTES
    private Case[][] board;

    //CONSTRUCTOR
    public EscampeBoard() {
        board = new Case[BOARD_SIZE][BOARD_SIZE];

        //Initializing the board with straight values from the subject
        //1st line
        board[0][0] = new Case(1);
        board[0][1] = new Case(2);
        board[0][2] = new Case(2);
        board[0][3] = new Case(3);
        board[0][4] = new Case(1);
        board[0][5] = new Case(2);
        //2nd line
        board[1][0] = new Case(3);
        board[1][1] = new Case(1);
        board[1][2] = new Case(3);
        board[1][3] = new Case(1);
        board[1][4] = new Case(3);
        board[1][5] = new Case(2);
        //3rd line
        board[2][0] = new Case(2);
        board[2][1] = new Case(3);
        board[2][2] = new Case(1);
        board[2][3] = new Case(2);
        board[2][4] = new Case(1);
        board[2][5] = new Case(3);
        //4th line
        board[3][0] = new Case(2);
        board[3][1] = new Case(1);
        board[3][2] = new Case(3);
        board[3][3] = new Case(2);
        board[3][4] = new Case(3);
        board[3][5] = new Case(1);
        //5th line
        board[4][0] = new Case(1);
        board[4][1] = new Case(3);
        board[4][2] = new Case(1);
        board[4][3] = new Case(3);
        board[4][4] = new Case(1);
        board[4][5] = new Case(2);
        //6th line
        board[5][0] = new Case(3);
        board[5][1] = new Case(2);
        board[5][2] = new Case(2);
        board[5][3] = new Case(1);
        board[5][4] = new Case(3);
        board[5][5] = new Case(2);
    }

    @Override
    public void setFromFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            for (int row = 1; row < lines.size() - 1; row++) {
                line = lines.get(row);
                for (int col = 3; col < line.length() - 3; col++) {
                    char c = line.charAt(col);
                    switch (c) {
                        case 'N':
                            System.out.println("white licorne");
                            System.out.println("row: " + (row-1) + " col: " + (col-3));
                            board[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_LICORNE, 1));
                            break;
                        case 'n':
                            System.out.println("white paladium");
                            System.out.println("row: " + (row-1) + " col: " + (col-3));
                            board[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_PALADIN, 1));
                            break;
                        case 'B':
                            System.out.println("black licorne");
                            System.out.println("row: " + (row-1) + " col: " + (col-3));
                            board[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_LICORNE, 2));
                            break;
                        case 'b':
                            System.out.println("black paladium");
                            System.out.println("row: " + (row-1) + " col: " + (col-3));
                            board[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_PALADIN, 2));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j].getValue() + " ");
            }
            System.out.println();
        }
    }

    public void printBoardWithPion() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].getPion() != null) {
                    System.out.print(board[i][j].getPion().getType() + " ");
                } else {
                    System.out.print(board[i][j].getValue() + " ");
                }
            }
            System.out.println();
        }
    }

    //PRIVATE CONSTANTS
    private static final int BOARD_SIZE = 6;
}
