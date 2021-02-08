package com.habbybolan.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.habbybolan.groceryplanner.databinding.ActivityRecipeDetailBinding;
import com.habbybolan.groceryplanner.details.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.details.recipedetails.RecipeDetailFragment;
import com.habbybolan.groceryplanner.details.recipesteps.RecipeStepFragment;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements IngredientEditFragment.IngredientEditListener, RecipeDetailFragment.RecipeDetailListener, RecipeStepFragment.RecipeStepListener {


    private Recipe recipe;
    ActivityRecipeDetailBinding binding;
    private Toolbar toolbar;

    private RecipeDetailFragment recipeDetailFragment;
    private RecipeStepFragment recipeStepFragment;

    /**
     * 2 fragments that can be swiped through.
     */
    private final int NUM_FRAGMENTS = 2;

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
        if (extras != null && extras.containsKey(Recipe.RECIPE))
            recipe = extras.getParcelable(Recipe.RECIPE);

        mPager = binding.recipePager;
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPager.setAdapter(pagerAdapter);
        setToolBar();
    }

    private void setToolBar() {
        toolbar = (Toolbar) binding.toolbarRecipeDetails;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates and starts the EditFragment to add/edit an Ingredient in the list
     * @param ingredient    The Ingredient to edit
     */
    private void startEditFragment(Ingredient ingredient) {
        IngredientEditFragment ingredientEditFragment = IngredientEditFragment.getInstance(recipe, ingredient);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ingredient_edit_container, ingredientEditFragment)
                .commit();
    }

    @Override
    public void onDoneEditing() {
        if (recipeDetailFragment != null) {
            recipeDetailFragment.reloadList();
        }
        mPager.setCurrentItem(0);
        showPagerContainer();
    }

    @Override
    public void onItemClicked(Ingredient ingredient) {
        startEditFragment(ingredient);
        showIngredientEditContainer();
    }

    @Override
    public void createNewItem() {
        startEditFragment(new Ingredient());
        showIngredientEditContainer();
    }

    /**
     * Shows the Ingredient edit Fragment container, hiding the 2 pager fragments.
     */
    private void showIngredientEditContainer() {
        toolbar.getMenu().clear();
        binding.recipePager.setVisibility(View.GONE);
        binding.ingredientEditContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the Ingredient and Step fragment lists in the pager, hiding the Ingredient edit Fragment container.
     */
    private void showPagerContainer() {
        binding.recipePager.setVisibility(View.VISIBLE);
        binding.ingredientEditContainer.setVisibility(View.GONE);
    }

    @Override
    public void onRecipeDeleted() {
        goBackToRecipeList();
    }

    /**
     * Go back to the Recipe List Activity
     */
    private void goBackToRecipeList() {
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.ingredientEditContainer.getVisibility() == View.VISIBLE) {
            // if in Ingredient edit fragment, then go back to viewPagers
            onDoneEditing();
        } else {
            if (mPager.getCurrentItem() == 0)
                // if on Ingredient List, then leave this activity to go back to recipe list
                goBackToRecipeList();
            else
                // otherwise, go from the steps list to Ingredients list
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    /**
     * Pager adapter to allow swiping between the Recipe Ingredient list and step list
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // position 0 corresponding to the Ingredient List for the Recipe
                recipeDetailFragment = RecipeDetailFragment.getInstance(recipe);
                return recipeDetailFragment;
            } else {
                // position 1 corresponds to the step List for the recipe
                recipeStepFragment = RecipeStepFragment.getInstance(recipe);
                return recipeStepFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }
    }
}
