package com.design.hikingapp;

import java.util.List;

public class WeatherData {
    private List<TemperatureHour> hourlyTemperatures;
    private List<PrecipHour> hourlyPrecipitations;
    private List<WindHour> hourlyWinds;

    private int currentTemp;
    private int currentFeelsLike;
    private String currentCondition;
    private int weatherBackground;
    private int snowDepthInches;



    public void addTemperature(int temperature, String condition, String time) {
        int imageResource = R.drawable.sunny_icon;
        switch (condition) {
            case "Sunny":
                imageResource = R.drawable.sunny_icon;
            case "Cloudy":
                imageResource = R.drawable.cloudy_icon;
            case "Rainy":
                imageResource = R.drawable.rainy_icon;
            case "Snowy":
                imageResource = R.drawable.snowy_icon;
            case "Night":
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

    public void addCurrentWeather(int currentTemp, int currentFeelsLike, String currentCondition, int snowDepthInches) {
        this.currentTemp = currentTemp;
        this.currentFeelsLike = currentFeelsLike;
        this.currentCondition = currentCondition;

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
