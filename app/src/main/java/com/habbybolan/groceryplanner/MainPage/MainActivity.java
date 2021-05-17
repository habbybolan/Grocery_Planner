package com.habbybolan.groceryplanner.MainPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.habbybolan.groceryplanner.MainPage.recipesnippet.RecipeSnippetFragment;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeListType;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollFragment;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityMainBinding;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListActivity;
import com.habbybolan.groceryplanner.listing.ingredientlist.IngredientListActivity;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.online.discover.DiscoverActivity;
import com.habbybolan.groceryplanner.online.displayonlinerecipe.OnlineRecipeDetailActivity;
import com.habbybolan.groceryplanner.online.myrecipes.MyRecipesActivity;
import com.habbybolan.groceryplanner.online.savedrecipes.SavedRecipesActivity;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class MainActivity extends AppCompatActivity implements RecipeSideScrollFragment.RecipeSideScrollListener {

    private CustomToolbar toolbar;
    private ActivityMainBinding binding;

    private final String FRAGMENT_SNIPPET_TAG = "snippet_tag";

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setToolBar();
        setNavigationDrawer();
        setFragments();
    }

    /**
     * Sets up the toolbar with Up button.
     */
    private void setToolBar() {
        toolbar = new CustomToolbar.CustomToolbarBuilder("Account", getLayoutInflater(), binding.toolbarMainPage, getApplicationContext()).build();
        setSupportActionBar(toolbar.getToolbar());
    }

    private void setNavigationDrawer() {
        dl = (DrawerLayout) binding.mainActivityDrawer;
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        nv = (NavigationView) binding.mainActivityNavigation;
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.preferences) {
                    return true;
                } else if (id == R.id.change_account) {
                    return true;
                } else if (id == R.id.discover_recipes) {
                    gotoDiscoverRecipe();
                    return true;
                } else if (id == R.id.my_online_recipes) {
                    gotoMyRecipesList();
                    return true;
                } else if (id == R.id.saved_recipes) {
                    gotoSavedRecipesList();
                    return true;
                } else if (id == R.id.my_offline_recipes) {
                    gotoOfflineRecipeList();
                    return true;
                } else if (id == R.id.my_grocery_list) {
                    gotoGroceryList();
                    return true;
                } else if (id == R.id.my_ingredients) {
                    gotoIngredients();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
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
    }

    /** Sends the activity to MyRecipesActivity */
    private void gotoMyRecipesList() {
        Intent intent = new Intent(this, MyRecipesActivity.class);
        startActivity(intent);
    }

    /** Sends the activity to SavedRecipesActivity */
    private void gotoSavedRecipesList() {
        Intent intent = new Intent(this, SavedRecipesActivity.class);
        startActivity(intent);
    }

    /** Sends the activity to the DiscoverActivity. */
    private void gotoDiscoverRecipe() {
        Intent intent = new Intent(this, DiscoverActivity.class);
        startActivity(intent);
    }

    /** Sends the activity to GroceryListActivity. */
    private void gotoGroceryList() {
        Intent intent = new Intent(this, GroceryListActivity.class);
        startActivity(intent);
    }

    /** Sends the activity to RecipeListActivity. */
    private void gotoOfflineRecipeList() {
        Intent intent = new Intent(this, RecipeListActivity.class);
        startActivity(intent);
    }

    /** Sends the activity to IngredientListActivity */
    private void gotoIngredients() {
        Intent intent = new Intent(this, IngredientListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRecipeSelected(OnlineRecipe recipe) {
        Intent intent = new Intent(this, OnlineRecipeDetailActivity.class);
        intent.putExtra(Recipe.RECIPE, recipe);
        startActivity(intent);
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
