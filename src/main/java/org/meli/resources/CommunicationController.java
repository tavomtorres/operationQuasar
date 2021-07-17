package org.meli.resources;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.meli.exceptions.MessagesException;
import org.meli.exceptions.LocationException;
import org.meli.model.SatelliteWrapper;

import org.meli.service.CommunicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



@Path("/topsecret")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommunicationController {
    
    @Inject
    CommunicationService service;


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




}
