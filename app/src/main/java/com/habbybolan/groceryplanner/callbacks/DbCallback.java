package com.habbybolan.groceryplanner.callbacks;

import java.util.List;

/**
 * Database callback for a list of objects retrieved from the Room database
 * @param <T>   Object type in list returned from the database
 */
public interface DbCallback<T> {

    void onResponse(List<T> response);
}
