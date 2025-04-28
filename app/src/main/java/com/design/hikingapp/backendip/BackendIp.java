package com.design.hikingapp.backendip;

import com.design.hikingapp.util.FileStorable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BackendIp implements FileStorable {

    private String ip;

    public BackendIp() {}
    public BackendIp(String ip) {this.ip = ip;}
    public String getIp() {return this.ip;}
    public void setIp(String ip) {this.ip = ip;}


    @Override
    public void loadFromFile(File file) throws IOException {
        DataInputStream ins = null;
        try {
            ins = new DataInputStream(new FileInputStream(file));
            ip = ins.readUTF();
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (ins != null)
                ins.close();
        }
    }

    @Override
    public void saveToFile(File file) throws IOException {
        DataOutputStream out = null;
        try {
            file.createNewFile();
            out = new DataOutputStream(new FileOutputStream(file));
            out.writeUTF(ip);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (out != null)
                out.close();
        }
    }
}
