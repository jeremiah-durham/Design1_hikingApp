package com.design.hikingapp.user;

import android.util.Log;

import com.design.hikingapp.backend.BackendRepository;
import com.design.hikingapp.util.Result;

import java.io.File;
import java.io.IOException;

public class UserRepository {
    private static UserRepository instance;
    private User user = null;
    private File userFile;
    private UserRepository() {}

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    public static void initRepo(File filesDir) {
        var inst = getInstance();
        inst.userFile = new File(filesDir, "usr.usr");
    }


    public boolean isUserLoaded() {
        return user != null;
    }

    public boolean loadUser() {
        try {
            user = new User();
            user.loadFromFile(userFile);
        } catch (IOException e) {
            // loading must've failed, so lets assume that it failed because the file does not exist
            user = null;
        }

        return isUserLoaded();
    }

    public User getUser() {
        return this.user;
    }

    public void saveUser() {
        try {
            user.saveToFile(userFile);
            Log.d("User Repo", "Saved user profile");
        } catch (Exception e) {
            Log.e("User Repo", "Failed to save user", e);
        }
    }

    public void createUser(String name, String eemail, int weight, int height) {
        user = new User(name, eemail, weight, height);
        BackendRepository repo = BackendRepository.getInstance();
        repo.createUserRequest(user, (result) -> {
            if (result instanceof Result.Success) {
                Log.d("User Repo", "Create user success! uuid: " + ((Result.Success<String>) result).data);
                user.setUUID(((Result.Success<String>) result).data);
            } else {
                Log.e("User Repo", "Create user got error result", ((Result.Error<String>) result).exception);
            }

            saveUser();
        });
    }


}
