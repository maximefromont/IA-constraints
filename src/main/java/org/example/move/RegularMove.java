package org.example.move;

import org.example.Coordinate;

public class RegularMove implements  Move{
    //PRIVATE ATTRIBUTE
    private final Coordinate startCoordinate;
    private final Coordinate endCoordinate;
    private final Coordinate move;
    private final int playerId;

    //CONSTRUCTOR
    public RegularMove(Coordinate startCoordinate, Coordinate endCoordinate, int playerId) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.move = startCoordinate.move(endCoordinate);
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return startCoordinate.toString() + "-" + endCoordinate.toString();
    }


    public static RegularMove fromString(String move, int playerId) {
        //split the move string into two coordinates the string are in the format B1-B2
        String[] coordinates = move.split("-");
        Coordinate startCoordinate = new Coordinate(coordinates[0]);
        Coordinate endCoordinate = new Coordinate(coordinates[1]);
        return new RegularMove(startCoordinate, endCoordinate, playerId);
    }


    //PUBLIC INTERFACE
    public Coordinate getStartCoordinate() {
        return startCoordinate;
    }

    public Coordinate getEndCoordinate() {
        return endCoordinate;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Coordinate getMove() {
        return move;
    }
}
