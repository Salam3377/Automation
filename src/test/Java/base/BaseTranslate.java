package base;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;

public class BaseTranslate {

    protected static final String API_KEY = System.getenv("API_KEY");

    @BeforeAll
    static void setup(){
        baseURI = "https://translation.googleapis.com/language/translate/v2";
    }

    protected List<String> loadExpectedLanguages() {
        try {
            Map<String, List> langugesFull = new ObjectMapper()
                    .readValue(
                            new File("src/test/resources/translate/languages.json"),
                            new TypeReference<Map<String, List>>() {
                            }
                    );
            return  langugesFull.get("languages");

        } catch (IOException e) {
            throw new UncheckedIOException("Error", e);
        }
    }


}
