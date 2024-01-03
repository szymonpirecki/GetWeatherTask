package configuration.model;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class YamlModel {
    private String environment;
    private HashMap<String, Object> browserSettings;
    private HashMap<String, EnvironmentModel> environments;
}