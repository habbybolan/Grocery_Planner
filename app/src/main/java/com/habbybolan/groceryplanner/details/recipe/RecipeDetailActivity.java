package com.habbybolan.groceryplanner.details.recipe;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityRecipeDetailBinding;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsFragmentsBinding;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddFragment;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsFragment;
import com.habbybolan.groceryplanner.details.recipe.recipeinstructions.RecipeInstructionsFragment;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionFragment;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewFragment;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.ui.recipedetailsabstracts.RecipeDetailsAbstractActivity;

public class RecipeDetailActivity extends RecipeDetailsAbstractActivity
                                implements IngredientEditFragment.IngredientEditListener,
                                            RecipeIngredientsFragment.RecipeDetailListener,
                                            RecipeInstructionsFragment.RecipeStepListener,
                                            RecipeOverviewFragment.RecipeOverviewListener,
                                            RecipeNutritionFragment.RecipeNutritionListener,
                                            IngredientAddFragment.IngredientAddListener {

    private OfflineRecipe offlineRecipe;
    private ActivityRecipeDetailBinding binding;

    private RecipeIngredientsFragment recipeIngredientsFragment;
    private RecipeInstructionsFragment recipeInstructionsFragment;
    private RecipeOverviewFragment recipeOverviewFragment;
    private RecipeNutritionFragment recipeNutritionFragment;

    private final String EDIT_TAG = "edit_tag";
    private final String ADD_TAG = "add_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        Bundle extras = getIntent().getExtras();
        // get the bundled Ingredients to display in the fragment if any exist.
        if (extras != null) {
            if (extras.containsKey(OfflineRecipe.RECIPE))
                offlineRecipe = extras.getParcelable(OfflineRecipe.RECIPE);
        }
        RecipeDetailsFragmentsBinding fragmentsBinding = binding.recipeDetailsFragments;
        setViews(binding.bottomNavigation, fragmentsBinding);
    }

    @Override
    protected void setFragments() {
        recipeIngredientsFragment = new RecipeIngredientsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_ingredients,recipeIngredientsFragment)
                .commit();
        recipeOverviewFragment = new RecipeOverviewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_overview,recipeOverviewFragment)
                .commit();
        recipeInstructionsFragment = new RecipeInstructionsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_instructions,recipeInstructionsFragment)
                .commit();
        recipeNutritionFragment = new RecipeNutritionFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_nutrition,recipeNutritionFragment)
                .commit();
    }

    @Override
    public void onItemClicked(Ingredient ingredient) {
        startEditFragment(ingredient);
    }

    @Override
    public void createNewItem() {
        startEditFragment(new Ingredient());
    }

    /**
     * set visibility of the recipe details fragments.
     */
    private void detailsVisibility(int visibility) {
        fragmentsBinding.containerNavigation.setVisibility(visibility);
    }

    @Override
    public void onRecipeDeleted() {
        goBackToRecipeList();
    }

    /** Go back to the Recipe List Activity. */
    private void goBackToRecipeList() {
        finish();
    }

    @Override
    public OfflineRecipe getOfflineRecipe() {
        return offlineRecipe;
    }

    /**
     * Creates and starts the EditFragment to add/edit an Ingredient in the list.
     * @param ingredient    The Ingredient to edit
     */
    private void startEditFragment(Ingredient ingredient) {
        IngredientEditFragment ingredientEditFragment = IngredientEditFragment.getInstance(offlineRecipe, ingredient);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ingredient_edit_container, ingredientEditFragment, EDIT_TAG)
                .commit();
        detailsVisibility(View.GONE);
    }

    @Override
    public void gotoAddIngredients() {
        IngredientAddFragment ingredientAddFragment = IngredientAddFragment.newInstance(offlineRecipe);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ingredient_add_container, ingredientAddFragment, ADD_TAG)
                .commit();
        detailsVisibility(View.GONE);
    }

    @Override
    public void leaveIngredientEdit() {
        IngredientEditFragment ingredientEditFragment = (IngredientEditFragment) getSupportFragmentManager().findFragmentByTag(EDIT_TAG);
        // destroy the ingredient edit Fragment
        if (ingredientEditFragment != null) getSupportFragmentManager().beginTransaction().remove(ingredientEditFragment).commitAllowingStateLoss();
        reloadRecipeIngredientList();
    }

    @Override
    public void leaveIngredientAdd() {
        IngredientAddFragment ingredientAddFragment = (IngredientAddFragment) getSupportFragmentManager().findFragmentByTag(ADD_TAG);
        // destroy the ingredient add Fragment
        if (ingredientAddFragment != null) getSupportFragmentManager().beginTransaction().remove(ingredientAddFragment).commitAllowingStateLoss();
        reloadRecipeIngredientList();
    }

    private void reloadRecipeIngredientList() {
        if (recipeIngredientsFragment != null) {
            recipeIngredientsFragment.reloadList();
        }
        detailsVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(OfflineRecipe.RECIPE, offlineRecipe);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        offlineRecipe = savedInstanceState.getParcelable(OfflineRecipe.RECIPE);
    }

    @Override
    public void onBackPressed() {
        IngredientEditFragment ingredientEditFragment = (IngredientEditFragment) getSupportFragmentManager().findFragmentByTag(EDIT_TAG);
        IngredientAddFragment ingredientAddFragment = (IngredientAddFragment) getSupportFragmentManager().findFragmentByTag(ADD_TAG);
        // if one of the IngredientEdit or AddIngredient fragments are open, close them and reload the Ingredient list
        // otherwise, destroy and leave this activity.
        if (ingredientEditFragment != null) {
            // if in Ingredient edit fragment, then go back to viewPagers
            leaveIngredientEdit();
        } else if (ingredientAddFragment != null) {
            // if in Ingredient Add Fragment, then go back to viewPager
            leaveIngredientAdd();
        } else {
            super.onBackPressed();
        }
    }
}
