package weatherApiTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import model.Coordinates;
import testBase.TestBase;

public class WeatherBase extends TestBase {
    protected String token = System.getProperty("token");
    protected String weatherEndpoint = System.getProperty("endpoints-weatherEndpoint");
    protected String geoCodingEndpoint = System.getProperty("endpoints-geoCodingEndpoint");

    protected Coordinates getCoordinates(String city) {
        JsonPath jsonPath = RestAssured.given()
                .queryParam("appid", token)
                .queryParam("q", city)
                .when()
                .get(geoCodingEndpoint)
                .then()
                .statusCode(200)
                .extract().response().jsonPath();

        return new Coordinates(jsonPath.getDouble("[0].lat"), jsonPath.getDouble("[0].lon"));
    }
}
