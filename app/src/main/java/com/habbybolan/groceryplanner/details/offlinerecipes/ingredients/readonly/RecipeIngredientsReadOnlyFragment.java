package com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.readonly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeIngredientsReadOnlyBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.ingredients.RecipeIngredientsContract;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import java.util.List;

import javax.inject.Inject;


public abstract class RecipeIngredientsReadOnlyFragment
        <T extends RecipeIngredientsContract.RecipeIngredientsListener, U extends RecipeIngredientsContract.PresenterReadOnly<T3, T2>,
                T2 extends OfflineRecipe, T3 extends RecipeIngredientsContract.Interactor<T2>>
        extends Fragment implements RecipeIngredientsContract.IngredientsReadOnlyView {

    protected T recipeIngredientsListener;
    protected FragmentRecipeIngredientsReadOnlyBinding binding;
    protected CustomToolbar customToolbar;
    protected SortType sortType = new SortType();
    protected RecipeIngredientsReadOnlyAdapter adapter;
    protected List<Ingredient> ingredients;

    @Inject
    U presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_ingredients_read_only, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(OfflineRecipe.RECIPE)) {
            presenter.setupPresenter(this, bundle.getLong(OfflineRecipe.RECIPE));
        }
        setToolbar();
        return binding.getRoot();
    }

    protected abstract void setToolbar();

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        presenter.createIngredientList();
    }

    /**
     * Initiates the Recycler View to display list of Ingredients and button clickers.
     */
    protected void initLayout() {
        adapter = new RecipeIngredientsReadOnlyAdapter(ingredients);
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    @Override
    public SortType getSortType() {
        return sortType;
    }

    @Override
    public void showList(List<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        adapter.notifyDataSetChanged();
    }
}