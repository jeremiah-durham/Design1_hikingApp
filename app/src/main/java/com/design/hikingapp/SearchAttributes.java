package com.design.hikingapp;

import androidx.annotation.NonNull;

public class SearchAttributes {
    private boolean Biking;
    private boolean Views;
    private boolean River;
    private boolean History;
    private boolean Forest;
    private boolean Lake;
    private int minLen;
    private int maxLen;
    private boolean Easy;
    private boolean Moderate;
    private boolean Hard;
    private int minEle;
    private int maxEle;
    private double minTime;
    private double maxTime;
    private String searchBar;

    public SearchAttributes(){
        this.Biking = false;
        this.Views = false;
        this.River = false;
        this.History = false;
        this.Forest = false;
        this.Lake = false;
        this.minLen = 0;
        this.maxLen = 15;
        this.Easy = false;
        this.Moderate = false;
        this.Hard = false;
        this.minEle = 0;
        this.maxEle = 2500;
        this.minTime = 0;
        this.maxTime = 5;
        this.searchBar = "";
    }
    public SearchAttributes(boolean Biking, boolean Views, boolean River, boolean History, boolean Forest,
                            boolean Lake, int minLen, int maxLen, boolean Easy,
                            boolean Moderate, boolean Hard, int minEle, int maxEle, double minTime,
                            double maxTime, String searchBar){
        this.Biking = Biking;
        this.Views = Views;
        this.River = River;
        this.History = History;
        this.Forest = Forest;
        this.Lake = Lake;
        this.minLen = minLen;
        this.maxLen = maxLen;
        this.Easy = Easy;
        this.Moderate = Moderate;
        this.Hard = Hard;
        this.minEle = minEle;
        this.maxEle = maxEle;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.searchBar = searchBar;
    }
    @NonNull
    @Override
    public String toString(){
        return "" + Biking + Views + River + History + Forest + Lake + minLen + maxLen + Easy + Moderate + Hard + minEle + maxEle + minTime + maxTime + searchBar;
    }

    public void clear(){
        this.Biking = false;
        this.Views = false;
        this.River = false;
        this.History = false;
        this.Forest = false;
        this.Lake = false;
        this.minLen = 0;
        this.maxLen = 15;
        this.Easy = false;
        this.Moderate = false;
        this.Hard = false;
        this.minEle = 0;
        this.maxEle = 2500;
        this.minTime = 0;
        this.maxTime = 5;
    }
    public void setAll(boolean Biking, boolean Views, boolean River, boolean History, boolean Forest,
                       boolean Lake, int minLen, int maxLen, boolean Easy,
                       boolean Moderate, boolean Hard, int minEle, int maxEle, double minTime,
                       double maxTime){
        this.Biking = Biking;
        this.Views = Views;
        this.River = River;
        this.History = History;
        this.Forest = Forest;
        this.Lake = Lake;
        this.minLen = minLen;
        this.maxLen = maxLen;
        this.Easy = Easy;
        this.Moderate = Moderate;
        this.Hard = Hard;
        this.minEle = minEle;
        this.maxEle = maxEle;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }
    //getter functions
    public String getMinEle(){
        return "" + minEle;
    }
    public String getMaxEle(){
        return "" + maxEle;
    }
    public String getMinTime(){
        return "" + minTime;
    }
    public String getMaxTime(){
        return "" + maxTime;
    }
    public boolean getBiking(){
        return Biking;
    }
    public boolean getViews(){
        return Views;
    }
    public boolean getHistory(){
        return History;
    }
    public boolean getRiver(){
        return River;
    }
    public boolean getLake(){
        return Lake;
    }
    public boolean getForest(){
        return Forest;
    }
    public boolean getEasy(){
        return Easy;
    }
    public boolean getModerate(){
        return Moderate;
    }
    public boolean getHard(){
        return Hard;
    }

    public void clearQuery(){
        this.searchBar = "";
    }

    public void setQuery(String Query){
        this.searchBar = Query;
    }
}
