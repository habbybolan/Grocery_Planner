package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;

public interface RecipeListPresenter {

    void destroy();
    void deleteRecipe(Recipe recipe);
    void deleteRecipes(List<Recipe> recipes);
    void addRecipe(Recipe recipe);

    //
    void addRecipesToCategory(ArrayList<Recipe> recipe, RecipeCategory category);
    void removeRecipesFromCategory(ArrayList<Recipe> recipes);

    void setView(RecipeListView view);
    void createRecipeList();

    /**
     * Attached the current Recipe category to the presenter.
     * @param recipeCategory    Recipe Category to attach
     */
    void attachCategory(RecipeCategory recipeCategory);
    void fetchCategories() ;
    ArrayList<RecipeCategory> getLoadedRecipeCategories();
}
