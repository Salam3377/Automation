package translate;

import base.BaseTranslate;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class Translate extends BaseTranslate {

    @Test
    public void translateTest() {
        String text = "Вареная курица";
        String language = "en";

        Response response = given()
                .queryParam("key", API_KEY)
                .queryParam("q", text)
                .queryParam("target", language)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();


        String translatedText = response.jsonPath().getString("data.translations[0].translatedText");
        String detectedSourceLanguage = response.jsonPath().getString("data.translations[0].detectedSourceLanguage");

        assertEquals("Boiled chicken", translatedText);
        assertEquals("ru", detectedSourceLanguage);

    }

    @Test
    public void translateTestPayload() {
        String[] text = {"Вареная курица", "нарезанный лук", "холодное пиво"};
        String[] translations = {"Boiled chicken", "chopped onion", "cold beer"};
        String language = "en";
        String[] languageCode = {"ru", "ru", "ru"};

        Response response = given()
                .queryParam("key", API_KEY)
                .queryParam("q", text)
                .queryParam("target", language)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().response();

        for (int i = 0; i < text.length; i++) {
            String translatedText = response.jsonPath().getString("data.translations[" + i + "].translatedText");
            String detectedSourceLanguage = response.jsonPath().getString("data.translations[" + i + "].detectedSourceLanguage");

            assertEquals(translations[i], translatedText);
            assertEquals(languageCode[i], detectedSourceLanguage);
        }
    }
}