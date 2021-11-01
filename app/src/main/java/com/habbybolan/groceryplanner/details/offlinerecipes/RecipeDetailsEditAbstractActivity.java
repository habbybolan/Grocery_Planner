package com.habbybolan.groceryplanner.details.offlinerecipes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit.RecipeIngredientsEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit.RecipeInstructionsEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit.RecipeNutritionEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionReadOnlyFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit.RecipeOverviewEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewReadOnlyFragment;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public abstract class RecipeDetailsEditAbstractActivity
        <T1 extends RecipeOverviewReadOnlyFragment,
                T2 extends RecipeNutritionReadOnlyFragment,
                T3 extends RecipeInstructionsReadOnlyFragment,
                T4 extends RecipeIngredientsReadOnlyFragment,
                U extends OfflineRecipe>
        extends RecipeDetailsReadOnlyAbstractActivity<T1, T2, T3, T4, U> {

    private boolean isReadOnlyMode = true;

    protected RecipeOverviewEditFragment overviewEditFragment;
    protected RecipeIngredientsEditFragment ingredientsEditFragment;
    protected RecipeInstructionsEditFragment instructionsEditFragment;
    protected RecipeNutritionEditFragment nutritionEditFragment;

    protected final String EDIT_TAG = "edit_tag";
    protected final String ADD_TAG = "add_tag";

    protected void setEditFragments() {
        // Overview
        overviewEditFragment = new RecipeOverviewEditFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_overview, overviewEditFragment)
                .commit();
        // detach the edit view
        getSupportFragmentManager().beginTransaction().detach(overviewEditFragment).commit();

        // Ingredients
        ingredientsEditFragment = new RecipeIngredientsEditFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_ingredients, ingredientsEditFragment)
                .commit();
        // detach the edit view
        getSupportFragmentManager().beginTransaction().detach(ingredientsEditFragment).commit();

        // Instructions
        instructionsEditFragment = new RecipeInstructionsEditFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_instructions, instructionsEditFragment)
                .commit();
        // detach the edit view
        getSupportFragmentManager().beginTransaction().detach(instructionsEditFragment).commit();

        // Nutrition
        nutritionEditFragment = new RecipeNutritionEditFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_nutrition, nutritionEditFragment)
                .commit();
        // detach the edit view
        getSupportFragmentManager().beginTransaction().detach(nutritionEditFragment).commit();
    }

    /**
     * Helper for swapping between the read-only and edit fragment
     * @param readOnlyFragment  Read-only fragment
     * @param editFragment      Edit fragment
     */
    private void swapView(Fragment readOnlyFragment, Fragment editFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isReadOnlyMode) {
            transaction.detach(readOnlyFragment);
            transaction.attach(editFragment);
        } else {
            transaction.detach(editFragment);
            transaction.attach(readOnlyFragment);
        }
        transaction.commit();
        isReadOnlyMode = !isReadOnlyMode;
    }

    protected void swapViewOverview() {
        swapView(overviewReadOnlyFragment, overviewEditFragment);
    }

    protected void swapViewNutrition() {
        swapView(nutritionReadOnlyFragment, nutritionEditFragment);
    }

    protected void swapViewInstructions() {
        swapView(instructionsReadOnlyFragment, instructionsEditFragment);
    }

    protected void swapViewIngredients() {
        swapView(ingredientsReadOnlyFragment, ingredientsEditFragment);
    }
}
