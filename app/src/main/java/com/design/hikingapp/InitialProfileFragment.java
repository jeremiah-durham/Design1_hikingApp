package com.design.hikingapp;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.design.hikingapp.user.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitialProfileFragment extends Fragment {

    private String name = "0'";
    private String heightFeet = "0'";
    private String heightInches = "0\"";
    private String weight = "140";
    private String emergencyEmail;

    private final static String EMAIL_PATTERN = "^[_A-Za-z0-9'!#$%^&*`{|}+=?~-]+(\\.[_A-Za-z0-9'!#$%^&*`{|}+=?~-]+)*@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,}$";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Spinner feetDropdown;
        Spinner inchesDropdown;
        EditText nameText;
        EditText weightText;
        EditText emergencyEmailText;
        ImageButton nextButton;
        MainActivity mainActivity = (MainActivity) requireActivity();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_initial_profile, container, false);

        // Find the spinner in the layout
        nameText = rootView.findViewById(R.id.nameText);
        feetDropdown = rootView.findViewById(R.id.feetDropdown);
        inchesDropdown = rootView.findViewById(R.id.inchesDropdown);
        weightText = rootView.findViewById(R.id.editTextText);
        emergencyEmailText = rootView.findViewById(R.id.editTextText2);
        nextButton = rootView.findViewById(R.id.imageButton);


        // add text validators to name, weight and eemail
        nameText.addTextChangedListener(new TextValidator(nameText) {
            @Override
            public void validate(TextView textView, String text) {
                if(text.isEmpty()) {
                    textView.setError("Name cannot be empty");
                }
            }
        });

        weightText.addTextChangedListener(new TextValidator(weightText) {
            @Override
            public void validate(TextView textView, String text) {
                if(text.isEmpty()) {
                    textView.setError("Weight cannot be empty");
                }
            }
        });

        emergencyEmailText.addTextChangedListener(new TextValidator(emergencyEmailText) {
            final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            @Override
            public void validate(TextView textView, String text) {
                Matcher matcher = pattern.matcher(text);
                if (!matcher.find()) {
                   textView.setError("Invalid Email Address");
                }
            }
        });


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
            name = nameText.getText().toString();
            nextButton.setImageResource(R.drawable.next_clicked);

            if(weight.isEmpty()) {
                weightText.setError("Weight cannot be empty");
            }
            if(emergencyEmail.isEmpty()) {
                emergencyEmailText.setError("Email cannot be empty");
            }

            if(weightText.getError() == null && emergencyEmailText.getError() == null && nameText.getError() == null) {
                // Create user info

                UserRepository inst = UserRepository.getInstance();
                inst.createUser(getName(), getEmergencyEmail(), getWeight(), getHeightFeet()*12+getHeightInches());

                //Load next page
                mainActivity.loadFragment(new SearchFragment());
            }
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

    public String getName() {
        return name;
    }

    private abstract class TextValidator implements TextWatcher {
        private final TextView textView;

        public TextValidator(TextView textView) {
            this.textView = textView;
        }

        public abstract void validate(TextView textView, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = textView.getText().toString();
            validate(textView, text);
        }
        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}
