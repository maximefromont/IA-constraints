package org.example;

import org.example.enums.PIECE_TYPE;
import org.example.enums.TEAM_COLOR;

public record Piece (PIECE_TYPE pieceType,
     TEAM_COLOR playerTeamColor){

    //PUBLIC INTERFACE
    public PIECE_TYPE getPieceType() {
        return pieceType;
    }

    public TEAM_COLOR getPlayerTeamColor() {
        return playerTeamColor;
    }

    //clone method
    public Piece clone(){
        return new Piece(pieceType, playerTeamColor);
    }

}
