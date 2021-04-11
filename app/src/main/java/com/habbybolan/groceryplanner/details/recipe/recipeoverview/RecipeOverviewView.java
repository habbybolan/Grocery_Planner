package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.List;

public interface RecipeOverviewView {
    void loadingStarted();
    void loadingFailed(String message);

    /**
     * Displays the Array of category names in an Alert Dialogue
     * @param categoryNames The Array of category names to display
     */
    void createCategoriesAlertDialogue(String[] categoryNames);

    /**
     * Displays the current RecipeCategory in the fragment
     * @param recipeCategory    RecipeCategory's name to display
     */
    void displayRecipeCategory(RecipeCategory recipeCategory);

    /**
     * Displays the Groceries that are holding the current Recipe.
     * @param groceries The Groceries holding the Recipe to display
     */
    void displayGroceriesHoldingRecipe(List<GroceryRecipe> groceries);

    /**
     * Displays ingredients, checked if they will be added /  already added to Grocery list
     * @param ingredients       ArrayList of ingredients objects that can be selected
     * @param ingredientNames   Array of Ingredient names to display in list.
     * @param grocery           Grocery list to add the recipe ingredients to
     */
    void displayRecipeIngredients(List<IngredientWithGroceryCheck> ingredients, String[] ingredientNames, Grocery grocery);

    /**
     * Displays the Groceries not holding the recipes inside a Dialog.
     * @param groceries Groceries to display not holding the recipe
     */
    void displayGroceriesNotHoldingRecipe(List<Grocery> groceries);

    /**
     * Display the ingredients that are added and not added to the Grocery list through the current Recipe.
     * @param grocery   The Grocery holding the ingredients
     */
    void displayRecipeIngredientsInGrocery(Grocery grocery);
}
