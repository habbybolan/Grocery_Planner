package com.habbybolan.groceryplanner.listing.recipelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist.RecipeCategoryFragment;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

public abstract class RecipeListActivity<T extends RecipeListFragment> extends AppCompatActivity {

    protected String RECIPE_LIST_TAG;
    protected String RECIPE_CATEGORY_TAG;

    protected RecipeCategoryListener recipeCategoryListener;
    protected RecipeListListener recipeListListener;

    protected RecipeCategory recipeCategory;

    protected ViewGroup recipeCategoryContainer;
    protected ViewGroup recipeListContainer;

    protected final static int RETURNED_FROM_RECIPE_DETAILS = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBinding();

        Bundle extras = getIntent().getExtras();
        // get the bundled Ingredients to display in the fragment if any exist.
        if (extras != null && extras.containsKey(RecipeCategory.RECIPE_CATEGORY))
            recipeCategory = extras.getParcelable(RecipeCategory.RECIPE_CATEGORY);

        RECIPE_LIST_TAG = getString(R.string.RECIPE_LIST_TAG);
        RECIPE_CATEGORY_TAG = getString(R.string.RECIPE_CATEGORY_TAG);

        initListeners();
        initContainers();
    }

    @Override
    public void onResume() {
        super.onResume();
        // manually attach the listeners as they are created explicitly in this activity and sent to the fragments
        T recipeListFragment = (T) getSupportFragmentManager().findFragmentByTag(RECIPE_LIST_TAG);
        if (recipeListFragment != null) {
            recipeListFragment.attachListener(recipeListListener);
            recipeListFragment.resetList(recipeCategory);
        }
        RecipeCategoryFragment recipeCategoryFragment = (RecipeCategoryFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_CATEGORY_TAG);
        if (recipeCategoryFragment != null) recipeCategoryFragment.attachListener(recipeCategoryListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if returning from details activity, reload the recipe and category list to represent any changes that may have occurred
        if (requestCode == RETURNED_FROM_RECIPE_DETAILS) {
            T recipeListFragment = (T) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.RECIPE_LIST_TAG));
            if (recipeListFragment != null) recipeListFragment.resetList(recipeCategory);
            RecipeCategoryFragment recipeCategoryFragment = (RecipeCategoryFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.RECIPE_CATEGORY_TAG));
            if (recipeCategoryFragment != null) recipeCategoryFragment.resetList();
        }
    }

    /**
     * Clicker functionality for displaying recipe categories.
     */
    protected void gotoRecipeCategories() {
        recipeListContainer.setVisibility(View.GONE);
        recipeCategoryContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Clicker functionality for selecting a recipe category. Brings up RecipeListFragment to display the recipes associated with the category clicked.
     * @param recipeCategory    The recipe category that holds the Recipes to display.
     */
    protected void gotoRecipeListCategorized(RecipeCategory recipeCategory) {
        recipeCategoryContainer.setVisibility(View.GONE);
        this.recipeCategory = recipeCategory;
        T recipeListFragment = (T) getSupportFragmentManager().findFragmentByTag(RECIPE_LIST_TAG);
        if (recipeListFragment != null) {
            recipeListContainer.setVisibility(View.VISIBLE);
            recipeListFragment.resetList(recipeCategory);
        }
    }


    protected void gotoRecipeListUnCategorized() {
        gotoRecipeListCategorized(null);
    }

    protected RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    /**
     * @return  true if the recipe list container is visible.
     */
    protected boolean isRecipeListVisible() {
        return recipeListContainer.getVisibility() == View.VISIBLE;
    }

    public void onCategoryListChange() {
        T fragment = (T) getSupportFragmentManager().findFragmentByTag(RECIPE_LIST_TAG);
        fragment.onCategoryListChanged();
    }

    /**
     * Listener to implement methods in RecipeCategoryFragment listener interface.
     */
    public class RecipeCategoryListener implements RecipeCategoryFragment.RecipeCategoryListener {

        @Override
        public void onItemClicked(RecipeCategory recipeCategory) {
            gotoRecipeListCategorized(recipeCategory);
        }

        @Override
        public void gotoRecipeListUnCategorized() {
            RecipeListActivity.this.gotoRecipeListUnCategorized();
        }

        @Override
        public void gotoRecipeCategories() {
            RecipeListActivity.this.gotoRecipeCategories();
        }

        @Override
        public void onCategoryListChange() {
            RecipeListActivity.this.onCategoryListChange();
        }
    }

    /**
     * Listener to implement methods in RecipeListFragment listener interface.
     */
    public class RecipeListListener implements RecipeListFragment.RecipeListListener {

        @Override
        public void onItemClicked(OfflineRecipe offlineRecipe) {
            onRecipeItemClicked(offlineRecipe);
        }

        @Override
        public void gotoRecipeListUnCategorized() {
            RecipeListActivity.this.gotoRecipeListUnCategorized();
        }

        @Override
        public void gotoRecipeCategories() {
            RecipeListActivity.this.gotoRecipeCategories();
        }
    }

    @Override
    public void onBackPressed() {
        if (isRecipeListVisible()) gotoRecipeCategories();
        else super.onBackPressed();
    }

    abstract public void onRecipeItemClicked(OfflineRecipe offlineRecipe);

    /**
     * Child set their binding to attach the view;
     */
    abstract protected void setupBinding();

    /**
     * Set up the category and recipe list listeners.
     */
    abstract protected void initListeners();

    /**
     * Set up the category and recipe list ViewGroup containers.
     */
    abstract protected void initContainers();
}
