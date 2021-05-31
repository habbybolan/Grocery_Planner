package com.habbybolan.groceryplanner;

public interface DbSingleCallback<T> {
    void onResponse(T response);
}
