package com.design.hikingapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.design.hikingapp.trail.Trail;
import com.design.hikingapp.trail.TrailRepository;
import com.design.hikingapp.user.UserRepository;
import com.design.hikingapp.util.RepositoryCallback;
import com.design.hikingapp.util.Result;
import com.design.hikingapp.weather.WeatherRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SavedTrailDetailsFragment extends Fragment {

    private final Trail trail;

    private ImageView trailImage;
    private ScrollView scrollView;
    private ImageButton startButton;
    private TextView remTime;

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
    private ImageView weatherIcon;

    private RecyclerView weatherRecyclerView;
    private RecyclerView precipRecyclerView;
    private RecyclerView windRecyclerView;

    private RecyclerView packingRecyclerView;
    private PersonalizedCalculations calculations;

    private boolean activeState = false;

    private final WeatherRepository weatherRepository;

    private Thread timeThread;

    public SavedTrailDetailsFragment(Trail trail, WeatherRepository weatherRepository) {
        this.trail = trail;
        this.weatherRepository = weatherRepository;
    }

    private Thread createTimeThread(View view) {
        return new Thread(new Runnable() {
            public long delTime;

            @Override
            public void run() {
                // wait for the view to start working
                for(int i = 0; i < 10; i++) {
                    if(view.isShown())
                        break;
                    try {Thread.sleep(100);} catch (Exception e) {}
                    if(i == 9) return;
                }
                long endMillis = TrailRepository.getInstance().getActiveTrail().getEndDate().getTime();
                while(activeState) {
                    delTime = endMillis - Calendar.getInstance().getTimeInMillis();
                    if(!view.isShown()) {
                        break;
                    }
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            remTime.setText("T-"+(delTime / (3600 * 1000)) + "h " + (delTime / (60 * 1000))%60 + "m " + (delTime / 1000)%60 + "s" );
                        }
                    });

                    if(!activeState)
                        break;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("TIME THREAD", "",e);
                        return;
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_trail_details, container, false);

        initializeViews(view);

        this.timeThread = createTimeThread(view);

        //Populate page with selected trail data
        populateTrailData();

        // TODO: logic for checking if the hike is active or not
        // if(hike is active) set button to stop and start time remaining updates
        if (trail.isActive()) {
            // set initial active state and image resource
            activeState = true;
            startButton.setImageResource(R.drawable.stop_button);

            // start updates for trails??
            this.timeThread.start();
        }

        startButton.setOnClickListener(v -> {
            if (activeState) {
                // deactivate hike
                TrailRepository.getInstance().setTrailInactive();
                // just assume that the stop worked
                startButton.setImageResource(R.drawable.start_button);
                activeState = !activeState;
                try {
                    this.timeThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                remTime.setText("Trail not started");
            } else {
                // activate hike
                TrailRepository.getInstance().setTrailActive(trail);
                if(TrailRepository.getInstance().isTrailActive()) {
                    startButton.setImageResource(R.drawable.stop_button);
                    activeState = !activeState;
                    if(this.timeThread.getState() == Thread.State.TERMINATED)
                        this.timeThread = createTimeThread(view);
                    this.timeThread.start();
                }
            }
        });

        double lat = trail.getLat();
        double lon = trail.getLon();

        //Populate weather data from API
        weatherRepository.fetchWeatherData(lat, lon, new RepositoryCallback<WeatherData>() {
            @Override
            public void onComplete(Result<WeatherData> result) {
                if (result instanceof Result.Success) {
                    view.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            populateWeatherData(((Result.Success<WeatherData>) result).data);
                        }});
                } else {
                    Log.e("Trail Details Fragment", "An error occurred fetching weather data",((Result.Error<WeatherData>) result).exception);
                }
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


        trailImage = view.findViewById(R.id.trailImage);
        name = view.findViewById(R.id.trailNameText);
        difficulty = view.findViewById(R.id.trailDifficulty);
        distance = view.findViewById(R.id.milesValue);
        elevation = view.findViewById(R.id.feetValue);
        timeHrs = view.findViewById(R.id.timeHrsValue);
        timeMins = view.findViewById(R.id.timeMinsValue);
        waterNeeded = view.findViewById(R.id.litersValue);
        calsBurned = view.findViewById(R.id.calsValue);

        startButton = view.findViewById(R.id.startButton);
        remTime = view.findViewById(R.id.remTime);

        packingRecyclerView = view.findViewById(R.id.packingListRecycler);
        packingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Vertical list

        //Initialize current weather
        currentTemperature = view.findViewById(R.id.degreesValue);
        currentFeelsLike = view.findViewById(R.id.feelsLikeValue);
        weatherBackground = view.findViewById(R.id.weatherBackgroundImage);
        weatherIcon = view.findViewById(R.id.weatherIcon);

        //Initialize RecyclerViews for weather display
        weatherRecyclerView = view.findViewById(R.id.weatherRecyclerView);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        precipRecyclerView = view.findViewById(R.id.precipRecyclerView);
        precipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        windRecyclerView = view.findViewById(R.id.windRecyclerView);
        windRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void populateTrailData() {
        Bitmap bmp = null;
        if((bmp = trail.getImgBmp()) == null) {
            trailImage.setImageResource(trail.getImageResource());
        } else {
            trailImage.setImageBitmap(bmp);
        }
        name.setText(trail.getName());
        difficulty.setText(trail.getDifficulty());
        distance.setText("" + trail.getDistance());
        elevation.setText("" + trail.getElevation());
        timeHrs.setText("" + trail.getTimeHrs());
        timeMins.setText("" + trail.getTimeMins());
    }

    public void populatePersonalData(PersonalizedCalculations calculations) {
        waterNeeded.setText(calculations.getWaterNeeded() + " oz");
        calsBurned.setText("" + calculations.getCaloriesBurned() + " cal");
    }

    public void populateWeatherData(WeatherData weatherData) {
        currentTemperature.setText(weatherData.getCurrentTemp() + "°");
        currentFeelsLike.setText("feels like " + weatherData.getCurrentFeelsLike());

        weatherBackground.setImageResource(weatherData.getWeatherBackground());


        weatherIcon.setImageResource(weatherData.getWeatherIcon());

        TemperatureAdapter weatherAdapter = new TemperatureAdapter(weatherData.getHourlyTemperatures());
        weatherRecyclerView.setAdapter(weatherAdapter);

        PrecipAdapter precipAdapter = new PrecipAdapter(weatherData.getHourlyPrecipitations());
        precipRecyclerView.setAdapter(precipAdapter);

        WindAdapter adapter = new WindAdapter(weatherData.getHourlyWinds());
        windRecyclerView.setAdapter(adapter);

        calculations = new PersonalizedCalculations(trail, weatherData, UserRepository.getInstance().getUser().getWeight());

        populatePersonalData(calculations);
        populatePackingList(calculations.getPackingList());
    }

    public void populatePackingList(List<PackingItem> packingList) {
        PackingListAdapter packingAdapter = new PackingListAdapter(packingList);
        packingRecyclerView.setAdapter(packingAdapter);
    }


    public Trail getSavedTrail() {
        return trail;
    }
}