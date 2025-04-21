package com.design.hikingapp.backend;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.design.hikingapp.R;
import com.design.hikingapp.trail.Trail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TrailResponseParser {
    private List<Trail> trails;
    public TrailResponseParser() {}

    // alternative debug parse function
    public List<Trail> pars(InputStream in) throws IOException {
        StringBuilder response = new StringBuilder();

        try (BufferedReader reader = new BufferedReader( new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        Log.d("Response Parser", "response: " + response.toString());

        return null;
    }
    public List<Trail> parse(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            return readTrailsArray(reader);
        } finally {
            reader.close();
        }
    }

    private List<Trail> readTrailsArray(JsonReader reader) throws IOException {
        List<Trail> trails = new ArrayList<Trail>();

        reader.beginArray();
        while(reader.hasNext()) {
            trails.add(readTrail(reader));
        }
        reader.endArray();
        return trails;
    }

    private Trail readTrail(JsonReader reader) throws IOException {
        String trailName = "";
        String difficulty = "";
        int trail_id = 0;
        double elevation = 0;
        double timeMin = 0;
        double distance = 0;
        double lat = 0.0;
        double lon = 0.0;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if (reader.peek() == JsonToken.NULL) {
                reader.skipValue();
            } else if (name.equals("trail_id")) {
                trail_id = reader.nextInt();
            } else if (name.equals("trail_name")) {
                trailName = reader.nextString();
            } else if (name.equals("difficulty")) {
                difficulty = reader.nextString();
            } else if (name.equals("elevation_delta")) {
                elevation = reader.nextDouble();
            } else if (name.equals("distance")) {
                distance = reader.nextDouble();
            } else if (name.equals("est_time_min")) {
                timeMin = reader.nextDouble();
            } else if (name.equals("lat")) {
                lat = reader.nextDouble();
            } else if (name.equals("lon")) {
                lon = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }

        reader.endObject();

        Trail t = new Trail(R.drawable.sample_trail_image, R.drawable.map_bg, trailName, distance, difficulty, (int)elevation, ((int)timeMin) / 60, ((int)timeMin)%60, trail_id);
        if (lat != 0.0 && lon != 0.0) {
            t.setLocation(lat, lon);
        }
        return t;
    }
}
