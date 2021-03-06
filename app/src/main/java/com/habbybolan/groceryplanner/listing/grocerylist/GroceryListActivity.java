package com.habbybolan.groceryplanner.listing.grocerylist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.details.grocerydetails.GroceryDetailsActivity;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityGroceryListBinding;
import com.habbybolan.groceryplanner.listing.grocerylist.grocerylist.GroceryListFragment;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;
import com.habbybolan.groceryplanner.models.Grocery;

public class GroceryListActivity extends AppCompatActivity implements GroceryListFragment.GroceryListListener {

    private ActivityGroceryListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grocery_list);
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
}
