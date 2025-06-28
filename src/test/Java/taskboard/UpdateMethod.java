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



public class UpdateMethod {

    @BeforeEach
    public void setup() {
        baseURI = "https://taskboard.portnov.com";
    }

    @Test
    public void update() {

        Integer taskID = 633;
        String taskName = "Updated RestAssured3";
        String taskDescription = "random description";
        Integer taskPriority = 0;
        String taskStatus = "In Progress";
        String taskAuthor = "Here";


        JSONObject body = new JSONObject();
        body.put("id", taskID);
        body.put("taskName", taskName);
        body.put("description", taskDescription);
        body.put("dueDate", "2025-05-29T02:28:13.653Z");
        body.put("priority", taskPriority);
        body.put("status", taskStatus);
        body.put("author", taskAuthor);
        String stringBody = body.toString();


        Response response = given()
                .contentType(ContentType.JSON)
                .body(stringBody)
                .when()
                .put("/api/Task/"+taskID);

        JsonPath jp = response.jsonPath();

        response.prettyPrint();
        //           expected    vs    actual

        assertEquals(204, response.statusCode());

        if (response.statusCode() == 204) {
            System.out.println("Task ID: " + taskID + " was successfully updated");
        }

        //Check Get Method file using same taskID to see the updates

    }

}
