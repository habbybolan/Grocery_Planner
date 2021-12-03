package com.habbybolan.groceryplanner.callbacks;

import java.util.List;

/**
 * Callback for http responses
 * @param <T>   Object type being retrieved from http response
 */
public interface WebServiceCallback<T> {

    void onResponse(List<T> response);
    void onFailure(int responseCode, String message);
}
