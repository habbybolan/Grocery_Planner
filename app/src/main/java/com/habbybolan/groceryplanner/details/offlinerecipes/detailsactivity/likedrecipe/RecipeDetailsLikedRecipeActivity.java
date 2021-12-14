package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.likedrecipe;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityRecipeDetailsLikedRecipeBinding;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsReadOnlyFragmentsBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.RecipeDetailsReadOnlyAbstractActivity;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly.RecipeIngredientsLikedRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly.RecipeInstructionsLikedRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly.RecipeNutritionLikedRecipeFragment;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly.RecipeOverviewLikedRecipeFragment;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import javax.inject.Inject;

public class RecipeDetailsLikedRecipeActivity extends RecipeDetailsReadOnlyAbstractActivity
        <RecipeOverviewLikedRecipeFragment,
                RecipeNutritionLikedRecipeFragment,
                RecipeInstructionsLikedRecipeFragment,
                RecipeIngredientsLikedRecipeFragment,
                LikedRecipe>
                                        implements RecipeOverviewLikedRecipeFragment.RecipeOverviewListener,
                                            RecipeIngredientsLikedRecipeFragment.RecipeIngredientsListener,
                                            RecipeInstructionsLikedRecipeFragment.RecipeInstructionsListener,
                                            RecipeNutritionLikedRecipeFragment.RecipeNutritionLikedRecipeListener,
                                            RecipeDetailsContract.DetailsView<LikedRecipe> {

    @Inject
    RecipeDetailsContract.PresenterLikedRecipe presenter;
    ActivityRecipeDetailsLikedRecipeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
        presenter.setView(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details_liked_recipe);
        Bundle extras = getIntent().getExtras();
        // get the bundled Ingredients to display in the fragment if any exist.
        if (extras != null) {
            if (extras.containsKey(OfflineRecipe.RECIPE)) {
                recipeId = extras.getLong(OfflineRecipe.RECIPE);
            }
        }
        RecipeDetailsReadOnlyFragmentsBinding fragmentsBinding = binding.recipeDetailsReadOnlyFragments;
        setViews(binding.bottomNavigation, fragmentsBinding);
    }

    @Override
    protected void setReadOnlyFragments() {
        overviewReadOnlyFragment = RecipeOverviewLikedRecipeFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_read_only_overview, overviewReadOnlyFragment)
                .commit();

        // Ingredients
        ingredientsReadOnlyFragment = RecipeIngredientsLikedRecipeFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_read_only_ingredients, ingredientsReadOnlyFragment)
                .commit();

        // Instructions
        instructionsReadOnlyFragment = RecipeInstructionsLikedRecipeFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_read_only_instructions, instructionsReadOnlyFragment)
                .commit();

        // Nutrition
        nutritionReadOnlyFragment = RecipeNutritionLikedRecipeFragment.getInstance(recipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_read_only_nutrition, nutritionReadOnlyFragment)
                .commit();
    }

    @Override
    public void updateFragments() {
        overviewReadOnlyFragment.updateRecipe();
        ingredientsReadOnlyFragment.updateRecipe();
        nutritionReadOnlyFragment.updateRecipe();
        instructionsReadOnlyFragment.updateRecipe();
    }
}