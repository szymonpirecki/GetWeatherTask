package configuration.handler;

import configuration.model.YamlModel;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class EnvironmentHandler {

    public static void setEnvironmentProperties() {
        log.info("Setting environment properties...");
        YamlModel yamlModel = YamlReader.getInstance().getYamlModel();
        String currentEnv = yamlModel.getEnvironment();
        Map<String, Object> envProperties = yamlModel.getEnvironments().get(currentEnv).getEnvironmentPropertiesMap();
        setSystemProperties(envProperties);
        log.info("Environment properties set for environment: {}", currentEnv);
    }

    public static void initializeRestAssuredLogging() {
        if (getBoolean("logRequestsDetails"))
            RestAssured.filters(new RequestLoggingFilter());
        if (getBoolean("logResponsesDetails"))
            RestAssured.filters(new ResponseLoggingFilter());
    }

    public static void applyBaseUriSettings() {
        var baseUri = System.getProperty("baseUri");
        RestAssured.baseURI = baseUri;
        if (baseUri.contains("https"))
            RestAssured.useRelaxedHTTPSValidation();
    }

    private static void setSystemProperties(Map<String, Object> map) {
        log.debug("Setting system properties...");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                log.error("{} = null. Value can't be null", key);
                throw new RuntimeException("Value for key " + key + " cannot be null.");
            } else if (value instanceof String || value instanceof Boolean || value instanceof Number) {
                System.setProperty(key, value.toString());
            } else if (value instanceof Map<?, ?>) {
                @SuppressWarnings("unchecked")
                Map<String, Object> innerMap = (Map<String, Object>) value;
                setSystemPropertyForMap(innerMap, key);
            } else {
                log.error("Unsupported type for {}: {}", key, value.getClass().getSimpleName());
                throw new IllegalArgumentException("Unsupported type for key " + key);
            }
        }
        log.debug("System properties set.");
    }

    private static void setSystemPropertyForMap(Map<String, Object> map, String parentKey) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = parentKey + "-" + entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                log.error("{} = null. Value can't be null", key);
                throw new RuntimeException("Value for key " + key + " cannot be null.");
            } else if (value instanceof Map<?, ?>) {
                @SuppressWarnings("unchecked")
                Map<String, Object> innerMap = (Map<String, Object>) value;
                setSystemPropertyForMap(innerMap, key);
            } else {
                System.setProperty(key, value.toString());
                log.debug("{} = {}", key, value);
            }
        }
    }

    private static boolean getBoolean(String propertyName) {
        return Boolean.parseBoolean(System.getProperty(propertyName));
    }
}