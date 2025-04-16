package com.design.hikingapp;

import java.util.ArrayList;
import java.util.List;

public class WeatherData {
    private List<TemperatureHour> hourlyTemperatures;
    private List<PrecipHour> hourlyPrecipitations;
    private List<WindHour> hourlyWinds;

    private int currentTemp;
    private int currentFeelsLike;
    private String currentCondition;
    private int weatherBackground;
    private int weatherIcon;
    private int snowDepthInches;

    public WeatherData() {
        hourlyTemperatures = new ArrayList<>();
        hourlyPrecipitations = new ArrayList<>();
        hourlyWinds = new ArrayList<>();
    }

    public void addTemperature(int temperature, String condition, String time) {
        int imageResource = R.drawable.sunny_icon;
        switch (condition) {
            case "Sunny":
                imageResource = R.drawable.sunny_icon;
                break;
            case "Cloudy":
                imageResource = R.drawable.cloudy_icon;
                break;
            case "Rainy":
                imageResource = R.drawable.rainy_icon;
                break;
            case "Snowy":
                imageResource = R.drawable.snowy_icon;
                break;
        }

        String ftime = time.split("T")[1].split(":")[0];
        int integerTime = Integer.parseInt(ftime);

        if ((integerTime < 7 || integerTime > 19) && condition.equals("Sunny")) {
            imageResource = R.drawable.night_icon;
        }

        hourlyTemperatures.add(new TemperatureHour(temperature, imageResource, formatTime(time)));
    }

    public void addPrecipitation(int precipPercent, String time) {
        hourlyPrecipitations.add(new PrecipHour(precipPercent, formatTime(time)));
    }

    public void addWind(int speed, int direction, String time) {
        hourlyWinds.add(new WindHour(speed, direction, formatTime(time)));
    }

    public void addCurrentWeather(int currentTemp, int currentFeelsLike, String currentCondition, int snowDepthInches, String time) {
        this.currentTemp = currentTemp;
        this.currentFeelsLike = currentFeelsLike;
        this.currentCondition = currentCondition;

        int backgroundResource = R.drawable.sunny_bg;
        int weatherIcon = R.drawable.sunny_icon;
        switch (currentCondition) {
            case "Sunny":
                backgroundResource = R.drawable.sunny_bg;
                weatherIcon = R.drawable.sunny_icon;
                break;
            case "Cloudy":
                backgroundResource = R.drawable.cloudy_bg;
                weatherIcon = R.drawable.cloudy_icon;
                break;
            case "Rainy":
                backgroundResource = R.drawable.rainy_bg;
                weatherIcon = R.drawable.rainy_icon;
                break;
            case "Snowy":
                backgroundResource = R.drawable.snowy_bg;
                weatherIcon = R.drawable.snowy_icon;
                break;
        }

        String ftime = time.split("T")[1].split(":")[0];
        int integerTime = Integer.parseInt(ftime);

        if ((integerTime < 7 || integerTime > 19) && currentCondition.equals("Sunny")) {
            backgroundResource = R.drawable.night_bg;
            weatherIcon = R.drawable.night_icon;
        }

        this.weatherIcon = weatherIcon;
        this.weatherBackground = backgroundResource;
        this.snowDepthInches = snowDepthInches;
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
    public String getCurrentCondition() {
        return currentCondition;
    }

    public int getWeatherBackground() {
        return weatherBackground;
    }
    public int getWeatherIcon() {
        return weatherIcon;
    }
    public int getSnowDepthInches() {
        return snowDepthInches;
    }

    private String formatTime(String time) {
        String ftime = time.split("T")[1].split(":")[0];

        int integerTime = Integer.parseInt(ftime);
        int hour = (integerTime == 0 || integerTime == 12) ? 12 : integerTime % 12;

        String amPm = (integerTime >= 12) ? " pm" : " am";

        return hour + amPm;
    }
}
