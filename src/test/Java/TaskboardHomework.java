import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskboardHomework {

    @Test
    public void getTask() {
        baseURI = "https://taskboard.portnov.com";
        //Classic Style
        Response response = get("/api/Task");
        int status = response.getStatusCode();
        //method   expected result & actual result
        assertEquals(200,status);
    }

    @Test
    public void getTaskBDD() {
        baseURI = "https://taskboard.portnov.com";

        //BDD Style
        given()
                .get("/api/Task")
                .then()
                .statusCode(200)
                .log().all();
    }


    @Test
    public void createTask() {
        baseURI = "https://taskboard.portnov.com";

        //JSON preferred way
        JSONObject json = new JSONObject();
        json.put("id", 0);
        json.put("taskName", "JSON style");
        json.put("description", "random description");
        json.put("dueDate", "2025-05-29T02:28:13.653Z");
        json.put("priority", 0);
        json.put("status", "In Progress");
        json.put("author", "Here");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(json.toString()) // Must convert JSONObject to string
                .when()
                .post("/api/Task")
                .then()
                .statusCode(201)
                .extract().response();

        int taskId = response.jsonPath().getInt("id");

        System.out.println(taskId);
    }

    //remember printed ID and add down below the correct ID
    @Test
    public void getSingleTask() {

        baseURI = "https://taskboard.portnov.com";
        int id = 224;
        Response response = get("/api/Task/"+ id);
        int status = response.getStatusCode();
        //method   expected result & actual result
        assertEquals(200,status);
    }


    @Test
    public void UpdateTask() {

        baseURI = "https://taskboard.portnov.com";

        int id = 224;

        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("taskName", "updated restAssured");
        json.put("description", "description edited by Junit");
        json.put("dueDate","2025-05-29T02:28:13.653Z");
        json.put("priority", 0);
        json.put("status", "Completed");
        json.put("author", "Za Author");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(json.toString()) // Must convert JSONObject to string
                .when()
                .put("/api/Task/" + id)
                .then()
                .statusCode(204)
                .extract().response();
    }

    @Test
    public void deleteTask() {

        baseURI = "https://taskboard.portnov.com";
        int id = 100;
        given()
                .when()
                .delete("/api/Task/" + id)
                .then()
                .statusCode(204); // Or 204 if deletion is successful
    }


}