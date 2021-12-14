package com.habbybolan.groceryplanner.details.offlinerecipes.overview.readonly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeOverviewReadOnlyBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes.AddRecipeToGroceryListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.ui.CustomToolbar;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;

import javax.inject.Inject;

/**
 * Fragment for only displaying the recipe overview information.
 * Uses the basic forms of the Presenter and Interface for only retrieving recipe data.
 */
public abstract class RecipeOverviewReadOnlyFragment
        <T extends RecipeOverviewContract.RecipeOverviewListener, U extends RecipeOverviewContract.PresenterReadOnly<T3, T2>,
                T2 extends OfflineRecipe, T3 extends RecipeOverviewContract.Interactor<T2>>
        extends Fragment implements RecipeOverviewContract.OverviewView {

    @Inject
    U presenter;

    protected T recipeOverviewListener;

    protected CustomToolbar customToolbar;
    protected RecipeTagRecyclerView tagRV;
    protected FragmentRecipeOverviewReadOnlyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_overview_read_only, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(OfflineRecipe.RECIPE)) {
            presenter.setupPresenter(this, bundle.getLong(OfflineRecipe.RECIPE));
        }
        setToolbar();
        return binding.getRoot();
    }

    /**
     * Set up RecyclerView for displaying the tags added to the recipe
     */
    protected void setRV() {
        tagRV = new RecipeTagRecyclerView(presenter.getRecipe().getRecipeTags(), binding.recipeOverviewRvTags, getContext());
    }

    protected void setRecipeGroceryFragment() {
        Fragment RecipeGroceryFragment = AddRecipeToGroceryListFragment.newInstance(presenter.getRecipe());
        getChildFragmentManager()
                .beginTransaction()
                .replace(binding.recipesGroceryContainer.getId(), RecipeGroceryFragment).commit();
    }

    /**
     * Sets the display to the current recipe's values.
     */
    protected void setDisplayToCurrentRecipe() {
        binding.setName(presenter.getRecipe().getName());

        // if the recipe has no category, display it has no category
        if (presenter.getRecipe().getCategoryId() == null)
            binding.setCategoryName(Category.NO_CATEGORY);
        else
            // otherwise, fetch the categoryId and display it
            presenter.fetchRecipeCategory();

        // set serving size, prep time, cook time, and description
        binding.setServingSize(String.valueOf(presenter.getRecipe().getServingSize()));
        binding.setCookTime(String.valueOf(presenter.getRecipe().getCookTime()));
        binding.setPrepTime(String.valueOf(presenter.getRecipe().getPrepTime()));
        binding.setDescription(presenter.getRecipe().getDescription());
    }


    @Override
    public void displayRecipeCategory(RecipeCategory recipeCategory) {
        binding.setCategoryName(recipeCategory.getName());
    }

    @Override
    public void updateRecipe() {
        presenter.loadUpdatedRecipe();
    }

    protected abstract void setToolbar();
}