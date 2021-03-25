package com.habbybolan.groceryplanner.loginpage.login;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.http.requests.HttpLoginSignUp;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class LoginInteractorImpl implements LoginInteractor {

    private HttpLoginSignUp httpRequest;
    private Context context;

    @Inject
    public LoginInteractorImpl(HttpLoginSignUp httpRequest, Context context) {
        this.httpRequest = httpRequest;
        this.context = context;
    }

    @Override
    public void login(String username, String password, ObservableField<JSONObject> loginResponse) {
        try {
            httpRequest.loginUser(username, password, loginResponse);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTokenToPreferences(String token) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sharedPref_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getResources().getString(R.string.saved_token), token);
        editor.apply();
    }


    /*
     * Sends the user information to the web service to try and login.
     * @param username  Username input by user
     * @param password  Password input by user
     */
    /*private String loginUser(String username, String password) throws ExecutionException, InterruptedException {


        Callable<String> task = () -> {
            final String SERVER = "http://192.168.0.16:8080/api/login";
            final String REQUEST_METHOD = "POST";
            final int READ_TIMEOUT  = 1500;
            final int CONNECTION_TIMEOUT = 1500;

            Authenticator.setDefault(new Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("user1","abcd".toCharArray());
                }});

            // web service authentication
            //final String basicAuth = "Basic " + Base64.encodeToString("user1:abcd".getBytes(), Base64.NO_WRAP);
            StringBuilder response = new StringBuilder();

            try {
                URL myUrl = new URL(SERVER);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                //connection.setRequestProperty("Authorization", basicAuth);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                // input parameters
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userName", username)
                        .appendQueryParameter("password", password);
                String query = builder.build().getEncodedQuery();

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
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response.append(line);
                    }
                }
                else {
                    response = new StringBuilder();
                }
            } catch (IOException e) {
                e.printStackTrace();
                response = new StringBuilder("error");
            }

            return response.toString();
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> futureTask = executorService.submit(task);
        return futureTask.get();
    }*/


}
