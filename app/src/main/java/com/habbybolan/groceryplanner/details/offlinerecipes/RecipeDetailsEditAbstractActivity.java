package com.habbybolan.groceryplanner.details.offlinerecipes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsEditFragmentsBinding;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsReadOnlyFragmentsBinding;
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

    protected RecipeDetailsEditFragmentsBinding editBinding;

    protected void setEditFragments() {
        // if null, probably used the setViews from read-only parent
        assert(editBinding != null);
        // Overview
        overviewEditFragment = RecipeOverviewEditFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_edit_overview, overviewEditFragment)
                .hide(overviewEditFragment)
                .commit();

        // Ingredients
        ingredientsEditFragment = RecipeIngredientsEditFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_edit_ingredients, ingredientsEditFragment)
                .hide(overviewEditFragment)
                .commit();

        // Instructions
        instructionsEditFragment = RecipeInstructionsEditFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_edit_instructions, instructionsEditFragment)
                .hide(overviewEditFragment)
                .commit();

        // Nutrition
        nutritionEditFragment = RecipeNutritionEditFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_edit_nutrition, nutritionEditFragment)
                .hide(overviewEditFragment)
                .commit();
    }

    /**
     * Helper for swapping between the read-only and edit mode of a details view
     * @param readOnlyFragment  Read-only fragment
     * @param editFragment      Edit fragment
     */
    private void swapView(Fragment readOnlyFragment, Fragment editFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isReadOnlyMode) {
            transaction.hide(readOnlyFragment);
            transaction.show(editFragment);
        } else {
            transaction.hide(editFragment);
            transaction.show(readOnlyFragment);
        }
        transaction.commit();
        isReadOnlyMode = !isReadOnlyMode;
    }

    protected void swapViewOverview() {
        // reload read-only in case any changes made
        if (!isReadOnlyMode) overviewReadOnlyFragment.updateRecipe();
        swapView(overviewReadOnlyFragment, overviewEditFragment);
    }

    protected void swapViewNutrition() {
        // reload read-only in case any changes made
        if (!isReadOnlyMode) nutritionReadOnlyFragment.updateRecipe();
        swapView(nutritionReadOnlyFragment, nutritionEditFragment);
    }

    protected void swapViewInstructions() {
        // reload read-only in case any changes made
        if (!isReadOnlyMode) instructionsReadOnlyFragment.updateRecipe();
        swapView(instructionsReadOnlyFragment, instructionsEditFragment);
    }

    protected void swapViewIngredients() {
        // reload read-only in case any changes made
        if (!isReadOnlyMode) ingredientsReadOnlyFragment.updateRecipe();
        swapView(ingredientsReadOnlyFragment, ingredientsEditFragment);
    }

    @Override
    protected void showOverviewFragment() {
        isReadOnlyMode = true;
        hideAllEditFragments();
        super.showOverviewFragment();
    }
    @Override
    protected void showIngredientsFragment() {
        isReadOnlyMode = true;
        hideAllEditFragments();
        super.showIngredientsFragment();
    }
    @Override
    protected void showInstructionsFragment() {
        isReadOnlyMode = true;
        hideAllEditFragments();
        super.showInstructionsFragment();
    }
    @Override
    protected void showNutritionFragment() {
        isReadOnlyMode = true;
        hideAllEditFragments();
        super.showNutritionFragment();
    }

    private void hideAllEditFragments() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .hide(overviewEditFragment)
                .hide(instructionsEditFragment)
                .hide(ingredientsEditFragment)
                .hide(nutritionEditFragment)
                .commit();
    }

    protected void setViews(BottomNavigationView view, RecipeDetailsReadOnlyFragmentsBinding readOnlyBinding, RecipeDetailsEditFragmentsBinding editBinding) {
        this.editBinding = editBinding;
        setEditFragments();
        super.setViews(view, readOnlyBinding);
    }
}
