package com.design.hikingapp;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WeatherPageAdapter extends FragmentStateAdapter {

    public WeatherPageAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TemperatureFragment();  // A fragment displaying temperature data
            case 1:
                return new PrecipitationFragment(); // A fragment displaying precipitation data
            case 2:
                return new WindFragment(); // A fragment displaying wind data
            default:
                return new TemperatureFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // There are 3 tabs
    }
}
