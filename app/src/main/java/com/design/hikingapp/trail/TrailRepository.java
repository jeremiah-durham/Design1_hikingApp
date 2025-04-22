package com.design.hikingapp.trail;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrailRepository {
    private static TrailRepository instance;
    private File trailsDir;
    private ArrayList<File> trailFiles;
    private ArrayList<Integer> trailIds;
    private ArrayList<Trail> loadedTrails;
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
            } catch (Exception e) {
                // if an exception occured, simply dont try to load the file
                Log.e("Trail Repository", "Error occured loading trail", e);
            }
        });
    }
    public List<Trail> getLoadedTrails() {return loadedTrails;}
}
