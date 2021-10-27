package com.habbybolan.groceryplanner.details.myrecipe.ingredients.readonly;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeIngredientsReadOnlyBinding;
import com.habbybolan.groceryplanner.details.myrecipe.detailsactivity.RecipeIngredientsActivity;
import com.habbybolan.groceryplanner.details.myrecipe.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import java.util.List;

import javax.inject.Inject;


public class RecipeIngredientsReadOnlyFragment extends Fragment implements RecipeIngredientsContract.IngredientsReadOnlyView {

    private RecipeIngredientsListener recipeIngredientsListener;
    private FragmentRecipeIngredientsReadOnlyBinding binding;
    private CustomToolbar customToolbar;
    private SortType sortType = new SortType();
    private RecipeIngredientsReadOnlyAdapter adapter;
    private List<Ingredient> ingredients;

    @Inject
    RecipeIngredientsContract.PresenterReadOnly presenter;

    public RecipeIngredientsReadOnlyFragment() {}

    public static RecipeIngredientsReadOnlyFragment getInstance() {
        RecipeIngredientsReadOnlyFragment fragment = new RecipeIngredientsReadOnlyFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeIngredientsListener = (RecipeIngredientsListener) context;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_ingredients_read_only, container, false);
        ingredients = recipeIngredientsListener.getMyRecipe().getIngredients();
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.ingredients_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        presenter.searchIngredients(recipeIngredientsListener.getMyRecipe(), search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        presenter.createIngredientList(recipeIngredientsListener.getMyRecipe());
                    }
                }, SortType.SORT_LIST_ALPHABETICAL)
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
        presenter.createIngredientList(recipeIngredientsListener.getMyRecipe());
    }

    /**
     * Initiates the Recycler View to display list of Ingredients and button clickers.
     */
    private void initLayout() {
        adapter = new RecipeIngredientsReadOnlyAdapter(ingredients);
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
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
    public SortType getSortType() {
        return sortType;
    }

    @Override
    public void showList(List<Ingredient> ingredients) {
        adapter.notifyDataSetChanged();
    }


    /**
     * Listener interface implemented by {@link RecipeIngredientsActivity}
     */
    public interface RecipeIngredientsListener extends RecipeIngredientsContract.RecipeIngredientsListener {
    }
}