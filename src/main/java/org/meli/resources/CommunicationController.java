package org.meli.resources;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.meli.exceptions.MessagesException;
import org.meli.exceptions.SatelliteException;
import org.meli.exceptions.LocationException;
import org.meli.model.SatelliteWrapper;
import org.meli.model.Satellite;
import org.meli.service.CommunicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommunicationController {
    
    @Inject
    CommunicationService service;

    @Path("/topsecret")
    @POST
    public Response topsecret(SatelliteWrapper requestEntity){

        try {
            return Response.ok().entity(service.getGalacticShip(requestEntity)).build();
        } catch (MessagesException e ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }catch (LocationException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @POST
    @Path("topsecret_split/{satellite_name}")
    public Response topSecretSplit(@PathParam("satellite_name")  String satelliteName, final Satellite satellite){
        satellite.setName(satelliteName);
            try {
                return Response.ok().entity(service.saveInfoSatellite(satellite)).build();
            } catch (MessagesException e ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            } catch (LocationException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
            catch (SatelliteException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
    }

    @GET
    @Path("topsecret_split")
    public Response getTopSecretSplit(){
            try {
                return Response.ok().entity(service.getInfoSplit()).build();
            } catch (MessagesException e ) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }catch (LocationException e){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
    }


}
