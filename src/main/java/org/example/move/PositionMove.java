package org.example.move;
import org.example.Coordinate;


public class PositionMove implements Move{
    //PRIVATE ATTRIBUTE add an array of 6 coordinates
    private final Coordinate[] coordinates;
    private final int playerId;

    //CONSTRUCTOR
    public PositionMove(Coordinate[] coordinates, int playerId) {
        this.coordinates = coordinates;
        this.playerId = playerId;
    }

    public static PositionMove fromString(String move, int playerId) {
        //split the move string into two coordinates the string are in the format B1/B2/B3/B4/B5/B6
        String[] coordinates = move.split("/");
        Coordinate[] coordinatesArray = new Coordinate[6];
        for (int i = 0; i < 6; i++) {
            coordinatesArray[i] = new Coordinate(coordinates[i]);
        }
        return new PositionMove(coordinatesArray, playerId);

    }

    @Override
    public String toString() {
        String result = "";
        for (Coordinate coordinate : coordinates) {
            result += coordinate.toString() + "/";
        }
        return result.substring(0, result.length() - 1);
    }

    //PUBLIC INTERFACE
    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public int getPlayerId() {
        return playerId;
    }








}
