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
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class RecipeOverviewMyRecipeFragment
        extends RecipeOverviewReadOnlyFragment<RecipeOverviewContract.RecipeOverviewMyRecipeListener,
        RecipeOverviewContract.PresenterMyRecipe, MyRecipe, RecipeOverviewContract.InteractorMyRecipeReadOnly> {

    private RecipeOverviewMyRecipeFragment() {}

    public static RecipeOverviewMyRecipeFragment getInstance(long recipeId) {
        RecipeOverviewMyRecipeFragment fragment = new RecipeOverviewMyRecipeFragment();
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
    protected void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.overview_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeOverviewListener.onSwapViewOverview();
                    }
                })
                .addSyncIcon(new CustomToolbar.SyncCallback() {
                    @Override
                    public void syncClicked() {
                        recipeOverviewListener.onSync();
                    }
                })
                .build();
        customToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeOverviewListener = (RecipeOverviewMyListener) context;
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

    public interface RecipeOverviewMyListener extends RecipeOverviewContract.RecipeOverviewMyRecipeListener {}
}
