package org.example;

import org.example.enums.PIECE_TYPE;
import org.example.enums.TEAM_COLOR;

public class Printinator {

    //PUBLIC STATIC METHODS
    public static String getPieceCharacter(PIECE_TYPE pieceType, TEAM_COLOR teamColor) {
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

    //PUBLIC STATIC CONSTANTS
    //Be careful, n for "Noir" in french and b for "Blanc" in french (not "Black" and "White")
    public static final String LICORNE_FROM_BLACK_TEAM_IDENTIFIER = "N";
    public static final String PALADIN_FROM_BLACK_TEAM_IDENTIFIER = "n";
    public static final String LICORNE_FROM_WHITE_TEAM_IDENTIFIER = "B";
    public static final String PALADIN_FROM_WHITE_TEAM_IDENTIFIER = "b";

}
