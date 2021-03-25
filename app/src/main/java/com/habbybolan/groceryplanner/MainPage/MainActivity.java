package com.habbybolan.groceryplanner.MainPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityMainBinding;
import com.habbybolan.groceryplanner.databinding.ToolbarBinding;
import com.habbybolan.groceryplanner.http.requests.HttpGrocery;
import com.habbybolan.groceryplanner.http.requests.HttpGroceryImpl;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListActivity;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActivityMainBinding binding;

    private HttpGrocery httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setToolBar();
        httpRequest = new HttpGroceryImpl(getApplication());

         // for testing
        binding.btnHttpRequest.setOnClickListener(l -> {
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sharedPref_name), Context.MODE_PRIVATE);
            String token = sharedPref.getString(getString(R.string.saved_token), "");
            httpRequest.getGroceries(token);
        });
    }

    /**
     * Sets up the toolbar with Up button.
     */
    private void setToolBar() {
        ToolbarBinding toolbarBinding = binding.toolbarMainPage;
        toolbar = toolbarBinding.toolbar;
        toolbarBinding.setTitle(getString(R.string.title_main_page));
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

}
