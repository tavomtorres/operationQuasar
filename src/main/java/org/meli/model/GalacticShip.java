package org.meli.model;

public class GalacticShip {

    private String message;
    private Position position; //posicion de la nave al momento de mandar mensaje

    public GalacticShip(Position position, String message){
        this.setPosition(position);
        this.message = message;
    }

    public Position getPosition(){
        return position;
    }
    
    public String getMessage(){
        return message;
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public void setMessage(String message){
        this.message= message;
    }


}
