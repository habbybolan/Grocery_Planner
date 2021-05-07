package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public interface RecipeListPresenter {

    void destroy();
    void deleteRecipe(Recipe recipe);
    void deleteRecipes(List<Recipe> recipes);
    void addRecipe(Recipe recipe, Timestamp dateCreated);

    void addRecipesToCategory(ArrayList<Recipe> recipe, RecipeCategory category);
    void removeRecipesFromCategory(ArrayList<Recipe> recipes);

    void setView(RecipeListView view);
    void createRecipeList();

    void fetchCategories() ;
    ArrayList<RecipeCategory> getLoadedRecipeCategories();

    /**
     * Search for the recipes with name recipeSearch.
     * @param recipeSearch   recipe to search for
     */
    void searchRecipes(String recipeSearch);
}
