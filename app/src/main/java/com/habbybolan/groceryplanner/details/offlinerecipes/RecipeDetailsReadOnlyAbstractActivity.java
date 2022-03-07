package com.habbybolan.groceryplanner.details.offlinerecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsReadOnlyFragmentsBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionReadOnlyFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewReadOnlyFragment;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

/**
 * Holds functionality to swap between different Recipe views
 */
public abstract class RecipeDetailsReadOnlyAbstractActivity
        <T1 extends RecipeOverviewReadOnlyFragment,
                T2 extends RecipeNutritionReadOnlyFragment,
                T3 extends RecipeInstructionsReadOnlyFragment,
                T4 extends RecipeIngredientsReadOnlyFragment,
                U extends OfflineRecipe>
        extends AppCompatActivity {

    protected RecipeDetailsReadOnlyFragmentsBinding readOnlyBinding;

    protected T1 overviewReadOnlyFragment;
    protected T4 ingredientsReadOnlyFragment;
    protected T3 instructionsReadOnlyFragment;
    protected T2 nutritionReadOnlyFragment;

    protected long recipeId;

    private void setBottomNavigation(BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.item_overview) {
                showOverviewFragment();
            } else if (id == R.id.item_ingredients) {
                showIngredientsFragment();
            } else if (id == R.id.item_instructions) {
                showInstructionsFragment();
            } else {
                showNutritionFragment();
            }
            return true;
        });
    }

    /** Shows the Overview Fragments and hides the other 3 details Fragments */
    protected void showOverviewFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .show(overviewReadOnlyFragment)
                .hide(instructionsReadOnlyFragment)
                .hide(ingredientsReadOnlyFragment)
                .hide(nutritionReadOnlyFragment)
                .commit();
    }
    /** Shows the Ingredients Fragments and hides the other 3 details Fragments */
    protected void showIngredientsFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .hide(overviewReadOnlyFragment)
                .hide(instructionsReadOnlyFragment)
                .show(ingredientsReadOnlyFragment)
                .hide(nutritionReadOnlyFragment)
                .commit();
    }
    /** Shows the Instructions Fragments and hides the other 3 details Fragments */
    protected void showInstructionsFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .hide(overviewReadOnlyFragment)
                .show(instructionsReadOnlyFragment)
                .hide(ingredientsReadOnlyFragment)
                .hide(nutritionReadOnlyFragment)
                .commit();
    }
    /** Shows the Nutrition Fragments and hides the other 3 details Fragments */
    protected void showNutritionFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .hide(overviewReadOnlyFragment)
                .hide(instructionsReadOnlyFragment)
                .hide(ingredientsReadOnlyFragment)
                .show(nutritionReadOnlyFragment)
                .commit();
    }

    /**
     * Sets the bottom navigation and calls the method to set the Fragments implemented in the child class.
     * Should only be called if the Activity is purely a read-only activity. Call {@link RecipeDetailsEditAbstractActivity} setViews instead if it's an edit activity.
     * @param view             Bottom navigation view
     * @param fragmentsBinding Included binding layout that holds the fragments to navigate to through bottom navigation
     */
    protected void setViews(BottomNavigationView view, RecipeDetailsReadOnlyFragmentsBinding fragmentsBinding) {
        this.readOnlyBinding = fragmentsBinding;
        setReadOnlyFragments();
        setBottomNavigation(view);
        showOverviewFragment();
    }

    /**
     * Sets up read-only fragments.
     */
    protected abstract void setReadOnlyFragments();

}
