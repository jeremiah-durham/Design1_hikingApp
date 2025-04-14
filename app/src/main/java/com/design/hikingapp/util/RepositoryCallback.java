package com.design.hikingapp.util;

public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
