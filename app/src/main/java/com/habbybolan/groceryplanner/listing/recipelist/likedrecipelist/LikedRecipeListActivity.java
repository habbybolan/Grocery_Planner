package com.habbybolan.groceryplanner.listing.recipelist.likedrecipelist;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityLikedRecipeListBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.likedrecipe.RecipeDetailsLikedRecipeActivity;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;
import com.habbybolan.groceryplanner.listing.recipelist.likedrecipelist.recipelist.LikedRecipeListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public class LikedRecipeListActivity extends RecipeListActivity<LikedRecipeListFragment> {

    private ActivityLikedRecipeListBinding binding;

    @Override
    protected void setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_liked_recipe_list);
    }

    @Override
    public void onRecipeItemClicked(OfflineRecipe offlineRecipe) {
        Intent intent = new Intent(this, RecipeDetailsLikedRecipeActivity.class);
        intent.putExtra(OfflineRecipe.RECIPE, offlineRecipe.getId());
        startActivityForResult(intent, RETURNED_FROM_RECIPE_DETAILS);
    }

    @Override
    protected void initListeners() {
        recipeListListener = new RecipeListListener();
        recipeCategoryListener = new RecipeCategoryListener();
    }

    @Override
    protected void initContainers() {
        recipeListContainer = binding.recipeListContainer;
        recipeCategoryContainer = binding.recipeCategoryContainer;
    }
}