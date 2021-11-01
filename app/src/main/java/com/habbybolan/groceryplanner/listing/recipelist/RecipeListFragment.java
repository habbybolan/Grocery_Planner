package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.listfragments.CategoryListFragment;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

public abstract class RecipeListFragment extends CategoryListFragment<OfflineRecipe> implements ListViewInterface<OfflineRecipe> {

    public abstract void resetList(RecipeCategory recipeCategory);

    public abstract void attachListener(RecipeListListener listener);

    public abstract void onCategoryListChanged();

    public interface RecipeListListener extends ItemListener<OfflineRecipe> {

        void gotoRecipeListUnCategorized();
        void gotoRecipeCategories();
    }
}
