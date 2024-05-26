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
    private final Case[][] boardArray;
    private int lastLisere = -1;
    private boolean BlackTeamWin =  false;
    private boolean WhiteTeamWin =  false;


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

                            boardArray[row-1][col-3].setPiece(new Piece(PIECE_TYPE.LICORNE, TEAM_COLOR.BLACK_TEAM));
                            break;
                        case 'n':
                            boardArray[row-1][col-3].setPiece(new Piece(PIECE_TYPE.PALADIN, TEAM_COLOR.BLACK_TEAM));
                            break;
                        case 'B':
                            boardArray[row-1][col-3].setPiece(new Piece(PIECE_TYPE.LICORNE, TEAM_COLOR.WHITE_TEAM));
                            break;
                        case 'b':

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
                Logger.getLogger(EscampeBoard.class.getName()).log(Level.SEVERE, "An error occurred.", e);
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
            Logger.getLogger(EscampeBoard.class.getName()).log(Level.SEVERE, "An error occurred.", e);
        }



    }

    @Override
    public boolean isValidMove(String move, String player) {
            if (move.length() == 2) {
                return isValidMove(new RegularMove(move, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM), player);
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
            RegularMove[] moves = possibleMovesPaw(player, coordinate);
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
            RegularMove regularMove = new RegularMove(move, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM);
            if (isValidMove(regularMove, player)) {
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
            if (isValidMove(positionMove, player)) {
                //get the coordinates
                Coordinate[] coordinates = positionMove.getCoordinates();

                //placer des pions sur les 5 premières cases et une licorne sur la 6ème
                for (int i = 0; i < 5; i++) {
                    boardArray[coordinates[i].getY()][coordinates[i].getX()].setPiece(new Piece(PIECE_TYPE.PALADIN, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM));
                }
                boardArray[coordinates[5].getY()][coordinates[5].getX()].setPiece(new Piece(PIECE_TYPE.LICORNE, player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM));

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


                } else if (player.equals("blanc")) {
                    //check if the first this.move is a position move
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

            case "org.example.move.RegularMove":
                //convert the move to a regular move
                assert move instanceof RegularMove;
                RegularMove regularMove = (RegularMove) move;

                //test if last lisiere is defined
                if (lastLisere == -1) {
                    int tempLastLisere = boardArray[regularMove.getStartCoordinate().getY()][regularMove.getStartCoordinate().getX()].getValue();
                    return isValidMoveFromLisere(regularMove, tempLastLisere);
                }
                return isValidMoveFromLisere(regularMove, lastLisere);
            default:
                throw new IllegalStateException("Unexpected value: " + move.getClass().getName());
        }
       


    }

    //get all the possible moves for a prawn at a given position
    public RegularMove[] possibleMovesPaw(String player, Coordinate position) {
        TEAM_COLOR playerColor = player.equals("blanc") ? TEAM_COLOR.WHITE_TEAM : TEAM_COLOR.BLACK_TEAM;

        //test if start position is get a piece of the current player
        if (this.boardArray[position.getY()][position.getX()].getPiece() == null || this.boardArray[position.getY()][position.getX()].getPiece().getPlayerTeamColor() != playerColor) {
            return new RegularMove[0];
        }

        //get the Case position value
        int currentLisere = this.boardArray[position.getY()][position.getX()].getValue();
        RegularMove[] potentialMoves;
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
                                index2 = getPotentialMove(player, position, playerColor, potentialMoves, index2, i, j);
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
                                index3 = getPotentialMove(player, position, playerColor, potentialMoves, index3, i, j);
                            }
                        }
                    }
                    return potentialMoves;

            default:
                    return new RegularMove[0];




        }




    }


    private int getPotentialMove(String player, Coordinate position, TEAM_COLOR playerColor, RegularMove[] potentialMoves, int index3, int i, int j) {
        try {if (isInBoard(new Coordinate(position.getX() + i, position.getY() + j))) {
            //test if the move is valid
            if (isValidMove(new RegularMove(position, new Coordinate(position.getX() + i, position.getY() + j), playerColor), player)) {
                potentialMoves[index3] = new RegularMove(new Coordinate(position.getX(), position.getY()), new Coordinate(position.getX() + i, position.getY() + j), playerColor);
                index3++;
            }
        }
        return  index3;
        }catch (Exception e){
            return index3;
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
                Piece currentPiece = boardArray[i][j].getPiece();
                if (currentPiece != null) {
                    System.out.print(currentPiece.getPieceType().getStringAccordingToPieceType(currentPiece.getPlayerTeamColor().toString()) + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }

    public void printPossibleMoves(RegularMove[] moves) {
        //get the number of non-null moves
        int count = 0;
        for (RegularMove move : moves) {
            if (move != null) {
                count++;
            }
        }

        System.out.println("number of movements possibles : " + count);
        System.out.println("    A B C D E F");
        System.out.println("----------------");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print("0" + (i + 1) + " |");

            for (int j = 0; j < BOARD_SIZE; j++) {


                boolean isPossible = false;
                for (RegularMove move : moves) {
                    if (move != null) {
                        if (move.getEndCoordinate().equals(new Coordinate(j, i))){


                            isPossible = true;
                            break;
                        }
                    }
                }

                Piece currentPiece = boardArray[i][j].getPiece();

                if (currentPiece != null) {

                    if(isPossible) {
                        //test if the pion is a licorne
                        if (currentPiece.getPieceType() == PIECE_TYPE.LICORNE) {
                            System.out.print("X ");
                        } else {
                            System.out.print("E ");
                        }
                    } else {
                        System.out.print(currentPiece.getPieceType().getStringAccordingToPieceType(currentPiece.getPlayerTeamColor().toString()) + " ");
                    }


                } else {
                    if(isPossible) {
                        System.out.print("O ");
                    }else {
                        System.out.print("- ");
                    }

                }
            }
            System.out.println();
        }

    }

    public String boardToString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece currentPiece = boardArray[i][j].getPiece();
                if (currentPiece != null) {
                    res.append(currentPiece.getPieceType().getStringAccordingToPieceType(currentPiece.getPlayerTeamColor().toString()));
                } else {
                    res.append("-");
                }
            }
        }
        return res.toString();
    }

    //PRIVATE CONSTANTS
    private static final int BOARD_SIZE = 6;

    //add main
    public static void main(String[] args) {

        //TODO make a test for the class
        EscampeBoard escampeBoard = new EscampeBoard();
        System.out.println("lisere map :");
        escampeBoard.printBoard();
        System.out.println();
        escampeBoard.printBoardWithPion();
       escampeBoard.setFromFile("src/demo1_board.txt");
        System.out.println();
       System.out.println("board after load file :");
       escampeBoard.printBoardWithPion();

       //tes possible move for black in F5
        RegularMove[] movesB2 = escampeBoard.possibleMovesPaw("blanc",new Coordinate("B2"));
        escampeBoard.printPossibleMoves(movesB2);
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
            RegularMove[]  moves = escampeBoard.possibleMovesPaw("noir", coordinate);
            System.out.println("possible moves for "+coordinate);
            escampeBoard.printPossibleMoves(moves);
        }
        for (Coordinate coordinate : whitePiece) {
            System.out.println("possible moves for "+coordinate);
            RegularMove[] moves = escampeBoard.possibleMovesPaw("blanc", coordinate);
            escampeBoard.printPossibleMoves(moves);
        }





        System.out.println("possible moves :");
        String[] moves = escampeBoard.possiblesMoves("noir");
        System.out.println("possible moves black:");

        for (String move : moves) {
            System.out.println(move);
        }


        String[] moves2 = escampeBoard.possiblesMoves("blanc");
        System.out.println("possible moves white:");

        for (String move : moves2) {
            System.out.println(move);
        }



        escampeBoard.play("B5-A5","noir");
        escampeBoard.printBoardWithPion();

    }
}
