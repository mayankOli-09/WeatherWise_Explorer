package com.example;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RecommendationModule
{
    // ── SCORING ENGINE (mirrors original RecommendationModule.java) ──────────

    public List<PlaceInfo> rankPlaces(List<PlaceInfo> places, String tag)
    {
        return places.stream()
                .sorted((a, b) -> Integer.compare(
                        calculateScore(b, tag),
                        calculateScore(a, tag)))
                .limit(5)
                .toList();
    }

    public int calculateScore(PlaceInfo p, String tag)
    {
        return weatherScore(p, tag) + categoryScore(p, tag) + popularityScore(p);
    }

    private int weatherScore(PlaceInfo p, String tag)
    {
        return switch (tag)
        {
            case "RAIN", "STORM" -> p.isIndoor() ? 10 : -5;
            case "HOT"           -> p.isIndoor() ?  8 :  2;
            case "COLD"          -> p.isIndoor() ?  7 :  3;
            case "WINDY"         -> p.isIndoor() ?  6 :  1;
            case "CLEAR", "CLOUDY" -> p.isIndoor() ? 3 : 10;
            default -> 0;
        };
    }

    private int categoryScore(PlaceInfo p, String tag)
    {
        String cat = p.getCategory().toLowerCase();
        int score  = 0;
        switch (tag)
        {
            case "HOT"          -> { if (cat.contains("nature")) score += 4; if (cat.contains("water")) score += 5; }
            case "COLD"         -> { if (cat.contains("cafe") || cat.contains("indoor")) score += 4; }
            case "RAIN", "STORM"-> { if (cat.contains("museum") || cat.contains("mall")) score += 5; }
            case "CLEAR"        -> { if (cat.contains("adventure") || cat.contains("nature")) score += 5; }
        }
        return score;
    }

    private int popularityScore(PlaceInfo p)
    {
        return Math.max(0, Math.min(10, p.getPopularity() / 10));
    }

    // ── REASON GENERATOR (mirrors original getReason()) ──────────────────────

    public String getReason(PlaceInfo p, String tag)
    {
        String cat = p.getCategory().toLowerCase();
        return switch (tag)
        {
            case "RAIN", "STORM" ->
                p.isIndoor()
                    ? "Perfect indoor place to stay safe from this bad weather."
                    : "Outdoor spot — consider visiting later.";

            case "HOT" ->
                cat.contains("nature")
                    ? "Natural surroundings help keep things cool."
                    : p.isIndoor()
                        ? "Great place to escape the heat."
                        : "Carry water and sun protection.";

            case "COLD" ->
                p.isIndoor()
                    ? "Warm and cozy indoor experience."
                    : "Cold weather — dress warmly.";

            case "CLEAR" ->
                p.isIndoor()
                    ? "Good option, but outdoors are ideal now since it is clear."
                    : "Perfect weather to explore outdoors!";

            default -> "Good place to visit right now since the weather is great.";
        };
    }
}
