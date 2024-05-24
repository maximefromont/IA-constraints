package org.example;
import org.junit.Assert;
// TODO add restriction to the coordinate
public class Coordinate {


    //PRIVATE ATTRIBUTES
    private int x;
    private int y;


    //CONSTRUCTORS
    public Coordinate(int x, int y) {
        this.y = y;
        this.x = x;
    }

    public Coordinate(String coordinate) {
        this.x = coordinate.charAt(0) - 'A';
        this.y = Integer.parseInt(coordinate.substring(1)) - 1;
    }


    //PUBLIC METHODS
    public String toString() {
        return (char)('A' + x) + "" + (y + 1);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            Coordinate other = (Coordinate) obj;
            return x == other.x && y == other.y;
        }
        return false;
    }

    public Coordinate move(Coordinate other){
        return new Coordinate(other.x - x, other.y - y);
    }


    //GETTERS
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //MAIN
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


        Assert.assertEquals("A1", new Coordinate(0,0).toString());
        Assert.assertEquals("A2", new Coordinate(0,1).toString());
        Assert.assertEquals("A3", new Coordinate(0,2).toString());


        Assert.assertEquals("B1", new Coordinate(1,0).toString());
        Assert.assertEquals("B2", new Coordinate(1,1).toString());
        Assert.assertEquals("B3", new Coordinate(1,2).toString());


        Assert.assertEquals("C1", new Coordinate(2,0).toString());
        Assert.assertEquals("C2", new Coordinate(2,1).toString());
        Assert.assertEquals("C3", new Coordinate(2,2).toString());




        Assert.assertEquals("A1", new Coordinate("A1").toString());
        Assert.assertEquals("B2", new Coordinate("B2").toString());
        Assert.assertEquals("C3", new Coordinate("C3").toString());
        Assert.assertEquals("D4", new Coordinate("D4").toString());
        Assert.assertEquals("E5", new Coordinate("E5").toString());
        Assert.assertEquals("F6", new Coordinate("F6").toString());

        Assert.assertEquals("A2", new Coordinate("A2").toString());
        Assert.assertEquals("A4", new Coordinate("A4").toString());

        Assert.assertEquals("B3", new Coordinate("B3").toString());
        Assert.assertEquals("B5", new Coordinate("B5").toString());

        Assert.assertEquals("C4", new Coordinate("C4").toString());
        Assert.assertEquals("C6", new Coordinate("C6").toString());

        Assert.assertEquals("D5", new Coordinate("D5").toString());
        Assert.assertEquals("D6", new Coordinate("D6").toString());

    }

}
