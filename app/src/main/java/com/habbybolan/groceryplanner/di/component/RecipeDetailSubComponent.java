package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.details.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsFragment;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionFragment;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewFragment;
import com.habbybolan.groceryplanner.details.recipe.recipesteps.RecipeStepFragment;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.di.scope.DetailScope;

import dagger.Subcomponent;

@DetailScope
@Subcomponent(modules = {RecipeDetailModule.class, IngredientEditModule.class})
public interface RecipeDetailSubComponent {

    void inject(RecipeNutritionFragment fragment);
    void inject(RecipeOverviewFragment fragment);
    void inject(IngredientEditFragment fragment);
    void inject(RecipeIngredientsFragment fragment);
    void inject(RecipeStepFragment fragment);
}
