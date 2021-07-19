package org.meli.model;

public class Spacecraft extends GalacticShip{
    
    private String message; //mensaje de la nave transbordador

    public Spacecraft(){
        
    }
    public Spacecraft(String message, Position position){
        this.setPosition(position);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message= message;
    }
}
