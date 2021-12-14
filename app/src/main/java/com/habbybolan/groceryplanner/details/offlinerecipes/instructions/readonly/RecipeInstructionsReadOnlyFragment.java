package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeInstructionsReadOnlyBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;


public abstract class RecipeInstructionsReadOnlyFragment
        <T extends RecipeInstructionsContract.RecipeInstructionsListener, U extends RecipeInstructionsContract.PresenterReadOnly<T3, T2>,
                T2 extends OfflineRecipe, T3 extends RecipeInstructionsContract.Interactor<T2>>
        extends Fragment implements RecipeInstructionsContract.RecipeInstructionsView {

    @Inject
    U presenter;

    protected FragmentRecipeInstructionsReadOnlyBinding binding;
    protected T recipeInstructionsListener;
    protected CustomToolbar customToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_instructions_read_only, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(OfflineRecipe.RECIPE)) {
            presenter.setupPresenter(this, bundle.getLong(OfflineRecipe.RECIPE));
        }
        setToolbar();
        return binding.getRoot();
    }

    protected abstract void setToolbar();
}