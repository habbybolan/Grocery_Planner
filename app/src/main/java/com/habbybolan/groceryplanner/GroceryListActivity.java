package com.habbybolan.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.databinding.ActivityGroceryListBinding;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListFragment;
import com.habbybolan.groceryplanner.models.Grocery;

public class GroceryListActivity extends AppCompatActivity implements GroceryListFragment.GroceryListListener {

    private ActivityGroceryListBinding binding;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grocery_list);
        setToolBar();
    }

    /**
     * Sets up the toolbar with Up button.
     */
    private void setToolBar() {
        toolbar = (Toolbar) binding.toolbarGroceryList;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.burger_menu, menu);
        return true;
    }

    @Override
    public void gotoRecipeList() {
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_enter_from_left, R.anim.anim_slide_exit_to_right);
    }

    @Override
    public void onItemClicked(Grocery grocery) {
        Intent intent = new Intent(this, GroceryDetailsActivity.class);
        intent.putExtra(Grocery.GROCERY, grocery);
        startActivity(intent);
    }

    @Override
    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }
}
