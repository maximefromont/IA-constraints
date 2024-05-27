package org.example.enums;

public enum TEAM_COLOR {
    BLACK_TEAM,
    WHITE_TEAM;


    public static TEAM_COLOR getTeamColorFromString(String teamColor) {
        if (teamColor.equals(WHITE_TEAM_STRING)) {
            return WHITE_TEAM;
        } else if (teamColor.equals(BLACK_TEAM_STRING)) {
            return BLACK_TEAM;
        }

        return null; //By default null = error
    }

    public static TEAM_COLOR getTeamColorFromInt(int teamColor) {
        if (teamColor == WHITE_TEAM_INT) {
            return WHITE_TEAM;
        } else if (teamColor == BLACK_TEAM_INT) {
            return BLACK_TEAM;
        }

        return null; //By default null = error
    }

    public static TEAM_COLOR getOppositeTeamColor(TEAM_COLOR teamColor) {
        if (teamColor == WHITE_TEAM) {
            return BLACK_TEAM;
        } else {
            return WHITE_TEAM;
        }
    }

    public static String getTeamColorStringFromTeamColor(TEAM_COLOR teamColor) {
        if (teamColor == WHITE_TEAM) {
            return WHITE_TEAM_STRING;
        } else if (teamColor == BLACK_TEAM) {
            return BLACK_TEAM_STRING;
        }

        return null; //By default null = error
    }

    //PUBLIC CONSTANTS
    public static final String WHITE_TEAM_STRING = "blanc";
    public static final String BLACK_TEAM_STRING = "noir";

    public static final int WHITE_TEAM_INT = -1;
    public static final int BLACK_TEAM_INT = 1;
}
