package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class RecipeIngredientsMyRecipeFragment
        extends RecipeIngredientsReadOnlyFragment<RecipeIngredientsContract.RecipeIngredientsMyRecipeListener,
        RecipeIngredientsContract.PresenterMyRecipe, MyRecipe, RecipeIngredientsContract.InteractorMyRecipe>{

    private RecipeIngredientsMyRecipeFragment() {}

    public static RecipeIngredientsMyRecipeFragment getInstance(long recipeId) {
        RecipeIngredientsMyRecipeFragment fragment = new RecipeIngredientsMyRecipeFragment();
        Bundle args = new Bundle();
        args.putLong(OfflineRecipe.RECIPE, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeIngredientsListener = (RecipeIngredientsListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
    }

    protected void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.ingredients_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        presenter.searchIngredients(search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        presenter.createIngredientList();
                    }
                }, SortType.SORT_LIST_ALPHABETICAL)
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeIngredientsListener.onSwapViewIngredients();
                    }
                })
                .addSyncIcon(new CustomToolbar.SyncCallback() {
                    @Override
                    public void syncClicked() {
                        recipeIngredientsListener.onSync();
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
    public void updateRecipe() {
        presenter.loadUpdatedRecipe();
    }

    @Override
    public void setupRecipeViews() {
        ingredients = presenter.getRecipe().getIngredients();
        initLayout();
    }

    public interface RecipeIngredientsListener extends RecipeIngredientsContract.RecipeIngredientsMyRecipeListener{}
}
