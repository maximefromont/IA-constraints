package org.example;

public class Pion {

    //PRIVATE INTERFACE
    private String type;
    private int playerId;

    //CONSTRUCTOR
    public Pion(String type, int playerId) {
        this.type = type;
        this.playerId = playerId;
    }

    //PUBLIC INTERFACE
    public String getType() {
        return type;
    }

    public int getPlayerId() {
        return playerId;
    }

    //PUBLIC CONSTANTS
    public static final String PION_TYPE_LICORNE = "Licorne";
    public static final String PION_TYPE_PALADIN = "Paladin";

}
