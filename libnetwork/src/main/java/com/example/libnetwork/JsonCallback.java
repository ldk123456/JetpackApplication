package com.example.libnetwork;

public interface JsonCallback<T> {
    void onSuccess(ApiResponse<T> response);

    void onError(ApiResponse<T> response);

    void onCacheSuccess(ApiResponse<T> response);
}
