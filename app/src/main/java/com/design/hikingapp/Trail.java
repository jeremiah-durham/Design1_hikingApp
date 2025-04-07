package com.design.hikingapp;

public class Trail {
    private int imageResource;
    private String trailName;
    private String trailDetails;
    private int trailID;

    public Trail(int imageResource, String trailName, String trailDetails, int trailID) {
        this.imageResource = imageResource;
        this.trailName = trailName;
        this.trailDetails = trailDetails;
        this.trailID = trailID;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTrailName() {
        return trailName;
    }

    public String getTrailDetails() {
        return trailDetails;
    }

    public int getTrailID() {
        return trailID;
    }
}