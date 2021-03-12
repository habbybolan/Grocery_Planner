package com.habbybolan.groceryplanner.MainPage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListActivity;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;
import com.habbybolan.groceryplanner.databinding.ActivityMainBinding;
import com.habbybolan.groceryplanner.databinding.ToolbarBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setToolBar();
        httpRequest();

        /*
        // todo: What does this do
            // For HttpUrlConnection in supporting basic authentication
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("user1", "abcd".toCharArray());
            }
        });*/
    }

    private void httpRequest() {
        binding.btnHttpRequest.setOnClickListener(l -> {
            HttpGetRequest httpGetRequest = new HttpGetRequest();
            httpGetRequest.execute();
        });
    }

    /**
     * Sets up the toolbar with Up button.
     */
    private void setToolBar() {
        ToolbarBinding toolbarBinding = binding.toolbarMainPage;
        toolbar = toolbarBinding.toolbar;
        toolbar.setTitle(R.string.title_main_page);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    /**
     * Clicker for entering the Recipe list
     * @param v     Recipe button view
     */
    public void onRecipeClick(View v) {
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
    }

    /**
     * Clicker for entering the Grocery list
     * @param v     Grocery button view
     */
    public void onGroceryClick(View v) {
        Intent intent = new Intent(this, GroceryListActivity.class);
        startActivity(intent);
    }

    private class HttpGetRequest extends AsyncTask<Void, Void, String> {

        private static final String SERVER = "http://10.0.2.2:8080/";
        private static final String REQUEST_METHOD = "GET";
        private static final int READ_TIMEOUT  = 1500;
        private static final int CONNECTION_TIMEOUT = 1500;

        @Override
        protected String doInBackground(Void... params) {
            String result;
            String inputLine;

            try {
                URL myUrl = new URL(SERVER);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();


                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = "error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

}
