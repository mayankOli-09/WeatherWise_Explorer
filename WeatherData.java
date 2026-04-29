package com.example;

public class WeatherData
{
    private final String cityName;
    private final String condition;
    private final String description;
    private final double temperature;
    private final int    humidity;
    private final double windSpeed;
    private final String tag;         // CLEAR | CLOUDY | RAIN | STORM | HOT | COLD | WINDY
    private final String insight;

    public WeatherData(String cityName, String condition, String description,
                       double temperature, int humidity, double windSpeed,
                       String tag, String insight)
    {
        this.cityName    = cityName;
        this.condition   = condition;
        this.description = description;
        this.temperature = temperature;
        this.humidity    = humidity;
        this.windSpeed   = windSpeed;
        this.tag         = tag;
        this.insight     = insight;
    }

    public String getCityName()    { return cityName;    }
    public String getCondition()   { return condition;   }
    public String getDescription() { return description; }
    public double getTemperature() { return temperature; }
    public int    getHumidity()    { return humidity;    }
    public double getWindSpeed()   { return windSpeed;   }
    public String getTag()         { return tag;         }
    public String getInsight()     { return insight;     }
}
