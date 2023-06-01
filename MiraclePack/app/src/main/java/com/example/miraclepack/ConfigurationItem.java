package com.example.miraclepack;

//This is a model of a ConfigurationItem from the Database
public class ConfigurationItem {
    private String name;
    private Compartment compartment;
    private String configurationName;

    public ConfigurationItem(String name, Compartment compartment, String configurationName) {
        this.name = name;
        this.compartment = compartment;
        this.configurationName = configurationName;
    }

    public ConfigurationItem() {
        this.compartment = new Compartment();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Compartment getCompartment() {
        return compartment;
    }

    public void setCompartment(Compartment compartment) {
        this.compartment = compartment;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }
}
