package com.design.hikingapp.trail;

public class TrailRepository {
    private static TrailRepository instance;
    private TrailRepository() {}
    public static TrailRepository getInstance() {
        if (instance == null)
            instance = new TrailRepository();
        return instance;
    }
}
