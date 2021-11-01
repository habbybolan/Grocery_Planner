package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.edit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeIngredientsEditBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.RecipeDetailsRecipeActivity;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;

public class RecipeIngredientsEditFragment extends NonCategoryListFragment<Ingredient> implements RecipeIngredientsContract.IngredientsEditView {

    private RecipeIngredientsListener recipeIngredientsListener;
    private FragmentRecipeIngredientsEditBinding binding;
    private CustomToolbar customToolbar;
    private SortType sortType = new SortType();

    @Inject
    RecipeIngredientsContract.PresenterEdit presenter;

    public RecipeIngredientsEditFragment() {}

    public static RecipeIngredientsEditFragment getInstance() {
        RecipeIngredientsEditFragment fragment = new RecipeIngredientsEditFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeIngredientsListener = (RecipeIngredientsListener) context;
        attachListener(getContext());
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_ingredients_edit, container, false);
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.ingredients_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        presenter.searchIngredients(recipeIngredientsListener.getRecipe(), search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        presenter.createIngredientList(recipeIngredientsListener.getRecipe());
                    }
                }, SortType.SORT_LIST_ALPHABETICAL)
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeIngredientsListener.onSwapViewIngredients();
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        presenter.setView(this);
        listItems = recipeIngredientsListener.getRecipe().getIngredients();
        presenter.createIngredientList(recipeIngredientsListener.getRecipe());
    }

    /**
     * Initiates the Recycler View to display list of Ingredients and button clickers.
     */
    private void initLayout() {
        adapter = new RecipeIngredientsEditAdapter(listItems, this);
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        binding.btnAddNewIngredient.setOnClickListener(l -> {
            recipeIngredientsListener.createNewItem();
        });

        binding.btnAddExistingIngredient.setOnClickListener(l -> {
            recipeIngredientsListener.gotoAddIngredients();
        });
    }

    private void deleteRecipe() {
        recipeIngredientsListener.onRecipeDeleted();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Recipe Ingredients");
    }

    @Override
    public void deleteSelectedItems() {
        presenter.deleteIngredients(recipeIngredientsListener.getRecipe(), listItemsChecked);
    }

    @Override
    public SortType getSortType() {
        return sortType;
    }

    public void reloadList() {
        presenter.createIngredientList(recipeIngredientsListener.getRecipe());
    }

    /**
     * Listener interface implemented by {@link RecipeDetailsRecipeActivity}
     */
    public interface RecipeIngredientsListener extends RecipeIngredientsContract.RecipeIngredientsMyRecipeListener, ItemListener<Ingredient> {

        void createNewItem();
        void onRecipeDeleted();
        void gotoAddIngredients();
    }
}
