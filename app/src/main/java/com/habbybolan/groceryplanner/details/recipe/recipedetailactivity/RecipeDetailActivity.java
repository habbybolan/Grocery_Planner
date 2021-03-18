package com.habbybolan.groceryplanner.details.recipe.recipedetailactivity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityRecipeDetailBinding;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddFragment;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.details.recipe.recipeingredients.RecipeIngredientsFragment;
import com.habbybolan.groceryplanner.details.recipe.recipenutrition.RecipeNutritionFragment;
import com.habbybolan.groceryplanner.details.recipe.recipeoverview.RecipeOverviewFragment;
import com.habbybolan.groceryplanner.details.recipe.recipesteps.RecipeStepFragment;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

public class RecipeDetailActivity extends AppCompatActivity
                                implements IngredientEditFragment.IngredientEditListener,
                                            RecipeIngredientsFragment.RecipeDetailListener,
                                            RecipeStepFragment.RecipeStepListener,
                                            RecipeOverviewFragment.RecipeOverviewListener,
                                            RecipeNutritionFragment.RecipeNutritionListener,
                                            IngredientAddFragment.IngredientAddListener {

    private Recipe recipe;
    private RecipeCategory recipeCategory;
    private ActivityRecipeDetailBinding binding;

    private RecipeIngredientsFragment recipeIngredientsFragment;
    private RecipeStepFragment recipeStepFragment;
    private RecipeOverviewFragment recipeOverviewFragment;
    private RecipeNutritionFragment recipeNutritionFragment;

    private final String EDIT_TAG = "edit_tag";
    private final String ADD_TAG = "add_tag";

    /**
     * 2 fragments that can be swiped through.
     */
    private final int NUM_FRAGMENTS = 4;

    /**
     * Pager widget that handles the animations and allows swiping horizontally
     */
    private ViewPager mPager;

    /**
     * Provides the pages to mPager widget
     */
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);

        Bundle extras = getIntent().getExtras();
        // get the bundled Ingredients to display in the fragment if any exist.
        if (extras != null) {
            if (extras.containsKey(Recipe.RECIPE))
                recipe = extras.getParcelable(Recipe.RECIPE);
        }

        mPager = binding.recipePager;
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPager.setAdapter(pagerAdapter);
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
     * Shows the Ingredient and Step fragment lists in the pager, hiding the Ingredient edit Fragment container.
     */
    private void pagerContainerVisibility(int visibility) {
        binding.recipePager.setVisibility(visibility);
    }

    @Override
    public void onRecipeDeleted() {
        goBackToRecipeList();
    }

    /**
     * Go back to the Recipe List Activity
     */
    private void goBackToRecipeList() {
        finish();
    }

    @Override
    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * Creates and starts the EditFragment to add/edit an Ingredient in the list
     * @param ingredient    The Ingredient to edit
     */
    private void startEditFragment(Ingredient ingredient) {
        IngredientEditFragment ingredientEditFragment = IngredientEditFragment.getInstance(recipe, ingredient);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ingredient_edit_container, ingredientEditFragment, EDIT_TAG)
                .commit();
        pagerContainerVisibility(View.GONE);
    }

    @Override
    public void gotoAddIngredients() {
        IngredientAddFragment ingredientAddFragment = IngredientAddFragment.newInstance(recipe);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ingredient_add_container, ingredientAddFragment, ADD_TAG)
                .commit();
        pagerContainerVisibility(View.GONE);
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
        mPager.setCurrentItem(0);
        pagerContainerVisibility(View.VISIBLE);
    }

    /**
     * Pager adapter to allow swiping between the Recipe Ingredient list and step list
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Ingredients";
                case 1:
                    return "Steps";
                case 2:
                    return "Overview";
                default:
                    return "Nutrition";
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // position 0 corresponding to the Ingredient List for the Recipe
                    recipeIngredientsFragment = RecipeIngredientsFragment.getInstance();
                    return recipeIngredientsFragment;
                case 1:
                    // position 1 corresponds to the step List for the recipe
                    recipeStepFragment = RecipeStepFragment.getInstance(recipe);
                    return recipeStepFragment;
                case 2:
                    // position 2 corresponding to the Recipe overview
                    recipeOverviewFragment = RecipeOverviewFragment.getInstance();
                    return recipeOverviewFragment;
                default:
                    // position 3 corresponding to the Recipe Nutrition
                    recipeNutritionFragment = RecipeNutritionFragment.newInstance();
                    return recipeNutritionFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(Recipe.RECIPE, recipe);
        savedInstanceState.putParcelable(RecipeCategory.RECIPE_CATEGORY, recipeCategory);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        recipe = savedInstanceState.getParcelable(Recipe.RECIPE);
        recipeCategory = savedInstanceState.getParcelable(RecipeCategory.RECIPE_CATEGORY);
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
            if (mPager.getCurrentItem() == 0)
                // if on Ingredient List, then leave this activity to go back to recipe list
                goBackToRecipeList();
            else
                // otherwise, go from the steps list to Ingredients list
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
}
