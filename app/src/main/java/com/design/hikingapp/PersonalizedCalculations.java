package com.design.hikingapp;

import java.util.ArrayList;
import java.util.List;

public class PersonalizedCalculations {

    private double distance;
    private double weight;
    private double terrainFactor = 1.2;
    private double estimatedTime;
    private double bagWeight;
    private double elevationChange;
    private double velocity;
    private double averageGrade;
    private String weatherCondition;
    private double caloriesBurned;
    private int waterNeeded;
    private double snowDepth;
    private int feelsLike;

    private List<PackingItem> gear = new ArrayList<>();

    public PersonalizedCalculations(Trail trail, WeatherData weatherData, int weight) {
        this.weight = weight;
        this.distance = trail.getDistance();
        this.estimatedTime = (trail.getTimeHrs() * 60) + trail.getTimeMins();
        this.bagWeight = weight/10.0;
        this.elevationChange = trail.getElevation();
        this.velocity = (distance * 1609) / (estimatedTime * 60);
        this.averageGrade = elevationChange / 6.9 * 5280;

        this.caloriesBurned = (1.5 * weight / 2.205) + (2 * (((weight / 2.205) + (bagWeight / 2.205)) * (((bagWeight / weight) * (bagWeight / weight)) / 2.205))) + terrainFactor * ((weight + bagWeight) / 2.205) * ((1.5 * velocity * velocity) + (.35 * velocity * averageGrade));

        double waterNeeded;
        if (feelsLike > 75){
            waterNeeded = (estimatedTime/60);
        } else{
            waterNeeded = (estimatedTime/120);
        }
        waterNeeded = Math.round((waterNeeded * 33.814)/4) * 4;
        this.waterNeeded = (int)(waterNeeded);
        this.snowDepth = weatherData.getSnowDepthInches();
        this.weatherCondition = weatherData.getCurrentCondition();
        this.feelsLike = weatherData.getCurrentFeelsLike();
    }

    public int getWaterNeeded() {
        return waterNeeded;
    }
    public int getCaloriesBurned() {
        return (int)(caloriesBurned);
    }

    public List<PackingItem> getPackingList() {
        gear.add(new PackingItem("First Aid Kit", R.drawable.backpack_icon));
        gear.add(new PackingItem("Multitool", R.drawable.backpack_icon));
        gear.add(new PackingItem("Cell Phone", R.drawable.backpack_icon));
        gear.add(new PackingItem("Portable Phone Charger", R.drawable.backpack_icon));

        if (feelsLike > 80 || weatherCondition.equals("Sunny")) {
            gear.add(new PackingItem("Sunscreen", R.drawable.backpack_icon));
        }

        if (feelsLike >= 55) {
            gear.add(new PackingItem("Shorts", R.drawable.backpack_icon));
        } else {
            gear.add(new PackingItem("Hiking Pants", R.drawable.backpack_icon));
        }

        if (feelsLike <= 32) {
            gear.add(new PackingItem("Thick Socks", R.drawable.backpack_icon));
        }

        if (weatherCondition.equals("Sunny") && feelsLike > 40) {
            gear.add(new PackingItem("Hat", R.drawable.backpack_icon));
        }

        if (feelsLike < 20) {
            gear.add(new PackingItem("Heavy Duty Gloves", R.drawable.backpack_icon));
        } else if (feelsLike < 32) {
            gear.add(new PackingItem("Medium Duty Gloves", R.drawable.backpack_icon));
        } else if (feelsLike < 45) {
            gear.add(new PackingItem("Light Duty Gloves", R.drawable.backpack_icon));
        }

        if ((weatherCondition.equals("Cloudy") || weatherCondition.equals("Snowy")) && feelsLike < 32) {
            gear.add(new PackingItem("Gaiter", R.drawable.backpack_icon));
        }
        if (snowDepth >= 20) {
            gear.add(new PackingItem("Snow Boots", R.drawable.backpack_icon));
            gear.add(new PackingItem("Snow Pants", R.drawable.backpack_icon));
            gear.add(new PackingItem("Waterproof Top Layer", R.drawable.backpack_icon));
        }
        if (feelsLike < 45) {
            gear.add(new PackingItem("Extra Layers", R.drawable.backpack_icon));
        }

        if (weatherCondition.equals("Rainy")) {
            gear.add(new PackingItem("Raincoat", R.drawable.backpack_icon));
        }

        return gear;
    }
}
