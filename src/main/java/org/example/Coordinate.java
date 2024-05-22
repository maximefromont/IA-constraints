package org.example;

public class Coordinate {


    //PRIVATE ATTRIBUTES
    private int x;
    private int y;

    //create a constructor from a string with the format A1
    public static Coordinate fromString(String coordinate) {
        int x = coordinate.charAt(0) - 'A';
        int y = Integer.parseInt(coordinate.substring(1)) - 1;
        return new Coordinate(x, y);
    }

    //CONSTRUCTOR
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //override the toString method

    public String toString() {
        return (char)('A' + y) + "" + (x + 1);
    }

    //add equals method
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            Coordinate other = (Coordinate) obj;
            return x == other.x && y == other.y;
        }
        return false;
    }

    //add a function to get the relative move to go from a coordinate tp an other
    public Coordinate move(Coordinate other){
        return new Coordinate(other.x - x, other.y - y);

    }





    //PUBLIC INTERFACE
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
