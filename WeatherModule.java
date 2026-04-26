package com.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class WeatherModule 
{

    private static final String GEO_URL = "https://geocoding-api.open-meteo.com/v1/search?count=1&language=en&format=json&name=";

    public WeatherData fetchWeather(String cityName) 
    {
        try 
        {
            double[] coords = geocode(cityName);
            if (coords == null) return null;

            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + coords[0] 
                          + "&longitude=" + coords[1] + "&current_weather=true&hourly=relative_humidity_2m&timezone=auto";

            JSONObject json = getJson(apiUrl);
            if (json == null) return null;

            JSONObject current = json.getJSONObject("current_weather");
            String[] info = mapCode(current.getInt("weathercode"));
            
            return new WeatherData(
                cityName, 
                info[0], 
                info[1], 
                current.getDouble("temperature"), 
                extractHumidity(json), 
                current.getDouble("windspeed")
            );
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void displayWeather(String cityName) 
    {
        System.out.println("\n Fetching live weather for " + cityName + "...");
        WeatherData d = fetchWeather(cityName);
        if (d == null) 
        {
            System.out.println(" Could not fetch weather. Check your internet connection.");
            return;
        }
        System.out.println("\n--- LIVE WEATHER: " + d.getCityName().toUpperCase() + " ---");
        System.out.println("  Condition   : " + d.getCondition() + " (" + d.getDescription() + ")");
        System.out.println("  Temperature : " + d.getTemperature() + " C");
        System.out.println("  Humidity    : " + d.getHumidity() + "%");
        System.out.println("  Wind Speed  : " + d.getWindSpeed() + " km/h");
        System.out.println("-".repeat(40));
    }

    private double[] geocode(String name) throws Exception 
    {
        JSONObject geo = getJson(GEO_URL + URLEncoder.encode(name, StandardCharsets.UTF_8));
        if (geo == null || !geo.has("results")) return null;
        JSONObject r = geo.getJSONArray("results").getJSONObject(0);
        return new double[]{ r.getDouble("latitude"), r.getDouble("longitude") };
    }

    private JSONObject getJson(String urlStr) throws Exception 
    {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() != 200) return null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return new JSONObject(sb.toString());
        }
    }

    private int extractHumidity(JSONObject json) 
    {
        try 
        {
            JSONArray times = json.getJSONObject("hourly").getJSONArray("time");
            JSONArray hums = json.getJSONObject("hourly").getJSONArray("relative_humidity_2m");
            String key = LocalDateTime.now().toString().substring(0, 13) + ":00";
            for (int i = 0; i < times.length(); i++) {
                if (key.equals(times.getString(i))) return hums.getInt(i);
            }
        } 
        catch (Exception e) {}
        return 0;
    }

    private String[] mapCode(int code) 
    {
        if (code == 0) return new String[]{"Clear", "clear sky"};
        if (code <= 3) return new String[]{"Cloudy", "partly cloudy"};
        if (code >= 95) return new String[]{"Storm", "thunderstorm"};
        if (code >= 51) return new String[]{"Rain", "rainy weather"};
        return new String[]{"Fair", "stable conditions"};
    }
}