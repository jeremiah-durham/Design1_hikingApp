package com.design.hikingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.design.hikingapp.backend.BackendRepository;
import com.design.hikingapp.trail.Trail;
import com.design.hikingapp.util.Result;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private TextView resultsCount;
    private RecyclerView recyclerView;
    private TrailAdapter trailAdapter;
    private List<Trail> trailList;
    private ImageButton filtersButton;
    private EditText searchBar;

    //Filters tab buttons
    private DrawerLayout drawerLayout;
    private ImageButton closeButton;

    //Toggle buttons
    private ImageButton bikingButton, mountainButton, riverButton, historicButton, forestButton, lakeButton;
    final boolean[] bikingButtonState = {false}, mountainButtonState = {false}, riverButtonState = {false}, historicButtonState = {false}, forestButtonState = {false}, lakeButtonState = {false};

    //Sliders and checkboxes
    private RangeSlider lengthSlider, elevationSlider, timeSlider;
    private TextView lengthRange, elevationRange, timeRange;
    private AppCompatCheckBox easyCheckbox, moderateCheckbox, hardCheckbox;
    private String query;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //Initialize views and link them to their XML
        initializeViews(view);

        //Initialize range slider
        initializeLengthSlider();
        initializeElevationSlider();
        initializeTimeSlider();

        // Initialize the trail list and adapter
        trailList = new ArrayList<>();
        trailAdapter = new TrailAdapter(trailList);
        BackendRepository repo = BackendRepository.getInstance();
        repo.fetchTrailList(null, (result) -> {
            view.post(()->{clearTrails();});
            if(result instanceof Result.Success) {
                Log.d("Search Frag", "Got success result");
                if(((Result.Success<List<Trail>>) result).data != null) {
                    List<Trail> trails = ((Result.Success<List<Trail>>) result).data;
                    for (int i = 0; i < trails.size(); i++) {
                        if(repo.fetchSemRequested())
                            break;
                        Trail trail = trails.get(i);
                        Bitmap bmp = repo.loadTrailBmp(trail.getId());
                        if(bmp != null)
                            trail.setImgBmp(bmp);
                        view.post(() -> {
                            addTrail(trail);
                            updateResultsCount();
                        });
                    }
                }
            } else {
                Log.e("Search Frag", "Got error result", ((Result.Error<List<Trail>>) result).exception);
            }
        });

        SearchAttributes myList = new SearchAttributes();
        //Set click listener for closing the filters tab
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                int minLen = Math.round(lengthSlider.getValues().get(0));
                int maxLen = Math.round(lengthSlider.getValues().get(1));
                int minEle = Math.round(elevationSlider.getValues().get(0));
                int maxEle = Math.round(elevationSlider.getValues().get(1));
                double minTime = timeSlider.getValues().get(0);
                double maxTime = timeSlider.getValues().get(1);
                myList.clear();
                myList.setAll(bikingButtonState[0], mountainButtonState[0], riverButtonState[0], historicButtonState[0], forestButtonState[0], lakeButtonState[0],
                        minLen, maxLen, easyCheckbox.isChecked(), moderateCheckbox.isChecked(), hardCheckbox.isChecked(), minEle, maxEle, minTime, maxTime);

                repo.fetchTrailList(myList, (result) -> {
                    view.post(()->{clearTrails();});
                    if(result instanceof Result.Success) {
                        Log.d("Search Frag", "Got success result");
                        if(((Result.Success<List<Trail>>) result).data != null) {
                            List<Trail> trails = ((Result.Success<List<Trail>>) result).data;
                            for (int i = 0; i < trails.size(); i++){
                                if(repo.fetchSemRequested())
                                    break;
                                Trail trail = trails.get(i);
                                Bitmap bmp = repo.loadTrailBmp(trail.getId());
                                if(bmp != null)
                                    trail.setImgBmp(bmp);
                                view.post(() -> {
                                    addTrail(trail);
                                    updateResultsCount();
                                });
                            }
                        }
                    } else {
                        Log.e("Search Frag", "Got error result", ((Result.Error<List<Trail>>) result).exception);
                    }
                });

                updateResultsCount();

            }
        });

        //Set focus listener for search bar to determine when the user has hit enter or leaves focus
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    query = searchBar.getText().toString();
                    myList.setQuery(query);

                    repo.fetchTrailList(myList, (result) -> {
                        view.post(() -> {clearTrails();});
                        if(result instanceof Result.Success) {
                            Log.d("Search Frag", "Got success result");
                            if(((Result.Success<List<Trail>>) result).data != null) {
                                List<Trail> trails = ((Result.Success<List<Trail>>) result).data;
                                for (int i = 0; i < trails.size(); i++){
                                    if(repo.fetchSemRequested())
                                        break;
                                    Trail trail = trails.get(i);
                                    Bitmap bmp = repo.loadTrailBmp(trail.getId());
                                    if(bmp != null)
                                        trail.setImgBmp(bmp);
                                    view.post(() -> {
                                        addTrail(trail);
                                        updateResultsCount();
                                    });
                                }
                            }
                        } else {
                            Log.e("Search Frag", "Got error result", ((Result.Error<List<Trail>>) result).exception);
                        }
                    });
                    updateResultsCount();
                }
            }
        });


        //Set click listeners for sliders, updating text and values
        lengthSlider.addOnChangeListener((slider, value, fromUser) -> {
            updateLengthSlider(slider);
        });

        elevationSlider.addOnChangeListener((slider, value, fromUser) -> {
            updateElevationSlider(slider);
        });

        timeSlider.addOnChangeListener((slider, value, fromUser) -> {
            updateTimeSlider(slider);
        });


        // Set up the RecyclerView with the adapter and layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(trailAdapter);

        filtersButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        });

        return view;
    }

    private void initializeViews(View view) {
        resultsCount = view.findViewById(R.id.results_count);
        recyclerView = view.findViewById(R.id.recyclerView);
        drawerLayout = view.findViewById(R.id.searchDrawerLayout);
        searchBar = view.findViewById(R.id.searchBar);

        //Link sliders and their text outputs
        lengthSlider = view.findViewById(R.id.lengthSlider);
        elevationSlider = view.findViewById(R.id.elevationSlider);
        timeSlider = view.findViewById(R.id.timeSlider);
        lengthRange = view.findViewById(R.id.milesRange);
        elevationRange = view.findViewById(R.id.elevationRange);
        timeRange = view.findViewById(R.id.timeRange);

        //Link checkboxes
        easyCheckbox = view.findViewById(R.id.easyCheck);
        moderateCheckbox = view.findViewById(R.id.moderateCheck);
        hardCheckbox = view.findViewById(R.id.hardCheck);

        //Link nav buttons
        filtersButton = view.findViewById(R.id.filterButton);
        closeButton = view.findViewById(R.id.closeButton);

        //Link filter buttons
        bikingButton = view.findViewById(R.id.bikingButton);
        mountainButton = view.findViewById(R.id.mountainButton);
        riverButton = view.findViewById(R.id.riverButton);
        historicButton = view.findViewById(R.id.historicButton);
        forestButton = view.findViewById(R.id.forestButton);
        lakeButton = view.findViewById(R.id.lakeButton);

        //Initialize click listeners for those buttons
        initializeClickButton(bikingButton, R.drawable.filter_biking, R.drawable.filter_biking_clicked, bikingButtonState);
        initializeClickButton(mountainButton, R.drawable.filter_mountain, R.drawable.filter_mountain_clicked, mountainButtonState);
        initializeClickButton(riverButton, R.drawable.filter_river, R.drawable.filter_river_clicked, riverButtonState);
        initializeClickButton(historicButton, R.drawable.filter_historic, R.drawable.filter_historic_clicked, historicButtonState);
        initializeClickButton(forestButton, R.drawable.filter_forest, R.drawable.filter_forest_clicked, forestButtonState);
        initializeClickButton(lakeButton, R.drawable.filter_lake, R.drawable.filter_lake_clicked, lakeButtonState);
    }

    private void initializeLengthSlider() {
        List<Float> initialValues = new ArrayList<>();
        initialValues.add(0f);
        initialValues.add(15f);
        lengthSlider.setCustomThumbDrawable(R.drawable.slider_handle);
        lengthSlider.setThumbRadius(40);
        lengthSlider.setTickVisible(false);
        lengthSlider.setMinSeparationValue(1f);
        lengthSlider.setTrackActiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.buttonBackground)));
        lengthSlider.setTrackInactiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.textSecondary)));
        lengthSlider.setValues(initialValues);
    }

    private void initializeElevationSlider() {
        List<Float> initialValues = new ArrayList<>();
        initialValues.add(0f);
        initialValues.add(2500f);
        elevationSlider.setCustomThumbDrawable(R.drawable.slider_handle);
        elevationSlider.setThumbRadius(40);
        elevationSlider.setTickVisible(false);
        elevationSlider.setMinSeparationValue(100f);
        elevationSlider.setTrackActiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.buttonBackground)));
        elevationSlider.setTrackInactiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.textSecondary)));
        elevationSlider.setValues(initialValues);
    }

    private void initializeTimeSlider() {
        List<Float> initialValues = new ArrayList<>();
        initialValues.add(0f);
        initialValues.add(5f);
        timeSlider.setCustomThumbDrawable(R.drawable.slider_handle);
        timeSlider.setThumbRadius(40);
        timeSlider.setTickVisible(false);
        timeSlider.setMinSeparationValue(0.5f);
        timeSlider.setTrackActiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.buttonBackground)));
        timeSlider.setTrackInactiveTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.textSecondary)));
        timeSlider.setValues(initialValues);
    }

    private void updateLengthSlider(RangeSlider slider) {
        List<Float> values = slider.getValues();
        String minValue = Math.round(values.get(0)) + "";
        String maxValue = Math.round(values.get(1)) + "";
        if (maxValue.equals("15")) {
            maxValue = "15+";
        }
        lengthRange.setText(minValue + "-" + maxValue + " mi");
    }

    private void updateElevationSlider(RangeSlider slider) {
        List<Float> values = slider.getValues();
        String minValue = Math.round(values.get(0)) + "";
        String maxValue = Math.round(values.get(1)) + "";
        if (maxValue.equals("2500")) {
            maxValue = "2500+";
        }
        elevationRange.setText(minValue + "-" + maxValue + " ft");
    }

    private void updateTimeSlider(RangeSlider slider) {
        List<Float> values = slider.getValues();
        String minValue = values.get(0) + "";
        String maxValue = values.get(1) + "";
        if (maxValue.equals("5.0")) {
            maxValue = "5+";
        }
        timeRange.setText(minValue + "-" + maxValue + " hrs");
    }

    public void batchAddTrail(List<Trail> trails) {
        trailList.addAll(trails);
        updateResultsCount();
    }
    public void addTrail(Trail trail) {
        //Replace sample image with trail image when possible
        trailList.add(trail);

        updateResultsCount();
    }

    public void clearTrails() {
        trailList.clear();
    }

    public void updateResultsCount() {
        //Update results text
        String results = trailList.size() + " results";
        resultsCount.setText(results);
        trailAdapter.notifyDataSetChanged();
    }

    private void initializeClickButton(ImageButton button, int unclickedId, int clickedId, boolean buttonState[]) {
        button.setOnClickListener(v -> {
            buttonState[0] = !buttonState[0];
            button.setImageResource(buttonState[0] ? clickedId : unclickedId);
        });
    }

    /**
     * Getters
     */
    public String getQuery() {
        return query;
    }

    //Slider values
    public List<Float> getLengthSliderValues() {
        return lengthSlider.getValues();
    }
    public List<Float> getElevationSliderValues() {
        return elevationSlider.getValues();
    }
    public List<Float> getTimeSliderValues() {
        return timeSlider.getValues();
    }


    //Checkboxes
    public boolean isEasyChecked() {
        return easyCheckbox.isChecked();
    }
    public boolean isModerateChecked() {
        return moderateCheckbox.isChecked();
    }
    public boolean isHardChecked() {
        return hardCheckbox.isChecked();
    }

    //Toggle buttons
    public boolean isBikingSelected() {
        return bikingButtonState[0];
    }
    public boolean isMountainSelected() {
        return mountainButtonState[0];
    }
    public boolean isRiverSelected() {
        return riverButtonState[0];
    }
    public boolean isHistoricSelected() {
        return historicButtonState[0];
    }
    public boolean isForestSelected() {
        return forestButtonState[0];
    }
    public boolean isLakeSelected() {
        return lakeButtonState[0];
    }
}
