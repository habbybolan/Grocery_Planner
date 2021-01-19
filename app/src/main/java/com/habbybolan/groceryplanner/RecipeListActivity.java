package com.habbybolan.groceryplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.databinding.ActivityRecipeListBinding;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListFragment;
import com.habbybolan.groceryplanner.models.Recipe;

public class RecipeListActivity extends AppCompatActivity implements RecipeListFragment.RecipeListListener {

    private ActivityRecipeListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);
        setToolBar();
    }

    private void setToolBar() {
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) binding.toolbarRecipeList;
        toolbar.setTitle(R.string.title_recipe_list);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(Recipe.RECIPE, recipe);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, GroceryListActivity.class);
        startActivity(intent);
        finish();
    }
}
