package com.habbybolan.groceryplanner.callbacks;

/**
 * Callback used when deleting an item from database.
 */
public interface DbCallbackEmptyResponse {

    void onSuccess();

    void onFailure(String message);

}
