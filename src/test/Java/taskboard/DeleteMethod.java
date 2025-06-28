package taskboard;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;



public class DeleteMethod {

    @BeforeEach
    public void setup() {
        baseURI = "https://taskboard.portnov.com";
    }

    @Test
    public void delete() {

        Integer taskID = 625;

        JSONObject body = new JSONObject();
        body.put("id", taskID);
        String stringBody = body.toString();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(stringBody)
                .when()
                .delete("/api/Task/"+taskID);

        JsonPath jp = response.jsonPath();

        response.prettyPrint();
        System.out.println(response);

        //Check Status 204, successfully deleted
        assertEquals(204, response.statusCode());


        // Check Get same Task ID for code 404, to confirm it does not exist anymore
        Response responseDeleted = get("api/Task/"+taskID);
        int status = responseDeleted.getStatusCode();

        assertEquals(404,status);


    }

}
