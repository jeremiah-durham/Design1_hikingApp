package com.design.hikingapp.backend;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class BackendFragment {
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
                testNet();
            }
        }).start();
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
