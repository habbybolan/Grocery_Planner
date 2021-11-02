package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.likedrecipe.RecipeDetailsLikedRecipeActivity;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsRecipeActivity;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit.RecipeIngredientsEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsLikedRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsMyRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit.RecipeInstructionsEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsLikedRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsMyRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit.RecipeNutritionEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionLikedRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionMyRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit.RecipeOverviewEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes.AddRecipeToGroceryListFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewLikedRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewMyRecipeFragment;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.di.scope.DetailScope;

import dagger.Subcomponent;

@DetailScope
@Subcomponent(modules = {RecipeDetailModule.class})
public interface RecipeDetailSubComponent {

    void inject(RecipeOverviewEditFragment fragment);
    void inject(RecipeOverviewMyRecipeFragment fragment);
    void inject(RecipeOverviewLikedRecipeFragment fragment);

    void inject(RecipeInstructionsEditFragment fragment);
    void inject(RecipeInstructionsMyRecipeFragment fragment);
    void inject(RecipeInstructionsLikedRecipeFragment fragment);

    void inject(RecipeNutritionEditFragment fragment);
    void inject(RecipeNutritionMyRecipeFragment fragment);
    void inject(RecipeNutritionLikedRecipeFragment fragment);

    void inject(RecipeIngredientsEditFragment fragment);
    void inject(RecipeIngredientsLikedRecipeFragment fragment);
    void inject(RecipeIngredientsMyRecipeFragment fragment);

    void inject(RecipeDetailsRecipeActivity activity);

    void inject(RecipeDetailsLikedRecipeActivity recipeDetailsLikedRecipeActivity);

    void inject(AddRecipeToGroceryListFragment addRecipeToGroceryListFragment);

}
