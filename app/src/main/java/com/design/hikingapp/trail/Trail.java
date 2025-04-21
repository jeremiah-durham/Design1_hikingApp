package com.design.hikingapp;

import android.graphics.Bitmap;

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
    private Bitmap imgBmp = null;
    private double lat = 39.75081252642373;
    private double lon = -105.22232351583222;

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

    public void setLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    public double getLat() {
        return this.lat;
    }
    public double getLon() {
        return this.lon;
    }
    public void setImgBmp(Bitmap imgBmp) {
        this.imgBmp = imgBmp;
    }
    public Bitmap getImgBmp() {
        return imgBmp;
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