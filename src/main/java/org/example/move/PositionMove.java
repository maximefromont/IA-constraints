package org.example.move;
import org.example.Coordinate;
import org.example.enums.TEAM_COLOR;


public class PositionMove extends Move{

    //PRIVATE ATTRIBUTE add an array of 6 coordinates
    private Coordinate[] coordinates;

    //CONSTRUCTORS
    public PositionMove(Coordinate[] coordinates, TEAM_COLOR teamColor) {
        super(teamColor);
        this.coordinates = coordinates;
    }

    public PositionMove(String move, TEAM_COLOR teamColor) {
        super(teamColor);
        //split the move string into two coordinates the string are in the format B1/B2/B3/B4/B5/B6
        String[] coordinates = move.split("/");
        Coordinate[] coordinatesArray = new Coordinate[6];
        for (int i = 0; i < 6; i++) {
            coordinatesArray[i] = new Coordinate(coordinates[i]);
        }
    }

    //PUBLIC INTERFACE
    @Override
    public String toString() {
        String result = "";
        for (Coordinate coordinate : coordinates) {
            result += coordinate.toString() + "/";
        }
        return result.substring(0, result.length() - 1);
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }
}
