package com.habbybolan.groceryplanner.listing.recipelist;

import androidx.appcompat.app.AppCompatActivity;

import com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.recipelist.MyRecipeListFragment;
import com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist.RecipeCategoryFragment;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

public abstract class RecipeListActivity extends AppCompatActivity {

    protected String RECIPE_LIST_TAG;
    protected String RECIPE_CATEGORY_TAG;
    protected RecipeCategoryListener recipeCategoryListener;
    protected RecipeCategory recipeCategory;

    abstract public void onRecipeItemClicked(OfflineRecipe offlineRecipe);
    abstract protected void gotoRecipeListCategorized(RecipeCategory recipeCategory);
    abstract protected boolean isRecipeListVisible();
    abstract protected void gotoRecipeCategories();
    abstract protected void gotoRecipeListUnCategorized();

    protected RecipeCategory getRecipeCategory() {
        return recipeCategory;
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
            MyRecipeListFragment fragment = (MyRecipeListFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_LIST_TAG);
            fragment.onCategoryListChanged();
        }
    }
}
