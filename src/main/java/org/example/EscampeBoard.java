package org.example;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EscampeBoard implements Partie1 {

    //PRIVATE ATTRIBUTES

    private ArrayList<Coup> coups;
    private Case[][] boardArray;

    //CONSTRUCTOR
    public EscampeBoard() {
        boardArray = new Case[BOARD_SIZE][BOARD_SIZE];

        //Initializing the board with straight values from the subject
        //1st line
        boardArray[0][0] = new Case(1);
        boardArray[0][1] = new Case(2);
        boardArray[0][2] = new Case(2);
        boardArray[0][3] = new Case(3);
        boardArray[0][4] = new Case(1);
        boardArray[0][5] = new Case(2);
        //2nd line
        boardArray[1][0] = new Case(3);
        boardArray[1][1] = new Case(1);
        boardArray[1][2] = new Case(3);
        boardArray[1][3] = new Case(1);
        boardArray[1][4] = new Case(3);
        boardArray[1][5] = new Case(2);
        //3rd line
        boardArray[2][0] = new Case(2);
        boardArray[2][1] = new Case(3);
        boardArray[2][2] = new Case(1);
        boardArray[2][3] = new Case(2);
        boardArray[2][4] = new Case(1);
        boardArray[2][5] = new Case(3);
        //4th line
        boardArray[3][0] = new Case(2);
        boardArray[3][1] = new Case(1);
        boardArray[3][2] = new Case(3);
        boardArray[3][3] = new Case(2);
        boardArray[3][4] = new Case(3);
        boardArray[3][5] = new Case(1);
        //5th line
        boardArray[4][0] = new Case(1);
        boardArray[4][1] = new Case(3);
        boardArray[4][2] = new Case(1);
        boardArray[4][3] = new Case(3);
        boardArray[4][4] = new Case(1);
        boardArray[4][5] = new Case(2);
        //6th line
        boardArray[5][0] = new Case(3);
        boardArray[5][1] = new Case(2);
        boardArray[5][2] = new Case(2);
        boardArray[5][3] = new Case(1);
        boardArray[5][4] = new Case(3);
        boardArray[5][5] = new Case(2);
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
                            boardArray[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_LICORNE, 1));
                            break;
                        case 'n':
                            System.out.println("white paladium");
                            System.out.println("row: " + (row-1) + " col: " + (col-3));
                            boardArray[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_PALADIN, 1));
                            break;
                        case 'B':
                            System.out.println("black licorne");
                            System.out.println("row: " + (row-1) + " col: " + (col-3));
                            boardArray[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_LICORNE, 2));
                            break;
                        case 'b':
                            System.out.println("black paladium");
                            System.out.println("row: " + (row-1) + " col: " + (col-3));
                            boardArray[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_PALADIN, 2));
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
        File file = new File(fileName);

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        try {
            FileWriter writer = new FileWriter(fileName, false); // false means do not append

            //create the file if it does not exist


            //write to the first line of the file "% ABCDEF"
            writer.write("% ABCDEF\n");
            String boardString= boardToString();

            for (int i = 0; i < 6; i++) {
                writer.write("0"+(i+1)+" "+boardString.substring(i*6,i*6+6)+" 0"+(i+1)+"\n");
            }
            writer.write("% ABCDEF\n");



            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



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
                System.out.print(boardArray[i][j].getValue() + " ");
            }
            System.out.println();
        }
    }

    public void printBoardWithPion() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (boardArray[i][j].getPion() != null) {
                    if (boardArray[i][j].getPion().getPlayerId() == 1) {
                        if (boardArray[i][j].getPion().getType().equals(Pion.PION_TYPE_LICORNE)) {
                            System.out.print("N ");
                        } else {
                            System.out.print("n ");
                        }
                    } else {
                        if (boardArray[i][j].getPion().getType().equals(Pion.PION_TYPE_LICORNE)) {
                            System.out.print("B ");
                        } else {
                            System.out.print("b ");
                        }

                    }

                } else {
                    System.out.print(boardArray[i][j].getValue() + " ");
                }
            }
            System.out.println();
        }
    }

    public String boardToString() {
        String res = "";
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (boardArray[i][j].getPion() != null) {
                    if (boardArray[i][j].getPion().getPlayerId() == 1) {
                        if (boardArray[i][j].getPion().getType().equals(Pion.PION_TYPE_LICORNE)) {
                            res += "N";
                        } else {
                            res += "n";
                        }
                    } else {
                        if (boardArray[i][j].getPion().getType().equals(Pion.PION_TYPE_LICORNE)) {
                            res += "B";
                        } else {
                            res += "b";
                        }

                    }

                } else {
                    res +="-";
                }
            }
        }
        return res;
    }

    //PRIVATE CONSTANTS
    private static final int BOARD_SIZE = 6;
}
