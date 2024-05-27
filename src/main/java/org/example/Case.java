package org.example;

public class Case {

    //PRIVATE ATTRIBUTES
    private Piece piece;
    private final int value;

    //CONSTRUCTOR
    public Case(int value) {
        this.value = value;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getValue() {
        return value;
    }
}
