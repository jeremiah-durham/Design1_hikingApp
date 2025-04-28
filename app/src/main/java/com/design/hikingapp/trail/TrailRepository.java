package com.design.hikingapp.trail;

import android.util.Log;

import com.design.hikingapp.backend.BackendRepository;
import com.design.hikingapp.user.UserRepository;
import com.design.hikingapp.util.Result;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

public class TrailRepository {
    private static TrailRepository instance;
    private File trailsDir;
    private ArrayList<File> trailFiles;
    private ArrayList<Integer> trailIds;
    private ArrayList<Trail> loadedTrails;
    private Trail activeTrail;
    private Semaphore activeTrailSem;
    private TrailRepository() {}
    public static TrailRepository getInstance() {
        if (instance == null)
            instance = new TrailRepository();
        return instance;
    }

    public static void initRepo(File fileDir) {
        var inst = getInstance();
        inst.trailsDir = new File(fileDir, "trails");
        inst.trailFiles = new ArrayList<>();
        inst.trailIds = new ArrayList<>();
        inst.loadedTrails = new ArrayList<>();
        inst.activeTrail = null;
        inst.activeTrailSem = new Semaphore(1, true);

        // create trails dir if it does not exist
        if(!inst.trailsDir.isDirectory()) {
            try {
                inst.trailsDir.delete();
            } finally {
                // if the file existed, assume it was deleted,
                // if the file didn't exist, then create the dir anyways :P
                inst.trailsDir.mkdir();
            }
        } else {
            // the directory exists, so lets make a list of existing files
            File[] flist = inst.trailsDir.listFiles((dir, name) -> {
                return name.matches("^\\d\\.trl$");
            });
            if(flist != null) {
                inst.trailFiles.addAll(Arrays.asList(flist));
                inst.trailFiles.forEach(file -> {
                    String name = file.getName();
                    inst.trailIds.add(Integer.decode(name.split("\\.")[0]));
                });
            }

            inst.loadTrails();
        }
    }

    public boolean isTrailDownloaded(int id) {
        return trailIds.contains(id);
    }

    public void downloadTrail(Trail trail) {
        try {
            trail.saveToFile(trailsDir);
            loadedTrails.add(trail);
            trailIds.add(trail.getId());
        } catch (Exception e) {
            Log.e("Trail Repository", "Error downloading trail", e);
        }
    }

    private void loadTrails() {
        // load a trail from each file
        trailFiles.forEach(file -> {
            try {
                Trail t = new Trail();
                t.loadFromFile(file);
                // assuming the trail loaded properly, add it to the list of loaded trails
                loadedTrails.add(t);
                if(t.isActive()) activeTrail = t;
            } catch (Exception e) {
                // if an exception occurred, simply don't try to load the file
                Log.e("Trail Repository", "Error occurred loading trail", e);
            }
        });
    }
    public List<Trail> getLoadedTrails() {return loadedTrails;}

    public synchronized boolean isTrailActive() {
        try {activeTrailSem.acquire();} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        activeTrailSem.release();
        return activeTrail != null;
    }

    public void setTrailInactive() {
        if(isTrailActive()) {
            // stop the hike
            BackendRepository repo = BackendRepository.getInstance();
            repo.hikeRequest(UserRepository.getInstance().getUser(), activeTrail, "STOP", (result) -> {
                if (result instanceof Result.Success) {
                    Log.d("Trail Repo", "probably stopped trail properly... msg: " + ((Result.Success<String>) result).data);
                    activeTrail.setInactive();
                    // resave trail
                    try {activeTrail.saveToFile(trailsDir); } catch (Exception e) {}
                    activeTrail = null;
                } else {
                    Log.e("Trail Repo", "Got error stoping hike...", ((Result.Error) result).exception);
                }
            });


        }
    }

    public void setTrailActive(Trail trail) {
        try {
            activeTrailSem.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        activeTrail = trail;

        // send request to start trail to backend and get start date and stop string from there
        Date startDate = Calendar.getInstance().getTime();

        BackendRepository repo = BackendRepository.getInstance();
        repo.hikeRequest(UserRepository.getInstance().getUser(), trail, "START", (result) -> {
            if (result instanceof Result.Success) {
                Log.d("Trail Repo", "Successfully started trail");
                // set active trail start
                try {
                    Date stopDate;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    stopDate = sdf.parse(((Result.Success<String>) result).data);
                    // set active trail
                    activeTrail.setActive(startDate, stopDate);
                } catch (Exception e) {
                    // this should not error
                    Log.e("Trail Repo", "Error when formatting time", e);
                }
            } else {
                Log.e("Trail Repo", "Start hike got error result", ((Result.Error<String>) result).exception);

                // fail setting up active trail
                activeTrail = null;
            }

            activeTrailSem.release();
        });


        // resave trail
        try {activeTrail.saveToFile(trailsDir);} catch (Exception e) {}
    }

    public Trail getActiveTrail() {return this.activeTrail;}
}
