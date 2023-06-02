package com.example.miraclepack;

//This is a model of a Configuration from the database.
public class Configuration {
    private String name;
    private String weekday;


    public Configuration(String name, String weekday) {
        this.name = name;
        this.weekday = weekday;
    }

    public Configuration() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
