package com.habbybolan.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.databinding.ActivityRecipeListBinding;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListFragment;
import com.habbybolan.groceryplanner.models.Recipe;

public class RecipeListActivity extends AppCompatActivity implements RecipeListFragment.RecipeListListener {

    private ActivityRecipeListBinding binding;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);
        setToolBar();
    }

    private void setToolBar() {
        toolbar = (Toolbar) binding.toolbarRecipeList;
        toolbar.setTitle(R.string.title_recipe_list);
        setSupportActionBar(toolbar);
    }

    @Override
    public void gotoGroceryList() {
        Intent intent = new Intent(this, GroceryListActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_enter_from_right, R.anim.anim_slide_exit_to_left);
    }

    @Override
    public void onItemClicked(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(Recipe.RECIPE, recipe);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(this, GroceryListActivity.class);
        startActivity(intent);
        finish();*/
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
