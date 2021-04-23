package com.habbybolan.groceryplanner;

import java.util.List;

public interface DbCallback<T> {

    void onResponse(List<T> response);
}
