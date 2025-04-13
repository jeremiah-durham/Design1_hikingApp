package com.design.hikingapp;

public class UserHikeStats {
    private double waterNeeded;
    private int caloriesBurned;
    public UserHikeStats(double waterNeeded, int caloriesBurned) {
        this.waterNeeded = waterNeeded;
        this.caloriesBurned = caloriesBurned;
    }

    public double getWaterNeeded() {
        return waterNeeded;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }
}
