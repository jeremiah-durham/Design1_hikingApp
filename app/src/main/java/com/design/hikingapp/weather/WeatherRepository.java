package com.design.hikingapp.weather;


import com.design.hikingapp.util.RepositoryCallback;
import com.design.hikingapp.util.Result;
import com.design.hikingapp.WeatherData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Executor;

public class WeatherRepository {
    private static WeatherRepository instance;

    private WeatherDataParser responseParser;
    private Executor executor;
    private WeatherRepository(){} // Do not initialize the singleton

    public static WeatherRepository getInstance() {
        if(instance == null) {
            instance = new WeatherRepository();
        }

        return instance;
    }

    public static void initRepo(WeatherDataParser responseParser, Executor executor) {
        var inst = getInstance();
        inst.responseParser = responseParser;
        inst.executor = executor;
    }

    public void fetchWeatherData(
            double lat, double lon,
            final RepositoryCallback<WeatherData> callback
            ){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<WeatherData> result = weatherSynchronousFetch(lat, lon);
                    callback.onComplete(result);
                } catch (Exception e) {
                    Result<WeatherData> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        });
    }

    public Result<WeatherData> weatherSynchronousFetch(double lat, double lon) {
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
                return new Result.Error<WeatherData>(new Exception("Weather API returned response code: " + responseCode + " (not HTTP_OK)"));
            }

            // Read response
            StringBuilder resultJson = new StringBuilder();
            try (Scanner scanner = new Scanner(apiConnection.getInputStream())) {
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
            }

            responseParser.parseWeatherData(resultJson.toString());
            WeatherData weatherData = responseParser.getWeatherData();
            return new Result.Success<WeatherData>(weatherData);
        } catch (IOException e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
            return new Result.Error<WeatherData>(e);
        } finally {
            if (apiConnection != null) {
                apiConnection.disconnect();
            }
        }
    }
}