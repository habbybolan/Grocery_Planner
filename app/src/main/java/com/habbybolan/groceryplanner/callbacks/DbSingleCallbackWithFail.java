package com.habbybolan.groceryplanner.callbacks;

/**
 * extends DbSingleCallback to have a fail method.
 */
public interface DbSingleCallbackWithFail<T> extends DbSingleCallback<T>{
    void onFail(String message);
}
