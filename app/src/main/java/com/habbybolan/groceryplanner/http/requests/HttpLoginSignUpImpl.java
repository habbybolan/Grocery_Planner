package com.habbybolan.groceryplanner.http.requests;

import android.content.Context;
import android.net.Uri;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.http.HttpRequestImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HttpLoginSignUpImpl extends HttpRequestImpl implements HttpLoginSignUp {

    public HttpLoginSignUpImpl(Context context) {
        super(context);
    }

    @Override
    public void signUpUser(String username, String password, String email, ObservableField<JSONObject> signUpResponse) throws ExecutionException, InterruptedException {

        Callable<JSONObject> task = () -> {
            JSONArray response = new JSONArray();

            HttpURLConnection connection = getHttpConnection("/api/signup", "POST", "");
            JSONObject jsonObject;
            if (connection.getResponseCode() != 200) {
                jsonObject = new JSONObject();
                jsonObject.put("code", connection.getResponseCode());
            } else {

                // input parameters
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", username)
                        .appendQueryParameter("password", password)
                        .appendQueryParameter("displayName", "testDisplay")
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("externalType", "")
                        .appendQueryParameter("externalId", "");
                String query = builder.build().getEncodedQuery();

                response = connectAndReadResponsePOST(query, connection);
                jsonObject = response.getJSONObject(0);
                jsonObject.put("code", connection.getResponseCode());
                return jsonObject;
            }
            return jsonObject;
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<JSONObject> futureTask = executorService.submit(task);
        signUpResponse.set(futureTask.get());
    }

    @Override
    public void loginUser(String username, String password, ObservableField<JSONObject> loginResponse) throws ExecutionException, InterruptedException {
        Callable<JSONObject> task = () -> {
            JSONArray response = new JSONArray();
            HttpURLConnection connection = getHttpConnection("/api/login", "POST", "");
            JSONObject jsonObject;
            if (connection.getResponseCode() != 200) {
                jsonObject = new JSONObject();
                jsonObject.put("code", connection.getResponseCode());
            } else {
                // input parameters
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", username)
                        .appendQueryParameter("password", password);
                String query = builder.build().getEncodedQuery();

                response = connectAndReadResponsePOST(query, connection);
                jsonObject = response.getJSONObject(0);
                jsonObject.put("code", connection.getResponseCode());
                return jsonObject;
            }
            return jsonObject;
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<JSONObject> futureTask = executorService.submit(task);
        loginResponse.set(futureTask.get());
    }
}
