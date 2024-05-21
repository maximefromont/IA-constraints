package org.example;

public class Coup {

    //PRIVATE ATTRIBUTE
    private Coordinate startCoordinate;
    private Coordinate endCoordinate;
    private int playerId;

    //CONSTRUCTOR
    public Coup(Coordinate startCoordinate, Coordinate endCoordinate, int playerId) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.playerId = playerId;
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

}

