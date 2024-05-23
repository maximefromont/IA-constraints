package org.example.enums;

public enum TEAM_COLOR {
    BLACK_TEAM,
    WHITE_TEAM;

    @Override
    public String toString() { //Be careful, n for "Noir" in french and b for "Blanc" in french (not "Black" and "White")
        if (this == BLACK_TEAM) {
            return "n";
        } else {
            return "b";
        }
    }
}
