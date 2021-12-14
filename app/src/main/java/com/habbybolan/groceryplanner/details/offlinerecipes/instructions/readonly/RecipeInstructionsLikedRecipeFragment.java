package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class RecipeInstructionsLikedRecipeFragment
        extends RecipeInstructionsReadOnlyFragment<RecipeInstructionsContract.RecipeInstructionsLikedRecipeListener,
        RecipeInstructionsContract.PresenterLikedRecipe, LikedRecipe, RecipeInstructionsContract.InteractorLikedRecipe> {

    private RecipeInstructionsLikedRecipeFragment() {}

    public static RecipeInstructionsLikedRecipeFragment getInstance(long recipeId) {
        RecipeInstructionsLikedRecipeFragment fragment = new RecipeInstructionsLikedRecipeFragment();
        Bundle args = new Bundle();
        args.putLong(OfflineRecipe.RECIPE, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeInstructionsListener = (RecipeInstructionsListener) context;
    }

    @Override
    protected void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.instructions_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .build();
        customToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void updateRecipe() {
        presenter.loadUpdatedRecipe();
    }

    @Override
    public void displayUpdatedRecipe() {
        binding.textInstructions.setText(presenter.getRecipe().getInstructions());
    }

    @Override
    public void setupRecipeViews() {
        binding.textInstructions.setText(presenter.getRecipe().getInstructions());
    }

    public interface RecipeInstructionsListener extends RecipeInstructionsContract.RecipeInstructionsLikedRecipeListener{}
}
