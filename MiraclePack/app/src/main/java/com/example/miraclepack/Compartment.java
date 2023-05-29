package com.example.miraclepack;

public class Compartment {
    private Integer compartmentId;
    private String description;

    public Compartment() {
    }

    public Compartment(Integer compartmentId, String description) {
        this.compartmentId = compartmentId;
        this.description = description;
    }

    public Integer getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(Integer compartmentId) {
        this.compartmentId = compartmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
