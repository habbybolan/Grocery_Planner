package com.habbybolan.groceryplanner.listing.grocerylist;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityGroceryListBinding;
import com.habbybolan.groceryplanner.details.grocerydetails.GroceryIngredientsActivity;
import com.habbybolan.groceryplanner.listing.grocerylist.grocerylist.GroceryListFragment;
import com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.MyRecipeListActivity;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

public class GroceryListActivity extends AppCompatActivity implements GroceryListFragment.GroceryListListener {

    private ActivityGroceryListBinding binding;
    private int RETURNED_FROM_GROCERY_DETAILS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grocery_list);
    }

    @Override
    public void gotoRecipeList() {
        Intent intent = new Intent(this, MyRecipeListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_enter_from_left, R.anim.anim_slide_exit_to_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if returning from RecipeDetailActivity, reload the recipe and category list to represent any changes that may have occurred
        if (requestCode == RETURNED_FROM_GROCERY_DETAILS) {
            GroceryListFragment groceryListFragment = (GroceryListFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.GROCERY_LIST_TAG));
            if (groceryListFragment != null) groceryListFragment.resetList();
        }
    }

    @Override
    public void onItemClicked(Grocery grocery) {
        Intent intent = new Intent(this, GroceryIngredientsActivity.class);
        intent.putExtra(Grocery.GROCERY, grocery);
        startActivityForResult(intent, RETURNED_FROM_GROCERY_DETAILS);
    }
}
