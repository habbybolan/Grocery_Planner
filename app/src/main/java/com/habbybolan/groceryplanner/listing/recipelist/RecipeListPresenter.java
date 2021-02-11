package com.habbybolan.groceryplanner.listing.recipelist;

import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;

public interface RecipeListPresenter {

    void destroy();
    void deleteRecipe(Recipe recipe);
    void deleteRecipes(List<Recipe> recipes);
    void addRecipe(Recipe recipe);
    void setView(ListViewInterface view);
    void createRecipeList();

}
