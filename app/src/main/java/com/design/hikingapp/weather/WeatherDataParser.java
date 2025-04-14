package com.design.hikingapp.weather;
import com.design.hikingapp.WeatherData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherDataParser {
    private WeatherData weatherData;

    public WeatherDataParser() {};

    public void parseWeatherData(String jsonString) {
            this.weatherData = new WeatherData();
            // Extract all required arrays from JSON
            String[] times = parseStringArray(extractArray(jsonString, "time"));
            double[] temps = parseDoubleArray(extractArray(jsonString, "temperature_2m"));
            double[] feelsLike = parseDoubleArray(extractArray(jsonString, "apparent_temperature"));
            double[] precipProb = parseDoubleArray(extractArray(jsonString, "precipitation_probability"));
            double[] precip = parseDoubleArray(extractArray(jsonString, "precipitation"));
            double[] snowDepths = parseDoubleArray(extractArray(jsonString, "snow_depth"));
            int[] weatherCodes = parseIntArray(extractArray(jsonString, "weather_code"));
            double[] windSpeeds = parseDoubleArray(extractArray(jsonString, "wind_speed_10m"));
            int[] windDirections = parseIntArray(extractArray(jsonString, "wind_direction_10m"));

            // Add current weather (first entry)
            if (times.length > 0 && temps.length > 0 && feelsLike.length > 0 && weatherCodes.length > 0) {
                weatherData.addCurrentWeather(
                        (int)(temps[0]),
                        (int)(feelsLike[0]),
                        getWeatherCondition(weatherCodes[0]),
                        (int)snowDepths[0]
                );
            }

            // Add hourly data
            for (int i = 0; i < times.length; i++) {
                // Add temperature data
                if (i < temps.length && i < weatherCodes.length) {
                    weatherData.addTemperature(
                            (int)temps[i],
                            getWeatherCondition(weatherCodes[i]),
                            times[i]
                    );
                }

                // Add precipitation data (now with snow depth)
                if (i < precipProb.length) {
                    weatherData.addPrecipitation(
                            (int)precipProb[i],
                            times[i]
                    );
                }

                // Add wind data (in degrees)
                if (i < windSpeeds.length && i < windDirections.length) {
                    weatherData.addWind(
                            (int)windSpeeds[i],
                            windDirections[i],  // Keep as degrees
                            times[i]
                    );
                }
            }
    }

    // ===== Helper Methods for JSON Parsing =====
    private String extractArray(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\":\\s*(\\[[^\\]]*\\])");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : "[]";
    }

    private double[] parseDoubleArray(String arrayStr) {
        String[] parts = arrayStr.replace("[", "").replace("]", "").split(",");
        double[] result = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Double.parseDouble(parts[i].trim());
        }
        return result;
    }

    private int[] parseIntArray(String arrayStr) {
        String[] parts = arrayStr.replace("[", "").replace("]", "").split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }

    private String[] parseStringArray(String arrayStr) {
        String[] parts = arrayStr.replace("[", "").replace("]", "").split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim().replace("\"", "");
        }
        return parts;
    }

    // ===== Weather Condition Converter =====
    private String getWeatherCondition(int weatherCode) {
        switch (weatherCode) {
            case 0: return "Sunny";
            case 1: return "Sunny";
            case 2: return "Cloudy";
            case 3: return "Cloudy";
            case 51: case 53: case 55: return "Rainy";
            case 61: case 63: case 65: return "Rainy";
            case 71: case 73: case 75: return "Snowy";
            case 80: case 81: case 82: return "Rainy";
            case 85: case 86: return "Snowy";
            default: return "Unknown";
        }
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }
}