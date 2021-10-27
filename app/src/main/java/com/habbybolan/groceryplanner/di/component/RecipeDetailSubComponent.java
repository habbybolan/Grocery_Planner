package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.details.myrecipe.detailsactivity.RecipeIngredientsActivity;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.edit.RecipeIngredientsEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.readonly.RecipeIngredientsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.readonly.RecipeInstructionsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.edit.RecipeNutritionEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.readonly.RecipeNutritionReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.overview.readonly.RecipeOverviewReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.overview.edit.RecipeOverviewEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.edit.RecipeInstructionsEditFragment;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.di.scope.DetailScope;

import dagger.Subcomponent;

@DetailScope
@Subcomponent(modules = {RecipeDetailModule.class})
public interface RecipeDetailSubComponent {

    void inject(RecipeOverviewEditFragment fragment);
    void inject(RecipeOverviewReadOnlyFragment fragment);

    void inject(RecipeInstructionsEditFragment fragment);
    void inject(RecipeInstructionsReadOnlyFragment recipeInstructionsReadOnlyFragment);

    void inject(RecipeNutritionEditFragment fragment);
    void inject(RecipeNutritionReadOnlyFragment recipeNutritionReadOnlyFragment);

    void inject(RecipeIngredientsEditFragment fragment);
    void inject(RecipeIngredientsReadOnlyFragment recipeIngredientsReadOnlyFragment);

    void inject(RecipeIngredientsActivity activity);




}
