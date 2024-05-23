package org.example;

import org.example.enums.PIECE_TYPE;
import org.example.enums.TEAM_COLOR;

public class Piece {

    //PRIVATE INTERFACE
    private PIECE_TYPE pieceType;
    private TEAM_COLOR playerTeamColor;

    //CONSTRUCTOR
    public Piece(PIECE_TYPE pieceType, TEAM_COLOR playerTeamColor) {
        this.pieceType = pieceType;
        this.playerTeamColor = playerTeamColor;
    }

    //PUBLIC INTERFACE
    public PIECE_TYPE getPieceType() {
        return pieceType;
    }

    public TEAM_COLOR getPlayerTeamColor() {
        return playerTeamColor;
    }

}
