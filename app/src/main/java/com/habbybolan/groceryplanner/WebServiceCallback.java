package com.habbybolan.groceryplanner;

import java.util.List;

public interface WebServiceCallback<T> {

    void onResponse(List<T> response, int responseCode);
}
