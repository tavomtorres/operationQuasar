package org.meli.service;

import org.meli.exceptions.LocationException;
import org.meli.exceptions.MessagesException;
import org.meli.model.GalacticShip;
import org.meli.model.Satellite;
import org.meli.model.responses.TopSecretSplitResponse;

public interface SplitService {
    
    public TopSecretSplitResponse saveInfoSatellite( final Satellite satellite) throws MessagesException, LocationException;
    public GalacticShip getInfoSatellite( ) throws MessagesException, LocationException;
}
