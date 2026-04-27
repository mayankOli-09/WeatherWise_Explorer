package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")   // allows the frontend HTML to call this backend
public class WeatherwiseController
{
    private final DatabaseManager       db;
    private final WeatherModule         weatherModule;
    private final RecommendationModule  recommender;

    public WeatherwiseController(DatabaseManager db,
                                 WeatherModule weatherModule,
                                 RecommendationModule recommender)
    {
        this.db            = db;
        this.weatherModule = weatherModule;
        this.recommender   = recommender;
    }

    // ── GET /api/cities ──────────────────────────────────────────────────────
    // Returns all 4 cities
    @GetMapping("/cities")
    public ResponseEntity<List<Map<String, Object>>> getCities()
    {
        List<Map<String, Object>> result = new ArrayList<>();
        for (CityInfo c : db.getAllCities())
        {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("cityKey",      c.getCityKey());
            map.put("name",         c.getName());
            map.put("countryState", c.getCountryState());
            map.put("description",  c.getDescription());
            map.put("lat",          c.getLat());
            map.put("lon",          c.getLon());
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }

    // ── GET /api/cities/{cityKey} ────────────────────────────────────────────
    // Returns info + all places for one city
    @GetMapping("/cities/{cityKey}")
    public ResponseEntity<?> getCity(@PathVariable String cityKey)
    {
        CityInfo city = db.getCity(cityKey);
        if (city == null)
            return ResponseEntity.badRequest().body(Map.of("error", "City not found: " + cityKey));

        List<Map<String, Object>> places = buildPlaceList(db.getPlaces(cityKey), null);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("cityKey",      city.getCityKey());
        response.put("name",         city.getName());
        response.put("countryState", city.getCountryState());
        response.put("description",  city.getDescription());
        response.put("places",       places);
        return ResponseEntity.ok(response);
    }

    // ── GET /api/weather/{cityKey} ───────────────────────────────────────────
    // Returns live weather for a city
    @GetMapping("/weather/{cityKey}")
    public ResponseEntity<?> getWeather(@PathVariable String cityKey)
    {
        CityInfo city = db.getCity(cityKey);
        if (city == null)
            return ResponseEntity.badRequest().body(Map.of("error", "City not found: " + cityKey));

        WeatherData w = weatherModule.fetchWeather(city);
        if (w == null)
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not fetch weather."));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("cityName",    w.getCityName());
        response.put("condition",   w.getCondition());
        response.put("description", w.getDescription());
        response.put("temperature", w.getTemperature());
        response.put("humidity",    w.getHumidity());
        response.put("windSpeed",   w.getWindSpeed());
        response.put("tag",         w.getTag());
        response.put("insight",     w.getInsight());
        return ResponseEntity.ok(response);
    }

    // ── GET /api/recommend/{cityKey} ─────────────────────────────────────────
    // Returns weather + top 5 ranked places
    @GetMapping("/recommend/{cityKey}")
    public ResponseEntity<?> getRecommendations(@PathVariable String cityKey)
    {
        CityInfo city = db.getCity(cityKey);
        if (city == null)
            return ResponseEntity.badRequest().body(Map.of("error", "City not found: " + cityKey));

        WeatherData w = weatherModule.fetchWeather(city);
        if (w == null)
            return ResponseEntity.internalServerError().body(Map.of("error", "Could not fetch weather."));

        List<PlaceInfo> ranked  = recommender.rankPlaces(db.getPlaces(cityKey), w.getTag());
        List<Map<String, Object>> places = buildPlaceList(ranked, w.getTag());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("cityName",    w.getCityName());
        response.put("condition",   w.getCondition());
        response.put("description", w.getDescription());
        response.put("temperature", w.getTemperature());
        response.put("humidity",    w.getHumidity());
        response.put("windSpeed",   w.getWindSpeed());
        response.put("tag",         w.getTag());
        response.put("insight",     w.getInsight());
        response.put("places",      places);
        return ResponseEntity.ok(response);
    }

    // ── GET /api/search?keyword=temple ──────────────────────────────────────
    // Searches places by name or category
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword)
    {
        if (keyword == null || keyword.isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "keyword is required"));

        List<PlaceInfo> results = db.searchPlaces(keyword);
        return ResponseEntity.ok(buildPlaceList(results, null));
    }

    // ── Helper ───────────────────────────────────────────────────────────────
    private List<Map<String, Object>> buildPlaceList(List<PlaceInfo> places, String tag)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        int rank = 1;
        for (PlaceInfo p : places)
        {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id",         p.getId());
            map.put("cityKey",    p.getCityKey());
            map.put("name",       p.getName());
            map.put("category",   p.getCategory());
            map.put("placeType",  p.getPlaceType());
            map.put("popularity", p.getPopularity());
            if (tag != null)
            {
                map.put("rank",   rank++);
                map.put("reason", recommender.getReason(p, tag));
                map.put("score",  recommender.calculateScore(p, tag));
            }
            list.add(map);
        }
        return list;
    }
}
