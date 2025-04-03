package com.design.hikingapp;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchFragment extends Fragment {
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Transition fade = new Fade();
//        fade.setDuration(1000); // You can adjust duration as needed
//
//        setEnterTransition(fade);
//        setExitTransition(fade);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }
}
