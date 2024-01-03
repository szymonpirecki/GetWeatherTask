package configuration.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
@Getter
public class EnvironmentModel {

    @JsonAnyGetter
    @JsonAnySetter
    private HashMap<String, Object> environmentPropertiesMap = new HashMap<>();

}
