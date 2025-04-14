package com.design.hikingapp.weather;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherData {
    private StringBuilder temperatureData;
    private StringBuilder precipitationData;  // Now includes snow depth
    private StringBuilder windData;
    private String currentWeather;

    public WeatherData() {
        temperatureData = new StringBuilder();
        precipitationData = new StringBuilder();
        windData = new StringBuilder();
        currentWeather = "";
    }

    // Updated to include snow depth
    public void addPrecipitation(double chance, double snowDepth, String time) {
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("h a"));
        precipitationData.append(String.format(
                "Time: %s, Precip Chance: %.0f%%, Snow Depth: %.2f m%n",
                formattedTime, chance, snowDepth
        ));
    }

    public void addTemperature(double temp, String condition, String time) {
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("h a"));
        temperatureData.append(String.format("Time: %s, Temperature: %.1f°F, Condition: %s%n",
                formattedTime, temp, condition));
    }

    public void addWind(double speed, String direction, String time) {
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("h a"));
        windData.append(String.format("Time: %s, Wind Speed: %.1f mph, Direction: %s%n",
                formattedTime, speed, direction));
    }

    public void addCurrentWeather(double temp, double feelsLike, String condition) {
        currentWeather = String.format("Current Weather - Temp: %.1f°F, Feels Like: %.1f°F, Condition: %s",
                temp, feelsLike, condition);
    }

    public void displayWeatherData() {
        System.out.println("=== WEATHER DATA ===");
        System.out.println(currentWeather);
        System.out.println("\nHourly Temperature Data:");
        System.out.println(temperatureData.toString());
        System.out.println("Hourly Precipitation Data:");
        System.out.println(precipitationData.toString());
        System.out.println("Hourly Wind Data:");
        System.out.println(windData.toString());
    }
}