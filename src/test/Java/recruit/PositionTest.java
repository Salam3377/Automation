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
public class PositionTest extends BaseRecruit  {

    private static String positionID;
    private static String token;


    //CREATE
    @Test
    @Order(1)
    public void createPosition() {
        token = generateToken();
        String body = readFromJsonFile("src/test/resources/recruit/positionJSON/createPosition.json");
        Response position = createNewPosition(body, token);
        positionID = position.jsonPath().getString("id");
        assertEquals(201,position.getStatusCode());

    }


    //READ
    @Test
    @Order(2)
    public void readPosition() {
        Response read = given()
                .contentType(ContentType.JSON)
                .pathParam("id", positionID) // set id equals to positionID to include in get URL
                .when()
                .get("/positions/{id}")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("recruit/verifyResponseSchema.json"))
                .log().all()
                .extract().response();
        assertEquals(200, read.getStatusCode());
        assertEquals(positionID, read.jsonPath().getString("id"));

    }



    //UPDATE
    @Test
    @Order(3)
    public void updatePosition() {
        String body = readFromJsonFile("src/test/resources/recruit/positionJSON/putPosition.json");
        System.out.println(token + "<------This is token");

        Response update = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .pathParam("id", positionID)
                .when()
                .put("/positions/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();
        assertEquals(200, update.getStatusCode());
        assertEquals("Certified Turtle 2", update.jsonPath().getString("title"));


    }



    //DELETE
    @Test
    @Order(4)
    public void deletePosition() {
        Response delete = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("id", positionID)
                .when()
                .delete("positions/{id}")
                .then()
                .statusCode(204)
                .extract().response();

        assertEquals(204, delete.getStatusCode());

        Response get = given()
                .contentType(ContentType.JSON)
                .pathParam("id", positionID)
                .when()
                .get("/positions/{id}")
                .then()
                .statusCode(400)
                .extract().response();

        assertEquals(400, get.statusCode());

    }


}
