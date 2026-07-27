# 🌤️ WeatherWise Explorer — Smart Outing Planner

<div align="center">

**Plan smarter outings with real-time weather + curated location recommendations**

*Weather Forecasts • Location Intelligence • Outing Suggestions • Full-Stack Java • JavaFX Desktop*

[![GitHub stars](https://img.shields.io/github/stars/mayankOli-09/WeatherWise_Explorer?style=social)](https://github.com/mayankOli-09/WeatherWise_Explorer)
[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.2-blue?logo=java)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Build-Maven-red?logo=apachemaven)](https://maven.apache.org/)
[![Weather API](https://img.shields.io/badge/Powered%20by-OpenWeatherMap-blue?logo=cloudflare)](https://openweathermap.org/api)

</div>

---

## 🎯 What is WeatherWise Explorer?

WeatherWise Explorer is a **full-stack Java Spring Boot application** (with a **JavaFX desktop client**) that takes the guesswork out of planning your day out. It combines **live weather data** with **curated location insights** to recommend the best places to visit — right now or in the forecast window.

No more checking weather apps and Google Maps separately. WeatherWise brings it all together.

---

## ✨ Key Features

| Feature | Description |
|--------|-------------|
| 🌦️ **Real-Time Weather** | Fetches live conditions using the OpenWeatherMap API |
| 📍 **Smart Location Picks** | Recommends places based on current or forecasted weather |
| 🗓️ **Forecast-Aware Planning** | Suggests outings for upcoming days, not just today |
| 🖥️ **Full-Stack Java** | Spring Boot backend with a clean, responsive frontend |
| 🖼️ **JavaFX Desktop App** | Standalone desktop dashboard built with JavaFX |
| ⚡ **Fast & Lightweight** | Built with Maven for quick builds and easy dependency management |

---

## 🚀 Quick Start

### Prerequisites

Make sure you have the following installed:

- **Java 17+**
- **Maven 3.6+**
- **JavaFX 21.0.2** (pulled automatically via Maven — see [JavaFX Setup](#-javafx-setup))
- An **OpenWeatherMap API key** → [Get one free here](https://openweathermap.org/api)

### Setup & Run (Spring Boot Web App)

```bash
# 1. Clone the repository
git clone https://github.com/mayankOli-09/WeatherWise_Explorer.git
cd WeatherWise_Explorer

# 2. Add your API key
# Open src/main/resources/application.properties and set:
# weather.api.key=take from openmeteo

# 3. Build the project
mvn clean install

# 4. Run the application
mvn spring-boot:run
```

Then open your browser at **`http://localhost:8080`** and start exploring! 🗺️

### Run the JavaFX Desktop App

```bash
mvn javafx:run
```

This launches the standalone desktop dashboard (`com.example.WeatherWiseApp`) instead of the web frontend.

---

## 🖼️ JavaFX Setup

The project now ships with a JavaFX desktop client alongside the Spring Boot web app. The following were added to `pom.xml`:

**Dependencies:**

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.2</version>
</dependency>

<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>21.0.2</version>
</dependency>

<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-graphics</artifactId>
    <version>21.0.2</version>
</dependency>
```

**Maven Plugin:**

```xml
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <configuration>
        <mainClass>com.example.WeatherWiseApp</mainClass>
    </configuration>
</plugin>
```

---

## 🔑 API Key Configuration

In `src/main/resources/application.properties`:

```properties
weather.api.key=your_openweathermap_api_key
server.port=8080
```

> **Get your free API key** at [openweathermap.org](https://openweathermap.org/api) — the free tier is more than enough to run this project.

---

## 🏗️ Project Structure

```
WeatherWise_Explorer/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/weatherwise/
│   │   │       ├── controller/       # REST & MVC controllers
│   │   │       ├── service/          # Weather + location business logic
│   │   │       └── model/            # Data models & DTOs
│   │   ├── com/example/
│   │   │   └── WeatherWiseApp.java   # JavaFX desktop entry point
│   │   └── resources/
│   │       ├── templates/            # Frontend (HTML/Thymeleaf)
│   │       ├── static/               # CSS, JS, assets
│   │       └── application.properties
├── pom.xml
└── README.md
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Java 17, Spring Boot 3.x |
| **Desktop UI** | JavaFX 21.0.2 (Controls, FXML, Graphics) |
| **Build Tool** | Apache Maven |
| **Weather Data** | OpenWeatherMap REST API |
| **Frontend** | HTML, CSS, JavaScript (Spring MVC / Thymeleaf) |
| **HTTP Client** | RestTemplate / WebClient |

---

## 📸 How It Works

```
User enters location
        ↓
WeatherWise fetches real-time weather via OpenWeatherMap API
        ↓
App analyzes conditions (sunny, rainy, windy, etc.)
        ↓
Smart engine recommends best-fit locations to visit
        ↓
User gets a curated outing plan — instantly!
```

---

## 🩹 Recent Fixes & Changes

A round of bug fixes resolved 100+ compilation errors and got the project to a clean `mvn clean compile` / `BUILD SUCCESS`. Summary of what changed:

### `CityInfo.java`
No major changes needed — already exposed `getCountryState()` and `getDescription()`.

### `CityInfoModule.java`
- Replaced `info.getArea()` → `info.getCountryState()`
- Replaced `info.getNote()` → `info.getDescription()`

### `pom.xml`
- Added JavaFX dependencies (`javafx-controls`, `javafx-fxml`, `javafx-graphics`, v21.0.2)
- Added the `javafx-maven-plugin` (v0.0.8), configured with main class `com.example.WeatherWiseApp`

### `WeatherWiseApp.java`
- Added missing fields to `DashboardCity`: `liveWeatherLoaded`, `liveWeatherLoading`
- Fixed the weather call — `fetchWeather()` expects a `CityInfo` object, not a `String`:
  ```java
  CityInfo cityInfo = db.getCity(activeCityKey);
  WeatherData data = weatherModule.fetchWeather(cityInfo);
  ```
- Fixed the recommendation call — `recommend()` returns `void`, so recommendations are now pulled directly from the database instead:
  ```java
  List<PlaceInfo> backendPlaces = db.getPlaces(cityName);
  ```

### Build Verification
| Stage | Result |
|-------|--------|
| Before fixes | 100+ compilation errors |
| After fixes | `mvn clean compile` → **BUILD SUCCESS** |

---

## ⚠️ Known Issues / Not Yet Implemented

A few methods still throw `UnsupportedOperationException` and will **compile but crash at runtime** if called:

**`WeatherModule.java`**
```java
private JSONObject getJson(String apiUrl) {
    throw new UnsupportedOperationException("Unimplemented method 'getJson'");
}

public void displayWeather(String city) {
    throw new UnsupportedOperationException("Unimplemented method 'displayWeather'");
}
```

**`RecommendationModule.java`**
```java
public void recommend(String city) {
    throw new UnsupportedOperationException("Unimplemented method 'recommend'");
}
```

These are tracked as open TODOs — contributions welcome!

---

## ✅ Current Status

| Component | Status |
|-----------|--------|
| Maven Build | ✅ Working |
| JavaFX Dependencies | ✅ Added |
| Spring Boot Dependencies | ✅ Working |
| CityInfo Module | ✅ Fixed |
| WeatherWiseApp Compilation | ✅ Fixed |
| `mvn clean compile` | ✅ Success |
| JavaFX Runtime | ✅ Success |
| HTML Frontend | ✅ Success |
| Unimplemented Methods (`getJson`, `displayWeather`, `recommend`) | ⚠️ Present |

---

## 🤝 Contributing

Contributions are welcome! Here's how:

```bash
# Fork the repo, then:
git checkout -b feature/your-feature-name
git commit -m "Add: your feature description"
git push origin feature/your-feature-name
# Open a Pull Request 🎉
```

Good first issues: implementing `WeatherModule.getJson()` / `displayWeather()` and `RecommendationModule.recommend()`.

<div align="center">

Built with ❤️ by <a href="https://github.com/mayankOli-09">Mayank Oli</a> &nbsp;|&nbsp; ⭐ Star this repo if it helped you!

</div>
