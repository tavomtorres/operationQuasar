package org.meli;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meli.exceptions.LocationException;
import org.meli.exceptions.MessagesException;
import org.meli.model.GalacticShip;
import org.meli.model.Satellite;
import org.meli.model.SatelliteWrapper;
import org.meli.model.Spacecraft;
import org.meli.model.Position;
import org.meli.service.CommunicationService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;


@QuarkusTest
public class CommunicationControllerTest {
    
    // @Inject
    // CommunicationService service;

    // @Test
    // public void testCommunicationController() throws MessagesException, LocationException{
    //     List<Satellite> listSatellites = new ArrayList<Satellite>();
    //     SatelliteWrapper satellities = new SatelliteWrapper();
    //     List<String> msj1 = Arrays.asList("este", "", "", "mensaje", "");
    //     List<String> msj2 = Arrays.asList("", "es", "", "", "secreto");
    //     List<String> msj3 = Arrays.asList("este", "", "un", "", "");
    //     double d1=199.18082237;
    //     double d2=122.347864714;
    //     double d3=75.7693869581;
    //     double[] points ={48.0,-56.0};
    //     Position posfinal = new Position(points);
    //     Satellite sat1 = new Satellite();
    //     Satellite sat2 = new Satellite();
    //     Satellite sat3 = new Satellite();

    //     sat1.setName("kenobi");
    //     sat1.setDistance(d1);
    //     sat1.setMessage(msj1);

    //     sat2.setName("skywalker");
    //     sat2.setDistance(d2);
    //     sat2.setMessage(msj2);

    //     sat3.setName("sato");
    //     sat3.setDistance(d3);
    //     sat3.setMessage(msj3);

    //     listSatellites.add(sat1);
    //     listSatellites.add(sat2);
    //     listSatellites.add(sat3);

    //     satellities.setSatellities(listSatellites);

    //     //aqui en el body nose que poner
    //     given()
    //     .contentType("application/json")
    //     .body(service.getGalacticShip(satellities))
    //     .when()
    //     .post("/satellites/topsecret")
    //     .then()
    //     .statusCode(200);
       
    // }

}
