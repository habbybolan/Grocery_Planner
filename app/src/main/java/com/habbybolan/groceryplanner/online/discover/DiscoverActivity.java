package com.habbybolan.groceryplanner.online.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityDiscoverBinding;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListContract;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListFragment;
import com.habbybolan.groceryplanner.online.discover.searchfilter.OnlineRecipeTag;
import com.habbybolan.groceryplanner.online.discover.searchfilter.RecipeFilterFragment;
import com.habbybolan.groceryplanner.online.displayonlinerecipe.OnlineRecipeDetailActivity;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiscoverActivity extends AppCompatActivity implements RecipeFilterFragment.RecipeFilterListener,
        OnlineRecipeListContract.OnlineRecipeListListener {

    private ActivityDiscoverBinding binding;
    private CustomToolbar customToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_discover);
        searchForRecipe(new ArrayList<>(), new SortType());
        setToolbar();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder("Discover", getLayoutInflater(), binding.discoverToolbar, getApplicationContext()).build();
        setSupportActionBar(customToolbar.getToolbar());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void searchForRecipe(List<OnlineRecipeTag> recipeTags, SortType sortType) {
        OnlineRecipeListFragment fragment = (OnlineRecipeListFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.ONLINE_RECIPE_LIST_TAG));
        if (fragment != null) {
            fragment.searchRecipes(recipeTags, sortType);
        }
    }

    @Override
    public void onRecipeClicked(OnlineRecipe onlineRecipe) {
        Intent intent = new Intent(this, OnlineRecipeDetailActivity.class);
        intent.putExtra(OfflineRecipe.RECIPE, onlineRecipe);
        startActivity(intent);
    }
}