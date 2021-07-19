package org.meli.service;

import org.meli.model.GalacticShip;
import org.meli.model.Satellite;
import org.meli.model.SatelliteWrapper;
import org.meli.model.responses.TopSecretSplitResponse;
import org.meli.exceptions.MessagesException;
import org.meli.exceptions.SatelliteException;
import org.meli.exceptions.LocationException;

public interface CommunicationService {
    
    public GalacticShip getGalacticShip(SatelliteWrapper requestEntity) throws MessagesException, LocationException;
    public TopSecretSplitResponse saveInfoSatellite( final Satellite satellite) throws MessagesException, LocationException, SatelliteException;
    public GalacticShip getInfoSplit( ) throws MessagesException, LocationException;
}
