package recruit;

import base.BaseRecruit;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CandidateTest extends BaseRecruit {

    private static String candidateID;
    private static String token;
    //CRUD
    //Create, Read, Update, Delete

    @Test
    @Order(1)
    public void createCandidate() {
        String body = readFromJsonFile("src/test/resources/recruit/candidateJSON/createCandidate.json");
        Response candidate = createNewCandidate(body);
        assertEquals(201, candidate.getStatusCode());
        candidateID = candidate.jsonPath().getString("id");
    }

    @Test
    @Order(2)
    public void readCandidate() {
        Response read = given()
                .contentType(ContentType.JSON)
                .pathParam("id", candidateID)
                .when()
                .get("/candidates/{id}")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("recruit/verifyResponseSchema.json"))
                .log().all()
                .extract().response();
        assertEquals(200, read.getStatusCode());
        assertEquals(candidateID, read.jsonPath().getString("id"));
        //Assert all other data
    }

    @Test
    @Order(3)
    public void updateCandidate() {
        String body = readFromJsonFile("src/test/resources/recruit/candidateJSON/putCandidate.json");
        token = generateToken();

        Response update = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .pathParam("id", candidateID)
                .when()
                .put("/candidates/{id}")
                .then()
                .statusCode(200)
                .extract().response();
        assertEquals(200, update.getStatusCode());
        assertEquals("KirkUPdated", update.jsonPath().getString("firstName"));
    }

    @Test
    @Order(4)
    public void deleteCandidate() {
        Response delete = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", candidateID)
                .when()
                .delete("/candidates/{id}")
                .then()
                .statusCode(204)
                .extract().response();

        assertEquals(204, delete.statusCode());

        Response get = given()
                .contentType(ContentType.JSON)
                .pathParam("id", candidateID)
                .when()
                .get("/candidates/{id}")
                .then()
                .statusCode(400)
                .extract().response();

        assertEquals(400, get.statusCode());
    }
}

