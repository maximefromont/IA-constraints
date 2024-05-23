package org.example;
import org.junit.Assert;
// TODO add restriction to the coordinate
public class Coordinate {


    //PRIVATE ATTRIBUTES
    private int y;
    private int x;

    //CONSTRUCTOR
    public Coordinate(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public Coordinate(String coordinate) {
        this.y = coordinate.charAt(0) - 'A';
        this.x = Integer.parseInt(coordinate.substring(1)) - 1;
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


    //add main method
    public static void main(String[] args) {
        Coordinate coordinate = new Coordinate(0, 0);
        System.out.println(coordinate.toString());
        System.out.println(coordinate.move(new Coordinate(1, 1)).toString());
        System.out.println(coordinate.move(new Coordinate(1, 0)).toString());
        System.out.println(coordinate.move(new Coordinate(0, 1)).toString());
        System.out.println(coordinate.move(new Coordinate(-1, -1)).toString());
        System.out.println(coordinate.move(new Coordinate(-1, 0)).toString());
        System.out.println(coordinate.move(new Coordinate(0, -1)).toString());

        System.out.println("A1: "+new Coordinate("A1").toString());
        System.out.println("B2: "+new Coordinate("B2").toString());
        System.out.println("C3: "+new Coordinate("C3").toString());
        System.out.println("D4: "+new Coordinate("D4").toString());
        System.out.println("E5: "+new Coordinate("E5").toString());
        System.out.println("F6: "+new Coordinate("F6").toString());

        System.out.println("A2: "+new Coordinate("A2").toString());
        System.out.println("B3: "+new Coordinate("B3").toString());




        Assert.assertEquals("A1", new Coordinate("A1").toString());
        Assert.assertEquals("B2", new Coordinate("B2").toString());
        Assert.assertEquals("C3", new Coordinate("C3").toString());
        Assert.assertEquals("D4", new Coordinate("D4").toString());
        Assert.assertEquals("E5", new Coordinate("E5").toString());
        Assert.assertEquals("F6", new Coordinate("F6").toString());

        Assert.assertEquals("A2", new Coordinate("A2").toString());

    }

}
