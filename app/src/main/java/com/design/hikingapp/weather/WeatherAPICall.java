package com.design.hikingapp.weather;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherAPICall {
    public static void getWeatherData(double latitude, double longitude) {
        String response = fetchWeatherData(latitude, longitude);
        if (response != null) {
            writeToJsonFile(response);
        } else {
            System.out.println("Failed to fetch weather data.");
        }
    }

    private static String fetchWeatherData(double lat, double lon) {
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&daily=temperature_2m_max,temperature_2m_min&hourly=temperature_2m,apparent_temperature,precipitation_probability,precipitation,weather_code,wind_speed_10m,snow_depth,wind_direction_10m&forecast_days=1&wind_speed_unit=mph&temperature_unit=fahrenheit&precipitation_unit=inch";

        HttpURLConnection apiConnection = null;
        try {
            URL url = new URL(urlString);
            apiConnection = (HttpURLConnection) url.openConnection();
            apiConnection.setRequestMethod("GET");
            apiConnection.connect();

            // Check for a successful response
            int responseCode = apiConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Error: API returned response code " + responseCode);
                return null;
            }

            // Read response
            StringBuilder resultJson = new StringBuilder();
            try (Scanner scanner = new Scanner(apiConnection.getInputStream())) {
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
            }
            return resultJson.toString();
        } catch (IOException e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
            return null;
        } finally {
            if (apiConnection != null) {
                apiConnection.disconnect();
            }
        }
    }

    private static void writeToJsonFile(String jsonData) {
        try (FileWriter file = new FileWriter("weather_data3.json")) {
            file.write(jsonData);
            System.out.println("Weather data saved to weather_data.json");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}