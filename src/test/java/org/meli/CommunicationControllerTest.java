package org.meli;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test
;
import org.meli.exceptions.LocationException;
import org.meli.exceptions.MessagesException;
import org.meli.model.Satellite;
import org.meli.model.SatelliteWrapper;
import org.meli.service.CommunicationService;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

import com.google.gson.Gson;



@QuarkusTest
public class CommunicationControllerTest {
    
    @Inject
    CommunicationService service;

    @Test
    public void testCommunicationController() throws MessagesException, LocationException{
        List<Satellite> listSatellites = new ArrayList<Satellite>();
        SatelliteWrapper satellities = new SatelliteWrapper();
        List<String> msj1 = Arrays.asList("este", "", "", "mensaje", "");
        List<String> msj2 = Arrays.asList("", "es", "", "", "secreto");
        List<String> msj3 = Arrays.asList("este", "", "un", "", "");
        double d1=199.18082237;
        double d2=122.347864714;
        double d3=75.7693869581;
        Satellite sat1 = new Satellite();
        Satellite sat2 = new Satellite();
        Satellite sat3 = new Satellite();

        sat1.setName("kenobi");
        sat1.setDistance(d1);
        sat1.setMessage(msj1);

        sat2.setName("skywalker");
        sat2.setDistance(d2);
        sat2.setMessage(msj2);

        sat3.setName("sato");
        sat3.setDistance(d3);
        sat3.setMessage(msj3);

        listSatellites.add(sat1);
        listSatellites.add(sat2);
        listSatellites.add(sat3);

        satellities.setSatellities(listSatellites);

        Gson jsonBody = new Gson();

        //se serializa la clase SatelliteWrapper a Json string
        String json = jsonBody.toJson(satellities);

        given()
        .contentType("application/json")
        .body(json)
        .when()
        .post("topsecret")
        .then()
        .statusCode(200);
       
    }

}
