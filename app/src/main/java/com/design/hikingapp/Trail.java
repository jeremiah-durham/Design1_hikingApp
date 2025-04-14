package com.design.hikingapp;

public class Trail {
    private int imageResource;
    private int mapResource;
    private String name;
    private double distance;
    private String difficulty;
    private int elevation;
    private int timeHrs;
    private int timeMins;
    private int id;

    public Trail(int imageResource, int mapResource, String name, double distance, String difficulty, int elevation, int timeHrs, int timeMins, int id) {
        this.imageResource = imageResource;
        this.mapResource = mapResource;
        this.name = name;
        this.distance = distance;
        this.elevation = elevation;
        this.difficulty = difficulty;
        this.timeHrs = timeHrs;
        this.timeMins = timeMins;

        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }
    public String getName() {
        return name;
    }
    public double getDistance() {
        return distance;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public int getElevation() {
        return elevation;
    }
    public int getTimeHrs() {
        return timeHrs;
    }
    public int getTimeMins() {
        return timeMins;
    }

    public int getId() {
        return id;
    }
}