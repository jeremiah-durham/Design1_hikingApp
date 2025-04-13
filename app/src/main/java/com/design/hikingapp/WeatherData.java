package com.design.hikingapp;

import java.util.List;

public class WeatherData {
    private List<TemperatureHour> hourlyTemperatures;
    private List<PrecipHour> hourlyPrecipitations;
    private List<WindHour> hourlyWinds;

    private int currentTemp;
    private int currentFeelsLike;
    private int weatherBackground;



    public void addTemperature(int temperature, String condition, String time) {
        int imageResource = R.drawable.sunny_icon;
        switch (condition) {
            case "SUNNY":
                imageResource = R.drawable.sunny_icon;
            case "CLOUDY":
                imageResource = R.drawable.cloudy_icon;
            case "RAINY":
                imageResource = R.drawable.rainy_icon;
            case "SNOWY":
                imageResource = R.drawable.snowy_icon;
            case "NIGHT":
                imageResource = R.drawable.night_icon;
        }
        hourlyTemperatures.add(new TemperatureHour(temperature, imageResource, time));
    }

    public void addPrecipitation(int precipPercent, String time) {
        hourlyPrecipitations.add(new PrecipHour(precipPercent, time));
    }

    public void addWind(int speed, int direction, String time) {
        hourlyWinds.add(new WindHour(speed, direction, time));
    }

    public void addCurrentWeather(int currentTemp, int currentFeelsLike, String currentCondition) {
        this.currentTemp = currentTemp;
        this.currentFeelsLike = currentFeelsLike;
        int backgroundResource = R.drawable.sunny_bg;
        switch (currentCondition) {
            case "SUNNY":
                backgroundResource = R.drawable.sunny_bg;
            case "CLOUDY":
                backgroundResource = R.drawable.cloudy_bg;
            case "RAINY":
                backgroundResource = R.drawable.rainy_bg;
            case "SNOWY":
                backgroundResource = R.drawable.snowy_bg;
            case "NIGHT":
                backgroundResource = R.drawable.night_bg;
        }
    }

    public List<TemperatureHour> getHourlyTemperatures() {
        return hourlyTemperatures;
    }

    public List<PrecipHour> getHourlyPrecipitations() {
        return hourlyPrecipitations;
    }

    public List<WindHour> getHourlyWinds() {
        return hourlyWinds;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public int getCurrentFeelsLike() {
        return currentFeelsLike;
    }

    public int getWeatherBackground() {
        return weatherBackground;
    }
}
