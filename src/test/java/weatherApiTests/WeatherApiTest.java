package weatherApiTests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherApiTest extends WeatherBase {

    @ParameterizedTest
    @CsvFileSource(resources = "/cities.csv")
    public void shouldGetWeather(String city) {
        var coordinates = getCoordinates(city);
        JsonPath jsonPath = RestAssured.given()
                .param("appid", token)
                .param("lat", coordinates.getLat())
                .param("lon", coordinates.getLon())
                .when()
                .get(weatherEndpoint)
                .then()
                .statusCode(200)
                .extract().response().jsonPath();
        assertThat(jsonPath.get("name").toString()).isEqualTo(city);
        assertThat(jsonPath.getList("weather")).isNotNull();
    }
}