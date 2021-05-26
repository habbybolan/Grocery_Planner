package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

public interface RecipeListView extends ListViewInterface<OfflineRecipe> {

    RecipeCategory getRecipeCategory();
}
