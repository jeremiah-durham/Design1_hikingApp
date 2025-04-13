package com.design.hikingapp;

import androidx.fragment.app.Fragment;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.design.hikingapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;

    //this is a comment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initialize bottom navbar
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Load intial profile gathering
        loadFragment(new InitialProfileFragment());
        //Disable navbar for this page
        bottomNavigationView.setVisibility(BottomNavigationView.GONE);

        // Set up fragment transaction for item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.search) {
                selectedFragment = new SearchFragment();
            } else if (item.getItemId() == R.id.saved) {
                selectedFragment = new SavedFragment();
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

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }
}