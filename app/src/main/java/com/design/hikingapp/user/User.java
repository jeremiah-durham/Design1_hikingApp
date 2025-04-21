package com.design.hikingapp.user;

import com.design.hikingapp.util.FileStorable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class User implements FileStorable {
    private String uuid;
    private String eemail;
    private String name;
    private int weight;
    private int height;

    public User() {}
    public User(String name, String eemail, int weight, int height) {
        this.name = name;
        this.eemail = eemail;
        this.weight = weight;
        this.height = height;
        this.uuid = "";
    }

    public String getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getEemail() {
        return eemail;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void loadFromFile(File file) throws IOException {
        DataInputStream ins = null;
        try {
            ins = new DataInputStream(new FileInputStream(file));
            uuid = ins.readUTF();
            eemail = ins.readUTF();
            name = ins.readUTF();
            weight = ins.readInt();
            height = ins.readInt();
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
            out.writeUTF(uuid);
            out.writeUTF(eemail);
            out.writeUTF(name);
            out.writeInt(weight);
            out.writeInt(height);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (out != null)
                out.close();
        }
    }
}
