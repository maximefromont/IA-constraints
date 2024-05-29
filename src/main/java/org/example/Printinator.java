package org.example;

import org.example.enums.PIECE_TYPE;
import org.example.enums.TEAM_COLOR;
import org.example.move.RegularMove;

public class Printinator {

    //PUBLIC STATIC METHODS

    //Identifiers processing method
    public static char getPieceCharacter(PIECE_TYPE pieceType, TEAM_COLOR teamColor) {
        if (pieceType == PIECE_TYPE.LICORNE) {
            if (teamColor == TEAM_COLOR.BLACK_TEAM) {
                return LICORNE_FROM_BLACK_TEAM_IDENTIFIER;
            } else {
                return LICORNE_FROM_WHITE_TEAM_IDENTIFIER;
            }
        } else {
            if (teamColor == TEAM_COLOR.BLACK_TEAM) {
                return PALADIN_FROM_BLACK_TEAM_IDENTIFIER;
            } else {
                return PALADIN_FROM_WHITE_TEAM_IDENTIFIER;
            }
        }
    }

    //Unpected value message processing method
    public static String getUnexpectedValueErrorMessage(String value) {
        return UNEXPECTED_VALUE_ERROR_MESSAGE + " : " + value;
    }

    //Debug print methods
    public static void printCurrentLisere(int currentLisere) {
        System.out.println("Current lisere : " + currentLisere);
    }

    public static void printPosition(Coordinate position) {
        System.out.println("Position : " + position.toString());
    }

    public static void printBoard(Case[][] boardArray, String headerMessage) {
        if(headerMessage != null)
            System.out.println(headerMessage);

        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[i].length; j++) {
                System.out.print(boardArray[i][j].getValue() + " ");
            }
            System.out.println();
        }
    }

    public static void printBoardWithPion(Case[][] boardArray, String headerMessage) {
        if (headerMessage != null)
            System.out.println(headerMessage);

        System.out.println("    A B C D E F");
        System.out.println("----------------");
        for (int i = 0; i < boardArray.length; i++) {
            System.out.print("0" + (i + 1) + " |");

            for (int j = 0; j < boardArray[i].length; j++) {
                Piece currentPiece = boardArray[i][j].getPiece();
                if (currentPiece != null) {
                    System.out.print(Printinator.getPieceCharacter(currentPiece.getPieceType(), currentPiece.getPlayerTeamColor()) + " ");
                } else {
                    System.out.print("- ");
                }
            }

            printLineSpace();
        }
    }

    public static void printPossibleMoves(RegularMove[] moves, Case[][] boardArray, String headerMessage) {
        if (headerMessage != null)
            System.out.println(headerMessage);

        //get the nyumber of non null moves
        int count = 0;
        for (RegularMove move : moves) {
            if (move != null) {
                count++;
            }
        }

        System.out.println("Nombre de mouvements possibles : " + count);
        System.out.println("    A B C D E F");
        System.out.println("----------------");
        for (int i = 0; i < boardArray.length; i++) {
            System.out.print("0" + (i + 1) + " |");

            for (int j = 0; j < boardArray[i].length; j++) {


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
                            System.out.print(POSSIBLE_CAPTURE_IDENTIFIER + " ");
                        } else {
                            System.out.print(POSSIBLE_EAT_IDENTIFIER + " ");
                        }
                    } else {
                        System.out.print(Printinator.getPieceCharacter(currentPiece.getPieceType(), currentPiece.getPlayerTeamColor()) + " ");
                    }

                } else {
                    if(isPossible) {
                        System.out.print(POSSIBLE_MOVE_IDENTIFIER + " ");
                    }else {
                        System.out.print(EMPTY_CASE_IDENTIFIER + " ");
                    }

                }
            }
            System.out.println();
        }

    }

    public static void printListOfPossibleMoves(RegularMove[] moves, String headerMessage) {
        if (headerMessage != null)
            System.out.println(headerMessage);

        //print all the possible moves if their not null
        for(RegularMove move : moves){
            if(move != null){
                System.out.println(move.toString()+" move : x="+move.getMove().getX()+" y="+move.getMove().getY());
            }
        }
    }

    //File managment print method
    public static void printFileCreationMessage(String filePath, boolean success) {
        if (success) {
            System.out.println(FILE_CREATION_SUCCESS_MESSAGE + " : " + filePath);
        } else {
            System.out.println(FILE_CREATION_FAILURE_MESSAGE + " : " + filePath);
        }

    }

    //LineSpacePrintMethod
    public static void printLineSpace() {
        System.out.println();
    }



    //PUBLIC STATIC CONSTANTS

    //Identifiers characters
    //Be careful, n for "Noir" in french and b for "Blanc" in french (not "Black" and "White")
    public static final char LICORNE_FROM_BLACK_TEAM_IDENTIFIER = 'N';
    public static final char PALADIN_FROM_BLACK_TEAM_IDENTIFIER = 'n';
    public static final char LICORNE_FROM_WHITE_TEAM_IDENTIFIER = 'B';
    public static final char PALADIN_FROM_WHITE_TEAM_IDENTIFIER = 'b';
    public static final char EMPTY_CASE_IDENTIFIER = '-';
    public static final char POSSIBLE_MOVE_IDENTIFIER = 'O';
    public static final char POSSIBLE_CAPTURE_IDENTIFIER = 'X';
    public static final char POSSIBLE_EAT_IDENTIFIER = 'E';

    public static final String WHITE_TEAM_BASE_IDENTIFIER = "blanc";
    public static final String BLACK_TEAM_BASE_IDENTIFIER = "noir";




    //PRIVATE STATIC CONSTANTS

    //File managment messages
    private static final String FILE_CREATION_SUCCESS_MESSAGE = "File created successfully";
    private static final String FILE_CREATION_FAILURE_MESSAGE = "File already exists";

    //Unpected value error message
    private static final String UNEXPECTED_VALUE_ERROR_MESSAGE = "Unexpected value";
}
