package com.habbybolan.groceryplanner.http.requests;

import android.content.Context;

import com.habbybolan.groceryplanner.http.HttpRequestImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpGroceryImpl extends HttpRequestImpl implements HttpGrocery {

    public HttpGroceryImpl(Context context) {
        super(context);
    }

    @Override
    public void getGroceries(String token) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            JSONArray response = new JSONArray();

            try {
                HttpURLConnection connection = getHttpConnection("/api/groceries", "GET", token);

                response = connectAndReadResponseGET(connection);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
