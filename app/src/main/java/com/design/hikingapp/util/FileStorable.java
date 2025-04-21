package com.design.hikingapp.util;

import java.io.File;
import java.io.IOException;

public interface FileStorable {
    public void loadFromFile(File file) throws IOException;
    public void saveToFile(File file) throws IOException;
}
