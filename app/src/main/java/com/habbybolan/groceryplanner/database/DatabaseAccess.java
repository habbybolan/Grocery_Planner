package com.habbybolan.groceryplanner.database;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.IngredientHolder;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;
import com.habbybolan.groceryplanner.models.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DatabaseAccess {

    void deleteGrocery(Grocery grocery);
    void deleteGroceries(List<Grocery> groceries);
    void addGrocery(Grocery grocery);
    void fetchGroceries(ObservableArrayList<Grocery> groceriesObserver) throws ExecutionException, InterruptedException;

    void deleteRecipe(Recipe recipe);
    void deleteRecipes(List<Recipe> recipes);
    void addRecipe(Recipe recipe);
    void fetchRecipes(Long recipeCategoryId, ObservableArrayList<Recipe> recipesObserver) throws ExecutionException, InterruptedException;
    List<Recipe> fetchUnCategorizedRecipes() throws ExecutionException, InterruptedException;
    void updateRecipes(ArrayList<Recipe> recipes);
    void fetchRecipe(ObservableField<Recipe> recipeObserver, long recipeId) throws ExecutionException, InterruptedException;

    void deleteRecipeCategory(RecipeCategory recipeCategory);
    void deleteRecipeCategories(List<RecipeCategory> recipeCategories);
    void fetchRecipeCategories(ObservableArrayList<RecipeCategory> recipeCategoriesObserved) throws ExecutionException, InterruptedException;
    void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long recipeCategoryId) throws ExecutionException, InterruptedException;
    void addRecipeCategory(RecipeCategory recipeCategory);

    List<Step> fetchStepsFromRecipe(long recipeId) throws ExecutionException, InterruptedException;
    void addStep(Step step);

    void addIngredients(IngredientHolder ingredientHolder, List<Ingredient> ingredients);
    void addIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);
    void deleteIngredients(IngredientHolder ingredientHolder, List<Ingredient> ingredients);
    void deleteIngredient(IngredientHolder ingredientHolder, Ingredient ingredient);
    void deleteIngredientHolderRelationship(IngredientHolder ingredientHolder, Ingredient ingredient);
    void fetchIngredientsFromGrocery(Grocery grocery, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException;
    void fetchIngredientsFromRecipe(Recipe recipe, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException;
    void fetchIngredientsNotInRecipe(Recipe recipe, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException;
    void fetchIngredientsNotInGrocery(Grocery grocery, ObservableArrayList<Ingredient> ingredientsObserver) throws ExecutionException, InterruptedException;
}
