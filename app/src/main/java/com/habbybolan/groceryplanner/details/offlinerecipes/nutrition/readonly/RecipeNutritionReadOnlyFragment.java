package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.readonly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeNutritionReadOnlyBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.nutrition.RecipeNutritionContract;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;

/**
 *
 */
public abstract class RecipeNutritionReadOnlyFragment
        <T extends RecipeNutritionContract.RecipeNutritionListener, U extends RecipeNutritionContract.PresenterReadOnly<T3, T2>,
                T2 extends OfflineRecipe, T3 extends RecipeNutritionContract.Interactor<T2>>
        extends Fragment
        implements RecipeNutritionContract.NutritionView {

    @Inject
    U presenter;

    protected FragmentRecipeNutritionReadOnlyBinding binding;
    protected T recipeNutritionListener;
    protected CustomToolbar customToolbar;
    protected RecipeNutritionReadOnlyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_nutrition_read_only, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(OfflineRecipe.RECIPE)) {
            presenter.setupPresenter(this, bundle.getLong(OfflineRecipe.RECIPE));
        }
        setToolbar();
        return binding.getRoot();
    }

    protected void setList() {
        adapter = new RecipeNutritionReadOnlyAdapter(presenter.getRecipe().getAllNutritionList());
        RecyclerView rv = binding.recipeNutritionList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    protected abstract void setToolbar();
}