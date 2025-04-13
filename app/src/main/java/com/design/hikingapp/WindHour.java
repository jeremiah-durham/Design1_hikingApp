package com.design.hikingapp;

public class WindHour {
    public int speed; // in mph
    public int direction; // Degree heading
    public String time;

    public WindHour(int speed, int direction, String time) {
        this.speed = speed;
        this.direction = direction;
        this.time = time;
    }
}
