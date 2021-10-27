package com.habbybolan.groceryplanner.listing.recipelist.myrecipelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityRecipeListBinding;
import com.habbybolan.groceryplanner.details.myrecipe.detailsactivity.RecipeIngredientsActivity;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;
import com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.recipelist.MyRecipeListFragment;
import com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist.RecipeCategoryFragment;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

public class MyRecipeListActivity extends RecipeListActivity {

    private ActivityRecipeListBinding binding;
    protected RecipeListListener recipeListListener;
    private final static int RETURNED_FROM_RECIPE_DETAILS = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);

        Bundle extras = getIntent().getExtras();
        // get the bundled Ingredients to display in the fragment if any exist.
        if (extras != null && extras.containsKey(RecipeCategory.RECIPE_CATEGORY))
            recipeCategory = extras.getParcelable(RecipeCategory.RECIPE_CATEGORY);

        RECIPE_LIST_TAG = getString(R.string.RECIPE_LIST_TAG);
        RECIPE_CATEGORY_TAG = getString(R.string.RECIPE_CATEGORY_TAG);

        initListeners();
    }

    private void initListeners() {
        recipeListListener = new RecipeListListener();
        recipeCategoryListener = new RecipeCategoryListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        // manually attach the listeners as they are created explicitly in this activity and sent to the fragments
        MyRecipeListFragment myRecipeListFragment = (MyRecipeListFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_LIST_TAG);
        if (myRecipeListFragment != null) {
            myRecipeListFragment.attachListener(recipeListListener);
            myRecipeListFragment.resetList(recipeCategory);
        }
        RecipeCategoryFragment recipeCategoryFragment = (RecipeCategoryFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_CATEGORY_TAG);
        if (recipeCategoryFragment != null) recipeCategoryFragment.attachListener(recipeCategoryListener);
    }

    /**
     * Sends the app the the RecipeDetailsActivity for selecting a specific recipe.
     * @param offlineRecipe    The recipe info to display
     */
    @Override
    public void onRecipeItemClicked(OfflineRecipe offlineRecipe) {
        Intent intent = new Intent(this, RecipeIngredientsActivity.class);
        intent.putExtra(OfflineRecipe.RECIPE, offlineRecipe.getId());
        startActivityForResult(intent, RETURNED_FROM_RECIPE_DETAILS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if returning from RecipeDetailActivity, reload the recipe and category list to represent any changes that may have occurred
        if (requestCode == RETURNED_FROM_RECIPE_DETAILS) {
            MyRecipeListFragment myRecipeListFragment = (MyRecipeListFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.RECIPE_LIST_TAG));
            if (myRecipeListFragment != null) myRecipeListFragment.resetList(recipeCategory);
            RecipeCategoryFragment recipeCategoryFragment = (RecipeCategoryFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.RECIPE_CATEGORY_TAG));
            if (recipeCategoryFragment != null) recipeCategoryFragment.resetList();
        }
    }

    /**
     * Clicker functionality for selecting a recipe category. Brings up RecipeListFragment to display the recipes associated with the category clicked.
     * @param recipeCategory    The recipe category that holds the Recipes to display.
     */
    @Override
    protected void gotoRecipeListCategorized(RecipeCategory recipeCategory) {
        binding.recipeCategoryContainer.setVisibility(View.GONE);
        this.recipeCategory = recipeCategory;
        MyRecipeListFragment myRecipeListFragment = (MyRecipeListFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_LIST_TAG);
        if (myRecipeListFragment != null) {
            binding.recipeListContainer.setVisibility(View.VISIBLE);
            myRecipeListFragment.resetList(recipeCategory);
        }
    }

    @Override
    protected void gotoRecipeListUnCategorized() {
        gotoRecipeListCategorized(null);
    }

    @Override
    protected void gotoRecipeCategories() {
        binding.recipeListContainer.setVisibility(View.GONE);
        binding.recipeCategoryContainer.setVisibility(View.VISIBLE);
    }

    /**
     * @return  True if the Recipe List Fragment in currently visible
     */
    @Override
    protected boolean isRecipeListVisible() {
        return binding.recipeListContainer.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onBackPressed() {
        if (isRecipeListVisible()) gotoRecipeCategories();
        else super.onBackPressed();
    }

    /**
     * Listener to implement methods in RecipeListFragment listener interface.
     */
    private class RecipeListListener implements MyRecipeListFragment.RecipeListListener {

        @Override
        public void onItemClicked(OfflineRecipe offlineRecipe) {
            onRecipeItemClicked(offlineRecipe);
        }

        @Override
        public void gotoRecipeListUnCategorized() {
            MyRecipeListActivity.this.gotoRecipeListUnCategorized();
        }

        @Override
        public void gotoRecipeCategories() {
            MyRecipeListActivity.this.gotoRecipeCategories();
        }
    }


}
