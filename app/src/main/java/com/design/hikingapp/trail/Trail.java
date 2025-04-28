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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Trail implements FileStorable {

    private class ActiveInfo {
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        public Date startTime;
        public Date estEndTime;

        public ActiveInfo() {}

        public void setStartTime(String dt) {
            try {
                startTime = sdf.parse(dt);
            } catch (ParseException e) {
                // no
            }
        }
        public void setStartTime(Date dt) {
            startTime = dt;
        }

        public void setEndTime(String dt) {
            try {
                estEndTime = sdf.parse(dt);
            } catch (ParseException e) {
                // no
            }
        }
        public void setEndTime(Date dt) {
            estEndTime = dt;
        }

        public Date getStartDate() {return startTime;}
        public Date getEndDate() {return estEndTime;}
        public String getStartString() {return sdf.format(startTime);}
        public String getEndString() {return sdf.format(estEndTime);}
    }
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
    private boolean isActive = false;
    private ActiveInfo activeInfo = null;

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

    public boolean isActive() {return isActive;}
    public Date getStartDate() {return activeInfo.getStartDate();}
    public Date getEndDate() {return activeInfo.getEndDate();}

    public void setActive(Date startDate, String endTime) {
        this.isActive = true;
        this.activeInfo = new ActiveInfo();
        this.activeInfo.setStartTime(startDate);
        this.activeInfo.setEndTime(endTime);
    }

    public void setActive(Date startDate, Date stopDate) {
        this.isActive = true;
        this.activeInfo = new ActiveInfo();
        this.activeInfo.setStartTime(startDate);
        this.activeInfo.setEndTime(stopDate);
    }

    public void setInactive() {
        this.isActive = false;
        this.activeInfo = null;
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
            this.isActive = is.readBoolean();
            if(isActive) {
                // read active info
                this.activeInfo = new ActiveInfo();
                this.activeInfo.setStartTime(is.readUTF());
                this.activeInfo.setEndTime(is.readUTF());
            }

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
//            throw new IOException(trailFile.getPath() + " already exists");
            // overwrite trail file
            trailFile.delete();
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
            out.writeBoolean(this.isActive);
            if(isActive) {
                out.writeUTF(this.activeInfo.getStartString());
                out.writeUTF(this.activeInfo.getEndString());
            }

        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (out != null)
                out.close();
        }
    }
}