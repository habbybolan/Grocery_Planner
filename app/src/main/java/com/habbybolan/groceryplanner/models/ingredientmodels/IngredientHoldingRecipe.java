package com.habbybolan.groceryplanner.models.ingredientmodels;

import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

import java.util.ArrayList;
import java.util.List;

/*
Ingredients with functionality to hold recipes that are associated with it.
Used for creating Grocery Ingredient lists and displaying if the Ingredient is from a Recipe.
 */
public class IngredientHoldingRecipe extends Ingredient {

    // Recipes associated with this Ingredients
    private List<Recipe> recipes = new ArrayList<>();

    public IngredientHoldingRecipe(Ingredient ingredient, List<Recipe> recipes) {
        super(ingredient);
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }
}
