package com.example.miraclepack;

public class Compartment {
    private Integer compartmentIDd;
    private String description;

    public Compartment() {
    }

    public Compartment(Integer compartmentIDd, String description) {
        this.compartmentIDd = compartmentIDd;
        this.description = description;
    }

    public Integer getCompartmentIDd() {
        return compartmentIDd;
    }

    public void setCompartmentIDd(Integer compartmentIDd) {
        this.compartmentIDd = compartmentIDd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
