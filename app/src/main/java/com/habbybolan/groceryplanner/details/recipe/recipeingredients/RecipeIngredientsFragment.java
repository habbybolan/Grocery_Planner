package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

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
import com.habbybolan.groceryplanner.databinding.FragmentRecipeDetailBinding;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailActivity;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;

public class RecipeIngredientsFragment extends NonCategoryListFragment<Ingredient> {

    private RecipeDetailListener recipeDetailListener;
    private FragmentRecipeDetailBinding binding;
    private CustomToolbar customToolbar;
    private SortType sortType = new SortType();

    @Inject
    RecipeIngredientsPresenter recipeIngredientsPresenter;

    public RecipeIngredientsFragment() {}

    public static RecipeIngredientsFragment getInstance() {
        RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeDetailListener = (RecipeDetailListener) context;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.ingredients_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        recipeIngredientsPresenter.searchIngredients(recipeDetailListener.getOfflineRecipe(), search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        recipeIngredientsPresenter.createIngredientList(recipeDetailListener.getOfflineRecipe());
                    }
                }, SortType.SORT_LIST_ALPHABETICAL)
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
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
        recipeIngredientsPresenter.setView(this);
        if (getArguments() != null && getArguments().containsKey(Ingredient.INGREDIENT)) {
             listItems = getArguments().getParcelableArrayList(Ingredient.INGREDIENT);
        } else {
            // retrieve ingredients associated with recipe
            recipeIngredientsPresenter.createIngredientList(recipeDetailListener.getOfflineRecipe());
        }
    }

    /**
     * Initiates the Recycler View to display list of Ingredients and button clickers.
     */
    private void initLayout() {
        adapter = new RecipeIngredientsAdapter(listItems, this);
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        binding.btnAddNewIngredient.setOnClickListener(l -> {
            recipeDetailListener.createNewItem();
        });

        binding.btnAddExistingIngredient.setOnClickListener(l -> {
            recipeDetailListener.gotoAddIngredients();
        });
    }

    private void deleteRecipe() {
        recipeIngredientsPresenter.deleteRecipe(recipeDetailListener.getOfflineRecipe());
        recipeDetailListener.onRecipeDeleted();
    }

    public void reloadList() {
        recipeIngredientsPresenter.createIngredientList(recipeDetailListener.getOfflineRecipe());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Recipe Ingredients");
    }

    @Override
    public void deleteSelectedItems() {
        recipeIngredientsPresenter.deleteIngredients(recipeDetailListener.getOfflineRecipe(), listItemsChecked);
    }

    @Override
    public SortType getSortType() {
        return sortType;
    }

    /**
     * Listener interface implemented by {@link RecipeDetailActivity}
     */
    public interface RecipeDetailListener extends ItemListener<Ingredient> {

        void createNewItem();
        void onRecipeDeleted();
        OfflineRecipe getOfflineRecipe();
        void gotoAddIngredients();
    }
}
