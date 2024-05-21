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

    public void setValue(int value) {
        if(value < 0 || value > 3) throw new IllegalArgumentException("Value must be between 0 and 3");
        this.value = value;
    }
}
