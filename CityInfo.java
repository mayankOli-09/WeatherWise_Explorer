package com.example;

public class CityInfo 
{

    private final String cityKey;
    private final String name;
    private final String countryState;
    private final String description;

    public CityInfo(String cityKey, String name, String countryState, String description) 
    {
        this.cityKey      = cityKey;
        this.name         = name;
        this.countryState = countryState;
        this.description  = description;
    }

    public String getCityKey()     
    {
    	return cityKey;     
    }
    public String getName()         
    {
    	return name;        
    }
    public String getCountryState() 
    { 
    	return countryState; 
    }
    public String getDescription() 
    {
    	return description; 
    }
}