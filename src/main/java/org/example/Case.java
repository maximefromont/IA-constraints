package org.example;

public class Case {

    //PRIVATE ATTRIBUTES
    private Pion pion;
    private int value;

    //CONSTRUCTOR
    public Case(int value) {
        this.value = value;
    }

    public Pion getPion() {
        return pion;
    }

    public void setPion(Pion pion) {
        this.pion = pion;
    }

    public int getValue() {
        return value;
    }
}
