package org.meli.model;


import java.util.List;

public class Satellite extends GalacticShip {
    
    private String name; //Kenobi - Skywalker - Sato
    private double distance;
    private List<String> message; // message = ["esta es", "una", " ", "lista", "de mensajes"]

    public double getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

}
