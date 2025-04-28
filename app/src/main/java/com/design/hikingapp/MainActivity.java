package com.design.hikingapp;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.design.hikingapp.backend.BackendRepository;
import com.design.hikingapp.backend.TrailResponseParser;
import com.design.hikingapp.backendip.BackendIpRepository;
import com.design.hikingapp.trail.TrailRepository;
import com.design.hikingapp.user.UserRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import com.design.hikingapp.databinding.ActivityMainBinding;
import com.design.hikingapp.weather.WeatherDataParser;
import com.design.hikingapp.weather.WeatherRepository;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    //this is a comment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackendIpRepository.initRepo(getFilesDir());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = binding.bottomNavView;
        bottomNavigationView.setVisibility(BottomNavigationView.GONE);


        if(!BackendIpRepository.getInstance().loadIp()) {
            // Loading the ip failed, so we clearly need to pull up the debug menu and get everything sorted
            loadFragment(new DebugFragment());
            bottomNavigationView.setVisibility(BottomNavigationView.GONE);
            return;
        } else {
            BackendRepository.setIP(BackendIpRepository.getInstance().getBip());
        }


        WeatherRepository.initRepo(new WeatherDataParser(), Executors.newSingleThreadScheduledExecutor());
        BackendRepository.initRepo(new TrailResponseParser(), Executors.newSingleThreadScheduledExecutor());
        UserRepository.initRepo(getFilesDir());
        TrailRepository.initRepo(getFilesDir());

        // try loading user profile
        if(!UserRepository.getInstance().loadUser()) {
            // if loading failed, then create new user

            // Load initial screen
            loadFragment(new InitialProfileFragment());
            //Disable navbar for this page
            bottomNavigationView.setVisibility(BottomNavigationView.GONE);
        } else {
            // load search screen
            loadFragment(new SearchFragment());
        }

        // Set up fragment transaction for item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.search) {
                selectedFragment = new SearchFragment();
            } else if (item.getItemId() == R.id.saved) {
                if(TrailRepository.getInstance().isTrailActive()) {
                    selectedFragment = new SavedTrailDetailsFragment(TrailRepository.getInstance().getActiveTrail(), WeatherRepository.getInstance());
                } else {
                    selectedFragment = new SavedFragment();
                }
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new ProfileFragment();
            }
            // Replace the fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.fragment_pop_in,  // Entering fragment animation
                            R.anim.fragment_pop_out, // Exiting fragment animation
                            R.anim.fragment_pop_in,  // Pop-back enter animation
                            R.anim.fragment_pop_out  // Pop-back exit animation
                    )
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        });
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_pop_in,  // Entering fragment animation
                        R.anim.fragment_pop_out, // Exiting fragment animation
                        R.anim.fragment_pop_in,  // Pop-back enter animation
                        R.anim.fragment_pop_out  // Pop-back exit animation
                )
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // Enables back navigation
                .commit();
        bottomNavigationView.setVisibility(BottomNavigationView.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(BottomNavigationView.LABEL_VISIBILITY_UNLABELED);
    }
}