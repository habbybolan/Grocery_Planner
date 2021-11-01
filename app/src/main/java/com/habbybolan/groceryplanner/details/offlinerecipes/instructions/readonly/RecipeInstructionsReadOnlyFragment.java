package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.readonly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeInstructionsReadOnlyBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;


public abstract class RecipeInstructionsReadOnlyFragment<T extends RecipeInstructionsContract.RecipeInstructionsListener> extends Fragment implements RecipeInstructionsContract.RecipeInstructionsView {

    @Inject
    RecipeInstructionsContract.PresenterReadOnly presenter;

    protected FragmentRecipeInstructionsReadOnlyBinding binding;
    protected T recipeInstructionsListener;
    protected CustomToolbar customToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_instructions_read_only, container, false);
        presenter.setView(this);
        setToolbar();
        binding.textInstructions.setText(recipeInstructionsListener.getRecipe().getInstructions());
        return binding.getRoot();
    }

    protected abstract void setToolbar();

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        presenter.setView(this);
    }
}