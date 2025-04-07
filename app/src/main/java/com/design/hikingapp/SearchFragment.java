package com.design.hikingapp;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private TextView resultsCount;
    private RecyclerView recyclerView;
    private TrailAdapter trailAdapter;
    private List<Trail> trailList;
    private ImageButton filtersButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        resultsCount = rootView.findViewById(R.id.results_count);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        filtersButton = rootView.findViewById(R.id.filterButton);

        // Initialize the trail list and adapter
        trailList = new ArrayList<>();
        trailAdapter = new TrailAdapter(trailList);

        // Set up the RecyclerView with the adapter and layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(trailAdapter);

        // Add sample data to the list (replace with addTrail when ready)
        loadSampleData();
        addTrail("Trailalalala", 0, 10.0, "Moderate", 6, 3, 5);

        filtersButton.setOnClickListener(v -> {
            filtersButton.setImageResource(R.drawable.filter_button_clicked);
        });

        return rootView;
    }

    private void addTrail(String name, int image, double length, String difficulty, int estTimeHr, int estTimeMin, int id) {
        //Replace sample image with trail image when possible
        String trailDetails = length + " mi  |  " + difficulty + "  |  Est. " + estTimeHr + " hr " + estTimeMin + " min";
        trailList.add(new Trail(R.drawable.sample_trail_image, name, trailDetails, id));

        //Update results text
        String results = trailList.size() + " results";
        resultsCount.setText(results);
        trailAdapter.notifyDataSetChanged();
    }

    // Example method to load data into the RecyclerView
    private void loadSampleData() {
        trailList.add(new Trail(R.drawable.sample_trail_image, "Trail 1", "12.7 mi  |  Hard  |  Est. 6 hr 7 min", 1));
        trailList.add(new Trail(R.drawable.sample_trail_image, "Trail 2", "5.2 mi  |  Moderate  |  Est. 3 hr 4 min", 2));
        trailList.add(new Trail(R.drawable.sample_trail_image, "Trail 2", "5.2 mi  |  Moderate  |  Est. 3 hr 4 min", 3));
        trailList.add(new Trail(R.drawable.sample_trail_image, "Trail 2", "5.2 mi  |  Moderate  |  Est. 3 hr 4 min", 4));
        trailAdapter.notifyDataSetChanged();
    }
}
