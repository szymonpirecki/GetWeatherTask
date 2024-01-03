package testBase;

import configuration.handler.EnvironmentHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

@Slf4j
public class TestBase {

    @BeforeAll
    static void beforeAll() {
        initializeTestEnvironment();
    }

    private static void initializeTestEnvironment() {
        EnvironmentHandler.setEnvironmentProperties();
        EnvironmentHandler.initializeRestAssuredLogging();
        EnvironmentHandler.applyBaseUriSettings();
    }
}