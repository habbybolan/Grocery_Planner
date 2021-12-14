package com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class RecipeOverviewLikedRecipeFragment
        extends RecipeOverviewReadOnlyFragment<RecipeOverviewContract.RecipeOverviewLikedRecipeListener,
        RecipeOverviewContract.PresenterLikedRecipe, LikedRecipe, RecipeOverviewContract.InteractorLikedRecipeReadOnly> {

    private RecipeOverviewLikedRecipeFragment() {}

    public static RecipeOverviewLikedRecipeFragment getInstance(long recipeId) {
        RecipeOverviewLikedRecipeFragment fragment = new RecipeOverviewLikedRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(OfflineRecipe.RECIPE, recipeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeOverviewListener = (RecipeOverviewListener) context;
    }

    @Override
    protected void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.overview_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .build();
        customToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void displayUpdatedRecipe() {
        tagRV.updateDisplay();
        setDisplayToCurrentRecipe();
    }

    @Override
    public void setupRecipeViews() {
        setRV();
        setDisplayToCurrentRecipe();
        setRecipeGroceryFragment();
    }

    public interface RecipeOverviewListener extends RecipeOverviewContract.RecipeOverviewLikedRecipeListener {}
}
