package com.habbybolan.groceryplanner.models.ingredientmodels;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the Ingredients inside a Grocery list while representing
 */
public class GroceryIngredient extends Ingredient {

    // The recipes associated with this ingredient inside a particular grocery list
    private List<RecipeWithIngredient> recipeWithIngredients = new ArrayList<>();
    private boolean isChecked;

    public GroceryIngredient(Ingredient ingredient, boolean isChecked) {
        super(ingredient);
        this.isChecked = isChecked;
    }

    /**
     * Adds a recipe associated with the ingredient.
     * @param recipeWithIngredient  Holds the recipe associated with the ingredient the the
     *                              amount of the Ingredient it adds
     */
    public void addRecipe(RecipeWithIngredient recipeWithIngredient) {
        recipeWithIngredients.add(recipeWithIngredient);
    }

    public List<RecipeWithIngredient> getRecipeWithIngredients() {
        return recipeWithIngredients;
    }

    public Ingredient getIngredient() {
        return this;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    public boolean getIsChecked() {
        return isChecked;
    }
}
