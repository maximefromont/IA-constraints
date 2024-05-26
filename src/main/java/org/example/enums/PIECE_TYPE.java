package org.example.enums;

import org.example.Printinator;

public enum PIECE_TYPE {

    LICORNE,
    PALADIN;

    public String getStringAccordingToPieceType(String team_color) {
        if (this == LICORNE) {
            return team_color.toUpperCase();
        } else {
            return team_color.toLowerCase();
        }
    }



}
