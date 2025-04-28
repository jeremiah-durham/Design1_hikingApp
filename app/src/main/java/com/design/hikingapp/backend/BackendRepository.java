package com.design.hikingapp.backend;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.design.hikingapp.SearchAttributes;
import com.design.hikingapp.backendip.BackendIp;
import com.design.hikingapp.trail.Trail;
import com.design.hikingapp.user.User;
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
    private static String HTTP_URL = "http://10.0.2.2:80";
    private static final String TRAIL_QUERY_PATH = "json";
    private static final String USER_QUERY_PATH = "user";
    private static final String HIKE_ACTION_PATH = "hikes";
    private static final int CONNECTION_TIMEOUT = 200;

    private static BackendRepository instance;
    private TrailResponseParser responseParser;
    private Executor executor;

    public static void setIP(BackendIp bip) {
        HTTP_URL = "http://" + bip.getIp() + ":80";
    }

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
                    .value("trail_id")
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
            if(!filter.getQuery().isEmpty()) {
                writer.name("fulltext").value(filter.getQuery());
            }
            if(filter.getMaxEle() != 2500 || filter.getMinEle() != 0){
                writer.name("elevation_delta");
                writer.beginObject();
                if(filter.getMaxEle() != 2500){
                    writer.name("leq").value(filter.getMaxEle());
                }
                if(filter.getMinEle() != 0){
                    writer.name("geq").value(filter.getMinEle());
                }
                writer.endObject();
            }
            if(filter.getMaxTime() != 5.0 || filter.getMinTime() != 0.0) {
                writer.name("est_time_min");
                writer.beginObject();
                if (filter.getMaxTime() != 5.0) {
                    writer.name("leq").value(filter.getMaxTime()*60.0);
                }
                if (filter.getMinTime() != 0.0) {
                    writer.name("geq").value(filter.getMinTime()*60.0);
                }
                writer.endObject();
            }
            if(filter.getMaxLen() != 15 || filter.getMinLen() != 0) {
                writer.name("distance");
                writer.beginObject();
                if (filter.getMaxLen() != 15) {
                    writer.name("leq").value(filter.getMaxLen());
                }
                if (filter.getMinLen() != 0) {
                    writer.name("geq").value(filter.getMinLen());
                }
                writer.endObject();
            }
            if(filter.getBiking() || filter.getViews() || filter.getLake() ||
                    filter.getForest() || filter.getHistory() || filter.getRiver()) {
                writer.name("traits");
                writer.beginObject();
                        if(filter.getBiking()) {
                            writer.name("biking").value(filter.getBiking());
                        }
                        if(filter.getViews()) {
                            writer.name("mountain_views").value(filter.getViews());
                        }
                        if(filter.getLake()) {
                            writer.name("lake").value(filter.getLake());
                        }
                        if(filter.getForest()) {
                            writer.name("forest").value(filter.getForest());
                        }
                        if(filter.getHistory()) {
                            writer.name("hist_sites").value(filter.getHistory());
                        }
                        if(filter.getRiver()) {
                            writer.name("river").value(filter.getRiver());
                        }
                writer.endObject();
            }
            if(filter.getEasy() || filter.getModerate() || filter.getHard()) {
                writer.name("difficulty").beginArray();
                if (filter.getEasy()) {
                    writer.value("easy");
                }
                if (filter.getModerate()) {
                    writer.value("moderate");
                }
                if (filter.getHard()) {
                    writer.value("hard");
                }
                writer.endArray();
            }
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
            con.setConnectTimeout(CONNECTION_TIMEOUT);
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

    private String generateCreateUserRequest(User user) throws IOException {
        StringWriter strwtr = new StringWriter();
        JsonWriter writer = new JsonWriter(strwtr);

        writer.beginObject();
        writer.name("name").value(user.getName());
        writer.name("eemail").value(user.getEemail());
        writer.name("weight").value(user.getWeight());
        writer.name("height").value(user.getHeight());
        writer.endObject();
        writer.close();

        return strwtr.toString();
    }
    private Result<String> createUserSynchronousPost(User user) {
        URL url;
        HttpURLConnection con = null;
        try {
            url = new URL(HTTP_URL + "/" + USER_QUERY_PATH);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            String requestString = generateCreateUserRequest(user);
            con.getOutputStream().write(requestString.getBytes(StandardCharsets.UTF_8));

            int responseCode = con.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return new Result.Error<String>(new Exception("Backend Api returned response code: " + responseCode));
            }

            String uuid = null;
            // inline response parser
            JsonReader reader = new JsonReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            try {
                // read response object
                reader.beginObject();
                while(reader.hasNext()) {
                    String name = reader.nextName();
                    if(name.equals("uuid")) {
                        uuid = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } finally {
                reader.close();
            }

            if(uuid != null) {
                return new Result.Success<String>(uuid);
            } else {
                return new Result.Error<String>(new Exception("did not get uuid response object"));
            }

        } catch (Exception e) {
            return new Result.Error<String>(e);
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    public void createUserRequest(
            User user,
            final RepositoryCallback<String> callback
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<String> result = createUserSynchronousPost(user);
                    callback.onComplete(result);
                } catch (Exception e) {
                    Result<String> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                }
            }
        });
    }

    private String generateHikeActionRequest(User user, Trail trail, String action) throws IOException {
        StringWriter strwtr = new StringWriter();
        JsonWriter writer = new JsonWriter(strwtr);

        writer.beginObject();
        writer.name("user_uuid").value(user.getUUID());
        writer.name("trail_id").value(trail.getId());
        writer.name("action").value(action);
        writer.endObject();
        writer.close();

        return strwtr.toString();
    }
    private Result<String> hikeSynchronousPost(User user, Trail trail, String action) {
        URL url;
        HttpURLConnection con = null;
        try {
            url = new URL(HTTP_URL + "/" + HIKE_ACTION_PATH);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            String requestString = generateHikeActionRequest(user, trail, action);
            con.getOutputStream().write(requestString.getBytes(StandardCharsets.UTF_8));

            int responseCode = con.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return new Result.Error<>(new Exception("Backend Api returned response code: " + responseCode));
            }

            String estendtime = null;
            String msg = null;
            JsonReader reader = new JsonReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            try {
                reader.beginObject();
                while(reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("esttime")) {
                        estendtime = reader.nextString();
                    } else if (name.equals("message")) {
                        msg = reader.nextString();
                    } else {
                            reader.skipValue();
                    }
                }
                reader.endObject();
            } finally {
                reader.close();
            }

            if (action.equals("START")) {
                if (estendtime != null) {
                    return new Result.Success<>(estendtime);
                } else {
                    return new Result.Error<>(new Exception("did not get estendtime... got msg: " + msg));
                }
            } else {
                if (msg != null) {
                    return new Result.Success<>(msg);
                } else {
                    return new Result.Error<>(new Exception("Got error result without message... action: " + action));
                }
            }

        } catch (Exception e) {
            return new Result.Error<>(e);
        } finally {
            if (con != null)
                con.disconnect();
        }
    }
    public void hikeRequest(
            User user,
            Trail trail,
            String action,
            final RepositoryCallback<String> callback
    ) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result<String> result = hikeSynchronousPost(user, trail, action);
                    callback.onComplete(result);
                } catch (Exception e) {
                    Result<String> errorResult = new Result.Error<>(e);
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
