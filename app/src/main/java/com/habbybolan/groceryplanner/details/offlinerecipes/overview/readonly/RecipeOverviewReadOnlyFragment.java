package com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeOverviewReadOnlyBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeGroceriesAdapter;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.ui.CustomToolbar;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;

import javax.inject.Inject;

/**
 * Fragment for only displaying the recipe overview information.
 * Uses the basic forms of the Presenter and Interface for only retrieving recipe data.
 */
public abstract class RecipeOverviewReadOnlyFragment<T extends RecipeOverviewContract.RecipeOverviewListener> extends Fragment implements RecipeOverviewContract.OverviewView {

    @Inject
    RecipeOverviewContract.PresenterReadOnly presenter;

    protected T recipeOverviewListener;

    private RecipeGroceriesAdapter groceriesAdapter;

    protected CustomToolbar customToolbar;
    private RecipeTagRecyclerView tagRV;
    protected FragmentRecipeOverviewReadOnlyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_overview_read_only, container, false);
        presenter.setView(this);
        setToolbar();
        setRV();
        setDisplayToCurrentRecipe();
        return binding.getRoot();
    }

    /**
     * Set up the RecyclerView for displaying Groceries holding the current Recipe
     * Set up RecyclerView for displaying the tags added to the recipe
     */
    private void setRV() {
        RecyclerView rvGroceries = binding.rvRecipeOverviewGroceries;
        groceriesAdapter = new RecipeGroceriesAdapter(presenter.getLoadedGroceriesHoldingRecipe(), this);
        rvGroceries.setAdapter(groceriesAdapter);
        presenter.fetchGroceriesHoldingRecipe(recipeOverviewListener.getRecipe());

        tagRV = new RecipeTagRecyclerView(recipeOverviewListener.getRecipe().getRecipeTags(), binding.recipeOverviewRvTags, getContext());
    }



    /**
     * Sets the display to the current recipe's values.
     */
    private void setDisplayToCurrentRecipe() {
        binding.setName(recipeOverviewListener.getRecipe().getName());

        // if the recipe has no category, display it has no category
        if (recipeOverviewListener.getRecipe().getCategoryId() == null)
            binding.setCategoryName(Category.NO_CATEGORY);
        else
            // otherwise, fetch the categoryId and display it
            presenter.fetchRecipeCategory(recipeOverviewListener.getRecipe().getCategoryId());

        // set serving size, prep time, cook time, and description
        binding.setServingSize(String.valueOf(recipeOverviewListener.getRecipe().getServingSize()));
        binding.setCookTime(String.valueOf(recipeOverviewListener.getRecipe().getCookTime()));
        binding.setPrepTime(String.valueOf(recipeOverviewListener.getRecipe().getPrepTime()));
        binding.setDescription(recipeOverviewListener.getRecipe().getDescription());
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayGroceriesHoldingRecipe() {
        groceriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayRecipeCategory(RecipeCategory recipeCategory) {
        binding.setCategoryName(recipeCategory.getName());
    }

    @Override
    public void displayRecipeTags() {
        tagRV.updateDisplay();
    }

    @Override
    public void onGroceryHoldingRecipeClicked(Grocery grocery) {
        // todo: should ingredients added to grocery be shown?
        //      todo: possibly message notifying that edit mode must be enabled to select and modify grocery list
    }

    protected abstract void setToolbar();
}