package com.example.miraclepack;

public class Configuration {
    private String name;
    private String weekday;
    private boolean main;

    public Configuration(String name, String weekday, boolean main) {
        this.name = name;
        this.weekday = weekday;
        this.main = main;
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

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}
