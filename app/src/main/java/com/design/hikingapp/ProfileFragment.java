package com.design.hikingapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    private TextView nameText;
    private TextView heightText;
    private TextView weightText;
    private TextView emergencyContactText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameText = view.findViewById(R.id.profileNameText);
        heightText = view.findViewById(R.id.profileHeightText);
        weightText = view.findViewById(R.id.profileWeightText);
        emergencyContactText = view.findViewById(R.id.profileEmergencyText);

        setProfile("Example Name", 63, 100, "example@gmail.com");

        return view;
    }

    public void setProfile(String name, int height, int weight, String emergencyEmail) {
        nameText.setText(name);

        int heightFeet = height/12;
        int heightInches = height%12;
        heightText.setText(heightFeet + "' " + heightInches + "\"");

        weightText.setText(weight + " lbs");

        emergencyContactText.setText(emergencyEmail);
    }
}
