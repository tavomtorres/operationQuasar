package org.meli.service;

import javax.enterprise.context.ApplicationScoped;
import org.meli.exceptions.LocationException;
import org.meli.exceptions.MessagesException;
import org.meli.model.GalacticShip;
import org.meli.model.Position;
import org.meli.model.SatelliteWrapper;
import org.meli.model.Spacecraft;

@ApplicationScoped
public class CommunicationServiceImpl implements CommunicationService {
    
    @Override
    public GalacticShip getGalacticShip(SatelliteWrapper requestEntity) throws MessagesException , LocationException{

        double[] points = {1.0,2.0};
        Position pos = new Position(points);
        return new Spacecraft("mensaje ok!", pos);
    }
}
