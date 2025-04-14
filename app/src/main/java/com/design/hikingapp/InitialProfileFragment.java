package com.design.hikingapp;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.transition.Fade;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class InitialProfileFragment extends Fragment {

    private String heightFeet = "0'";
    private String heightInches = "0\"";
    private String weight = "140";
    private String emergencyEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Spinner feetDropdown;
        Spinner inchesDropdown;
        EditText weightText;
        EditText emergencyEmailText;
        ImageButton nextButton;
        MainActivity mainActivity = (MainActivity) requireActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_initial_profile, container, false);

        // Find the spinner in the layout
        feetDropdown = rootView.findViewById(R.id.feetDropdown);
        inchesDropdown = rootView.findViewById(R.id.inchesDropdown);
        weightText = rootView.findViewById(R.id.editTextText);
        emergencyEmailText = rootView.findViewById(R.id.editTextText2);
        nextButton = rootView.findViewById(R.id.imageButton);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.feet, R.layout.spinner_item);
        ArrayAdapter<CharSequence> inchesAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.inches, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        feetAdapter.setDropDownViewResource(R.layout.spinner_item);
        inchesAdapter.setDropDownViewResource(R.layout.spinner_item);

        // Apply the adapter to the spinner
        feetDropdown.setAdapter(feetAdapter);
        inchesDropdown.setAdapter(inchesAdapter);

        // Set up a listener to detect when an item is selected
        feetDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                heightFeet = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                heightFeet = "5'";
            }
        });

        inchesDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                heightInches = parentView.getItemAtPosition(position).toString();
                System.out.println(emergencyEmail);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                heightInches = "7\"";
            }
        });

        nextButton.setOnClickListener(v -> {
            //Update weight and emergency email
            weight = weightText.getText().toString();
            emergencyEmail = emergencyEmailText.getText().toString();
            nextButton.setImageResource(R.drawable.next_clicked);

            //Load next page
            mainActivity.loadFragment(new SearchFragment());
        });

        return rootView;
    }

    // Getter methods for private variables
    public int getHeightFeet() {
        String modifiedHeightString = heightFeet.replace("'", "");
        return Integer.parseInt(modifiedHeightString);
    }
    public int getHeightInches() {
        String modifiedHeightString = heightInches.replace("\"", "");
        return Integer.parseInt(modifiedHeightString);
    }
    public int getWeight() {
        return Integer.parseInt(weight);
    }
    public String getEmergencyEmail() {
        return emergencyEmail;
    }
}
