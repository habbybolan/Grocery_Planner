package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class RecipeNutritionMyRecipeFragment extends RecipeNutritionReadOnlyFragment<RecipeNutritionContract.RecipeNutritionRecipeListener> {

    public RecipeNutritionMyRecipeFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeNutritionListener = (RecipeNutritionRecipeListener) context;
    }

    protected void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.nutrition_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeNutritionListener.onSwapViewNutrition();
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

    public interface RecipeNutritionRecipeListener extends RecipeNutritionContract.RecipeNutritionRecipeListener {
    }
}
