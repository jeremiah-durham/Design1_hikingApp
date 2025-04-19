package com.design.hikingapp.backend;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonWriter;
import android.util.Log;

import com.design.hikingapp.SearchAttributes;
import com.design.hikingapp.Trail;
import com.design.hikingapp.util.RepositoryCallback;
import com.design.hikingapp.util.Result;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    private String generateTrailRequest(SearchAttributes filter) throws IOException {
        StringWriter strwtr = new StringWriter();
        JsonWriter writer = new JsonWriter(strwtr);

        writer.beginObject();
        writer.name("fields")
                .beginArray()
                    .value("trail_name")
                    .value("elevation_delta")
                    .value("difficulty")
                    .value("est_time_min")
                    .value("distance")
                    .value("lat")
                    .value("lon")
                .endArray();
        if(filter != null) {
            writer.name("filters");
            writer.beginObject();
            // create filter fields here
            // BEGIN FILTER FIELDS
            writer.name("elevation_delta");
            writer.beginObject();
                if(filter.getMinEle != 0){
                    writer.name("leq").value(filter.getMinEle());
                }
                if(filter.getMaxEle != 2500){
                    writer.name("geq").value(filter.getMaxEle());
                }
            writer.endObject();
            writer.name("time");
            writer.beginObject();
            if(filter.getMinTime != 0){
                writer.name("leq").value(filter.getMinTime());
            }
            if(filter.getMaxTime != 5){
                writer.name("geq").value(filter.getMaxTime());
            }
            writer.endObject();
            writer.name("traits");
            writer.beginObject()
                    .name("biking").value(filter.getBiking())
                    .name("hist_sites").value(filter.getHistory())
                    .name("river").value(filter.getRiver());
            writer.endObject();
            writer.name("difficulty").beginArray();
                if(filter.getEasy()){
                    writer.name("easy");
                }
                if(filter.getModerate()){
                    writer.name("moderate");
                }
                if(filter.getHard()){
                    writer.name("hard");
                }
            writer.endArray();
            // END FILTER FIELDS
            writer.endObject();
        }
        writer.endObject();
        writer.close();

        return strwtr.toString();
    }

    private Result<List<Trail>> trailSynchronousFetch(SearchAttributes filter) {
        URL url;
        try {
            url = new URL(HTTP_URL+"/"+TRAIL_QUERY_PATH);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            String requestString = generateTrailRequest(filter);
            con.getOutputStream().write(requestString.getBytes(StandardCharsets.UTF_8));

            int responseCode = con.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return new Result.Error<List<Trail>>(new Exception("Backend Api returned response code: " + responseCode + " (not HTTP_OK)"));
            }

            List<Trail> trailList = responseParser.parse(con.getInputStream());

            // tmp trail image population
            trailList.forEach(trail -> {
                Bitmap bmp = null;
                try {
                    InputStream in = new URL(HTTP_URL + "/img/" + "placeholderTrail.png").openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // bah
                }
                trail.setImgBmp(bmp);
            });

            return new Result.Success<List<Trail>>(trailList);
        } catch (Exception e) {
            return new Result.Error<List<Trail>>(e);
        }
    }

    public void fetchTrailList(
            SearchAttributes filter,
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
