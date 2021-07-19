package org.meli.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.meli.exceptions.LocationException;
import org.meli.exceptions.MessagesException;
import org.meli.model.GalacticShip;
import org.meli.model.Position;
import org.meli.model.SatelliteWrapper;
import org.meli.model.Spacecraft;
import java.util.Arrays;

@ApplicationScoped
public class CommunicationServiceImpl implements CommunicationService {
    
    @Inject
    LocationFoundService locationFoundService;

    @Inject
    DecryptMessageService decryptMessageService;

    @ConfigProperty(name = "nSatellities") 
    String nSatellities;

    /**posicion de los satelites definida en el application properties */
    @ConfigProperty(name = "satellities.0.position") 
    String satellite0Pos;

    @ConfigProperty(name = "satellities.1.position") 
    String satellite1Pos;

    @ConfigProperty(name = "satellities.2.position") 
    String satellite2Pos;

    @Override
    public GalacticShip getGalacticShip(SatelliteWrapper requestEntity) throws MessagesException , LocationException{

        boolean intersect =false;

        if(requestEntity.getMessages().size() < 2)
            throw new MessagesException("El minimo de mensajes no se ha cumplido en la comunicacion");

        String message = decryptMessageService.getMessage(requestEntity.getMessages());

        uploadPositions(requestEntity);

        if( (requestEntity.getPositions().length < 2) || (requestEntity.getDistances().length < 2) )
            throw new LocationException("El numero de Distancias o posiciones es incorrecto");
    

        double[] points = locationFoundService.getLocation(requestEntity.getPositions(), requestEntity.getDistances());
        Position pos = new Position(points);

        for (int i = 0; i < requestEntity.getSatellities().size(); i++) {
            intersect = locationFoundService.verificationIntersection(requestEntity.getSatellities().get(i), pos);
            if(intersect == false){
                break;
            }      
        }
        
        if(intersect){
            pos.setX(Math.round(pos.getX()*100d/100d));
            pos.setY(Math.round(pos.getY()*100d/100d));
            return new Spacecraft(message, pos);
        }else{
            throw new LocationException("imposible encontrar la nave...las distancias de los satelites no estan en rango de comunicacion");
        }  
    }

    public void uploadPositions(SatelliteWrapper requestEntity){ //setea las posiciones definidas en cada uno de los 3 satelites.

        if(requestEntity.getPositions()[0] == null) {

            int numberSat = Integer.parseInt(nSatellities);
            double[][] pointsList = new double[numberSat][];
            String[] satellitePosList = {satellite0Pos,satellite1Pos,satellite2Pos}; //se graban las 3 posiciones de los satelites
            String[] satellitePos;
            for (int i = 0; i < requestEntity.getSatellities().size(); i++) {
                satellitePos = satellitePosList[i].split(",");
                pointsList[i] = Arrays.stream(satellitePos)
                        .map(Double::valueOf)
                        .mapToDouble(Double::doubleValue)
                        .toArray();
            }
            requestEntity.setPositions(pointsList);
        }
    }
}
