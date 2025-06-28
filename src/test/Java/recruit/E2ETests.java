package recruit;

import base.BaseRecruit;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class E2ETests extends BaseRecruit {

    //Create a New Candidate > POST /candidates
    //Login with this new candaidate to get token/store token > POST /login (body new stuff)
    //Create a new positon > POST /positions
    //Create a new application (candidate + position) > POST /applications
    //Validate the Application > GET /application/id
    //Work backwards to delete

    private static String candidateToken;
    private static String candidateID;
    private static String positionID;
    private static String applicationID;


    @Test
    public void e2eTest() throws IOException {
        //Create a New Candidate > POST /candidates
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        String email = "candidate" + randomNumber + "@example.com";

        String firstName = "James";
        String lastName = "Hetfield";

        JSONObject candidate = new JSONObject();
        candidate.put("firstName", "James");
        candidate.put("middleName", "");
        candidate.put("lastName", "Hetfield");
        candidate.put("email", email);
        candidate.put("password", "welcome");
        candidate.put("address", " 323 San Dimas St");
        candidate.put("city", "San Fran");
        candidate.put("state", "CA");
        candidate.put("zip", "94949");
        candidate.put("summary", "YEAAAAAHH I am the table");

        candidateID = createNewCandidate(candidate.toString()).jsonPath().getString("id");

        JSONObject credentials = new JSONObject();
        credentials.put("email", email);
        credentials.put("password", "welcome");

        //Login with this new candidate to get token/store token > POST /login (body new stuff)
        candidateToken = loginWithUser(credentials.toString()).jsonPath().getString("token");
        String adminToken = generateToken();

        //Create a new positon > POST /positions
        JSONObject position = new JSONObject();
        position.put("title", "Singer and Guitarist");
        position.put("address", "424 San Mateo Blvd");
        position.put("city", "Los Angeles");
        position.put("zip", "90004");
        position.put("description", "Rad dude with attitude and long hair");
        String date = LocalDate.now().toString();
        position.put("dateOpen", date);
        position.put("company", "Metallica");

        positionID = createNewPosition(position.toString(), adminToken).jsonPath().getString("id");
        //Create a new application (candidate + position) > POST /applications

        JSONObject application = new JSONObject();
        application.put("candidateId", candidateID);
        application.put("positionId", positionID);
        application.put("dateApplied", date);

        applicationID = createNewApplication(application.toString(), candidateToken).jsonPath().getString("id");

        //Validate the Application > GET /application/id
        Response validateResponse = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/applications/" + applicationID)
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("recruit/verifyResponseSchema.json"))
                .log().body()
                .extract().response();
        JsonPath jsonValidate = validateResponse.jsonPath();

//        {
//            "id": 57,
//                "candidateId": 83,
//                "positionId": 21,
//                "firstName": "James",
//                "middleName": "",
//                "lastName": "Hetfield",
//                "email": "candidate8492@example.com",
//                "title": "Singer and Guitarist",
//                "dateApplied": "2025-06-13T07:00:00.000Z",
//                "summary": "YEAAAAAHH I am the table",
//                "description": "Rad dude with attitude and long hair",
//                "dateOpen": "2025-06-13T07:00:00.000Z",
//                "company": "Metallica",
//                "candidate_address": " 323 San Dimas St",
//                "candidate_city": "San Fran",
//                "candidate_state": "CA",
//                "candidate_zip": "94949",
//                "position_address": "424 San Mateo Blvd",
//                "position_city": "Los Angeles",
//                "position_state": null,
//                "position_zip": "90004"
//        }

//                expected vs actual
        assertEquals(applicationID, jsonValidate.getString("id"));
        assertEquals(candidateID, jsonValidate.getString("candidateId"));
        assertEquals(positionID, jsonValidate.getString("positionId"));

        assertEquals(firstName, jsonValidate.getString("firstName"));
        assertEquals(lastName, jsonValidate.getString("lastName"));
        assertEquals(email, jsonValidate.getString("email"));
        assertEquals("Singer and Guitarist", jsonValidate.getString("title"));
        assertEquals("2025-06-18T07:00:00.000Z", jsonValidate.getString("dateApplied"));
        assertEquals("YEAAAAAHH I am the table", jsonValidate.getString("summary"));
        assertEquals("Rad dude with attitude and long hair", jsonValidate.getString("description"));
        assertEquals("2025-06-18T07:00:00.000Z", jsonValidate.getString("dateOpen"));
        assertEquals("Metallica", jsonValidate.getString("company"));
        assertEquals(" 323 San Dimas St", jsonValidate.getString("candidate_address"));
        assertEquals("San Fran", jsonValidate.getString("candidate_city"));
        assertEquals("CA", jsonValidate.getString("candidate_state"));
        assertEquals("94949", jsonValidate.getString("candidate_zip"));
        assertEquals("424 San Mateo Blvd", jsonValidate.getString("position_address"));
        assertEquals("Los Angeles", jsonValidate.getString("position_city"));
        assertEquals("90004", jsonValidate.getString("position_zip"));

    }
}