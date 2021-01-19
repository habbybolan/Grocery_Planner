package com.habbybolan.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.databinding.ActivityGroceryListBinding;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListFragment;
import com.habbybolan.groceryplanner.models.Grocery;

public class GroceryListActivity extends AppCompatActivity implements GroceryListFragment.GroceryListListener {


    private ActivityGroceryListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grocery_list);
        setToolBar();
    }

    /**
     * Sets up the toolbar.
     */
    private void setToolBar() {
        Toolbar toolbar = (Toolbar) binding.toolbarGroceryList;
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.burger_menu, menu);
        return true;
    }

    @Override
    public void onGroceryClicked(Grocery grocery) {
        Intent intent = new Intent(this, GroceryDetailsActivity.class);
        intent.putExtra(Grocery.GROCERY, grocery);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
        finish();
    }
}
