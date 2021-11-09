package com.habbybolan.groceryplanner.listing.recipelist.myrecipelist;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityRecipeListBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe.RecipeDetailsMyRecipeActivity;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;
import com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.recipelist.MyRecipeListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public class MyRecipeListActivity extends RecipeListActivity<MyRecipeListFragment> {

    private ActivityRecipeListBinding binding;

    @Override
    protected void setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);
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

    /**
     * Sends the app the the RecipeDetailsActivity for selecting a specific recipe.
     * @param offlineRecipe    The recipe info to display
     */
    @Override
    public void onRecipeItemClicked(OfflineRecipe offlineRecipe) {
        Intent intent = new Intent(this, RecipeDetailsMyRecipeActivity.class);
        intent.putExtra(OfflineRecipe.RECIPE, offlineRecipe.getId());
        startActivityForResult(intent, RETURNED_FROM_RECIPE_DETAILS);
    }
}
