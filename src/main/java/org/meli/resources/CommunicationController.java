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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
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
    Logger LOG;

    @Inject
    CommunicationService service;

    @Path("/topsecret")
    @POST
    @Operation(
            operationId ="topsecret", 
            summary = "triangula la posicion de la nave" , 
            description = "Triangula la posicion de una nave a traves de 3 satelites usando trillateration *REVISAR README para obtener ejemplos de request*"
    )
    @APIResponse(
            responseCode = "200",
            description = "triangulacion exitosa, se encontro el mensaje",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SatelliteWrapper.class))
    )
    @APIResponse(
        responseCode = "404",
        description = "No se pudo determinar la posicion o el mensaje",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SatelliteWrapper.class))
)
    @Schema(implementation = SatelliteWrapper.class)
    public Response topsecret(
            @RequestBody(
                    description = "Lista de satelites",
                    required =true,
                    content = @Content(schema = @Schema(implementation = SatelliteWrapper.class))
                    )    
                    SatelliteWrapper requestEntity){

        try {
            LOG.info("/topsecret POST BEGIN...");
            return Response.ok().entity(service.getGalacticShip(requestEntity)).build();
            
        } catch (MessagesException e ) {
            LOG.error("MessagesException ==> " , e );
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }catch (LocationException e){
            LOG.error("LocationException ==> " , e );
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        catch (SatelliteException e){
            LOG.error("SatelliteException ==> " , e );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        catch(Exception e){
            LOG.error("Error que excede las excepciones controladas ! ==> ");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        
    }

    @POST
    @Path("topsecret_split/{satellite_name}")
    @Operation(
        operationId ="topsecret_split", 
        summary = "Agrega un satelite" , 
        description = "Agrega uno por uno los satelites que triangularan la posicion de la nave *REVISAR README para mas informacion*"
    )
    @APIResponse(
            responseCode = "200",
            description = "Satelite agregado con exito",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Satellite.class))
    )
    @APIResponse(
        responseCode = "400",
        description = "No se pudo agregar el satelite",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Satellite.class))
    )
    @Schema(implementation = Satellite.class)
    public Response topSecretSplit(@PathParam("satellite_name") String satelliteName, final Satellite satellite){
        satellite.setName(satelliteName);
            try {
                LOG.info("/topsecret_split POST BEGIN...");
                return Response.ok().entity(service.saveInfoSatellite(satellite)).build();
            } catch (MessagesException e ) {
                LOG.error("MessagesException ==> " , e );
               
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            } catch (LocationException e){
                LOG.error("LocationException ==> " , e );
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
            catch (SatelliteException e){
                LOG.error("SatelliteException ==> " , e );
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
    }

    @GET
    @Path("topsecret_split")
    @Operation(
        operationId ="topsecret_split", 
        summary = "Triangula la posicion de la nave" , 
        description = "Una vez agregados 3 satelites con el metodo POST de topsecret_split , triangulara la posicion de la nave.*REVISAR README para obtener ejemplos de request*"
    )
    @APIResponse(
            responseCode = "200",
            description = "Nave encontrada y mensaje descrifrado"
    )
    @APIResponse(
        responseCode = "404",
        description = "Informacion insuficiente para encontrar la nave y descrifrar el mensaje"
    )
    public Response getTopSecretSplit(){
            try {
                LOG.info("/topsecret_split GET BEGIN...");
                return Response.ok().entity(service.getInfoSplit()).build();
            } catch (MessagesException e ) {
                LOG.error("MessagesException ==> " , e );
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }catch (LocationException e){
                LOG.error("LocationException ==> " , e );
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            }
    }


}
