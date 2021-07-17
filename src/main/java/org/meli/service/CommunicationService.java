package org.meli.service;

import org.meli.model.GalacticShip;

import org.meli.model.SatelliteWrapper;
import org.meli.exceptions.MessagesException;
import org.meli.exceptions.LocationException;

public interface CommunicationService {
    
    public GalacticShip getGalacticShip(SatelliteWrapper requestEntity) throws MessagesException, LocationException;
}
