package com.habbybolan.groceryplanner.details.myrecipe.detailsactivity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityRecipeDetailBinding;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsFragmentsBinding;
import com.habbybolan.groceryplanner.details.RecipeDetailsAbstractActivity;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddFragment;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.edit.RecipeIngredientsEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.readonly.RecipeIngredientsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.edit.RecipeInstructionsEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.readonly.RecipeInstructionsReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.edit.RecipeNutritionEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.nutrition.readonly.RecipeNutritionReadOnlyFragment;
import com.habbybolan.groceryplanner.details.myrecipe.overview.edit.RecipeOverviewEditFragment;
import com.habbybolan.groceryplanner.details.myrecipe.overview.readonly.RecipeOverviewReadOnlyFragment;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import javax.inject.Inject;

public class RecipeIngredientsActivity extends RecipeDetailsAbstractActivity
                                implements RecipeOverviewEditFragment.RecipeOverviewListener,
                                            RecipeOverviewReadOnlyFragment.RecipeOverviewListener,
                                            IngredientEditFragment.IngredientEditListener,
                                            RecipeIngredientsEditFragment.RecipeIngredientsListener,
                                            RecipeIngredientsReadOnlyFragment.RecipeIngredientsListener,
                                            RecipeInstructionsEditFragment.RecipeStepListener,
                                            RecipeInstructionsReadOnlyFragment.RecipeStepListener,
                                            RecipeNutritionEditFragment.RecipeNutritionListener,
                                            RecipeNutritionReadOnlyFragment.RecipeNutritionListener,
                                            IngredientAddFragment.IngredientAddListener,
                                            RecipeDetailsContract.DetailsView {

    private MyRecipe myRecipe;
    private ActivityRecipeDetailBinding binding;

    private final String EDIT_TAG = "edit_tag";
    private final String ADD_TAG = "add_tag";

    @Inject
    RecipeDetailsContract.Presenter presenter;

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
    public MyRecipe getMyRecipe() {
        return myRecipe;
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

    /**
     * Creates and starts the EditFragment to add/edit an Ingredient in the list.
     * @param ingredient    The Ingredient to edit
     */
    private void startEditFragment(Ingredient ingredient) {
        IngredientEditFragment ingredientEditFragment = IngredientEditFragment.getInstance(myRecipe, ingredient);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ingredient_edit_container, ingredientEditFragment, EDIT_TAG)
                .commit();
        detailsVisibility(View.GONE);
    }

    @Override
    public void gotoAddIngredients() {
        IngredientAddFragment ingredientAddFragment = IngredientAddFragment.newInstance(myRecipe);
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
        savedInstanceState.putParcelable(OfflineRecipe.RECIPE, myRecipe);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        myRecipe = savedInstanceState.getParcelable(OfflineRecipe.RECIPE);
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
        this.myRecipe = myRecipe;
        setFragments();
    }
}
