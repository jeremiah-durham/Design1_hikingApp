package com.design.hikingapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.design.hikingapp.trail.Trail;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    private RecyclerView recyclerView;
    private SavedTrailAdapter trailAdapter;
    private List<Trail> trailList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_saved, container, false);

        recyclerView = view.findViewById(R.id.savedTrailsRecycler);

        // Initialize the trail list and adapter
        trailList = new ArrayList<>();
        trailAdapter = new SavedTrailAdapter(trailList);

        // Set up the RecyclerView with the adapter and layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(trailAdapter);

        // Add sample data to the list (replace with addTrail when ready)
        addTrail(new Trail(R.drawable.sample_trail_image, R.drawable.map_bg, "Upper Mule Deer Trail to Windy Saddle Overpass with more useless text", 10.0, "Moderate", 2000, 6, 3, 1));

        return view;
    }


    public void addTrail(Trail trail) {
        //Replace sample image with trail image when possible
        trailList.add(trail);
    }
}
