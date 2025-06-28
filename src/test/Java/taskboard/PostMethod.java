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

//Last Created ID: 633


public class PostMethod {

    @BeforeEach
    public void setup() {
        baseURI = "https://taskboard.portnov.com";
    }

    @Test
    public void post() {

        String taskName = "JSON-restAssured";
        String taskDescription = "random description";
        Integer taskPriority = 0;
        String taskStatus = "In Progress";
        String taskAuthor = "Here";

        JSONObject body = new JSONObject();
        body.put("id", 0);
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
                .post("/api/Task");

        JsonPath jp = response.jsonPath();

        response.prettyPrint();
        System.out.println(response);

        int taskId = response.jsonPath().getInt("id");

        //           expected    vs    actual

        assertEquals(201, response.statusCode());

        //ID verify ID is not EMPTY
        assertEquals(taskId, jp.getInt("id"));
        assertEquals(taskName, jp.getString("taskName"));
        assertEquals(taskDescription, jp.getString("description"));
        assertEquals("2025-05-29T02:28:13.653Z", jp.getString("dueDate"));
        assertEquals(taskPriority, jp.getInt("priority"));
        assertEquals(taskStatus, jp.getString("status"));
        assertEquals(taskAuthor, jp.getString("author"));

    }



}
