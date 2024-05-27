package org.example.move;

import org.example.enums.TEAM_COLOR;

public class Move {

    //PRIVATE ATTRIBTUE
    private TEAM_COLOR teamColor;

    //CONSTRUCTOR
    public Move(TEAM_COLOR teamColor) {
        this.teamColor = teamColor;
    }

    //PUBLIC INTERFACE
    public TEAM_COLOR getTeamColor() {
        return teamColor;
    }

}
