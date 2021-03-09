package com.habbybolan.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.databinding.ActivityRecipeListBinding;
import com.habbybolan.groceryplanner.databinding.ToolbarBinding;
import com.habbybolan.groceryplanner.details.recipe.recipedetailactivity.RecipeDetailActivity;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListFragment;
import com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist.RecipeCategoryFragment;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

public class RecipeListActivity extends AppCompatActivity {//RecipeListFragment.RecipeListListener, RecipeCategoryFragment.RecipeCategoryListener {

    private ActivityRecipeListBinding binding;
    private Toolbar toolbar;
    private String RECIPE_LIST_TAG;
    private String RECIPE_CATEGORY_TAG;
    protected RecipeListListener recipeListListener;
    private RecipeCategoryListener recipeCategoryListener;
    private RecipeCategory recipeCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);

        Bundle extras = getIntent().getExtras();
        // get the bundled Ingredients to display in the fragment if any exist.
        if (extras != null && extras.containsKey(RecipeCategory.RECIPE_CATEGORY))
            recipeCategory = extras.getParcelable(RecipeCategory.RECIPE_CATEGORY);

        RECIPE_LIST_TAG = getString(R.string.RECIPE_LIST_TAG);
        RECIPE_CATEGORY_TAG = getString(R.string.RECIPE_CATEGORY_TAG);

        setToolBar();
        initListeners();
    }

    private void initListeners() {
        recipeListListener = new RecipeListListener();
        recipeCategoryListener = new RecipeCategoryListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        // manually attach the listeners as they are created explicitly in this activity and sent to the fragments
        RecipeListFragment recipeListFragment = (RecipeListFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_LIST_TAG);
        if (recipeListFragment != null) recipeListFragment.attachListener(recipeListListener);
        RecipeCategoryFragment recipeCategoryFragment = (RecipeCategoryFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_CATEGORY_TAG);
        if (recipeCategoryFragment != null) recipeCategoryFragment.attachListener(recipeCategoryListener);
    }

    @Override
    public void onBackPressed() {
        if (isRecipeListVisible()) gotoRecipeCategories();
        else super.onBackPressed();
    }

    /**
     * Sets up the toolbar with Up button, title, and onClick for title
     */
    private void setToolBar() {
        ToolbarBinding toolbarBinding = binding.toolbarRecipeList;
        toolbar = binding.toolbarRecipeList.toolbar;
        toolbarBinding.setTitle(getString(R.string.title_recipe_list));

        // onClick event on toolbar title to swap between Recipe List and Category List
        toolbarBinding.toolbarTitle.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getApplicationContext(), v);
            popup.inflate(R.menu.menu_recipe_list_displayed);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_recipe_list:
                        gotoRecipeListUnCategorized();
                        return true;
                    case R.id.popup_recipe_category:
                        gotoRecipeCategories();
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        // display a back button to go to main page
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Sends the app to the GroceryListActivity
     */
    public void gotoGroceryListFunc() {
        Intent intent = new Intent(this, GroceryListActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_enter_from_right, R.anim.anim_slide_exit_to_left);
    }

    /**
     * Sends the app the the RecipeDetailsActivity for selecting a specific recipe.
     * @param recipe    The recipe info to display
     */
    public void onRecipeItemClicked(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(Recipe.RECIPE, recipe);
        startActivity(intent);
    }

    public void hideToolbarFunc() {
        toolbar.setVisibility(View.GONE);
    }

    public void showToolbarFunc() {
        toolbar.setVisibility(View.VISIBLE);
    }

    /**
     * Clicker functionality for selecting a recipe category. Brings up RecipeListFragment to display the recipes associated with the category clicked.
     * @param recipeCategory    The recipe category that holds the Recipes to display.
     */
    private void gotoRecipeListCategorized(RecipeCategory recipeCategory) {
        binding.recipeCategoryContainer.setVisibility(View.GONE);
        this.recipeCategory = recipeCategory;
        RecipeListFragment recipeListFragment = (RecipeListFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_LIST_TAG);
        if (recipeListFragment != null) {
            binding.recipeListContainer.setVisibility(View.VISIBLE);
            recipeListFragment.resetList();
        }
    }

    private void gotoRecipeListUnCategorized() {
        gotoRecipeListCategorized(null);
    }

    private void gotoRecipeCategories() {
        binding.recipeListContainer.setVisibility(View.GONE);
        binding.recipeCategoryContainer.setVisibility(View.VISIBLE);
    }

    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    /**
     * @return  True if the Recipe List Fragment in currently visible
     */
    private boolean isRecipeListVisible() {
        return binding.recipeListContainer.getVisibility() == View.VISIBLE;
    }

    /**
     * @return  True if the Recipe Category Fragment is visible
     */
    private boolean isRecipeCategoryVisible() {
        return binding.recipeCategoryContainer.getVisibility() == View.VISIBLE;
    }

    /**
     * Listener to implement methods in RecipeListFragment listener interface.
     */
    private class RecipeListListener implements RecipeListFragment.RecipeListListener {

        @Override
        public void onItemClicked(Recipe recipe) {
            onRecipeItemClicked(recipe);
        }

        @Override
        public void hideToolbar() {
            hideToolbarFunc();
        }

        @Override
        public void showToolbar() {
            showToolbarFunc();
        }

        @Override
        public void gotoGroceryList() {
            gotoGroceryListFunc();
        }
    }

    /**
     * Listener to implement methods in RecipeCategoryFragment listener interface.
     */
    private class RecipeCategoryListener implements RecipeCategoryFragment.RecipeCategoryListener {

        @Override
        public void onItemClicked(RecipeCategory recipeCategory) {
            gotoRecipeListCategorized(recipeCategory);
        }

        @Override
        public void hideToolbar() {
            hideToolbarFunc();
        }

        @Override
        public void showToolbar() {
            showToolbarFunc();
        }

        @Override
        public void gotoGroceryList() {
            gotoGroceryListFunc();
        }
    }
}
