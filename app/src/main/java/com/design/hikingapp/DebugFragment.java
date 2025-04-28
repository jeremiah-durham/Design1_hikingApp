package com.design.hikingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.design.hikingapp.backendip.BackendIp;
import com.design.hikingapp.backendip.BackendIpRepository;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebugFragment extends Fragment {

    private EditText ipField;
    private Button delButton;
    private Button exitButton;

    private String ip;

    public DebugFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debug, container, false);

        ipField = view.findViewById(R.id.ipField);
        delButton = view.findViewById(R.id.delButton);
        exitButton = view.findViewById(R.id.exitButton);

        ipField.addTextChangedListener(new TextValidator(ipField) {
            final Pattern patatern = Pattern.compile("^(\\d){1,3}\\.(\\d){1,3}\\.(\\d){1,3}\\.(\\d){1,3}$");
            @Override
            public void validate(TextView textView, String text) {
                Matcher matcher = patatern.matcher(text);
                if(!matcher.find()) {
                    textView.setError("Invalid IP Address");
                }
            }
        });

        if (BackendIpRepository.getInstance().isIpLoaded()) {
            ipField.setText(BackendIpRepository.getInstance().getBip().getIp());
        }
        
        delButton.setOnClickListener( v -> {
            File appDir = getActivity().getFilesDir();
            // delete user file
            File usrFile = new File(appDir, "usr.usr");
            if(usrFile.exists() && usrFile.isFile()) {
                usrFile.delete();
            }
            File trailDir = new File(appDir, "trails");
            if(trailDir.exists() && trailDir.isDirectory()) {
                File[] trailFiles = trailDir.listFiles();
                if(trailFiles.length > 0) {
                    for(File f : trailFiles) {
                        f.delete();
                    }
                }
                trailDir.delete();
            }

            Toast.makeText(getActivity(), "Deleted App Files\n app can now be restarted", Toast.LENGTH_SHORT).show();
        });

        exitButton.setOnClickListener( v -> {
            ip = ipField.getText().toString();

            if(ip.isEmpty()) {
                ipField.setError("IP field cannot be empty");
            }

            if(ipField.getError() == null) {
                BackendIpRepository inst = BackendIpRepository.getInstance();
                inst.saveIP(ip);

                Toast.makeText(getActivity(), "IP Saved, please restart the app", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        return view;
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