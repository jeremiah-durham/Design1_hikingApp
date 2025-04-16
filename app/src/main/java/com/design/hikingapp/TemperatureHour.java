package com.design.hikingapp;

public class TemperatureHour {
    public String temperature;
    public int iconResId;
    public String time;

    public TemperatureHour(int temperature, int iconResId, String time) {
        this.temperature = temperature + "°";
        this.iconResId = iconResId;
        this.time = time;
    }
}
