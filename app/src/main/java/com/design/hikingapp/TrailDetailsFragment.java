package com.design.hikingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TrailDetailsFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trail_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        trailImage = view.findViewById(R.id.trailImage);
        scrollView = view.findViewById(R.id.scrollView);
        name = view.findViewById(R.id.trailNameText);
        difficulty = view.findViewById(R.id.trailDifficulty);
        distance = view.findViewById(R.id.milesValue);
        elevation = view.findViewById(R.id.feetValue);
        timeHrs = view.findViewById(R.id.timeHrsValue);
        timeMins = view.findViewById(R.id.timeMinsValue);
        waterNeeded = view.findViewById(R.id.litersValue);
        calsBurned = view.findViewById(R.id.calsValue);

        String text = "hi";
        difficulty.setText(text);

        // Set scroll listener for parallax effect
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Apply parallax effect: move image slower than scroll
                float parallaxFactor = scrollY * 0.5f; // Adjust factor as necessary
                trailImage.setTranslationY(parallaxFactor);
            }
        });
    }
}