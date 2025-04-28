package com.design.hikingapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.design.hikingapp.user.User;
import com.design.hikingapp.user.UserRepository;

public class ProfileFragment extends Fragment {
    private TextView nameText;
    private TextView heightText;
    private TextView weightText;
    private TextView emergencyContactText;

    private ImageButton editButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameText = view.findViewById(R.id.profileNameText);
        heightText = view.findViewById(R.id.profileHeightText);
        weightText = view.findViewById(R.id.profileWeightText);
        emergencyContactText = view.findViewById(R.id.profileEmergencyText);
        editButton = view.findViewById(R.id.editButton);

        User user = UserRepository.getInstance().getUser();

        setProfile(user.getName(), user.getHeight(), user.getWeight(), user.getEemail());

        editButton.setOnClickListener(v -> {
            DebugFragment debugFragment = new DebugFragment();
            // Assuming you're in an Activity context, you can load the fragment:
            FragmentActivity activity = (FragmentActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.fragment_pop_in,  // Entering fragment animation
                            R.anim.fragment_pop_out, // Exiting fragment animation
                            R.anim.fragment_pop_in,  // Pop-back enter animation
                            R.anim.fragment_pop_out  // Pop-back exit animation
                    )
                    .replace(R.id.fragment_container, debugFragment) // Replace with the container ID of your fragment
                    .addToBackStack(null) // Add to the back stack to allow back navigation
                    .commit();
        });

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
