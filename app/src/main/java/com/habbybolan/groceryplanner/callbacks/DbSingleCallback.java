package com.habbybolan.groceryplanner.callbacks;

/**
 * Database callback for a single object retrieved from the Room database
 * @param <T>   Object type returned from the database
 */
public interface DbSingleCallback<T> {
    void onResponse(T response);
}
