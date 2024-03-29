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
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeListType;
import com.habbybolan.groceryplanner.MainPage.recipessidescroll.RecipeSideScrollFragment;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityMainBinding;
import com.habbybolan.groceryplanner.listing.grocerylist.GroceryListActivity;
import com.habbybolan.groceryplanner.listing.ingredientlist.IngredientListActivity;
import com.habbybolan.groceryplanner.listing.recipelist.likedrecipelist.LikedRecipeListActivity;
import com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.MyRecipeListActivity;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.online.discover.DiscoverActivity;
import com.habbybolan.groceryplanner.online.displayonlinerecipe.OnlineRecipeDetailActivityEdit;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class MainActivity extends AppCompatActivity implements RecipeSideScrollFragment.RecipeSideScrollListener {

    private CustomToolbar toolbar;
    private ActivityMainBinding binding;

    private final String FRAGMENT_SNIPPET_TAG = "snippet_tag";

    private ActionBarDrawerToggle t;

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
        DrawerLayout dl = (DrawerLayout) binding.mainActivityDrawer;
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        NavigationView nv = (NavigationView) binding.mainActivityNavigation;
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
                } else if (id == R.id.my_recipes) {
                    gotoMyRecipeList();
                    return true;
                } else if (id == R.id.liked_recipes) {
                    gotoLikedRecipeList();
                    return true;
                } else if (id == R.id.my_grocery_list) {
                    gotoGroceryList();
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
        // new recipes
        RecipeSideScrollFragment newScroll = RecipeSideScrollFragment.newInstance(RecipeListType.NEW_TYPE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_new_recipe_side_scroll, newScroll)
                .commit();
        // trending by day
        RecipeSideScrollFragment trendingDayScroll = RecipeSideScrollFragment.newInstance(RecipeListType.TRENDING_DAY_TYPE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_trending_day_recipe_side_scroll, trendingDayScroll)
                .commit();
        // trending by week
        RecipeSideScrollFragment trendingWeekScroll = RecipeSideScrollFragment.newInstance(RecipeListType.TRENDING_WEEK_TYPE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_trending_week_recipe_side_scroll, trendingWeekScroll)
                .commit();
        // trending by month
        RecipeSideScrollFragment trendingMonthScroll = RecipeSideScrollFragment.newInstance(RecipeListType.TRENDING_MONTH_TYPE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_trending_month_recipe_side_scroll, trendingMonthScroll)
                .commit();
        // trending by year
        RecipeSideScrollFragment trendingYearScroll = RecipeSideScrollFragment.newInstance(RecipeListType.TRENDING_YEAR_TYPE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_trending_year_recipe_side_scroll, trendingYearScroll)
                .commit();
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
    private void gotoMyRecipeList() {
        Intent intent = new Intent(this, MyRecipeListActivity.class);
        startActivity(intent);
    }

    private void gotoLikedRecipeList() {
        Intent intent = new Intent(this, LikedRecipeListActivity.class);
        startActivity(intent);
    }

    /** Sends the activity to IngredientListActivity */
    private void gotoIngredients() {
        Intent intent = new Intent(this, IngredientListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRecipeSelected(OnlineRecipe recipe) {
        Intent intent = new Intent(this, OnlineRecipeDetailActivityEdit.class);
        intent.putExtra(OfflineRecipe.RECIPE, recipe);
        startActivity(intent);
    }
}
