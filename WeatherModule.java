package com.example;

import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class WeatherModule
{
    // WMO weather code → [condition, description]
    private static final Map<Integer, String[]> WMO_MAP = Map.ofEntries(
        Map.entry(0,  new String[]{"Clear",  "clear sky"}),
        Map.entry(1,  new String[]{"Cloudy", "mainly clear"}),
        Map.entry(2,  new String[]{"Cloudy", "partly cloudy"}),
        Map.entry(3,  new String[]{"Cloudy", "overcast"}),
        Map.entry(45, new String[]{"Cloudy", "fog"}),
        Map.entry(48, new String[]{"Cloudy", "icy fog"}),
        Map.entry(51, new String[]{"Rain",   "light drizzle"}),
        Map.entry(53, new String[]{"Rain",   "moderate drizzle"}),
        Map.entry(55, new String[]{"Rain",   "dense drizzle"}),
        Map.entry(61, new String[]{"Rain",   "slight rain"}),
        Map.entry(63, new String[]{"Rain",   "moderate rain"}),
        Map.entry(65, new String[]{"Rain",   "heavy rain"}),
        Map.entry(71, new String[]{"Snow",   "slight snow"}),
        Map.entry(73, new String[]{"Snow",   "moderate snow"}),
        Map.entry(75, new String[]{"Snow",   "heavy snow"}),
        Map.entry(80, new String[]{"Rain",   "slight showers"}),
        Map.entry(81, new String[]{"Rain",   "moderate showers"}),
        Map.entry(82, new String[]{"Rain",   "violent showers"}),
        Map.entry(95, new String[]{"Storm",  "thunderstorm"}),
        Map.entry(96, new String[]{"Storm",  "thunderstorm with hail"}),
        Map.entry(99, new String[]{"Storm",  "heavy thunderstorm"})
    );

    public WeatherData fetchWeather(CityInfo city)
    {
        try
        {
            String apiUrl = "https://api.open-meteo.com/v1/forecast"
                + "?latitude="  + city.getLat()
                + "&longitude=" + city.getLon()
                + "&current_weather=true"
                + "&hourly=relative_humidity_2m"
                + "&timezone=auto";

            JSONObject json = getJson(apiUrl);
            if (json == null) return null;

            JSONObject current = json.getJSONObject("current_weather");
            int code = current.getInt("weathercode");
            String[] info = WMO_MAP.getOrDefault(code, new String[]{"Fair", "stable conditions"});

            double temp      = current.getDouble("temperature");
            double windSpeed = current.getDouble("windspeed");
            int    humidity  = extractHumidity(json);

            String tag     = classifyWeather(info[0], temp, windSpeed);
            String insight = getWeatherInsight(tag);

            return new WeatherData(
                city.getName(), info[0], info[1],
                temp, humidity, windSpeed, tag, insight
            );
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // Mirrors RecommendationModule.classifyWeather()
    public String classifyWeather(String condition, double temp, double windSpeed)
    {
        String c = condition.toLowerCase();
        if (c.contains("storm"))  return "STORM";
        if (c.contains("rain"))   return "RAIN";
        if (c.contains("snow"))   return "COLD";
        if (c.contains("fog") || c.contains("cloudy")) return "CLOUDY";
        if (windSpeed >= 25)      return "WINDY";
        if (temp >= 30)           return "HOT";
        if (temp <= 16)           return "COLD";
        if (c.contains("cloud")) return "CLOUDY";
        return "CLEAR";
    }

    public String getWeatherInsight(String tag)
    {
        return switch (tag)
        {
            case "HOT"   -> "It's pretty hot out there — stick to cool indoor spots or places with plenty of shade.";
            case "COLD"  -> "It's quite chilly — a warm and cozy place would be a great choice.";
            case "RAIN"  -> "Looks like it's raining — best to stay indoors and keep dry.";
            case "STORM" -> "The weather is rough with storms — it's safer to stay inside for now.";
            case "WINDY" -> "It's really windy today — not the best time for open or high places.";
            case "CLOUDY"-> "Calm and cloudy — perfect for a relaxed walk or light exploring.";
            default      -> "The weather is just right — a great time to head out and explore!";
        };
    }

    private int extractHumidity(JSONObject json)
    {
        try
        {
            JSONArray times = json.getJSONObject("hourly").getJSONArray("time");
            JSONArray hums  = json.getJSONObject("hourly").getJSONArray("relative_humidity_2m");
            String key = LocalDateTime.now().toString().substring(0, 13) + ":00";
            for (int i = 0; i < times.length(); i++)
                if (key.equals(times.getString(i))) return hums.getInt(i);
        }
        catch (Exception ignored) {}
        return 0;
    }

    private JSONObject getJson(String urlStr) throws Exception
    {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        if (conn.getResponseCode() != 200) return null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream())))
        {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return new JSONObject(sb.toString());
        }
    }
}
