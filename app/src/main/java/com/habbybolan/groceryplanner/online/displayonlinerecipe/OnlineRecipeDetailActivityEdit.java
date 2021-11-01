package com.habbybolan.groceryplanner.online.displayonlinerecipe;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityOnlineRecipeDetailBinding;
import com.habbybolan.groceryplanner.databinding.RecipeDetailsFragmentsBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.RecipeDetailsEditAbstractActivity;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.online.displayonlinerecipe.ingredients.OnlineRecipeIngredientsFragment;
import com.habbybolan.groceryplanner.online.displayonlinerecipe.nutrition.OnlineRecipeNutritionFragment;

// TODO: merge this and corresponding classes RecipeDetailsReadOnlyAbstractActivity
public class OnlineRecipeDetailActivityEdit extends RecipeDetailsEditAbstractActivity implements OnlineRecipeContract.OnlineRecipeListener {

    private OnlineRecipe onlineRecipe;
    private OnlineRecipeOverviewFragment overviewFragment;
    private OnlineRecipeInstructionFragment instructionFragment;
    private OnlineRecipeIngredientsFragment ingredientsFragment;
    private OnlineRecipeNutritionFragment nutritionFragment;

    private ActivityOnlineRecipeDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_online_recipe_detail);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            onlineRecipe = extras.getParcelable(OfflineRecipe.RECIPE);
        }
        RecipeDetailsFragmentsBinding fragmentsBinding = binding.recipeDetailsFragments;
        setViews(binding.bottomNavigation, fragmentsBinding);
        //setFragments();
    }

    @Override
    public OnlineRecipe getOnlineRecipe() {
        return onlineRecipe;
    }

    /*@Override
    protected void setFragments() {
        overviewFragment = new OnlineRecipeOverviewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_overview, overviewFragment)
                .commit();
        ingredientsFragment = new OnlineRecipeIngredientsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_ingredients, ingredientsFragment)
                .commit();
        instructionFragment = new OnlineRecipeInstructionFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_instructions, instructionFragment)
                .commit();
        nutritionFragment = new OnlineRecipeNutritionFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_nutrition, nutritionFragment)
                .commit();
    }*/

    @Override
    protected void setReadOnlyFragments() {
        // todo: garbage,
    }
}