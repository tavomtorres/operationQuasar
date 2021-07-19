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
import org.meli.exceptions.LocationException;
import org.meli.model.Satellite;


import org.meli.service.SplitService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Path("/topsecret_split")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommunicationSplitController {
    

    @Inject
    SplitService service;

    @GET
    public Response getTopSecretSplit(){
            try {
                return Response.ok().entity(service.getInfoSatellite()).build();
            } catch (MessagesException e ) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }catch (LocationException e){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
    }

    @POST
    @Path("/{satellite_name}")
    public Response topSecretSplit(@PathParam("satellite_name")  String satelliteName, final Satellite satellite){
        satellite.setName(satelliteName);
            try {
                return Response.ok().entity(service.saveInfoSatellite(satellite)).build();
            } catch (MessagesException e ) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }catch (LocationException e){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
    }




}
