package org.example;
import com.sun.tools.jconsole.JConsoleContext;
import org.example.move.Move;
import org.example.move.PositionMove;
import org.example.move.RegularMove;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

public class EscampeBoard implements Partie1 {

    //PRIVATE ATTRIBUTES

    private ArrayList<Move> move;
    private Case[][] boardArray;
    private int lastLisere;

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

                            boardArray[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_LICORNE, 1));
                            break;
                        case 'n':

                            boardArray[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_PALADIN, 1));
                            break;
                        case 'B':
                            boardArray[row-1][col-3].setPion(new Pion(Pion.PION_TYPE_LICORNE, 2));
                            break;
                        case 'b':

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
        PositionMove positionMove;
        Coordinate[] coordinateMoves;
        switch (playedCoups()){
            case 0:
                positionMove = PositionMove.fromString(move,2);
                coordinateMoves = positionMove.getCoordinates();
                if (coordinateMoves[0].getX()<2) {
                    for (int i = 0; i < 6; i++) {
                        if(coordinateMoves[i].getX()>=2){
                            return false;
                        }                    }
                }else{
                    for (int i = 0; i < 6; i++) {
                        if(coordinateMoves[i].getX()<=3){
                            return false;
                        }
                    }
                }
                return  true;
            case 1:

                if (this.move.getFirst() instanceof PositionMove) {
                    Coordinate firstMoveCoordinate = ((PositionMove) this.move.getFirst()).getCoordinates()[0];
                    positionMove = PositionMove.fromString(move,2);
                    coordinateMoves = positionMove.getCoordinates();


                    if (firstMoveCoordinate.getX() <2) {
                        for (int i = 0; i < 6; i++) {
                            if(coordinateMoves[i].getX()>3){
                                return false;
                            }
                        }


                    } else  {
                        for (int i = 0; i < 6; i++) {
                            if(coordinateMoves[i].getX()<2){
                                return false;
                            }
                        }
                    }




                }





                return true;




            default:
                //test if the la
                return  false;


        }


    }

    @Override
    public String[] possiblesMoves(String player) {
        return new String[0];
    }

    public int  playedCoups(){
        return move.size();
    }

    @Override
    public void play(String move, String player) {

    }

    @Override
    public boolean gameOver() {
        return false;
    }

    //***PERSONNAL FUNCTION*/

    public boolean isFree(Coordinate coordinate) {
        return boardArray[coordinate.getX()][coordinate.getY()].getPion() == null;
    }

    public boolean isInBoard(Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() < BOARD_SIZE && coordinate.getY() >= 0 && coordinate.getY() < BOARD_SIZE;
    }

    public boolean isValidMove(Move move, String player) {
        //make a switch case to test the type of the move
        switch (move.getClass().getName()) {
            case "org.example.move.PositionMove":

                for (int i = 0; i < 6; i++) {
                    for (int j = i + 1; j < 6; j++) {
                        if (((PositionMove) move).getCoordinates()[i].equals(((PositionMove) move).getCoordinates()[j])) {
                            return false;
                        }
                    }
                }
                //test if the player is the right one
                if (player.equals("noir")) {

                    //check if the coordinates are in the right place
                    if (((PositionMove) move).getCoordinates()[0].getX() < 2) {
                        for (int i = 0; i < 6; i++) {
                            if (((PositionMove) move).getCoordinates()[i].getX() >= 2) {
                                return false;
                            }
                        }
                    } else {
                        for (int i = 0; i < 6; i++) {
                            if (((PositionMove) move).getCoordinates()[i].getX() <= 3) {
                                return false;
                            }
                        }
                    }


                }
                else if (player.equals("blanc")) {
                    //chekc if the first this.move is a positionmove
                    if (this.move.size() == 0) {
                        return true;
                    } else if (this.move.getFirst() instanceof PositionMove) {
                        Coordinate firstMoveCoordinate = ((PositionMove) this.move.getFirst()).getCoordinates()[0];
                        for (int i = 0; i < 6; i++) {
                            if (firstMoveCoordinate.getX() < 2) {
                                if (((PositionMove) move).getCoordinates()[i].getX() > 3) {
                                    return false;
                                }
                            } else {
                                if (((PositionMove) move).getCoordinates()[i].getX() < 2) {
                                    return false;
                                }
                            }
                        }

                    } else {
                        return false;
                    }


                }

            case "org.example.move.RegularMove":
                //convert the move to a regular move
                assert move instanceof RegularMove;
                RegularMove regularMove = (RegularMove) move;

                switch (lastLisere){
                    case 1:
                        return this.isFree(regularMove.getEndCoordinate());
                    case 2:
                        //test if the final case is free
                        if(!this.isFree(regularMove.getEndCoordinate())){
                            return false;
                        }
                        //test if the intermediate case is free
                        else if(regularMove.getMove().equals(new Coordinate(0,2))){
                            //test if the intermediate case is free
                            return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()+1));
                        }
                        else if (regularMove.getMove().equals(new Coordinate(0,-2))){
                            return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()-1));
                        }
                        else if (regularMove.getMove().equals(new Coordinate(2,0))){
                            return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1,regularMove.getStartCoordinate().getY()));
                        }
                        else if (regularMove.getMove().equals(new Coordinate(-2,0))){
                            return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1,regularMove.getStartCoordinate().getY()));
                        }else if(regularMove.getMove().equals(new Coordinate(-1,1))) {
                            return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1,regularMove.getStartCoordinate().getY())) ||
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()+1));
                        }else if(regularMove.getMove().equals(new Coordinate(1,-1))) {
                            return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1,regularMove.getStartCoordinate().getY())) ||
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()-1));
                        }else if(regularMove.getMove().equals(new Coordinate(1,1))) {
                            return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1,regularMove.getStartCoordinate().getY())) ||
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()+1));
                        }else if(regularMove.getMove().equals(new Coordinate(-1,-1))) {
                            return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1,regularMove.getStartCoordinate().getY())) ||
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()-1));
                        }
                        else {
                            return false;
                        }
                    default:

                        //can the value of the case at the start of the move
                        int lisiereValue = boardArray[((RegularMove) move).getStartCoordinate().getX()][((RegularMove) move).getStartCoordinate().getY()].getValue();
                        //print the start coordinate

                        switch (lisiereValue){
                            case 1:
                                return this.isFree(regularMove.getEndCoordinate());
                            case 2:
                                //test if the final case is free
                                if(!this.isFree(regularMove.getEndCoordinate())){
                                    return false;
                                }
                                //test if the intermediate case is free
                                else if(regularMove.getMove().equals(new Coordinate(0,2))){
                                    //test if the intermediate case is free
                                    return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()+1));
                                }
                                else if (regularMove.getMove().equals(new Coordinate(0,-2))){
                                    return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()-1));
                                }
                                else if (regularMove.getMove().equals(new Coordinate(2,0))){
                                    return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1,regularMove.getStartCoordinate().getY()));
                                }
                                else if (regularMove.getMove().equals(new Coordinate(-2,0))){
                                    return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1,regularMove.getStartCoordinate().getY()));
                                }else if(regularMove.getMove().equals(new Coordinate(-1,1))) {
                                    return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1,regularMove.getStartCoordinate().getY())) ||
                                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()+1));
                                }else if(regularMove.getMove().equals(new Coordinate(1,-1))) {
                                    return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1,regularMove.getStartCoordinate().getY())) ||
                                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()-1));
                                }else if(regularMove.getMove().equals(new Coordinate(1,1))) {
                                    return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1,regularMove.getStartCoordinate().getY())) ||
                                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()+1));
                                }else if(regularMove.getMove().equals(new Coordinate(-1,-1))) {
                                    return this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1,regularMove.getStartCoordinate().getY())) ||
                                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()-1));
                                }
                                else {
                                    return false;
                                }


                        }


                }
            default:

                return false;
        }


    }

    //get all the possible moves for a prawn at a given position
    public RegularMove[] possibleMovesPaw(String player, Coordinate position) {
        int playerId = player.equals("blanc") ? 1 : 2;

        //get the Case position value
        int currentLisere = this.boardArray[position.getX()][position.getY()].getValue();
        System.out.println("current lisere : "+currentLisere);
        System.out.println("position : "+position.toString());
        RegularMove[] potentionalMoves;
        switch (currentLisere){
            case 1:
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (isInBoard(new Coordinate(position.getX() + i, position.getY() + j))) {
                            if (isFree(new Coordinate(position.getX() + i, position.getY() + j))) {
                                potentionalMoves = new RegularMove[1];
                                potentionalMoves[0] = new RegularMove(new Coordinate(position.getX(), position.getY()), new Coordinate(position.getX() + i, position.getY() + j), playerId);
                                return potentionalMoves;
                            }
                        }

                    }

                }
                case 2:
                    potentionalMoves = new RegularMove[8];
                    int index = 0;
                    for (int i = -2; i <= 2; i++) {
                        for (int j = -2; j <= 2; j++) {
                            if (abs(i) + abs(j) == 2) {
                                if (isInBoard(new Coordinate(position.getX() + i, position.getY() + j))) {


                                    //test if the move is valid
                                    if (isValidMove(new RegularMove(position, new Coordinate(position.getX() + i, position.getY() + j), playerId), player)) {

                                        potentionalMoves[index] = new RegularMove(new Coordinate(position.getX(), position.getY()), new Coordinate(position.getX() + i, position.getY() + j), playerId);
                                        index++;
                                    }
//
                                }
                            }
                        }
                    }
                    return potentionalMoves;

            default:
                    return null;




        }




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
        System.out.println("    A B C D E F");
        System.out.println("----------------");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print("0" + (i + 1) + " |");

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


    //add main
    public static void main(String[] args) {
        EscampeBoard escampeBoard = new EscampeBoard();
        System.out.println("lisere map :");
        escampeBoard.printBoard();
        System.out.println();
        escampeBoard.printBoardWithPion();
       escampeBoard.setFromFile("/home/kevin/Cour/résolution de contrainte/projet/demo1_board.txt");
        System.out.println();
       System.out.println("board after load file :");
       escampeBoard.printBoardWithPion();

//
        RegularMove[] moves = escampeBoard.possibleMovesPaw("blanc",new Coordinate(0,3));

    }
}
