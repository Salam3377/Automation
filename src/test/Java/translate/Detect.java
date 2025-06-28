package translate;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;


public class Detect {
    protected static final String API_KEY = System.getenv("API_KEY");

    @BeforeAll
    static void setup(){
        baseURI = "https://translation.googleapis.com/language/translate/v2";
    }

    @Test
    public void detectLanguage() {
        //String translate = "This man stole my vodka!";
        String translate = "Цей чоловік вкрав мою горілку!";
//        1
        Response response = given()
                .queryParam("key", API_KEY)
                .queryParam("q", translate)
                .when()
                .post("/detect")
                .then()
                .statusCode(200)
                .extract().response();
        response.prettyPrint();
//        2
        Map<String, Object> detections = response.jsonPath().getMap("data.detections[0][0]");
//        3
        assertEquals("uk", detections.get("language"));

        boolean isReliable = (Boolean) detections.get("isReliable");
        assertFalse(isReliable, "Should be false" );

//        double confidence = (double) detections.get("confidence");
//        assertTrue( confidence >= 0.9 , "Confidence level is too low" );


    }

}