package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityOnlineRecipeDetailsEditBinding;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsFragmentsBinding;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.ingredients.OnlineRecipeEditIngredientsFragment;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.ingredients.OnlineRecipeIngredientsEditContract;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.instructions.OnlineRecipeEditInstructionsFragment;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.instructions.OnlineRecipeInstructionsContract;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.nutrition.OnlineRecipeEditNutritionFragment;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.nutrition.OnlineRecipeNutritionContract;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.overview.OnlineRecipeEditOverviewContract;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.overview.OnlineRecipeEditOverviewFragment;
import com.habbybolan.groceryplanner.ui.recipedetailsabstracts.RecipeDetailsAbstractActivity;

public class OnlineRecipeDetailsEditActivity extends RecipeDetailsAbstractActivity implements
        OnlineRecipeEditOverviewContract.OverviewListener,
        OnlineRecipeIngredientsEditContract.IngredientListener,
        OnlineRecipeInstructionsContract.instructionsListener,
        OnlineRecipeNutritionContract.NutritionListener {

    private ActivityOnlineRecipeDetailsEditBinding binding;
    private OnlineRecipe onlineRecipe;

    private OnlineRecipeEditOverviewFragment overviewFragment;
    private OnlineRecipeEditIngredientsFragment ingredientsFragment;
    private OnlineRecipeEditInstructionsFragment instructionFragment;
    private OnlineRecipeEditNutritionFragment nutritionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_online_recipe_details_edit);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(Recipe.RECIPE))
                onlineRecipe = extras.getParcelable(Recipe.RECIPE);
        }
        RecipeDetailsFragmentsBinding fragmentsBinding = binding.recipeDetailsFragments;
        setViews(binding.bottomNavigation, fragmentsBinding);
    }

    @Override
    protected void setFragments() {
        overviewFragment = new OnlineRecipeEditOverviewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_overview, overviewFragment)
                .commit();
        ingredientsFragment = new OnlineRecipeEditIngredientsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_ingredients, ingredientsFragment)
                .commit();

        instructionFragment = new OnlineRecipeEditInstructionsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_instructions, instructionFragment)
                .commit();
        nutritionFragment = new OnlineRecipeEditNutritionFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_nutrition, nutritionFragment)
                .commit();
    }

    @Override
    public OnlineRecipe getRecipe() {
        return onlineRecipe;
    }
}