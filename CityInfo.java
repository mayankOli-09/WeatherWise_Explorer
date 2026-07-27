package com.example;

// used in weather + travel module to store basic city details
public class CityInfo {

    private String cityKey;
    private String name;
    private String countryState;
    private String description;
    private double lat;
    private double lon;

    public CityInfo(String cityKey,
                    String name,
                    String countryState,
                    String description,
                    double lat,
                    double lon) {

        this.cityKey = cityKey;
        this.name = name;
        this.countryState = countryState;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
    }

    public String getCityKey() {
        return cityKey;
    }

    public String getName() {
        return name;
    }

    public String getCountryState() {
        return countryState;
    }

    public String getDescription() {
        return description;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return name + " - " + countryState + " | " + description;
    }
}
