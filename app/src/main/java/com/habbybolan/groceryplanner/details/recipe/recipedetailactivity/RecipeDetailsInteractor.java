package com.habbybolan.groceryplanner.details.recipe.recipedetailactivity;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

public interface RecipeDetailsInteractor {

    void getRecipe(ObservableField<Recipe> recipeObserver, long recipeId);
    void getRecipeCategory(ObservableField<RecipeCategory> recipeObserver, long recipeCategoryId);
}
