package org.meli.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.logging.Logger;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.meli.exceptions.LocationException;
import org.meli.exceptions.MessagesException;
import org.meli.exceptions.SatelliteException;
import org.meli.model.GalacticShip;
import org.meli.model.Position;
import org.meli.model.Satellite;
import org.meli.model.SatelliteWrapper;
import org.meli.model.Spacecraft;
import org.meli.model.responses.TopSecretSplitResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class CommunicationServiceImpl implements CommunicationService {
    
    @Inject
    Logger LOG;

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

    final List<Satellite> listaSatelitesGuardados = new ArrayList<Satellite>();

    @Override
    public GalacticShip getGalacticShip(SatelliteWrapper requestEntity) throws MessagesException , LocationException, SatelliteException{
        LOG.info("/topsecret POST empezando analisis...");
        boolean intersect =false;

        if(requestEntity.getMessages().size() < 2){
            LOG.info("Se borran los satelites agregados en GET /topsecret_split...");
            listaSatelitesGuardados.clear();
            throw new MessagesException("El minimo de mensajes no se ha cumplido en la comunicacion");
        }
            

        String message = decryptMessageService.getMessage(requestEntity.getMessages());
        LOG.info("mensaje secreto desencriptado...");

        boolean allNulls = requestEntity.getSatellities().stream().allMatch(t-> t.getPosition() == null);
        boolean allWithPositions =requestEntity.getSatellities().stream().allMatch(t-> t.getPosition() != null);

        if(allNulls && !allWithPositions)
            uploadPositions(requestEntity);
               
        if(!allNulls && !allWithPositions )
            throw new SatelliteException("Formato incorrecto para posiciones de los satelites, todos deben tener posicion, o ninguno debe tener posicion.");
      
        if( (requestEntity.getPositions().length < 2) || (requestEntity.getDistances().length < 2) )
            throw new LocationException("El numero de Distancias o posiciones es incorrecto");
    
        LOG.info("buscando posicion de la nave...");
        double[] points = locationFoundService.getLocation(requestEntity.getPositions(), requestEntity.getDistances());
        Position pos = new Position(points);

        for (int i = 0; i < requestEntity.getSatellities().size(); i++) {
            intersect = locationFoundService.verificationIntersection(requestEntity.getSatellities().get(i), pos);
            if(intersect == false){
                break;
            }      
        }
        //intersect =true;
        if(intersect){
            pos.setX(Math.round(pos.getX()*100d/100d));
            pos.setY(Math.round(pos.getY()*100d/100d));
            LOG.info("posicion Encontrada!...");
            return new Spacecraft(message, pos);
        }else{
            listaSatelitesGuardados.clear();
            LOG.info("Se borran los satelites agregados en GET /topsecret_split...");
            LOG.error("error verificando la <interseccion de posicion de la nave> !...");
            throw new LocationException("imposible encontrar la nave...las distancias de los satelites no estan en rango de comunicacion");
        }  
    }


    @Override
    public TopSecretSplitResponse saveInfoSatellite(Satellite satellite) throws MessagesException, LocationException,SatelliteException {
       
        TopSecretSplitResponse tsResponse = new TopSecretSplitResponse();
        LOG.info("Virificando si el satelite ya existe...");
        boolean exists= listaSatelitesGuardados.stream().anyMatch(t-> t.getName().equals(satellite.getName()));
        
        if(exists)
            throw new SatelliteException("El nombre del satelite ya fue agregado, intente con otro nombre {kenobi,skywalker,sato}");

        try {
            uploadOneSatellitePosition(satellite);
            listaSatelitesGuardados.add(satellite);
            tsResponse.setMessage("satelite agregado! esperando siguiente peticion post...");
            tsResponse.setHttpStatus(HttpStatus.SC_OK);

            } catch (Exception e ) {
                LOG.error("Error al intentar agregar el satelite...!", e);
                throw new SatelliteException("error al agregar nuevo satelite");
            }
            LOG.info("satelite agregado!...");
            return tsResponse;
       
    }

    
    /**metodo get para topsecret_split */
    @Override
    public GalacticShip getInfoSplit() throws MessagesException, LocationException {
        Spacecraft spacecraft = new Spacecraft();

        LOG.info("Empezando analisis para encontrar nave split...");
        try { 
            LOG.info("Verificando si la lista de satelites fue usada en un anterior GET /topsecret_split..."); 
            if(listaSatelitesGuardados.get(0).getMessage().size() < 1){
                listaSatelitesGuardados.clear();
                LOG.info("Lista de satelites Limpiada...");       
            }
                SatelliteWrapper satelliteWrapper = new SatelliteWrapper();
                satelliteWrapper.setSatellities(listaSatelitesGuardados);
                LOG.info("Se inicia busqueda de nave split..."); 
                spacecraft = (Spacecraft)getGalacticShip(satelliteWrapper);
            
        } catch (MessagesException e) {
            listaSatelitesGuardados.clear();
            LOG.info("Lista de satelites Limpiada..."); 
            LOG.error("MessagesException GET split ==> ", e); 
            throw new MessagesException("la informacion es insuficiente para determinar la posicion y el mensaje de la nave");
        }
         catch (LocationException e) {
            listaSatelitesGuardados.clear();
            LOG.info("Lista de satelites Limpiada..."); 
            LOG.error("LocationException GET split ==> ", e); 
            throw new LocationException("la informacion es insuficiente para determinar la posicion y el mensaje de la nave");
        }
        catch( Exception e){
            listaSatelitesGuardados.clear();
            LOG.info("Lista de satelites Limpiada..."); 
            LOG.error("Exception no controlada en GET split ==> ", e); 
            throw new MessagesException("informacion borrada por seguridad intergalactica, vuelva a cargar los satelites");
        }
        LOG.info("se ha encontrado la nave split!..."); 
        listaSatelitesGuardados.clear();
        LOG.info("Lista de satelites Limpiada...");
        return new Spacecraft(spacecraft.getMessage(), spacecraft.getPosition());
    }

    private void uploadPositions(SatelliteWrapper requestEntity){ //setea las posiciones definidas en cada uno de los 3 satelites.

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

    private void uploadPositions2(SatelliteWrapper requestEntity){ //setea las posiciones definidas en cada uno de los 3 satelites de forma random

        // x1 = randint(-150,-80)
        // y1 = randint(-150,150)
        // x2 = randint(80,150)
        // y2 = randint(20,150)
        // x3 = randint(80,150)
        // y3 = randint(-150,-20)
        // x = randint(-60,60)
        // y = randint(-60,60)
        Random x1 = new Random();
        Random y1 = new Random();
        double randomX1 = -150 + (-80 - (-150)) * x1.nextDouble();
        double randomY1 = -150 + (150 - (-150)) * y1.nextDouble();
        Random x2 = new Random();
        Random y2 = new Random();
        double randomX2 = 80 + (150 - 80) * x2.nextDouble();
        double randomY2 = 20 + (150 - 20) * y2.nextDouble();
        Random x3 = new Random();
        Random y3 = new Random();
        double randomX3 = 80 + (150 - 80) * x3.nextDouble();
        double randomY3 = -150 + (-20 - (-150)) * y3.nextDouble();


        satellite0Pos= Double.toString(randomX1).format("%,.2f", randomX1) + ","+Double.toString(randomY1).format("%,.2f", randomY1);
        satellite1Pos= Double.toString(randomX2).format("%,.2f", randomX2) + ","+Double.toString(randomY2).format("%,.2f", randomY2);
        satellite2Pos= Double.toString(randomX2).format("%,.2f", randomX3) + ","+Double.toString(randomY2).format("%,.2f", randomY3);
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


    private void uploadOneSatellitePosition(Satellite satellite){ //setea la posicion del satelite correspondiente
        Position pos= null;
        if(satellite.getPosition() == null) {

            if(satellite.getName().toLowerCase().equals("kenobi"))
                pos= findPosition(0);
            if(satellite.getName().toLowerCase().equals("skywalker"))
                pos=  findPosition(1);
            if(satellite.getName().toLowerCase().equals("sato"))
                pos= findPosition(2);

        if(pos == null)
            satellite= null;
            satellite.setPosition(pos);
                
        }
    }

    private Position findPosition(int n){
        double[] arrayPuntos = {};
        String[] satellitePosArray;
        String[] satellitePosList = {satellite0Pos,satellite1Pos,satellite2Pos}; //se graban las 3 posiciones de los satelites
        satellitePosArray = satellitePosList[n].split(","); 
        arrayPuntos = Arrays.stream(satellitePosArray)
                      .map(Double::valueOf)
                      .mapToDouble(Double::doubleValue)
                      .toArray();
        return new Position(arrayPuntos);
    }


   
}
