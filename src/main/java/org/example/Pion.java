package org.example;

public class Pion {

    //PRIVATE INTERFACE
    private String type;

    //CONSTRUCTOR
    public Pion(String type) {
        this.type = type;
    }

    //PUBLIC INTERFACE
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //PUBLIC CONSTANTS
    public static final String PION_TYPE_LICORNE = "Licorne";
    public static final String PION_TYPE_PALADIN = "Paladin";

}
