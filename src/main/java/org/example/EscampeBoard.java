package org.example;
import org.example.enums.PIECE_TYPE;
import org.example.enums.TEAM_COLOR;
import org.example.move.Move;
import org.example.move.PositionMove;
import org.example.move.RegularMove;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.decrementExact;

public class EscampeBoard implements Partie1 {

    private static final Logger LOGGER = Logger.getLogger(EscampeBoard.class.getName());

    //PRIVATE ATTRIBUTES
    private final ArrayList<Move> move = new ArrayList<>();

    private Case[][] boardArray;
    private int lastLisere = -1;
    private boolean BlackTeamWin =  false;
    private boolean WhiteTeamWin =  false;


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
            LOGGER.log(Level.SEVERE, "An error occurred.", e);
        }
    }

    @Override
    public void saveToFile(String fileName) {
        //CONSTANTS FOR THE METHOD
        final String FILE_FIRST_LINE = "% ABCDEF\n";

        File file = new File(fileName);

        if (!file.exists()) {
            try {
                Printinator.printFileCreationMessage(fileName, file.createNewFile());
            } catch (IOException e) {
                System.out.println("An error occurred.");
                Logger.getLogger(EscampeBoard.class.getName()).log(Level.SEVERE, "An error occurred.", e);
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
            Logger.getLogger(EscampeBoard.class.getName()).log(Level.SEVERE, "An error occurred.", e);
        }
    }

    @Override
    public boolean isValidMove(String move, String player) {
            if (move.length() == 5) {
                return isValidMove(new RegularMove(move, TEAM_COLOR.getTeamColorFromString(player)));
            }
            return true;
    }

    @Override
    public String[] possiblesMoves(String player) {
        //get the player color
        TEAM_COLOR playerColor = player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM;

        //get all te coordinate of all player pawns
        ArrayList<Coordinate> pawnsCoordinates = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece currentPiece = boardArray[i][j].getPiece();
                if (currentPiece != null && currentPiece.getPlayerTeamColor() == playerColor) {
                    pawnsCoordinates.add(new Coordinate(j, i));
                }
            }
        }

        //get all the possible moves for all the pawns
        ArrayList<RegularMove> possibleMoves = new ArrayList<>();
        for (Coordinate coordinate : pawnsCoordinates) {
            RegularMove[] moves = possibleMovesPaw(TEAM_COLOR.getTeamColorFromString(player), coordinate);
            for (RegularMove move : moves) {
                if (move != null && (lastLisere == -1 || lastLisere == boardArray[move.getStartCoordinate().getY()][move.getStartCoordinate().getX()].getValue())) {
                    possibleMoves.add(move);
                }
            }
        }

        //convert the possible moves to a string array
        String[] res = new String[possibleMoves.size()];
        for (int i = 0; i < possibleMoves.size(); i++) {
            res[i] = possibleMoves.get(i).toString();
        }
        return res;

    }

    @Override
    public void play(String move, String player) {
        if (move.length() == 5) {
            RegularMove regularMove = new RegularMove(move, TEAM_COLOR.getTeamColorFromString(player));
            if (isValidMove(regularMove)) {
                //get the start and end coordinate
                Coordinate startCoordinate = regularMove.getStartCoordinate();
                Coordinate endCoordinate = regularMove.getEndCoordinate();

                //get the piece to move
                Piece piece = boardArray[startCoordinate.getY()][startCoordinate.getX()].getPiece();

                //if the end case  contains a licorne the game is over
                if (boardArray[endCoordinate.getY()][endCoordinate.getX()].getPiece() != null) {
                    if (boardArray[endCoordinate.getY()][endCoordinate.getX()].getPiece().getPieceType() == PIECE_TYPE.LICORNE) {
                        if (boardArray[endCoordinate.getY()][endCoordinate.getX()].getPiece().getPlayerTeamColor() == TEAM_COLOR.BLACK_TEAM) {
                            WhiteTeamWin = true;
                        } else {
                            BlackTeamWin = true;
                        }
                    }
                }

                //move the piece
                boardArray[startCoordinate.getY()][startCoordinate.getX()].setPiece(null);
                boardArray[endCoordinate.getY()][endCoordinate.getX()].setPiece(piece);

                //update the last lisere

                lastLisere = getLisereValue(new Coordinate(move.substring(3, 5)));


                //add the move to the move list
                this.move.add(regularMove);
            }
        }else {
            PositionMove positionMove = new PositionMove(move, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM);
            if (isValidMove(positionMove)) {
                //get the coordinates
                Coordinate[] coordinates = positionMove.getCoordinates();

                //placer des pions sur les 5 premières cases et une licorne sur la 6ème
                for (int i = 1; i < 6; i++) {
                    boardArray[coordinates[i].getY()][coordinates[i].getX()].setPiece(new Piece(PIECE_TYPE.PALADIN, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM));
                }
                boardArray[coordinates[0].getY()][coordinates[0].getX()].setPiece(new Piece(PIECE_TYPE.LICORNE, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM));
                this.move.add(positionMove);

            }
        }
    }

    @Override
    public boolean gameOver() {
        return BlackTeamWin || WhiteTeamWin;
    }

    //////////////////////////////////////////////////////////PERSONAL FUNCTION/////////////////////////////////////////////////////
    public int  playedCoups(){
        return move.size();
    }

    public boolean isFree(Coordinate coordinate) {
        return isInBoard(coordinate) && boardArray[coordinate.getY()][coordinate.getX()].getPiece() == null;
    }

    public boolean isInBoard(Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() < BOARD_SIZE && coordinate.getY() >= 0 && coordinate.getY() < BOARD_SIZE;
    }

    public int getLisereValue(Coordinate coordinate){
        return boardArray[coordinate.getY()][coordinate.getX()].getValue();
    }


    public boolean isValidMoveFromLisere(RegularMove regularMove,int lisereValue){
        boolean adverseLicorne = isAdverseLicorne(regularMove);
        switch (lisereValue){

            case 1:
                //test if the final case is free or if there is an adverse licorne
                return this.isFree(regularMove.getEndCoordinate())  || adverseLicorne;


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

    private boolean isAdverseLicorne(RegularMove regularMove) {
        boolean adverseLicorne = false;

        if (this.boardArray[regularMove.getEndCoordinate().getY()][regularMove.getEndCoordinate().getX()].getPiece() != null) {
            if (this.boardArray[regularMove.getEndCoordinate().getY()][regularMove.getEndCoordinate().getX()].getPiece().getPieceType() == PIECE_TYPE.LICORNE) {
                if (this.boardArray[regularMove.getEndCoordinate().getY()][regularMove.getEndCoordinate().getX()].getPiece().getPlayerTeamColor() != regularMove.getTeamColor()) {
                    //test if the initial case is licorne
                    if (this.boardArray[regularMove.getStartCoordinate().getY()][regularMove.getStartCoordinate().getX()].getPiece() != null) {
                        if (this.boardArray[regularMove.getStartCoordinate().getY()][regularMove.getStartCoordinate().getX()].getPiece().getPieceType() != PIECE_TYPE.LICORNE) {
                            adverseLicorne = true;
                        }}
                }
            }
        }
        return adverseLicorne;
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
                    if (((PositionMove) move).getCoordinates()[0].getY() < 2) {
                        for (int i = 0; i < 6; i++) {
                            if (((PositionMove) move).getCoordinates()[i].getY() >= 2) {
                                return false;
                            }
                        }
                    } else {
                        for (int i = 0; i < 6; i++) {
                            if (((PositionMove) move).getCoordinates()[i].getY() <= 3) {
                                return false;
                            }
                        }
                    }
                    return true;

                } else if (move.getTeamColor().equals(TEAM_COLOR.WHITE_TEAM)) {
                    //chekc if the first this.move is a positionmove
                    if (playedCoups() == 0) {
                        return true;
                    } else if (this.move.getFirst() instanceof PositionMove) {
                        Coordinate firstMoveCoordinate = ((PositionMove) this.move.getFirst()).getCoordinates()[0];
                        for (int i = 0; i < 6; i++) {
                            if (firstMoveCoordinate.getY() <= 2) {
                                if (((PositionMove) move).getCoordinates()[i].getY() <= 2) {
                                    return false;
                                }
                            } else {
                                if (((PositionMove) move).getCoordinates()[i].getY() > 2) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
                break;

            case RegularMove regularMove:

                //test if last lisiere is defined
                if (lastLisere == -1) {
                    int tempLastLisere = boardArray[regularMove.getStartCoordinate().getY()][regularMove.getStartCoordinate().getX()].getValue();
                    return isValidMoveFromLisere(regularMove, tempLastLisere);
                }
                return isValidMoveFromLisere(regularMove, lastLisere);
            default:
                throw new IllegalStateException(Printinator.getUnexpectedValueErrorMessage(move.getClass().getName()));
        }
        return false; //By default
    }


    public RegularMove[] possibleMovesPaw(TEAM_COLOR playerColor, Coordinate position) {

        //test if start position is get a piece of the current player
        if (this.boardArray[position.getY()][position.getX()].getPiece() == null || this.boardArray[position.getY()][position.getX()].getPiece().getPlayerTeamColor() != playerColor) {
            return new RegularMove[0];
        }

        RegularMove[] potentionalMoves;
        int currentLisere = this.boardArray[position.getY()][position.getX()].getValue();
        RegularMove[] potentialMoves;

        //Debug prints
//        Printinator.printCurrentLisere(currentLisere);
//        Printinator.printPosition(position);

        switch (currentLisere){

            case 1:
                potentialMoves = new RegularMove[4];
                int index = 0;
                for (int i = -1; i <=1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (abs(i) + abs(j) == 1) {
                            if (isInBoard(new Coordinate(position.getX() + i, position.getY() + j))) {
                                if (isFree(new Coordinate(position.getX() + i, position.getY() + j))) {
                                    potentialMoves[index] = new RegularMove(new Coordinate(position.getX(), position.getY()), new Coordinate(position.getX() + i, position.getY() + j), playerColor);
                                    index++;
                                }
                            }
                        }
                    }

                }

                return potentialMoves;
                case 2:
                    potentialMoves = new RegularMove[8];
                    int index2 = 0;
                    for (int i = -2; i <= 2; i++) {
                        for (int j = -2; j <= 2; j++) {
                            if (abs(i) + abs(j) == 2) {
                                index2 = getPotentialMove(position, playerColor, potentialMoves, index2, i, j);
                            }
                        }
                    }
                    return potentialMoves;
                case 3:
                    potentialMoves = new RegularMove[16];
                    int index3 = 0;
                    for (int i = -3; i <= 3; i++) {
                        for (int j = -3; j <= 3; j++) {
                            int i1 = abs(i) + abs(j);
                            if ((3 == i1) || (i1 == 1)){
                                index3 = getPotentialMove(position, playerColor, potentialMoves, index3, i, j);
                            }
                        }
                    }
                    return potentialMoves;

            default:
                    return new RegularMove[0];




        }




    }


    private int getPotentialMove(Coordinate position, TEAM_COLOR playerColor, RegularMove[] potentialMoves, int index3, int i, int j) {
        try {if (isInBoard(new Coordinate(position.getX() + i, position.getY() + j))) {
            //test if the move is valid
            //test if the end case is free
            if(!isFree(new Coordinate(position.getX() + i, position.getY() + j)) && !isAdverseLicorne(new RegularMove(position, new Coordinate(position.getX() + i, position.getY() + j), playerColor))){
                return index3;
            }

            if (isValidMove(new RegularMove(position, new Coordinate(position.getX() + i, position.getY() + j), playerColor))) {
                potentialMoves[index3] = new RegularMove(new Coordinate(position.getX(), position.getY()), new Coordinate(position.getX() + i, position.getY() + j), playerColor);
                index3++;
            }
        }
        return  index3;
        }catch (Exception e){
            return index3;
        }
    }

    public String boardToString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece currentPiece = boardArray[i][j].getPiece();
                if (currentPiece != null) {
                    res.append(Printinator.getPieceCharacter(currentPiece.getPieceType(), currentPiece.getPlayerTeamColor()));
                } else {
                    res.append("-");
                }
            }
        }
        return res.toString();
    }

    //GETTERS
    public Case[][] getBoardArray() {
        return boardArray;
    }

    public int getLastLisere() {
        return lastLisere;
    }

    public void setLastLisere(int lastLisere) {
        this.lastLisere = lastLisere;
    }

    public String getWinner(){
        if(BlackTeamWin){
            return "noir";
        }else if(WhiteTeamWin){
            return "blanc";
        }else{
            return "null";
        }
    }

    //create a clone of the board
    public EscampeBoard clone(){
        EscampeBoard clone = new EscampeBoard();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                clone.boardArray[i][j] = this.boardArray[i][j].clone();
            }
        }
        return clone;
    }

    public static void CoordinateTest(){
        EscampeBoard escampeBoard = new EscampeBoard();
        //A1
        Coordinate coordinate = new Coordinate(0,0);
        assert(Objects.equals(coordinate.toString(), "A1"));
        assert(new Coordinate("A1").equals(coordinate));
        assert(new Coordinate("A1").getX()==0);
        assert(new Coordinate("A1").getY()==0);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("A1"))==1);
        //A2
        coordinate = new Coordinate(0,1);
        assert(Objects.equals(coordinate.toString(), "A2"));
        assert(new Coordinate("A2").equals(coordinate));
        assert(new Coordinate("A2").getX()==0);
        assert(new Coordinate("A2").getY()==1);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert (escampeBoard.getLisereValue(new Coordinate("A2"))==3);
        //A3
        coordinate = new Coordinate(0,2);
        assert(Objects.equals(coordinate.toString(), "A3"));
        assert(new Coordinate("A3").equals(coordinate));
        assert(new Coordinate("A3").getX()==0);
        assert(new Coordinate("A3").getY()==2);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("A3"))==2);
        //A4
        coordinate = new Coordinate(0,3);
        assert(Objects.equals(coordinate.toString(), "A4"));
        assert(new Coordinate("A4").equals(coordinate));
        assert(new Coordinate("A4").getX()==0);
        assert(new Coordinate("A4").getY()==3);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("A4"))==2);
        //A5
        coordinate = new Coordinate(0,4);
        assert(Objects.equals(coordinate.toString(), "A5"));
        assert(new Coordinate("A5").equals(coordinate));
        assert(new Coordinate("A5").getX()==0);
        assert(new Coordinate("A5").getY()==4);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("A5"))==1);
        //A6
        coordinate = new Coordinate(0,5);
        assert(Objects.equals(coordinate.toString(), "A6"));
        assert(new Coordinate("A6").equals(coordinate));
        assert(new Coordinate("A6").getX()==0);
        assert(new Coordinate("A6").getY()==5);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("A6"))==3);
        //B1
        coordinate = new Coordinate(1,0);
        assert(Objects.equals(coordinate.toString(), "B1"));
        assert(new Coordinate("B1").equals(coordinate));
        assert(new Coordinate("B1").getX()==1);
        assert(new Coordinate("B1").getY()==0);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("B1"))==2);
        //B2
        coordinate = new Coordinate(1,1);
        assert(Objects.equals(coordinate.toString(), "B2"));
        assert(new Coordinate("B2").equals(coordinate));
        assert(new Coordinate("B2").getX()==1);
        assert(new Coordinate("B2").getY()==1);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("B2"))==1);
        //B3
        coordinate = new Coordinate(1,2);
        assert(Objects.equals(coordinate.toString(), "B3"));
        assert(new Coordinate("B3").equals(coordinate));
        assert(new Coordinate("B3").getX()==1);
        assert(new Coordinate("B3").getY()==2);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("B3"))==3);
        //B4
        coordinate = new Coordinate(1,3);
        assert(Objects.equals(coordinate.toString(), "B4"));
        assert(new Coordinate("B4").equals(coordinate));
        assert(new Coordinate("B4").getX()==1);
        assert(new Coordinate("B4").getY()==3);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("B4"))==1);
        //B5
        coordinate = new Coordinate(1,4);
        assert(Objects.equals(coordinate.toString(), "B5"));
        assert(new Coordinate("B5").equals(coordinate));
        assert(new Coordinate("B5").getX()==1);
        assert(new Coordinate("B5").getY()==4);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("B5"))==3);
        //B6
        coordinate = new Coordinate(1,5);
        assert(Objects.equals(coordinate.toString(), "B6"));
        assert(new Coordinate("B6").equals(coordinate));
        assert(new Coordinate("B6").getX()==1);
        assert(new Coordinate("B6").getY()==5);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("B6"))==2);
        //C1
        coordinate = new Coordinate(2,0);
        assert(Objects.equals(coordinate.toString(), "C1"));
        assert(new Coordinate("C1").equals(coordinate));
        assert(new Coordinate("C1").getX()==2);
        assert(new Coordinate("C1").getY()==0);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("C1"))==2);
        //C2
        coordinate = new Coordinate(2,1);
        assert(Objects.equals(coordinate.toString(), "C2"));
        assert(new Coordinate("C2").equals(coordinate));
        assert(new Coordinate("C2").getX()==2);
        assert(new Coordinate("C2").getY()==1);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("C2"))==3);
        //C3
        coordinate = new Coordinate(2,2);
        assert(Objects.equals(coordinate.toString(), "C3"));
        assert(new Coordinate("C3").equals(coordinate));
        assert(new Coordinate("C3").getX()==2);
        assert(new Coordinate("C3").getY()==2);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("C3"))==1);
        //C4
        coordinate = new Coordinate(2,3);
        assert(Objects.equals(coordinate.toString(), "C4"));
        assert(new Coordinate("C4").equals(coordinate));
        assert(new Coordinate("C4").getX()==2);
        assert(new Coordinate("C4").getY()==3);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("C4"))==3);
        //C5
        coordinate = new Coordinate(2,4);
        assert(Objects.equals(coordinate.toString(), "C5"));
        assert(new Coordinate("C5").equals(coordinate));
        assert(new Coordinate("C5").getX()==2);
        assert(new Coordinate("C5").getY()==4);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("C5"))==1);
        //C6
        coordinate = new Coordinate(2,5);
        assert(Objects.equals(coordinate.toString(), "C6"));
        assert(new Coordinate("C6").equals(coordinate));
        assert(new Coordinate("C6").getX()==2);
        assert(new Coordinate("C6").getY()==5);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("C6"))==2);
        //D1
        coordinate = new Coordinate(3,0);
        assert(Objects.equals(coordinate.toString(), "D1"));
        assert(new Coordinate("D1").equals(coordinate));
        assert(new Coordinate("D1").getX()==3);
        assert(new Coordinate("D1").getY()==0);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("D1"))==3);
        //D2
        coordinate = new Coordinate(3,1);
        assert(Objects.equals(coordinate.toString(), "D2"));
        assert(new Coordinate("D2").equals(coordinate));
        assert(new Coordinate("D2").getX()==3);
        assert(new Coordinate("D2").getY()==1);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("D2"))==1);
        //D3
        coordinate = new Coordinate(3,2);
        assert(Objects.equals(coordinate.toString(), "D3"));
        assert(new Coordinate("D3").equals(coordinate));
        assert(new Coordinate("D3").getX()==3);
        assert(new Coordinate("D3").getY()==2);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("D3"))==2);
        //D4
        coordinate = new Coordinate(3,3);
        assert(Objects.equals(coordinate.toString(), "D4"));
        assert(new Coordinate("D4").equals(coordinate));
        assert(new Coordinate("D4").getX()==3);
        assert(new Coordinate("D4").getY()==3);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("D4"))==2);
        //D5
        coordinate = new Coordinate(3,4);
        assert(Objects.equals(coordinate.toString(), "D5"));
        assert(new Coordinate("D5").equals(coordinate));
        assert(new Coordinate("D5").getX()==3);
        assert(new Coordinate("D5").getY()==4);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("D5"))==3);
        //D6
        coordinate = new Coordinate(3,5);
        assert(Objects.equals(coordinate.toString(), "D6"));
        assert(new Coordinate("D6").equals(coordinate));
        assert(new Coordinate("D6").getX()==3);
        assert(new Coordinate("D6").getY()==5);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("D6"))==1);
        //E1
        coordinate = new Coordinate(4,0);
        assert(Objects.equals(coordinate.toString(), "E1"));
        assert(new Coordinate("E1").equals(coordinate));
        assert(new Coordinate("E1").getX()==4);
        assert(new Coordinate("E1").getY()==0);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("E1"))==1);
        //E2
        coordinate = new Coordinate(4,1);
        assert(Objects.equals(coordinate.toString(), "E2"));
        assert(new Coordinate("E2").equals(coordinate));
        assert(new Coordinate("E2").getX()==4);
        assert(new Coordinate("E2").getY()==1);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("E2"))==3);
        //E3
        coordinate = new Coordinate(4,2);
        assert(Objects.equals(coordinate.toString(), "E3"));
        assert(new Coordinate("E3").equals(coordinate));
        assert(new Coordinate("E3").getX()==4);
        assert(new Coordinate("E3").getY()==2);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("E3"))==1);
        //E4
        coordinate = new Coordinate(4,3);
        assert(Objects.equals(coordinate.toString(), "E4"));
        assert(new Coordinate("E4").equals(coordinate));
        assert(new Coordinate("E4").getX()==4);
        assert(new Coordinate("E4").getY()==3);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("E4"))==3);
        //E5
        coordinate = new Coordinate(4,4);
        assert(Objects.equals(coordinate.toString(), "E5"));
        assert(new Coordinate("E5").equals(coordinate));
        assert(new Coordinate("E5").getX()==4);
        assert(new Coordinate("E5").getY()==4);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("E5"))==1);
        //E6
        coordinate = new Coordinate(4,5);
        assert(Objects.equals(coordinate.toString(), "E6"));
        assert(new Coordinate("E6").equals(coordinate));
        assert(new Coordinate("E6").getX()==4);
        assert(new Coordinate("E6").getY()==5);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("E6"))==3);
        //F1
        coordinate = new Coordinate(5,0);
        assert(Objects.equals(coordinate.toString(), "F1"));
        assert(new Coordinate("F1").equals(coordinate));
        assert(new Coordinate("F1").getX()==5);
        assert(new Coordinate("F1").getY()==0);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("F1"))==2);
        //F2
        coordinate = new Coordinate(5,1);
        assert(Objects.equals(coordinate.toString(), "F2"));
        assert(new Coordinate("F2").equals(coordinate));
        assert(new Coordinate("F2").getX()==5);
        assert(new Coordinate("F2").getY()==1);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("F2"))==2);
        //F3
        coordinate = new Coordinate(5,2);
        assert(Objects.equals(coordinate.toString(), "F3"));
        assert(new Coordinate("F3").equals(coordinate));
        assert(new Coordinate("F3").getX()==5);
        assert(new Coordinate("F3").getY()==2);
        assert(escampeBoard.getLisereValue(coordinate)==3);
        assert(escampeBoard.getLisereValue(new Coordinate("F3"))==3);
        //F4
        coordinate = new Coordinate(5,3);
        assert(Objects.equals(coordinate.toString(), "F4"));
        assert(new Coordinate("F4").equals(coordinate));
        assert(new Coordinate("F4").getX()==5);
        assert(new Coordinate("F4").getY()==3);
        assert(escampeBoard.getLisereValue(coordinate)==1);
        assert(escampeBoard.getLisereValue(new Coordinate("F4"))==1);
        //F5
        coordinate = new Coordinate(5,4);
        assert(Objects.equals(coordinate.toString(), "F5"));
        assert(new Coordinate("F5").equals(coordinate));
        assert(new Coordinate("F5").getX()==5);
        assert(new Coordinate("F5").getY()==4);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("F5"))==2);
        //F6
        coordinate = new Coordinate(5,5);
        assert(Objects.equals(coordinate.toString(), "F6"));
        assert(new Coordinate("F6").equals(coordinate));
        assert(new Coordinate("F6").getX()==5);
        assert(new Coordinate("F6").getY()==5);
        assert(escampeBoard.getLisereValue(coordinate)==2);
        assert(escampeBoard.getLisereValue(new Coordinate("F6"))==2);







        System.out.println("Coordinate test");

        //A1





    }

    //PUBLIC STATIC MAIN
    public static void main(String[] args) {
        //TODO make a test for the class
        EscampeBoard.CoordinateTest();
        EscampeBoard escampeBoard = new EscampeBoard();








        //Debugging prints
        Printinator.printBoard(escampeBoard.getBoardArray(), "Lisere map :");


        escampeBoard.setFromFile("src/demo2_board.txt");
        Printinator.printBoardWithPion(escampeBoard.getBoardArray(), null);

        //get the possible moves for the black team
        String[] possibleMoves = escampeBoard.possiblesMoves("blanc");

        //print the possible moves
        for (String move : possibleMoves) {
            System.out.println(move);
        }








    }



    //PRIVATE CONSTANTS
    private static final int BOARD_SIZE = 6;
}
