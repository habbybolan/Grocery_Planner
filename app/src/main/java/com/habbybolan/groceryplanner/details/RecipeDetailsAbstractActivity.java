package com.habbybolan.groceryplanner.details;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsFragmentsBinding;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.edit.RecipeIngredientsEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.readonly.RecipeIngredientsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.edit.RecipeInstructionsEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.readonly.RecipeInstructionsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.edit.RecipeNutritionEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.readonly.RecipeNutritionReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.overview.edit.RecipeOverviewEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.overview.readonly.RecipeOverviewReadOnlyFragment;

public abstract class RecipeDetailsAbstractActivity extends AppCompatActivity {

    private boolean isReadOnlyMode = true;

    protected RecipeDetailsFragmentsBinding fragmentsBinding;


    protected RecipeOverviewEditFragment overviewEditFragment;
    protected RecipeOverviewReadOnlyFragment overviewReadOnlyFragment;


    protected RecipeIngredientsEditFragment ingredientsEditFragment;
    protected RecipeIngredientsReadOnlyFragment ingredientsReadOnlyFragment;

    protected RecipeInstructionsEditFragment instructionsEditFragment;
    protected RecipeInstructionsReadOnlyFragment instructionsReadOnlyFragment;

    protected RecipeNutritionEditFragment nutritionEditFragment;
    protected RecipeNutritionReadOnlyFragment nutritionReadOnlyFragment;

    private void setBottomNavigation(BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.item_overview) {
                showOverviewFragment(fragmentsBinding.containerOverview, fragmentsBinding.containerIngredients,
                        fragmentsBinding.containerInstructions, fragmentsBinding.containerNutrition);
            } else if (id == R.id.item_ingredients) {
                showIngredientsFragment(fragmentsBinding.containerOverview, fragmentsBinding.containerIngredients,
                        fragmentsBinding.containerInstructions, fragmentsBinding.containerNutrition);
            }else if (id == R.id.item_instructions) {
                showInstructionsFragment(fragmentsBinding.containerOverview, fragmentsBinding.containerIngredients,
                        fragmentsBinding.containerInstructions, fragmentsBinding.containerNutrition);
            } else {
                showNutritionFragment(fragmentsBinding.containerOverview, fragmentsBinding.containerIngredients,
                        fragmentsBinding.containerInstructions, fragmentsBinding.containerNutrition);
            }
            return true;
        });
    }

    private void showOverviewFragment(ViewGroup overview, ViewGroup ingredients, ViewGroup instructions, ViewGroup nutrition) {
        nutrition.setVisibility(View.GONE);
        instructions.setVisibility(View.GONE);
        ingredients.setVisibility(View.GONE);
        overview.setVisibility(View.VISIBLE);
    }
    private void showIngredientsFragment(ViewGroup overview, ViewGroup ingredients, ViewGroup instructions, ViewGroup nutrition) {
        nutrition.setVisibility(View.GONE);
        instructions.setVisibility(View.GONE);
        ingredients.setVisibility(View.VISIBLE);
        overview.setVisibility(View.GONE);
    }
    private void showInstructionsFragment(ViewGroup overview, ViewGroup ingredients, ViewGroup instructions, ViewGroup nutrition) {
        nutrition.setVisibility(View.GONE);
        instructions.setVisibility(View.VISIBLE);
        ingredients.setVisibility(View.GONE);
        overview.setVisibility(View.GONE);
    }
    private void showNutritionFragment(ViewGroup overview, ViewGroup ingredients, ViewGroup instructions, ViewGroup nutrition) {
        nutrition.setVisibility(View.VISIBLE);
        instructions.setVisibility(View.GONE);
        ingredients.setVisibility(View.GONE);
        overview.setVisibility(View.GONE);
    }

    /**
     * Sets the bottom navigation and calls the method to set the Fragments implemented in the child class
     * @param view             Bottom navigation view
     * @param fragmentsBinding Included binding layout that holds the fragments to navigate to through bottom navigation
     */
    protected void setViews(BottomNavigationView view, RecipeDetailsFragmentsBinding fragmentsBinding) {
        this.fragmentsBinding = fragmentsBinding;
        setBottomNavigation(view);
    }

    /**
     * Set up the four fragments recipeIngredientsFragment, recipeInstructionsFragment,
     * recipeOverviewFragment, recipeNutritionFragment
     */
    protected void setFragments() {
        // Overview
        overviewEditFragment = new RecipeOverviewEditFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_overview, overviewEditFragment)
                .commit();
        overviewReadOnlyFragment = new RecipeOverviewReadOnlyFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_overview, overviewReadOnlyFragment)
                .commit();
        // detach the edit view
        getSupportFragmentManager().beginTransaction().detach(overviewEditFragment).commit();

        // Ingredients
        ingredientsEditFragment = new RecipeIngredientsEditFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_ingredients, ingredientsEditFragment)
                .commit();
        ingredientsReadOnlyFragment = new RecipeIngredientsReadOnlyFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_ingredients, ingredientsReadOnlyFragment)
                .commit();
        // detach the edit view
        getSupportFragmentManager().beginTransaction().detach(ingredientsEditFragment).commit();

        // Instructions
        instructionsEditFragment = new RecipeInstructionsEditFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_instructions, instructionsEditFragment)
                .commit();
        instructionsReadOnlyFragment = new RecipeInstructionsReadOnlyFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_instructions, instructionsReadOnlyFragment)
                .commit();
        // detach the edit view
        getSupportFragmentManager().beginTransaction().detach(instructionsEditFragment).commit();

        // Nutrition
        nutritionEditFragment = new RecipeNutritionEditFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_nutrition, nutritionEditFragment)
                .commit();
        nutritionReadOnlyFragment = new RecipeNutritionReadOnlyFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_nutrition, nutritionReadOnlyFragment)
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
