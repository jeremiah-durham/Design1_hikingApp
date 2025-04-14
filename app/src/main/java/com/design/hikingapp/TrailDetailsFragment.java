package com.design.hikingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.design.hikingapp.weather.WeatherDataParser;

import java.util.ArrayList;
import java.util.List;

public class TrailDetailsFragment extends Fragment {

    private Trail trail;

    private ImageButton downloadButton;
    private boolean downloadButtonState = false;

    private ImageView trailImage;
    private ScrollView scrollView;

    private TextView name;
    private TextView difficulty;
    private TextView distance;
    private TextView elevation;
    private TextView timeHrs;
    private TextView timeMins;
    private TextView waterNeeded;
    private TextView calsBurned;

    private TextView currentTemperature;
    private TextView currentFeelsLike;
    private ImageView weatherBackground;

    private RecyclerView weatherRecyclerView;
    private RecyclerView precipRecyclerView;
    private RecyclerView windRecyclerView;

    private RecyclerView packingRecyclerView;

    private WeatherDataParser weatherDataParser = new WeatherDataParser();
    private PersonalizedCalculations calculations;

    public TrailDetailsFragment(Trail trail) {
        this.trail = trail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trail_details, container, false);

        initializeViews(view);

        //Populate page with selected trail data
        populateTrailData();

        //Populate weather data from API
        populateWeatherData(weatherDataParser.getWeatherData());

        downloadButton.setOnClickListener(v -> {
            if (downloadButtonState == false) {
                downloadButtonState = true;
                downloadButton.setImageResource(R.drawable.download_button_clicked);
            }
        });

        // Set scroll listener for parallax effect
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Apply parallax effect: move image slower than scroll
                float parallaxFactor = scrollY * 0.5f; // Adjust factor as necessary
                trailImage.setTranslationY(parallaxFactor);
            }
        });

        return view;
    }

    private void initializeViews(View view) {
        // Initialize views, linking them to their XML
        scrollView = view.findViewById(R.id.scrollView);

        downloadButton = view.findViewById(R.id.downloadButton);

        trailImage = view.findViewById(R.id.trailImage);
        name = view.findViewById(R.id.trailNameText);
        difficulty = view.findViewById(R.id.trailDifficulty);
        distance = view.findViewById(R.id.milesValue);
        elevation = view.findViewById(R.id.feetValue);
        timeHrs = view.findViewById(R.id.timeHrsValue);
        timeMins = view.findViewById(R.id.timeMinsValue);
        waterNeeded = view.findViewById(R.id.litersValue);
        calsBurned = view.findViewById(R.id.calsValue);

        packingRecyclerView = view.findViewById(R.id.packingListRecycler);
        packingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Vertical list

        //Initialize current weather
        currentTemperature = view.findViewById(R.id.degreesValue);
        currentFeelsLike = view.findViewById(R.id.feelsLikeValue);
        weatherBackground = view.findViewById(R.id.weatherBackgroundImage);

        //Initialize RecyclerViews for weather display
        weatherRecyclerView = view.findViewById(R.id.weatherRecyclerView);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        precipRecyclerView = view.findViewById(R.id.precipRecyclerView);
        precipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        windRecyclerView = view.findViewById(R.id.windRecyclerView);
        windRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void populateTrailData() {
        trailImage.setImageResource(trail.getImageResource());
        name.setText(trail.getName());
        difficulty.setText(trail.getDifficulty());
        distance.setText("" + trail.getDistance());
        elevation.setText("" + trail.getElevation());
        timeHrs.setText("" + trail.getTimeHrs());
        timeMins.setText("" + trail.getTimeMins());
    }

    public void populatePersonalData(UserHikeStats userHikeStats) {
        waterNeeded.setText("" + userHikeStats.getWaterNeeded());
        calsBurned.setText("" + userHikeStats.getCaloriesBurned());
    }

    public void populateWeatherData(WeatherData weatherData) {
        currentTemperature.setText(weatherData.getCurrentTemp() + "°");
        currentFeelsLike.setText("feels like " + weatherData.getCurrentFeelsLike());
        weatherBackground.setImageResource(weatherData.getWeatherBackground());

        TemperatureAdapter weatherAdapter = new TemperatureAdapter(weatherData.getHourlyTemperatures());
        weatherRecyclerView.setAdapter(weatherAdapter);

        PrecipAdapter precipAdapter = new PrecipAdapter(weatherData.getHourlyPrecipitations());
        precipRecyclerView.setAdapter(precipAdapter);

        WindAdapter adapter = new WindAdapter(weatherData.getHourlyWinds());
        windRecyclerView.setAdapter(adapter);

        calculations = new PersonalizedCalculations(trail, weatherData, 140);

        populatePackingList(calculations.getPackingList());
    }

    public void populatePackingList(List<PackingItem> packingList) {
        PackingListAdapter packingAdapter = new PackingListAdapter(packingList);
        packingRecyclerView.setAdapter(packingAdapter);
    }

    public boolean getDownloadButtonState() {
        return downloadButtonState;
    }

    public Trail getSavedTrail() {
        return trail;
    }
}