package helper;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import model.Pet;
import model.PetStatus;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public class PetshopApiHelper {

    private static final String PETSHOP_BASE_URL = "https://petstore.swagger.io/v2";

    private static final String ADD_OR_UPDATE_PET = "/pet";

    private static final String EXEMPLAR_PET = "/pet/{petId}";
    private static final String PET_LIST = "pet/findByStatus";

    private static final RequestSpecification requestSpecification  = new RequestSpecBuilder()
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .setContentType(ContentType.JSON)
            .setBaseUri(PETSHOP_BASE_URL)
            .build();

    public static JsonPath addPet(Pet pet) {
        return given()
                .spec(requestSpecification)
                .body(pet)
                .expect()
                .statusCode(200)
                .then()
                .request()
                .post(ADD_OR_UPDATE_PET)
                .jsonPath();
    }

    public static JsonPath getPetsByStatus(PetStatus... statuses) {
        return given()
                .spec(requestSpecification)
                .queryParam("status", (Object[])statuses)
                .expect()
                .statusCode(200)
                .then()
                .request()
                .get(PET_LIST)
                .jsonPath();
    }


    public static JsonPath deletePet(String id) {
        return given()
                .spec(requestSpecification)
                .expect()
                .statusCode(200)
                .then()
                .request()
                .delete(EXEMPLAR_PET, id)
                .jsonPath();
    }

}
