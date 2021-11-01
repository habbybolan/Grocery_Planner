package com.habbybolan.groceryplanner.details.offlinerecipes;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsFragmentsBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionReadOnlyFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewReadOnlyFragment;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public abstract class RecipeDetailsReadOnlyAbstractActivity
        <T1 extends RecipeOverviewReadOnlyFragment,
                T2 extends RecipeNutritionReadOnlyFragment,
                T3 extends RecipeInstructionsReadOnlyFragment,
                T4 extends RecipeIngredientsReadOnlyFragment,
                U extends OfflineRecipe>
        extends AppCompatActivity {

    protected RecipeDetailsFragmentsBinding fragmentsBinding;

    protected T1 overviewReadOnlyFragment;
    protected T4 ingredientsReadOnlyFragment;
    protected T3 instructionsReadOnlyFragment;
    protected T2 nutritionReadOnlyFragment;

    protected U recipe;

    private void setBottomNavigation(BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.item_overview) {
                showOverviewFragment(fragmentsBinding.containerOverview, fragmentsBinding.containerIngredients,
                        fragmentsBinding.containerInstructions, fragmentsBinding.containerNutrition);
            } else if (id == R.id.item_ingredients) {
                showIngredientsFragment(fragmentsBinding.containerOverview, fragmentsBinding.containerIngredients,
                        fragmentsBinding.containerInstructions, fragmentsBinding.containerNutrition);
            } else if (id == R.id.item_instructions) {
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
     * Sets up read-only fragments.
     */
    protected abstract void setReadOnlyFragments();

}
