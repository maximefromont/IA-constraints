package org.example;
import org.example.enums.PIECE_TYPE;
import org.example.enums.TEAM_COLOR;
import org.example.move.Move;
import org.example.move.PositionMove;
import org.example.move.RegularMove;

import javax.swing.text.Position;
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
    private int lastLisere = -1;

    //CONSTRUCTOR
    public EscampeBoard() {
        boardArray = new Case[BOARD_SIZE][BOARD_SIZE];

        //Initializing the board with straight values from the subject
        boardArray = new Case[][]{
                {new Case(1), new Case(2), new Case(2), new Case(3), new Case(1), new Case(2)},
                {new Case(3), new Case(1), new Case(3), new Case(1), new Case(3), new Case(2)},
                {new Case(2), new Case(3), new Case(1), new Case(2), new Case(1), new Case(3)},
                {new Case(2), new Case(1), new Case(3), new Case(2), new Case(3), new Case(1)},
                {new Case(1), new Case(3), new Case(1), new Case(3), new Case(1), new Case(2)},
                {new Case(3), new Case(2), new Case(2), new Case(1), new Case(3), new Case(2)}
        };
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
                        case Printinator.LICORNE_FROM_BLACK_TEAM_IDENTIFIER:
                            boardArray[row-1][col-3].setPiece(new Piece(PIECE_TYPE.LICORNE, TEAM_COLOR.BLACK_TEAM));
                            break;
                        case Printinator.PALADIN_FROM_BLACK_TEAM_IDENTIFIER:
                            boardArray[row-1][col-3].setPiece(new Piece(PIECE_TYPE.PALADIN, TEAM_COLOR.BLACK_TEAM));
                            break;
                        case Printinator.LICORNE_FROM_WHITE_TEAM_IDENTIFIER:
                            boardArray[row-1][col-3].setPiece(new Piece(PIECE_TYPE.LICORNE, TEAM_COLOR.WHITE_TEAM));
                            break;
                        case Printinator.PALADIN_FROM_WHITE_TEAM_IDENTIFIER:
                            boardArray[row-1][col-3].setPiece(new Piece(PIECE_TYPE.PALADIN, TEAM_COLOR.WHITE_TEAM));
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
        //CONSTANTS FOR THE METHOD
        final String FILE_FIRST_LINE = "% ABCDEF\n";

        File file = new File(fileName);

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    Printinator.printFileCreationMessage(fileName, true);
                } else {
                    Printinator.printFileCreationMessage(fileName, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter writer = new FileWriter(fileName, false); // false means do not append

            //create the file if it does not exist

            writer.write(FILE_FIRST_LINE);
            String boardString= boardToString();

            for (int i = 0; i < 6; i++) {
                writer.write("0"+(i+1)+" "+boardString.substring(i*6,i*6+6)+" 0"+(i+1)+"\n");
            }
            writer.write(FILE_FIRST_LINE);

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
                positionMove = new PositionMove(move, TEAM_COLOR.WHITE_TEAM);
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
                    positionMove = new PositionMove(move, TEAM_COLOR.WHITE_TEAM);
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

    //////////////////////////////////////////////////////////PERSONNAL FUNCTION/////////////////////////////////////////////////////

    public boolean isFree(Coordinate coordinate) {
        return boardArray[coordinate.getY()][coordinate.getX()].getPiece() == null;
    }

    public boolean isInBoard(Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() < BOARD_SIZE && coordinate.getY() >= 0 && coordinate.getY() < BOARD_SIZE;
    }


    public boolean isValidMoveFromLisere(RegularMove regularMove,int lisereValue){
        boolean adverseLicorne = false;

        //retouner si il se trouve une licorne de couleur adverse sur la case d'arrivée
        if (this.boardArray[regularMove.getEndCoordinate().getY()][regularMove.getEndCoordinate().getX()].getPiece() != null) {
            if (this.boardArray[regularMove.getEndCoordinate().getY()][regularMove.getEndCoordinate().getX()].getPiece().getPieceType() == PIECE_TYPE.LICORNE) {
                if (this.boardArray[regularMove.getEndCoordinate().getY()][regularMove.getEndCoordinate().getX()].getPiece().getPlayerTeamColor() != regularMove.getTeamColor()) {
                    adverseLicorne = true;
                }
            }
        }

        switch (lisereValue){

            case 1:
                //test if the final case is free or if there is an adverse licorne
                return this.isFree(regularMove.getEndCoordinate()) &&! adverseLicorne;

            case 2:
                //test if the final case is free
                if(!this.isFree(regularMove.getEndCoordinate())&&!adverseLicorne){
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

            case 3:
                if (!this.isFree(regularMove.getEndCoordinate())&&!adverseLicorne) {
                    return false;
                }
                else if(regularMove.getMove().equals(new Coordinate(0,3))){
                    System.out.println("test B2");
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()+1)) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()+2)));
                } else if (regularMove.getMove().equals(new Coordinate(0,-3))) {
                    return  (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()-1)) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(),regularMove.getStartCoordinate().getY()-2)));
                } else if (regularMove.getMove().equals(new Coordinate(3,0))) {

                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1,regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+2,regularMove.getStartCoordinate().getY())));
                } else if (regularMove.getMove().equals(new Coordinate(-3,0))) {
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1,regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-2,regularMove.getStartCoordinate().getY())));
                }else if(regularMove.getMove().equals(new Coordinate(-1,2))) {
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() - 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1, regularMove.getStartCoordinate().getY() + 1)))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() + 1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() + 2)));
                }else if(regularMove.getMove().equals(new Coordinate(1,2))) {
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() + 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY() + 1)))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() + 1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() + 2)));
                }else if(regularMove.getMove().equals(new Coordinate(-2,1))) {
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() - 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-2, regularMove.getStartCoordinate().getY())))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() + 1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1, regularMove.getStartCoordinate().getY() + 1)));
                }else if(regularMove.getMove().equals(new Coordinate(0,1))) {

                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() - 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1, regularMove.getStartCoordinate().getY()+1)))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY() )) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY() + 1)));

                } else if (regularMove.getMove().equals(new Coordinate(2,1))) {
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() + 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+2, regularMove.getStartCoordinate().getY())))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() + 1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY() + 1)));

                }
                else if(regularMove.getMove().equals(new Coordinate(-1,0))) {
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() , regularMove.getStartCoordinate().getY()+1)) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1, regularMove.getStartCoordinate().getY()+1)))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() -1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1, regularMove.getStartCoordinate().getY() - 1)));

                } else if (regularMove.getMove().equals(new Coordinate(1,0))) {
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() , regularMove.getStartCoordinate().getY()+1)) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY()+1)))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() -1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY() -1)));

                }
                else if(regularMove.getMove().equals(new Coordinate(-2,-1))){
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() - 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-2, regularMove.getStartCoordinate().getY())))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() - 1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1, regularMove.getStartCoordinate().getY() - 1)));
                }
                else if(regularMove.getMove().equals(new Coordinate(0,-1))){

                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() - 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1, regularMove.getStartCoordinate().getY()-1)))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY() )) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY() - 1)));
                }else if(regularMove.getMove().equals(new Coordinate(2,-1))){
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() + 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+2, regularMove.getStartCoordinate().getY())))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() - 1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY() - 1)));
                }
                else if(regularMove.getMove().equals(new Coordinate(-1,-2))){

                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() - 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()-1, regularMove.getStartCoordinate().getY()-1)))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() - 1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() - 2)));
                }
                else if(regularMove.getMove().equals(new Coordinate(1,-2))){
                    return (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX() + 1, regularMove.getStartCoordinate().getY())) &&
                            this.isFree(new Coordinate(regularMove.getStartCoordinate().getX()+1, regularMove.getStartCoordinate().getY()-1)))||
                            (this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() - 1)) &&
                                    this.isFree(new Coordinate(regularMove.getStartCoordinate().getX(), regularMove.getStartCoordinate().getY() - 2)));
                }
                else {
                    return false;
                }
        }
        return false;
    }

    public boolean isValidMove(Move move) {

        switch (move) {

            case PositionMove positionMove:

                for (int i = 0; i < 6; i++) {
                    for (int j = i + 1; j < 6; j++) {
                        if (((PositionMove) move).getCoordinates()[i].equals(((PositionMove) move).getCoordinates()[j])) {
                            return false;
                        }
                    }
                }
                //test if the player is the right one
                if (move.getTeamColor().equals(TEAM_COLOR.BLACK_TEAM)) {

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


                } else if (move.getTeamColor().equals(TEAM_COLOR.WHITE_TEAM)) {
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
                break;

            case RegularMove regularMove:

                //teest if last lisiere is defined
                if (lastLisere == -1) {
                    lastLisere = boardArray[regularMove.getStartCoordinate().getY()][regularMove.getStartCoordinate().getX()].getValue();
                    return isValidMoveFromLisere(regularMove, lastLisere);
                }else {
                    return isValidMoveFromLisere(regularMove, lastLisere);
                }

            default:
                throw new IllegalStateException(Printinator.getUnexpectedValueErrorMessage(move.getClass().getName()));
        }
        return false; //By default
    }

    //get all the possible moves for a prawn at a given position
    public RegularMove[] possibleMovesPaw(TEAM_COLOR playerColor, Coordinate position) {

        //get the Case position value
        int currentLisere = this.boardArray[position.getX()][position.getY()].getValue();

        //Debug prints
        Printinator.printCurrentLisere(currentLisere);
        Printinator.printPosition(position);

        RegularMove[] potentionalMoves;
        switch (currentLisere){

            case 1:
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (isInBoard(new Coordinate(position.getX() + i, position.getY() + j))) {
                            if (isFree(new Coordinate(position.getX() + i, position.getY() + j))) {
                                potentionalMoves = new RegularMove[1];
                                potentionalMoves[0] = new RegularMove(new Coordinate(position.getX(), position.getY()), new Coordinate(position.getX() + i, position.getY() + j), playerColor);
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
                                    if (isValidMove(new RegularMove(position, new Coordinate(position.getX() + i, position.getY() + j), playerColor))) {

                                        potentionalMoves[index] = new RegularMove(new Coordinate(position.getX(), position.getY()), new Coordinate(position.getX() + i, position.getY() + j), playerColor);
                                        index++;
                                    }
//
                                }
                            }
                        }
                    }
                    return potentionalMoves;

                case 3:
                    potentionalMoves = new RegularMove[26];
                    index = 0;
                    for (int i = -3; i <= 3; i++) {
                        for (int j = -3; j <= 3; j++) {
                            if (abs(i) + abs(j) == 3 || abs(i) + abs(j) == 1){
                                if (isInBoard(new Coordinate(position.getX() + i, position.getY() + j))) {
                                    //test if the move is valid
                                    //potentionalMoves[index] = new RegularMove(new Coordinate(position.getX(), position.getY()), new Coordinate(position.getX() + i, position.getY() + j), playerId);
                                   // index++;
                                    if (isValidMove(new RegularMove(position, new Coordinate(position.getX() + i, position.getY() + j), playerColor))) {
                                        potentionalMoves[index] = new RegularMove(new Coordinate(position.getX(), position.getY()), new Coordinate(position.getX() + i, position.getY() + j), playerColor);
                                        index++;
                                    }
                                }
                            }
                        }
                    }
                    return potentionalMoves;

            default:
                    return new RegularMove[0];
        }
    }

    public String boardToString() {
        String res = "";
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {

                Piece currentPiece = boardArray[i][j].getPiece();
                if (currentPiece != null) {
                    res += Printinator.getPieceCharacter(currentPiece.getPieceType(), currentPiece.getPlayerTeamColor());
                } else {
                    res += "-";
                }
            }
        }
        return res;
    }

    //GETTERS
    public Case[][] getBoardArray() {
        return boardArray;
    }

    //PUBLIC STATIC MAIN
    public static void main(String[] args) {
        EscampeBoard escampeBoard = new EscampeBoard();

        //Debugging prints
        Printinator.printBoard(escampeBoard.getBoardArray(), "Lisere map :");
        Printinator.printLineSpace();
        Printinator.printBoardWithPion(escampeBoard.getBoardArray(), null);

        escampeBoard.setFromFile("src/demo1_board.txt");

        //Debugging prints
        Printinator.printLineSpace();
        Printinator.printBoard(escampeBoard.getBoardArray(), "Board after load file :");

        RegularMove[] moves = escampeBoard.possibleMovesPaw(TEAM_COLOR.WHITE_TEAM,new Coordinate("B5"));

        Printinator.printPossibleMoves(moves, escampeBoard.getBoardArray(), null);
        Printinator.printListOfPossibleMoves(moves, "Possible moves : ");

    }

    //PRIVATE CONSTANTS
    private static final int BOARD_SIZE = 6;
}
