package org.example;

import org.example.enums.PIECE_TYPE;
import org.example.enums.TEAM_COLOR;

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

    //File managment methods
    public static void printFileCreationMessage(String filePath, boolean success) {
        if (success) {
            System.out.println(FILE_CREATION_SUCCESS_MESSAGE + " : " + filePath);
        } else {
            System.out.println(FILE_CREATION_FAILURE_MESSAGE + " : " + filePath);
        }

    }



    //PUBLIC STATIC CONSTANTS

    //Identifiers characters
    //Be careful, n for "Noir" in french and b for "Blanc" in french (not "Black" and "White")
    public static final char LICORNE_FROM_BLACK_TEAM_IDENTIFIER = 'N';
    public static final char PALADIN_FROM_BLACK_TEAM_IDENTIFIER = 'n';
    public static final char LICORNE_FROM_WHITE_TEAM_IDENTIFIER = 'B';
    public static final char PALADIN_FROM_WHITE_TEAM_IDENTIFIER = 'b';



    //PRIVATE STATIC CONSTANTS

    //File managment messages
    private static final String FILE_CREATION_SUCCESS_MESSAGE = "File created successfully";
    private static final String FILE_CREATION_FAILURE_MESSAGE = "File already exists";

}
