package com.habbybolan.groceryplanner.http;

import android.content.Context;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequestImpl implements HttpRequest {

    private String address = "http://192.168.0.16:8080";
    Context context;

    public HttpRequestImpl(Context context) {
        this.context = context;
    }

   @Override
    public HttpURLConnection getHttpConnection(String path, String httpMethod, String token) throws IOException{

        final String SERVER = address + path;
       final int READ_TIMEOUT  = 1500;
        final int CONNECTION_TIMEOUT = 1500;

        URL myUrl = new URL(SERVER);
        HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
        connection.setRequestMethod(httpMethod);
        connection.setRequestProperty("Authorization", "Bearer " + Base64.encodeToString(token.getBytes(), Base64.NO_WRAP));
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        return connection;
    }

    @Override
    public JSONArray connectAndReadResponsePOST(String query, HttpURLConnection connection) throws IOException, JSONException {

        // todo: do on background thread

        StringBuilder response = new StringBuilder();

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, StandardCharsets.UTF_8));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line=br.readLine()) != null) {
                response.append(line);
            }
        }
        else {
            response = new StringBuilder();
        }
        return new JSONArray(response.toString());
    }

    @Override
    public JSONArray connectAndReadResponseGET(HttpURLConnection connection) throws IOException, JSONException {

        // todo: do on background thread

        StringBuilder response = new StringBuilder();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line=br.readLine()) != null) {
                response.append(line);
            }
        }
        else {
            response = new StringBuilder();
        }
        return new JSONArray(response.toString());
    }

}
