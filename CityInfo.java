package com.example;

// used in weather + travel module to store basic city details
public class CityInfo {

    // short id (like used in menu or file)
    private String code;

    // actual city name
    private String name;

    // country or state (kept together for simplicity)
    private String area;

    // small line about city (just for display)
    private String note;

    public CityInfo(String code, String name, String area, String note) {
        this.code = code;
        this.name = name;
        this.area = area;
        this.note = note;
    }

    // getters (no setters for now, not needed in my project)

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getNote() {
        return note;
    }

    // used when printing city list
    public String toString() {
        return name + " - " + area + " | " + note;
    }
}
