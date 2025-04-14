package com.design.hikingapp.backend;


import android.util.Log;

import com.design.hikingapp.Trail;
import com.design.hikingapp.util.RepositoryCallback;
import com.design.hikingapp.util.Result;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;

public class BackendRepository {
    private static final String TAG = "BackendRepository";

    /* 10.0.2.2 is the loopback address to the host machine
     i.e. its the address of your computer */
    private static final String HTTP_URL = "http://10.0.2.2:80";
    private static final String TRAIL_QUERY_PATH = "json";

    private static BackendRepository instance;
    private TrailResponseParser responseParser;
    private Executor executor;

    private BackendRepository() {}

//    public BackendRepository() {
//        Log.d(TAG, "BackendRepository created");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("TRD", "running network thread");
//                testPost();
//            }
//        }).start();
//    }

    public static BackendRepository getInstance() {
        if(instance == null) {
            instance = new BackendRepository();
        }

        return instance;
    }

    public static void initRepo(TrailResponseParser responseParser, Executor executor) {
        var inst = getInstance();
        inst.responseParser = responseParser;
        inst.executor = executor;
    }

    private String generateTrailRequest(Object filter) {
        String request = "{ \"fields\": [\"trail_name\", \"elevation_delta\", \"difficulty\", \"est_time_min\", \"distance\"] ";
        if(filter != null) {
            throw new RuntimeException("filters not implemented");
        }

        request += "}";

        return request;
    }

    private Result<List<Trail>> trailSynchronousFetch(Object filter) {
        URL url;
        try {
            url = new URL(HTTP_URL+"/"+TRAIL_QUERY_PATH);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            String requestString = generateTrailRequest(filter);
            con.getOutputStream().write(requestString.getBytes("utf-8"));

            int responseCode = con.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return new Result.Error<List<Trail>>(new Exception("Backend Api returned response code: " + responseCode + " (not HTTP_OK)"));
            }

            List<Trail> trailList = responseParser.parse(con.getInputStream());
            return new Result.Success<List<Trail>>(trailList);
        } catch (Exception e) {
            return new Result.Error<List<Trail>>(e);
        }
    }

    public void fetchTrailList(
            Object filter,
            final RepositoryCallback<List<Trail>> callback
            ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<List<Trail>> result = trailSynchronousFetch(filter);
                    callback.onComplete(result);
                } catch (Exception e) {
                    Result<List<Trail>> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        });
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
