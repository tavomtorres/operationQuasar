package org.meli.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SatelliteWrapper {
    
    private List<Satellite> satellities; //esto seria escalable en caso de recibir mas de 3 satelites por metodo POST.

    public List<Satellite> getSatellities(){
        return satellities;
    }

    public void setSatellities(List<Satellite> satellities){
        this.satellities = satellities;
    }

    public double[] getDistances(){ //obtiene las distancias de los satelites de la lista

        double [] distances = new double[satellities.size()];
        for(int i = 0; i < satellities.size(); i ++){
            distances[i] = satellities.get(i).getDistance();
        }
        return  distances;
    }

    public double[][] getPositions(){ //obtiene las posiciones de los satelites de la lista
        double [][] positions = new double[satellities.size()][];
        String[] points;
        for(int i = 0; i < satellities.size(); i ++){
            if(satellities.get(i).getPosition() != null) {
                points = satellities.get(i).getPosition().toString().split(",");
                positions[i] = Arrays.stream(points)
                        .map(Double::valueOf)
                        .mapToDouble(Double::doubleValue)
                        .toArray();
            }
        }
        return positions;
    }

    public void setPositions(double[][] pointsList){ //setea las posiciones e los satelites que estan en la lista
        Position position;
        for(int i = 0; i < pointsList.length; i++){
            position = new Position(pointsList[i]);
            satellities.get(i).setPosition(position);
        }
    }

    public List<List<String>> getMessages(){ //obtiene los mensajes de los satelites de la lista
        List<List<String>> messages = new ArrayList<List<String>>();
        for(Satellite s : satellities){
            messages.add(s.getMessage());
        }
        return  messages;
    }
}
