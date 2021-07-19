package org.meli.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.math3.geometry.Space;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.meli.exceptions.LocationException;
import org.meli.exceptions.MessagesException;
import org.meli.model.GalacticShip;
import org.meli.model.Position;
import org.meli.model.Satellite;
import org.meli.model.SatelliteWrapper;
import org.meli.model.Spacecraft;
import org.meli.model.responses.TopSecretSplitResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class SplitServiceImpl implements SplitService {
    
    @Inject
    LocationFoundService locationFoundService;

    @Inject
    CommunicationServiceImpl communicationService;

    @ConfigProperty(name = "satellities.0.position") 
    String kenobiPos;

    @ConfigProperty(name = "satellities.1.position") 
    String skywalkerPos;

    @ConfigProperty(name = "satellities.2.position") 
    String satoPos;

    

    final List<Satellite> listaSatelitesGuardados = new ArrayList<Satellite>();
    

    @Override
    public TopSecretSplitResponse saveInfoSatellite(Satellite satellite) throws MessagesException, LocationException {
        uploadPositions(satellite);
        TopSecretSplitResponse tsResponse = new TopSecretSplitResponse();
        try {

            if(!satellite.getPosition().equals(null)){
                listaSatelitesGuardados.add(satellite);
                tsResponse.setMessage("satelite agregado!...esperando peticion post");
                tsResponse.setHttpStatus(HttpStatus.SC_OK);
                return tsResponse;
            }else{
                tsResponse.setMessage("error agregando el satelite Bad request");
                tsResponse.setHttpStatus(HttpStatus.SC_BAD_REQUEST);
                return tsResponse;
            }
            
        } catch (Exception e) {
            throw new NullPointerException();
        }
       
       
    }


    public void uploadPositions(Satellite satellite){ //setea la posicion del satelite correspondiente
        Position pos= null;
        if(satellite.getPosition() == null) {

            if(satellite.getName().toLowerCase().equals("kenobi"))
                pos= findPosition(0);
            if(satellite.getName().toLowerCase().equals("skywalker"))
                pos=  findPosition(1);
            if(satellite.getName().toLowerCase().equals("sato"))
                pos= findPosition(2);

            satellite.setPosition(pos);
                
        }
    }

    public Position findPosition(int n){
        double[] arrayPuntos = {};
        String[] satellitePosArray;
        String[] satellitePosList = {kenobiPos,skywalkerPos,satoPos}; //se graban las 3 posiciones de los satelites
        satellitePosArray = satellitePosList[n].split(","); 
        arrayPuntos = Arrays.stream(satellitePosArray)
        .map(Double::valueOf)
        .mapToDouble(Double::doubleValue)
        .toArray();
        return new Position(arrayPuntos);
    }


    @Override
    public GalacticShip getInfoSatellite() throws MessagesException, LocationException {
        Spacecraft spacecraft = new Spacecraft(null, null);
        if(listaSatelitesGuardados.size() == 3){
            SatelliteWrapper satelliteWrapper = new SatelliteWrapper();
            satelliteWrapper.setSatellities(listaSatelitesGuardados);
            spacecraft = (Spacecraft)communicationService.getGalacticShip(satelliteWrapper);
            
        }else{
            String message= "error";
        }

        return new Spacecraft(spacecraft.getMessage(), spacecraft.getPosition());
    }
}
