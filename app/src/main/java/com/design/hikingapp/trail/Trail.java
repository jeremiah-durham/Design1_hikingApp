package com.design.hikingapp.trail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.design.hikingapp.util.FileStorable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Trail implements FileStorable {
    private int imageResource;
    private int mapResource;
    private String name;
    private double distance;
    private String difficulty;
    private int elevation;
    private int timeHrs;
    private int timeMins;
    private int id;
    private Bitmap imgBmp = null;
    private double lat = 39.75081252642373;
    private double lon = -105.22232351583222;

    public Trail() {};
    public Trail(int imageResource, int mapResource, String name, double distance, String difficulty, int elevation, int timeHrs, int timeMins, int id) {
        this.imageResource = imageResource;
        this.mapResource = mapResource;
        this.name = name;
        this.distance = distance;
        this.elevation = elevation;
        this.difficulty = difficulty;
        this.timeHrs = timeHrs;
        this.timeMins = timeMins;

        this.id = id;
    }

    public void setLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    public double getLat() {
        return this.lat;
    }
    public double getLon() {
        return this.lon;
    }
    public void setImgBmp(Bitmap imgBmp) {
        this.imgBmp = imgBmp;
    }
    public Bitmap getImgBmp() {
        return imgBmp;
    }

    public int getImageResource() {
        return imageResource;
    }
    public String getName() {
        return name;
    }
    public double getDistance() {
        return distance;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public int getElevation() {
        return elevation;
    }
    public int getTimeHrs() {
        return timeHrs;
    }
    public int getTimeMins() {
        return timeMins;
    }

    public int getId() {
        return id;
    }


    @Override
    public void loadFromFile(File trailFile) throws IOException, SecurityException {
        if(!trailFile.isFile()) {
            throw new IOException(trailFile.getPath() + " does not exist or is not a file");
        }

        DataInputStream is = null;
        try {
            is = new DataInputStream(new FileInputStream(trailFile));
            this.id = is.readInt();
            boolean hasBmp = is.readBoolean();
            if(hasBmp) {
                this.imgBmp = BitmapFactory.decodeStream(is);
            }
            this.name = is.readUTF();
            this.lat = is.readDouble();
            this.lon = is.readDouble();
            this.distance = is.readDouble();
            this.elevation = is.readInt();
            this.timeHrs = is.readInt();
            this.timeMins = is.readInt();
            this.difficulty = is.readUTF();

        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (is != null)
                is.close();
        }
    }

    @Override
    public void saveToFile(File trailDirectory) throws IOException, SecurityException {
        if(!trailDirectory.isDirectory()) {
            throw new IOException(trailDirectory.getPath() + " does not exist or is not a directory");
        }

        File trailFile = new File(trailDirectory, this.id + ".trl");
        if(trailFile.exists()) {
            throw new IOException(trailFile.getPath() + " already exists");
        }

        DataOutputStream out = null;
        try {
            trailFile.createNewFile();
            out = new DataOutputStream(new FileOutputStream(trailFile));
            out.writeInt(this.id);
            out.writeBoolean(this.imgBmp != null);
            if(this.imgBmp != null) {
                // there is a bmp image associated with the trail, so lets save it
                this.imgBmp.compress(Bitmap.CompressFormat.PNG, 0, out);
            }
            out.writeUTF(this.name);
            out.writeDouble(this.lat);
            out.writeDouble(this.lon);
            out.writeDouble(this.distance);
            out.writeInt(this.elevation);
            out.writeInt(this.timeHrs);
            out.writeInt(this.timeMins);
            out.writeUTF(this.difficulty);

        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (out != null)
                out.close();
        }
    }
}