package com.habbybolan.groceryplanner.MainPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.MainPage.recipesnippet.RecipeSnippetFragment;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeListType;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollFragment;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityMainBinding;
import com.habbybolan.groceryplanner.databinding.ToolbarBinding;
import com.habbybolan.groceryplanner.http.requests.HttpGrocery;
import com.habbybolan.groceryplanner.http.requests.HttpGroceryImpl;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListActivity;
import com.habbybolan.groceryplanner.listing.ingredientlist.IngredientListActivity;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

public class MainActivity extends AppCompatActivity implements RecipeSideScrollFragment.RecipeSideScrollListener {

    private Toolbar toolbar;
    private ActivityMainBinding binding;

    private HttpGrocery httpRequest;
    private final String FRAGMENT_SNIPPET_TAG = "snippet_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setToolBar();
        setFragments();
        httpRequest = new HttpGroceryImpl(getApplication());
    }

    private void setFragments() {
        RecipeSideScrollFragment newScroll = RecipeSideScrollFragment.newInstance(RecipeListType.NEW_TYPE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_new_recipe_side_scroll, newScroll)
                .commit();
        RecipeSideScrollFragment trendingScroll = RecipeSideScrollFragment.newInstance(RecipeListType.TRENDING_TYPE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_trending_recipe_side_scroll, trendingScroll)
                .commit();
        RecipeSideScrollFragment savedScroll = RecipeSideScrollFragment.newInstance(RecipeListType.SAVED_TYPE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_saved_recipe_side_scroll, savedScroll)
                .commit();
    }

    /**
     * Sets up the toolbar with Up button.
     */
    private void setToolBar() {
        ToolbarBinding toolbarBinding = binding.toolbarMainPage;
        toolbar = toolbarBinding.toolbar;
        toolbarBinding.setTitle(getString(R.string.title_main_page));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    /**
     * Clicker for entering the Recipe list
     * @param v     Recipe button view
     */
    public void onRecipeClick(View v) {
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
    }

    /**
     * Clicker for entering the Grocery list
     * @param v     Grocery button view
     */
    public void onGroceryClick(View v) {
        Intent intent = new Intent(this, GroceryListActivity.class);
        startActivity(intent);
    }

    public void onIngredientClick(View v) {
        Intent intent = new Intent(this, IngredientListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRecipeSelected(OnlineRecipe recipe) {
        RecipeSnippetFragment fragment;
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_SNIPPET_TAG) != null) {
            // if the fragment is already showing, retrieve the fragment
            fragment = (RecipeSnippetFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_SNIPPET_TAG);
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
        fragment = RecipeSnippetFragment.newInstance(recipe);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_enter_from_right, R.anim.anim_slide_exit_to_left)
                .add(R.id.fragment_recipe_snippet_container, fragment, FRAGMENT_SNIPPET_TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_SNIPPET_TAG) != null) {
            RecipeSnippetFragment snippetFragment = (RecipeSnippetFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_SNIPPET_TAG);
            if (snippetFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.anim_slide_enter_from_right, R.anim.anim_slide_exit_to_right)
                        .remove(snippetFragment)
                        .commit();
            }
        } else super.onBackPressed();
    }
}
