package com.example;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class DatabaseManager
{
    private final Map<String, CityInfo> cityMap  = new HashMap<>();
    private final List<PlaceInfo>       allPlaces = new ArrayList<>();

    public DatabaseManager()
    {
        loadData();
    }

    private void loadData()
    {
        // ── CITIES ──────────────────────────────────────────
        cityMap.put("dehradun",  new CityInfo("dehradun",  "Dehradun",  "India (Uttarakhand)", "Capital of Uttarakhand in the Doon Valley. Gateway to Mussoorie and Rishikesh.", 30.3165, 78.0322));
        cityMap.put("mussoorie", new CityInfo("mussoorie", "Mussoorie", "India (Uttarakhand)", "Queen of Hills at 2000m altitude. Famous for scenic views and colonial charm.",   30.4598, 78.0664));
        cityMap.put("rishikesh", new CityInfo("rishikesh", "Rishikesh", "India (Uttarakhand)", "Yoga Capital of the World. Sits on the banks of the holy Ganga river.",           30.0869, 78.2676));
        cityMap.put("haridwar",  new CityInfo("haridwar",  "Haridwar",  "India (Uttarakhand)", "One of the seven holiest Hindu cities. Famous for Ganga Aarti at Har Ki Pauri.", 29.9457, 78.1642));

        // ── DEHRADUN ────────────────────────────────────────
        allPlaces.add(new PlaceInfo(1,  "dehradun", "Forest Research Institute", "Museum",   "INDOOR",  90));
        allPlaces.add(new PlaceInfo(2,  "dehradun", "Robber's Cave",             "Nature",   "OUTDOOR", 85));
        allPlaces.add(new PlaceInfo(3,  "dehradun", "Paltan Bazaar",             "Market",   "INDOOR",  70));
        allPlaces.add(new PlaceInfo(4,  "dehradun", "Tapkeshwar Temple",         "Temple",   "OUTDOOR", 78));
        allPlaces.add(new PlaceInfo(5,  "dehradun", "Sahastradhara",             "Nature",   "OUTDOOR", 80));
        allPlaces.add(new PlaceInfo(6,  "dehradun", "Malsi Deer Park",           "Park",     "OUTDOOR", 72));
        allPlaces.add(new PlaceInfo(7,  "dehradun", "Mindrolling Monastery",     "Temple",   "INDOOR",  82));
        allPlaces.add(new PlaceInfo(8,  "dehradun", "Clock Tower",               "Landmark", "OUTDOOR", 75));

        // ── MUSSOORIE ───────────────────────────────────────
        allPlaces.add(new PlaceInfo(9,  "mussoorie", "Kempty Falls",             "Nature",    "OUTDOOR", 90));
        allPlaces.add(new PlaceInfo(10, "mussoorie", "Mall Road",                "Market",    "OUTDOOR", 88));
        allPlaces.add(new PlaceInfo(11, "mussoorie", "Gun Hill",                 "Viewpoint", "OUTDOOR", 85));
        allPlaces.add(new PlaceInfo(12, "mussoorie", "Lal Tibba",                "Viewpoint", "OUTDOOR", 80));
        allPlaces.add(new PlaceInfo(13, "mussoorie", "Library Bazaar",           "Market",    "INDOOR",  75));
        allPlaces.add(new PlaceInfo(14, "mussoorie", "Mussoorie Lake",           "Nature",    "OUTDOOR", 78));
        allPlaces.add(new PlaceInfo(15, "mussoorie", "Jwala Devi Temple",        "Temple",    "INDOOR",  72));
        allPlaces.add(new PlaceInfo(16, "mussoorie", "Cloud's End",              "Nature",    "OUTDOOR", 70));

        // ── RISHIKESH ───────────────────────────────────────
        allPlaces.add(new PlaceInfo(17, "rishikesh", "Lakshman Jhula",           "Landmark", "OUTDOOR", 92));
        allPlaces.add(new PlaceInfo(18, "rishikesh", "Ram Jhula",                "Landmark", "OUTDOOR", 90));
        allPlaces.add(new PlaceInfo(19, "rishikesh", "Beatles Ashram",           "Museum",   "INDOOR",  85));
        allPlaces.add(new PlaceInfo(20, "rishikesh", "Triveni Ghat",             "Temple",   "OUTDOOR", 88));
        allPlaces.add(new PlaceInfo(21, "rishikesh", "Neelkanth Mahadev Temple", "Temple",   "INDOOR",  82));
        allPlaces.add(new PlaceInfo(22, "rishikesh", "Rajaji National Park",     "Nature",   "OUTDOOR", 80));
        allPlaces.add(new PlaceInfo(23, "rishikesh", "Parmarth Niketan Ashram",  "Temple",   "INDOOR",  78));
        allPlaces.add(new PlaceInfo(24, "rishikesh", "Ganga Beach",              "Nature",   "OUTDOOR", 85));

        // ── HARIDWAR ────────────────────────────────────────
        allPlaces.add(new PlaceInfo(25, "haridwar", "Har Ki Pauri",              "Landmark", "OUTDOOR", 95));
        allPlaces.add(new PlaceInfo(26, "haridwar", "Mansa Devi Temple",         "Temple",   "OUTDOOR", 90));
        allPlaces.add(new PlaceInfo(27, "haridwar", "Chandi Devi Temple",        "Temple",   "OUTDOOR", 88));
        allPlaces.add(new PlaceInfo(28, "haridwar", "Maya Devi Temple",          "Temple",   "INDOOR",  85));
        allPlaces.add(new PlaceInfo(29, "haridwar", "Daksha Mahadev Temple",     "Temple",   "INDOOR",  78));
        allPlaces.add(new PlaceInfo(30, "haridwar", "Rajaji National Park",      "Nature",   "OUTDOOR", 82));
        allPlaces.add(new PlaceInfo(31, "haridwar", "Sapt Rishi Ashram",         "Temple",   "INDOOR",  75));
        allPlaces.add(new PlaceInfo(32, "haridwar", "Bara Bazaar",               "Market",   "INDOOR",  80));
    }

    public CityInfo getCity(String cityKey)
    {
        return cityMap.get(cityKey.toLowerCase().trim());
    }

    public List<CityInfo> getAllCities()
    {
        return new ArrayList<>(cityMap.values());
    }

    public List<PlaceInfo> getPlaces(String cityKey)
    {
        String key = cityKey.toLowerCase().trim();
        return allPlaces.stream()
                .filter(p -> p.getCityKey().equals(key))
                .toList();
    }

    public List<PlaceInfo> searchPlaces(String keyword)
    {
        String k = keyword.toLowerCase().trim();
        return allPlaces.stream()
                .filter(p -> p.getName().toLowerCase().contains(k)
                          || p.getCategory().toLowerCase().contains(k))
                .toList();
    }

    public List<PlaceInfo> getAllPlaces()
    {
        return Collections.unmodifiableList(allPlaces);
    }
}
