package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeDetailBinding;
import com.habbybolan.groceryplanner.details.recipe.recipedetailactivity.RecipeDetailActivity;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
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
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.title_grocery_list), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        recipeIngredientsPresenter.searchIngredients(recipeDetailListener.getRecipe(), search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        recipeIngredientsPresenter.createIngredientList(recipeDetailListener.getRecipe());
                    }
                }, SortType.SORT_LIST_ALPHABETICAL)
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
                .build();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeIngredientsPresenter.setView(this);
        if (getArguments() != null && getArguments().containsKey(Ingredient.INGREDIENT)) {
             listItems = getArguments().getParcelableArrayList(Ingredient.INGREDIENT);
        } else {
            // retrieve ingredients associated with recipe
            recipeIngredientsPresenter.createIngredientList(recipeDetailListener.getRecipe());
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

        // on click for adding an Ingredient from floating action button
        View view = binding.recipeDetailBottomAction;
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_bottom_bar_add);
        floatingActionButton.setOnClickListener(v -> recipeDetailListener.createNewItem());

        ImageButton btnAddIngredients = view.findViewById(R.id.btn_add_multiple);
        btnAddIngredients.setOnClickListener(l -> {
            recipeDetailListener.gotoAddIngredients();
        });
    }

    private void deleteRecipe() {
        recipeIngredientsPresenter.deleteRecipe(recipeDetailListener.getRecipe());
        recipeDetailListener.onRecipeDeleted();
    }

    public void reloadList() {
        recipeIngredientsPresenter.createIngredientList(recipeDetailListener.getRecipe());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Recipe Ingredients");
    }

    @Override
    public void deleteSelectedItems() {
        recipeIngredientsPresenter.deleteIngredients(recipeDetailListener.getRecipe(), listItemsChecked);
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
        Recipe getRecipe();
        void gotoAddIngredients();
    }
}
