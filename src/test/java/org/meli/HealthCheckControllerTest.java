package org.meli;

import io.quarkus.test.junit.QuarkusTest;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.meli.model.responses.HealthCheckResponse;

import static io.restassured.RestAssured.given;
import com.google.gson.Gson;


@QuarkusTest
public class HealthCheckControllerTest {

    @ConfigProperty(name = "quarkus.app.version") 
    String version;
    @ConfigProperty(name= "quarkus.app.name")
    String appName;

    @Test
    public void testHealthCheckEndpoint() {
        Gson jsonBody = new Gson();

        HealthCheckResponse response = new HealthCheckResponse();
        response.setVersion(version);
        response.setAppName(appName);
        response.setStatus("OK");
        String json = jsonBody.toJson(response);

        given()
        .contentType("application/json")
        .body(json)
        .when()
        .get("health")
        .then()
        .statusCode(200);

    }
}