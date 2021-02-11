package com.habbybolan.groceryplanner.database;

import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.IngredientHolder;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.Step;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DatabaseAccess {

    void deleteGrocery(Grocery grocery);
    void deleteGroceries(List<Grocery> groceries);
    void addGrocery(Grocery grocery);
    List<Grocery> fetchGroceries() throws ExecutionException, InterruptedException;

    void deleteRecipe(Recipe recipe);
    void deleteRecipes(List<Recipe> recipes);
    void addRecipe(Recipe recipe);
    List<Recipe> fetchRecipes() throws ExecutionException, InterruptedException;

    List<Step> fetchStepsFromRecipe(long recipeId) throws ExecutionException, InterruptedException;
    void addStep(Step step);

    void addIngredients(IngredientHolder ingredientHolder, List<Ingredient> ingredients);
    void addIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);
    void deleteIngredients(IngredientHolder ingredientHolder, List<Ingredient> ingredients);
    void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);
    void deleteIngredientHolderRelationship(IngredientHolder ingredientHolder, Ingredient ingredient);
    List<Ingredient> fetchIngredientsFromGrocery(Grocery grocery) throws ExecutionException, InterruptedException;
    List<Ingredient> fetchIngredientsFromRecipe(Recipe recipe) throws ExecutionException, InterruptedException;
}
