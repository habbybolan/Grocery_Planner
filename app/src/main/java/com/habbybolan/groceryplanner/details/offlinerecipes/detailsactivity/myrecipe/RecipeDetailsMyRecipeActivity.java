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
import com.habbybolan.groceryplanner.databinding.RecipeDetailsFragmentsBinding;
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
        // get the bundled Ingredients to display in the fragment if any exist.
        if (extras != null) {
            if (extras.containsKey(OfflineRecipe.RECIPE)) {
                presenter.loadFullRecipe(extras.getLong(OfflineRecipe.RECIPE));
            }
        }
        RecipeDetailsFragmentsBinding fragmentsBinding = binding.recipeDetailsFragments;
        setViews(binding.bottomNavigation, fragmentsBinding);
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
        presenter.onSyncMyRecipe(recipe, new SyncCompleteCallback() {
            @Override
            public void onSyncComplete() {
                presenter.loadFullRecipe(recipe.getId());
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
        IngredientEditFragment ingredientEditFragment = IngredientEditFragment.getInstance(recipe, ingredient);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ingredient_edit_container, ingredientEditFragment, EDIT_TAG)
                .commit();
        detailsVisibility(View.GONE);
    }

    @Override
    public void gotoAddIngredients() {
        IngredientAddFragment ingredientAddFragment = IngredientAddFragment.newInstance(recipe);
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
        if (ingredientsEditFragment != null) {
            ingredientsEditFragment.reloadList();
        }
        detailsVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(OfflineRecipe.RECIPE, recipe);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        recipe = savedInstanceState.getParcelable(OfflineRecipe.RECIPE);
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

    @Override
    public void showRecipe(MyRecipe myRecipe) {
        this.recipe = myRecipe;
        setReadOnlyFragments();
        setEditFragments();
    }

    @Override
    public OfflineRecipe getRecipe() {
        return recipe;
    }

    @Override
    protected void setReadOnlyFragments() {
        overviewReadOnlyFragment = new RecipeOverviewMyRecipeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_overview, overviewReadOnlyFragment)
                .commit();

        // Ingredients
        ingredientsReadOnlyFragment = new RecipeIngredientsMyRecipeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_ingredients, ingredientsReadOnlyFragment)
                .commit();

        // Instructions
        instructionsReadOnlyFragment = new RecipeInstructionsMyRecipeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_instructions, instructionsReadOnlyFragment)
                .commit();

        // Nutrition
        nutritionReadOnlyFragment = new RecipeNutritionMyRecipeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_nutrition, nutritionReadOnlyFragment)
                .commit();
    }
}
