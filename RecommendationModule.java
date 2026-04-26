package com.example;

import java.util.*;

public class RecommendationModule 
{

    private final WeatherModule weatherModule = new WeatherModule();
    private final DatabaseManager db = new DatabaseManager();

    // ─────────────────────────────────────────────
    // MAIN ENTRY POINT
    // ─────────────────────────────────────────────
    
    public void recommend(String cityName) 
    {

        WeatherData weather = weatherModule.fetchWeather(cityName);
        List<PlaceInfo> places = db.getPlaces(cityName);

        System.out.println("\n SMART RECOMMENDATIONS FOR " + cityName.toUpperCase());
        System.out.println("=".repeat(50));

        if (places == null || places.isEmpty()) 
        {
            System.out.println("  No place data available for this city.");
            return;
        }

        // Fallback if weather data is missing
        if (weather == null)
        {
            showFallback(places);
            return;
        }

        String tag = classifyWeather(weather);

        System.out.println("\n Weather: " + formatWeather(weather, tag));
        System.out.println(" Insight: " + getWeatherInsight(tag));

        // Rank places using multi-factor scoring
        List<PlaceInfo> ranked = places.stream()
                .sorted((a, b) -> Integer.compare(
                        calculateScore(b, tag),
                        calculateScore(a, tag)))
                .toList();

        displayTopPlaces(ranked, tag);

        System.out.println("=".repeat(50));
    }

    // ─────────────────────────────────────────────
    // WEATHER CLASSIFIER
    // ─────────────────────────────────────────────
    private String classifyWeather(WeatherData w) 
    {
        String condition = w.getCondition().toLowerCase();

        if (condition.contains("storm")) return "STORM";
        if (condition.contains("rain")) return "RAIN";
        if (condition.contains("snow")) return "COLD";
        if (condition.contains("fog")) return "CLOUDY";

        if (w.getWindSpeed() >= 25) return "WINDY";
        if (w.getTemperature() >= 30) return "HOT";
        if (w.getTemperature() <= 16) return "COLD";

        if (condition.contains("cloud")) return "CLOUDY";

        return "CLEAR";
    }

    // ─────────────────────────────────────────────
    // SCORING ENGINE
    // ─────────────────────────────────────────────
    private int calculateScore(PlaceInfo p, String tag) 
    {
        return weatherScore(p, tag) + categoryScore(p, tag) + popularityScore(p);
    }

    private int weatherScore(PlaceInfo p, String tag) 
    {
        return switch (tag) 
        		{
            case "RAIN", "STORM" -> p.isIndoor() ? 10 : -5;
            case "HOT"           -> p.isIndoor() ? 8  : 2;
            case "COLD"          -> p.isIndoor() ? 7  : 3;
            case "WINDY"         -> p.isIndoor() ? 6  : 1;
            case "CLEAR", "CLOUDY" -> p.isIndoor() ? 3 : 10;
            default -> 0;
        };
    }

    private int categoryScore(PlaceInfo p, String tag) 
    {
        String cat = p.getCategory().toLowerCase();
        int score = 0;

        switch (tag) 
        {
            case "HOT":
                if (cat.contains("nature")) score += 4;
                if (cat.contains("water")) score += 5;
                break;

            case "COLD":
                if (cat.contains("cafe") || cat.contains("indoor")) score += 4;
                break;

            case "RAIN", "STORM":
                if (cat.contains("museum") || cat.contains("mall")) score += 5;
                break;

            case "CLEAR":
                if (cat.contains("adventure") || cat.contains("nature")) score += 5;
                break;
        }

        return score;
    }

    private int popularityScore(PlaceInfo p) 
    {
        return Math.max(0, Math.min(10, p.getPopularity() / 10));
    }

    // ─────────────────────────────────────────────
    // DISPLAY HELPERS
    // ─────────────────────────────────────────────
    private void displayTopPlaces(List<PlaceInfo> places, String tag) 
    {
        System.out.println("\n TOP RECOMMENDED PLACES:\n");

        int limit = Math.min(5, places.size());
        for (int i = 0; i < limit; i++) 
        {
            PlaceInfo p = places.get(i);
            System.out.println((i + 1) + ". " + p.getName()
                    + " [" + p.getCategory() + "] [" + p.getPlaceType() + "]");
            System.out.println("    " + getReason(p, tag));
            System.out.println();
        }
    }

    private void showFallback(List<PlaceInfo> places) 
    {
        System.out.println("\n Weather unavailable — showing popular places:\n");
        places.stream()
                .sorted(Comparator.comparingInt(PlaceInfo::getPopularity).reversed())
                .limit(5)
                .forEach(p -> System.out.println("  • " + p.getName() + " [" + p.getPlaceType() + "]"));
    }

    // ─────────────────────────────────────────────
    // REASON GENERATOR
    // ─────────────────────────────────────────────
    private String getReason(PlaceInfo p, String tag) 
    {
        String cat = p.getCategory().toLowerCase();

        return switch (tag) 
        		{
            case "RAIN", "STORM" ->
                    p.isIndoor()
                            ? "Perfect indoor place to stay safe from this  bad weather."
                            : "Outdoor spot — consider visiting later.";

            case "HOT" -> cat.contains("nature")
                    ? "Natural surroundings help keep things cool."
                    : (p.isIndoor()
                            ? "Great place to escape the heat."
                            : "Carry water and sun protection for yourself please .");

            case "COLD" ->
                    p.isIndoor()
                            ? "Warm and cozy indoor experience will be better ."
                            : "Cold weather — dress warmly.";

            case "CLEAR" ->
                    p.isIndoor()
                            ? "Good option, but outdoors are ideal now since it is clear."
                            : "Perfect weather to explore outdoors!";

            default -> "Good place to visit right now since the weather is great.";
        };
    }

    // ─────────────────────────────────────────────
    // FORMATTED WEATHER INFO
    // ─────────────────────────────────────────────
    private String formatWeather(WeatherData w, String tag) 
    {
        return switch (tag) 
        		{
            case "HOT"    -> "Hot (" + w.getTemperature() + "°C)";
            case "COLD"   -> "Cold (" + w.getTemperature() + "°C)";
            case "RAIN"   -> "Rainy (" + w.getTemperature() + "°C)";
            case "STORM"  -> "Stormy (" + w.getTemperature() + "°C)";
            case "WINDY"  -> "Windy (" + w.getWindSpeed() + " km/h)";
            case "CLOUDY" -> "Cloudy (" + w.getTemperature() + "°C)";
            default       -> "Clear (" + w.getTemperature() + "°C)";
        };
    }

    private String getWeatherInsight(String tag) 
    {
        return switch (tag) 
        		{
        case "HOT"    -> "It's pretty hot out there today — you might want to stick to cool indoor spots or places with plenty of shade.";
        
        case "COLD"   -> "It's quite chilly right now — a warm and cozy place would be a great choice.";
        
        case "RAIN"   -> "Looks like it's raining — best to stay indoors and keep dry.";
        
        case "STORM"  -> "The weather is rough with storms — it's safer to stay inside for now.";
        
        case "WINDY"  -> "It's really windy today — not the best time for open or high places.";
        
        case "CLOUDY" -> "The weather is calm and cloudy — perfect for a relaxed walk or light exploring.";
        
        default       -> "The weather is just right — a great time to head out and explore the great city!";
        };
    }
}