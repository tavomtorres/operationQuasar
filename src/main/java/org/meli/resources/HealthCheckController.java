package org.meli.resources;


import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.meli.model.responses.HealthCheckResponse;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HealthCheckController {

    @ConfigProperty(name = "quarkus.app.version") 
    String version;
    @ConfigProperty(name= "quarkus.app.name")
    String appName;

    @GET
    @Operation(
        operationId ="healthCheck", 
        summary = "API HealthCheck" , 
        description = "Verifica el estado y la version de la API."
    )
    public Response healthCheck(){
        HealthCheckResponse response = new HealthCheckResponse();
        response.setAppName(appName);
        response.setVersion(version);
        response.setStatus("OK");
        return Response.ok().entity(response).build();
    }
}
