package com.habbybolan.groceryplanner.details.myrecipe.overview.readonly;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeOverviewReadOnlyBinding;
import com.habbybolan.groceryplanner.details.myrecipe.overview.RecipeGroceriesAdapter;
import com.habbybolan.groceryplanner.details.myrecipe.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.ui.CustomToolbar;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;

import javax.inject.Inject;

/**
 * Fragment for only displaying the recipe overview information.
 * Uses the basic forms of the Presenter and Interface for only retrieving recipe data.
 */
public class RecipeOverviewReadOnlyFragment extends Fragment implements RecipeOverviewContract.OverviewView {

    @Inject
    RecipeOverviewContract.PresenterReadOnly presenter;

    private RecipeOverviewListener recipeOverviewListener;

    private RecipeGroceriesAdapter groceriesAdapter;

    private CustomToolbar customToolbar;
    private RecipeTagRecyclerView tagRV;
    private FragmentRecipeOverviewReadOnlyBinding binding;

    public RecipeOverviewReadOnlyFragment() {
        // Required empty public constructor
    }

    public static RecipeOverviewReadOnlyFragment newInstance() {
        RecipeOverviewReadOnlyFragment fragment = new RecipeOverviewReadOnlyFragment();
        Bundle args = new Bundle();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeOverviewListener = (RecipeOverviewReadOnlyFragment.RecipeOverviewListener) context;
    }

    /**
     * Set up the RecyclerView for displaying Groceries holding the current Recipe
     * Set up RecyclerView for displaying the tags added to the recipe
     */
    private void setRV() {
        RecyclerView rvGroceries = binding.rvRecipeOverviewGroceries;
        groceriesAdapter = new RecipeGroceriesAdapter(presenter.getLoadedGroceriesHoldingRecipe(), this);
        rvGroceries.setAdapter(groceriesAdapter);
        presenter.fetchGroceriesHoldingRecipe(recipeOverviewListener.getMyRecipe());

        tagRV = new RecipeTagRecyclerView(presenter.getLoadedRecipeTags(), this, binding.recipeOverviewRvTags, getContext());
        presenter.createRecipeTagList(recipeOverviewListener.getMyRecipe());
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.overview_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeOverviewListener.onSwapViewOverview();
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

    /**
     * Sets the display to the current recipe's values.
     */
    private void setDisplayToCurrentRecipe() {
        binding.setName(recipeOverviewListener.getMyRecipe().getName());

        // if the recipe has no category, display it has no category
        if (recipeOverviewListener.getMyRecipe().getCategoryId() == null)
            binding.setCategoryName(Category.NO_CATEGORY);
        else
            // otherwise, fetch the categoryId and display it
            presenter.fetchRecipeCategory(recipeOverviewListener.getMyRecipe().getCategoryId());

        // set serving size, prep time, cook time, and description
        binding.setServingSize(String.valueOf(recipeOverviewListener.getMyRecipe().getServingSize()));
        binding.setCookTime(String.valueOf(recipeOverviewListener.getMyRecipe().getCookTime()));
        binding.setPrepTime(String.valueOf(recipeOverviewListener.getMyRecipe().getPrepTime()));
        binding.setDescription(recipeOverviewListener.getMyRecipe().getDescription());
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

    @Override
    public void onDeleteRecipeTag(RecipeTag recipeTag){
        // todo: segregate interface sop this is not needed?
    }


    public interface RecipeOverviewListener extends RecipeOverviewContract.RecipeOverviewListener {}
}