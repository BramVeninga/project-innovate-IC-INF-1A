package com.example.miraclepack;

public class ConfigurationItem {
    private String name;
    private Integer compartmentId;
    private String compartmentName;
    private String configurationName;

    public ConfigurationItem(String name, Integer compartmentId, String compartmentName, String configurationName) {
        this.name = name;
        this.compartmentId = compartmentId;
        this.compartmentName = compartmentName;
        this.configurationName = configurationName;
    }

    public ConfigurationItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(Integer compartmentId) {
        this.compartmentId = compartmentId;
    }

    public String getCompartmentName() {
        return compartmentName;
    }

    public void setCompartmentName(String compartmentName) {
        this.compartmentName = compartmentName;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }
}
