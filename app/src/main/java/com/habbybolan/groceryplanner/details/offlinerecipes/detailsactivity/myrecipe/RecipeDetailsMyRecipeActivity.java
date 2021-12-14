package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.callbacks.SyncCompleteCallback;
import com.habbybolan.groceryplanner.databinding.ActivityRecipeDetailBinding;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsEditFragmentsBinding;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsReadOnlyFragmentsBinding;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddFragment;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.RecipeDetailsEditAbstractActivity;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit.RecipeIngredientsEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsMyRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit.RecipeInstructionsEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsMyRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.edit.RecipeNutritionEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionMyRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit.RecipeOverviewEditFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewMyRecipeFragment;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import javax.inject.Inject;

public class RecipeDetailsMyRecipeActivity extends RecipeDetailsEditAbstractActivity
        <RecipeOverviewMyRecipeFragment,
                RecipeNutritionMyRecipeFragment,
                RecipeInstructionsMyRecipeFragment,
                RecipeIngredientsMyRecipeFragment,
                MyRecipe>
        implements RecipeOverviewEditFragment.RecipeOverviewMyListener,
                    RecipeOverviewMyRecipeFragment.RecipeOverviewMyListener,
                    IngredientEditFragment.IngredientEditListener,
                    RecipeIngredientsEditFragment.RecipeIngredientsListener,
                    RecipeIngredientsMyRecipeFragment.RecipeIngredientsListener,
                    RecipeInstructionsEditFragment.RecipeStepListener,
                    RecipeInstructionsMyRecipeFragment.RecipeInstructionsListener,
        RecipeNutritionEditFragment.RecipeNutritionMyListener,
        RecipeNutritionMyRecipeFragment.RecipeNutritionMyRecipeListener,
                    IngredientAddFragment.IngredientAddListener,
                    RecipeDetailsContract.DetailsView<MyRecipe> {

    private ActivityRecipeDetailBinding binding;

    @Inject
    RecipeDetailsContract.PresenterMyRecipe presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
        presenter.setView(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        Bundle extras = getIntent().getExtras();
        assert(extras != null && extras.containsKey(OfflineRecipe.RECIPE));
        recipeId = extras.getLong(OfflineRecipe.RECIPE);
        RecipeDetailsReadOnlyFragmentsBinding fragmentsReadOnlyBinding = binding.recipeDetailsReadOnlyFragments;
        RecipeDetailsEditFragmentsBinding fragmentsEditBinding = binding.recipeDetailsEditFragments;
        setViews(binding.bottomNavigation, fragmentsReadOnlyBinding, fragmentsEditBinding);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }


    @Override
    public void onItemClicked(Ingredient ingredient) {
        startEditFragment(ingredient);
    }

    /**
     * set visibility of the recipe details fragments.
     */
    private void detailsVisibility(int visibility) {
        readOnlyBinding.containerNavigation.setVisibility(visibility);
        editBinding.containerNavigation.setVisibility(visibility);
    }

    @Override
    public void onRecipeDeleted() {
        // todo: delete recipe from offline database
        goBackToRecipeList();
    }

    /** Go back to the Recipe List Activity. */
    private void goBackToRecipeList() {
        finish();
    }


    @Override
    public void onSwapViewIngredients() {
        swapViewIngredients();
    }

    @Override
    public void onSwapViewInstructions() {
        swapViewInstructions();
    }

    @Override
    public void onSwapViewNutrition() {
        swapViewNutrition();
    }

    @Override
    public void onSwapViewOverview() {
        swapViewOverview();
    }

    @Override
    public void onSync() {
        presenter.onSyncMyRecipe(recipeId, new SyncCompleteCallback() {
            @Override
            public void onSyncComplete() {
                updateFragments();
            }

            @Override
            public void onSyncFailed(String message) {
                // TODO: error message
            }
        });
    }

    /**
     * Creates and starts the EditFragment to add/edit an Ingredient in the list.
     * @param ingredient    The Ingredient to edit
     */
    private void startEditFragment(Ingredient ingredient) {
        IngredientEditFragment ingredientEditFragment = IngredientEditFragment.getInstance(new OfflineRecipe.RecipeBuilder("").setId(recipeId).build(), ingredient);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ingredient_edit_container, ingredientEditFragment, EDIT_TAG)
                .commit();
        detailsVisibility(View.GONE);
    }

    public void addIngredientToRecipe() {
        IngredientAddFragment ingredientAddFragment = IngredientAddFragment.newInstance(new OfflineRecipe.RecipeBuilder("").setId(recipeId).build());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ingredient_add_container, ingredientAddFragment, ADD_TAG)
                .commit();
        detailsVisibility(View.GONE);
    }

    @Override
    public void leaveIngredientEdit() {
        destroyIngredientEdit();
        reloadRecipeIngredientList();
    }

    @Override
    public void leaveIngredientAdd(Ingredient ingredient) {
        IngredientAddFragment ingredientAddFragment = (IngredientAddFragment) getSupportFragmentManager().findFragmentByTag(ADD_TAG);
        // destroy the ingredient add Fragment
        if (ingredientAddFragment != null) getSupportFragmentManager()
                .beginTransaction()
                .remove(ingredientAddFragment)
                .add(R.id.ingredient_edit_container, IngredientEditFragment.getInstance(new OfflineRecipe.RecipeBuilder("").setId(recipeId).build(), ingredient), EDIT_TAG)
                .commitAllowingStateLoss();
    }

    private void destroyIngredientEdit() {
        IngredientEditFragment ingredientEditFragment = (IngredientEditFragment) getSupportFragmentManager().findFragmentByTag(EDIT_TAG);
        // destroy the ingredient edit Fragment
        if (ingredientEditFragment != null) getSupportFragmentManager().beginTransaction().remove(ingredientEditFragment).commitAllowingStateLoss();
    }

    private void destroyIngredientAdd() {
        IngredientAddFragment ingredientAddFragment = (IngredientAddFragment) getSupportFragmentManager().findFragmentByTag(ADD_TAG);
        // destroy the ingredient add Fragment
        if (ingredientAddFragment != null) getSupportFragmentManager().beginTransaction().remove(ingredientAddFragment).commitAllowingStateLoss();
    }

    private void reloadRecipeIngredientList() {
        if (ingredientsEditFragment != null) {
            ingredientsEditFragment.reloadList();
        }
        detailsVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong(OfflineRecipe.RECIPE, recipeId);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        recipeId = savedInstanceState.getLong(OfflineRecipe.RECIPE);
    }

    @Override
    public void onBackPressed() {
        IngredientEditFragment ingredientEditFragment = (IngredientEditFragment) getSupportFragmentManager().findFragmentByTag(EDIT_TAG);
        IngredientAddFragment ingredientAddFragment = (IngredientAddFragment) getSupportFragmentManager().findFragmentByTag(ADD_TAG);
        // if one of the IngredientEdit or AddIngredient fragments are open, close them and reload the Ingredient list
        // otherwise, destroy and leave this activity.
        if (ingredientEditFragment != null) {
            // destroy ingredient edit fragment and reload ingredient list
            destroyIngredientEdit();
            reloadRecipeIngredientList();
        } else if (ingredientAddFragment != null) {
            // destroy ingredient add fragment and go back to ingredient list
            destroyIngredientAdd();
            reloadRecipeIngredientList();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateFragments() {
        overviewReadOnlyFragment.updateRecipe();
        overviewEditFragment.updateRecipe();
        ingredientsReadOnlyFragment.updateRecipe();
        ingredientsEditFragment.updateRecipe();
        nutritionReadOnlyFragment.updateRecipe();
        nutritionEditFragment.updateRecipe();
        instructionsReadOnlyFragment.updateRecipe();
        instructionsEditFragment.updateRecipe();
    }

    @Override
    protected void setReadOnlyFragments() {
        overviewReadOnlyFragment = RecipeOverviewMyRecipeFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_read_only_overview, overviewReadOnlyFragment)
                .commit();

        // Ingredients
        ingredientsReadOnlyFragment = RecipeIngredientsMyRecipeFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_read_only_ingredients, ingredientsReadOnlyFragment)
                .commit();

        // Instructions
        instructionsReadOnlyFragment = RecipeInstructionsMyRecipeFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_read_only_instructions, instructionsReadOnlyFragment)
                .commit();

        // Nutrition
        nutritionReadOnlyFragment = RecipeNutritionMyRecipeFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_read_only_nutrition, nutritionReadOnlyFragment)
                .commit();
    }
}
