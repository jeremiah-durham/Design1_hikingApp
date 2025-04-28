package com.design.hikingapp.backendip;

import android.util.Log;

import java.io.File;
import java.io.IOException;

public class BackendIpRepository {
    private static BackendIpRepository instance;
    private BackendIp bip = null;
    private File ipFile;

    private BackendIpRepository() {}

    public static BackendIpRepository getInstance() {
        if (instance == null)
            instance = new BackendIpRepository();
        return  instance;
    }

    public static void initRepo(File filesDir) {
        var inst = getInstance();
        inst.ipFile = new File(filesDir, "backend.ip");
    }

    public boolean isIpLoaded() {
        return bip != null;
    }

    public boolean loadIp() {
        try {
            bip = new BackendIp();
            bip.loadFromFile(ipFile);
        } catch (IOException e) {
            bip = null;
        }

        return isIpLoaded();
    }

    public BackendIp getBip() {return this.bip;}

    public void saveIP(String ip) {
        if (bip == null) {
            bip = new BackendIp(ip);
        } else {
            bip.setIp(ip);
        }
        try {
            bip.saveToFile(ipFile);
            Log.d("BIP Repo", "Saved ip file");
        } catch (Exception e) {
            Log.e("BIP Repo", "Failed to save bip", e);
            bip = null;
        }
    }
}
