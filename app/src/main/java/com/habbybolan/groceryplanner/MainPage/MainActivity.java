package com.habbybolan.groceryplanner.MainPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.GroceryListActivity;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.RecipeListActivity;
import com.habbybolan.groceryplanner.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setToolBar();
    }

    /**
     * Sets up the toolbar with Up button.
     */
    private void setToolBar() {
        toolbar = (Toolbar) binding.toolbarMainPage;
        toolbar.setTitle(R.string.title_main_page);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
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
