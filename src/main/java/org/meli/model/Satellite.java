package org.meli.model;


import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Satellite" , description = "Satelite de la alianza rebelde")
public class Satellite extends GalacticShip {
    
    @Schema(required = true)
    private String name; //Kenobi - Skywalker - Sato
    @Schema(required = true)
    private double distance;
    @Schema(required = true)
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
