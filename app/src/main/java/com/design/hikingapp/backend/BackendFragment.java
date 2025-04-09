package com.design.hikingapp.backend;


import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class BackendFragment {


    // backend is responsible for the following tasks:
    // 1. populating the minimum info for the search function
    // 2. requesting detailed information about a specific trail
    // 3. populating a list of trails based on a set of filters
    // 4. doing user management and telling the backend server user info
    // 5. checking if the device is online or not (?)


    private static final String TAG = "BackendFragment";
    // 10.0.2.2 is the loopback address to the host machine
    // i.e. its the address of your computer
    private static final String HTTP_URL = "http://10.0.2.2:80";

    public BackendFragment() {
        Log.d(TAG, "BackendFragment created");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TRD", "running network thread");
                testPost();
            }
        }).start();
    }

    private void testPost() {
        URL url;
        try {
            url = new URL(HTTP_URL+"/json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            con.setRequestProperty("Content-Type", "application/json");

            try (DataOutputStream os = new DataOutputStream(con.getOutputStream())) {
                os.writeBytes("{\"fields\": [ \"trail_id\", \"trail_name\" ] }");
                os.flush();
            }


            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();

                try (BufferedReader reader = new BufferedReader( new InputStreamReader( con.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                Log.d(TAG, "response: " + response.toString());

            } else {
                Log.e(TAG, "Error: HTTP response code: " + responseCode);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed Url", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
    }

    private void testNet() {
        URL url;
        try {
            url = new URL(HTTP_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            print_content(con);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed Url", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
    }

    private void print_content(HttpURLConnection con) {
        if (con != null) {
            try {
                Log.d(TAG, "URL CONTENT");

                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                String input;

                while ((input = br.readLine()) != null) {
                    Log.d(TAG, input);
                }
                br.close();
            } catch(IOException e) {
                Log.e(TAG, "Error reading content", e);
            }
        }
    }
}
