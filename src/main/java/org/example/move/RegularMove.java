package org.example.move;

import org.example.Coordinate;
import org.example.enums.TEAM_COLOR;

public class RegularMove extends Move{

    //PRIVATE ATTRIBUTE
    private Coordinate startCoordinate;
    private Coordinate endCoordinate;
    private Coordinate move;

    //CONSTRUCTORS
    public RegularMove(Coordinate startCoordinate, Coordinate endCoordinate, TEAM_COLOR teamColor) {
        super(teamColor);
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.move = startCoordinate.move(endCoordinate);
    }


    public RegularMove(String move, TEAM_COLOR teamColor) {
        super(teamColor);
        //split the move string into two coordinates the string are in the format B1-B2
        String[] coordinates = move.split("-");
        this.startCoordinate = new Coordinate(coordinates[0]);
        this.endCoordinate = new Coordinate(coordinates[1]);
        this.move = startCoordinate.move(endCoordinate);
    }


    //PUBLIC INTERFACE
    @Override
    public String toString() {
        return startCoordinate.toString() + "-" + endCoordinate.toString();
    }
    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }

    public Coordinate getEndCoordinate() {
        return endCoordinate;
    }

    public Coordinate getMove() {
        return move;
    }
}
