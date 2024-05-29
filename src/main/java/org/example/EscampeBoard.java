package org.example;
import org.example.enums.PIECE_TYPE;
import org.example.enums.TEAM_COLOR;
import org.example.move.Move;
import org.example.move.PositionMove;
import org.example.move.RegularMove;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.abs;

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
                if (move != null) {
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
                lastLisere = boardArray[endCoordinate.getY()][endCoordinate.getX()].getValue();

                //add the move to the move list
                this.move.add(regularMove);
            }
        }else {
            PositionMove positionMove = new PositionMove(move, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM);
            if (isValidMove(positionMove)) {
                //get the coordinates
                Coordinate[] coordinates = positionMove.getCoordinates();

                //placer des pions sur les 5 premières cases et une licorne sur la 6ème
                for (int i = 0; i < 5; i++) {
                    boardArray[coordinates[i].getY()][coordinates[i].getX()].setPiece(new Piece(PIECE_TYPE.PALADIN, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM));
                }
                boardArray[coordinates[5].getY()][coordinates[5].getX()].setPiece(new Piece(PIECE_TYPE.LICORNE, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM));
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
        return boardArray[coordinate.getY()][coordinate.getX()].getPiece() == null;
    }

    public boolean isInBoard(Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() < BOARD_SIZE && coordinate.getY() >= 0 && coordinate.getY() < BOARD_SIZE;
    }


    public boolean isValidMoveFromLisere(RegularMove regularMove,int lisereValue){
        boolean adverseLicorne = isAdverseLicorne(regularMove);
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
                   // System.out.println("test B2");
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
                    adverseLicorne = true;
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
                    if (playedCoups() == 0) {
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
                    potentialMoves = new RegularMove[26];
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

    //PUBLIC STATIC MAIN
    public static void main(String[] args) {

        //TODO make a test for the class
        EscampeBoard escampeBoard = new EscampeBoard();

        //Debugging prints
        Printinator.printBoard(escampeBoard.getBoardArray(), "Lisere map :");
        Printinator.printLineSpace();
        Printinator.printBoardWithPion(escampeBoard.getBoardArray(), null);

        escampeBoard.setFromFile("src/demo1_board.txt");
        Printinator.printBoardWithPion(escampeBoard.getBoardArray(), null);

        escampeBoard.play("B5-A5", "noir");
        Printinator.printBoardWithPion(escampeBoard.getBoardArray(), null);
        //test if the game is over
        System.out.println(escampeBoard.gameOver());

        //Debugging prints
        Printinator.printLineSpace();
        Printinator.printBoard(escampeBoard.getBoardArray(), "Board after load file :");
       //tes possible move for black in F5
        RegularMove[] movesB2 = escampeBoard.possibleMovesPaw(TEAM_COLOR.WHITE_TEAM,new Coordinate("B2"));
        Printinator.printPossibleMoves(movesB2, escampeBoard.getBoardArray(), "Possible moves : ");
        System.out.println("possible moves :");

        for(RegularMove move : movesB2){
            if(move != null){
                System.out.println(move+" move : x="+move.getMove().getX()+" y="+move.getMove().getY());
            }
        }


//       //get in an array all the coordinate of black piece and in other all coordinate pf whit piece
//
        ArrayList<Coordinate> blackPiece = new ArrayList<>();
        ArrayList<Coordinate> whitePiece = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece currentPiece = escampeBoard.boardArray[i][j].getPiece();
                if (currentPiece != null) {
                    if (currentPiece.getPlayerTeamColor() == TEAM_COLOR.BLACK_TEAM) {
                        blackPiece.add(new Coordinate(j, i));
                    } else {
                        whitePiece.add(new Coordinate(j, i));
                    }
                }
            }
        }
        System.out.println("black piece :");
        for (Coordinate coordinate : blackPiece) {
            System.out.println(coordinate);
        }
        System.out.println("white piece :");
        for (Coordinate coordinate : whitePiece) {
            System.out.println(coordinate);
        }

        //then print the possible move for each piece
        for (Coordinate coordinate : blackPiece) {
            RegularMove[]  moves = escampeBoard.possibleMovesPaw(TEAM_COLOR.BLACK_TEAM, coordinate);
            Printinator.printPossibleMoves(moves, escampeBoard.getBoardArray(), "Possible moves for " + coordinate);
        }
        for (Coordinate coordinate : whitePiece) {
            RegularMove[] moves = escampeBoard.possibleMovesPaw(TEAM_COLOR.WHITE_TEAM, coordinate);
            Printinator.printPossibleMoves(moves, escampeBoard.getBoardArray(), "Possible moves for "+coordinate);
        }

        String[] moves = escampeBoard.possiblesMoves(TEAM_COLOR.BLACK_TEAM_STRING);
        RegularMove[] regularMoves = new RegularMove[moves.length];
        for (int i = 0; i < moves.length; i++) {
            regularMoves[i] = new RegularMove(moves[i], TEAM_COLOR.BLACK_TEAM);
        }

        Printinator.printListOfPossibleMoves(regularMoves,"Possible moves black : ");

        escampeBoard.play("B5-A5", TEAM_COLOR.BLACK_TEAM_STRING);
        Printinator.printBoardWithPion(escampeBoard.getBoardArray(), null);

        //Ceci est un commentaire fait à la toute fin du merge

    }

    //PRIVATE CONSTANTS
    private static final int BOARD_SIZE = 6;
}
