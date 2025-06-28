package taskboard;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetMethod {

    @BeforeEach
    public void setup() {
        baseURI = "https://taskboard.portnov.com";
    }

    Integer taskID = 633;
    String taskName = "Updated RestAssured3";
    String taskDescription = "random description";
    Integer taskPriority = 0;
    String taskStatus = "In Progress";
    String taskAuthor = "Here";

    @Test
    public void getTask() {
        //Classic Style
        Response response = get("/api/Task");
        int status = response.getStatusCode();

        //method   expected result & actual result
        assertEquals(200,status);

    }

    @Test
    public void getTaskById() {
        Response response = get("api/Task/"+taskID);
        int status = response.getStatusCode();
        JsonPath jp = response.jsonPath();
        jp.prettyPrint();

        assertEquals(200,status);
        assertEquals(taskID, jp.getInt("id"));
        assertEquals(taskName, jp.getString("taskName"));
        assertEquals(taskDescription, jp.getString("description"));
        assertEquals("2025-05-29T02:28:13.653", jp.getString("dueDate"));
        assertEquals(taskPriority, jp.getInt("priority"));
        assertEquals(taskStatus, jp.getString("status"));
        assertEquals(taskAuthor, jp.getString("author"));

    }


}
