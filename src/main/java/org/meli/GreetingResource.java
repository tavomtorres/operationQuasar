package org.meli;

import org.meli.model.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;


@Path("/hello")
public class GreetingResource {

    private static final Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
         String name = "gustavo";
         LOGGER.info("se agarra el nombre de!!!!!: " + name);
         double[] points = {1.0,2.0}; 
         Position pos = new Position(points);
         GalacticShip nave = new GalacticShip(pos,"mensajexxxx");
         nave.setPosition(pos);
         
        return " posicion de la nave  :" + nave.getPosition() + "el mensaje es: " + nave.getMessage();
    }
}