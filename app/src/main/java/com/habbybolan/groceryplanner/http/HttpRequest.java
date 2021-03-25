package com.habbybolan.groceryplanner.http;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface HttpRequest {

    /**
     * Get the HttpConnection, set up its configurations and return.
     * @param path          Web path.
     * @param httpMethod    Http API method
     * @return              The HttpURLConnection created
     */
    HttpURLConnection getHttpConnection(String path, String httpMethod, String token) throws IOException;

    /**
     *
     * @param query
     * @param connection
     * @return
     */
    public JSONArray connectAndReadResponsePOST(String query, HttpURLConnection connection) throws IOException, JSONException;

    /**
     *
     * @param connection
     * @return
     */
    public JSONArray connectAndReadResponseGET(HttpURLConnection connection) throws IOException, JSONException;

}
