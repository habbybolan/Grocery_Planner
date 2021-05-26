package com.habbybolan.groceryplanner.online.myrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityMyRecipesBinding;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListContract;
import com.habbybolan.groceryplanner.online.discover.recipelist.OnlineRecipeListFragment;
import com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.OnlineRecipeDetailsEditActivity;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import java.util.Objects;

public class MyRecipesActivity extends AppCompatActivity implements OnlineRecipeListContract.OnlineRecipeListListener {

    private ActivityMyRecipesBinding binding;
    private CustomToolbar customToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_recipes);
        setToolbar();
        createUploadedRecipeList();
    }

    private void createUploadedRecipeList() {
        OnlineRecipeListFragment fragment = (OnlineRecipeListFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.ONLINE_RECIPE_LIST_TAG));
        if (fragment != null) {
            fragment.searchUploadedRecipes(1, new SortType());
        }
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder("Uploaded Recipes", getLayoutInflater(), binding.customToolbar, getApplicationContext()).build();
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
    public void onRecipeClicked(OnlineRecipe onlineRecipe) {
        Intent intent = new Intent(this, OnlineRecipeDetailsEditActivity.class);
        intent.putExtra(OfflineRecipe.RECIPE, onlineRecipe);
        startActivity(intent);
    }
}