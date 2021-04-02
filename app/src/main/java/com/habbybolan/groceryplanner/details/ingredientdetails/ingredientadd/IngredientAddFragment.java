package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentIngredientAddBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.IngredientAddModule;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.primarymodels.IngredientHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Deals with adding multiple Ingredients to the IngredientHolder that are not currently inside the IngredientHolder
 */
public class IngredientAddFragment extends Fragment implements IngredientAddView{

    private IngredientHolder ingredientHolder;
    private FragmentIngredientAddBinding binding;
    private IngredientAddListener ingredientAddListener;
    private List<Ingredient> ingredients = new ArrayList<>();
    private IngredientAddAdapter adapter;
    private Toolbar toolbar;

    @Inject
    IngredientAddPresenter ingredientAddPresenter;

    public IngredientAddFragment() {}

    public static IngredientAddFragment newInstance(IngredientHolder ingredientHolder) {
        IngredientAddFragment fragment = new IngredientAddFragment();
        Bundle args = new Bundle();
        args.putParcelable(IngredientHolder.INGREDIENT_HOLDER, ingredientHolder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ingredientAddListener = (IngredientAddListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().ingredientAddSubComponent(new IngredientAddModule()).inject(this);
        if (getArguments() != null) {
            ingredientHolder = getArguments().getParcelable(IngredientHolder.INGREDIENT_HOLDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_add, container, false);
        setToolbar();
        setList();
        setOnClick();
        ingredientAddPresenter.setView(this);
        ingredientAddPresenter.fetchIngredientsNotInIngredientHolder(ingredientHolder);
        return binding.getRoot();
    }

    /**
     * Set up the on click events.
     */
    private void setOnClick() {
        binding.btnAddIngredients.setOnClickListener(l -> {
            ingredientAddPresenter.addCheckedToIngredientHolder(ingredientHolder);
            ingredientAddListener.leaveIngredientAdd();
        });
    }

    /**
     * Sets up the Adapter for the RecyclerView list of Ingredients.
     */
    private void setList() {
       adapter = new IngredientAddAdapter(ingredients, new IngredientAddItemClickedListener() {
           @Override
           public void onIngredientSelected(Ingredient ingredient) {
                ingredientAddPresenter.selectIngredient(ingredient);
           }

           @Override
           public void onIngredientUnSelected(Ingredient ingredient) {
               ingredientAddPresenter.unSelectIngredient(ingredient);
           }
       });
        RecyclerView rv = binding.ingredientAddList;
        rv.setAdapter(adapter);
    }

    /**
     * Sets up the toolbar.
     */
    private void setToolbar() {
        toolbar = binding.toolbarIngredientAdd.toolbar;
        toolbar.inflateMenu(R.menu.menu_ingredient_add);
        toolbar.setTitle(getString(R.string.title_ingredient_add));
    }

    @Override
    public void showListOfIngredients(List<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "Loading Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // todo: save the loaded and checked Ingredients from the presenter.
        outState.putParcelable(IngredientHolder.INGREDIENT_HOLDER, ingredientHolder);
    }

    public interface IngredientAddItemClickedListener {

        /**
         * Called when an Ingredient is selected
         */
        void onIngredientSelected(Ingredient ingredient);
        /**
         * Called when an Ingredient is un-selected
         */
        void onIngredientUnSelected(Ingredient ingredient);
    }

    public interface IngredientAddListener {

        /**
         * Called when fragment is done
         */
        void leaveIngredientAdd();
    }
}
