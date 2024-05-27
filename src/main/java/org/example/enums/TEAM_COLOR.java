package org.example.enums;

import org.example.Printinator;

public enum TEAM_COLOR {
    BLACK_TEAM,
    WHITE_TEAM;


    public static TEAM_COLOR getTeamColorFromString(String teamColor) {
        if (teamColor.equals(WHITE_TEAM_LOWERCASE)) {
            return WHITE_TEAM;
        } else if (teamColor.equals(BLACK_TEAM_LOWERCASE)) {
            return BLACK_TEAM;
        }

        return null;
    }

    //PUBLIC CONSTANTS
    public static final String WHITE_TEAM_LOWERCASE = "blanc";
    public static final String BLACK_TEAM_LOWERCASE = "noir";
}
