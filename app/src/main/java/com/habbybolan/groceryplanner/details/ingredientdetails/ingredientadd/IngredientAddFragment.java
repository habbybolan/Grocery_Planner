package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.habbybolan.groceryplanner.models.primarymodels.OfflineIngredientHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Deals with adding multiple Ingredients to the IngredientHolder that are not currently inside the IngredientHolder
 */
public class IngredientAddFragment extends Fragment implements IngredientAddView{

    private OfflineIngredientHolder ingredientHolder;
    private FragmentIngredientAddBinding binding;
    private IngredientAddListener ingredientAddListener;
    private List<Ingredient> ingredients = new ArrayList<>();
    private IngredientAddAdapter adapter;
    private Toolbar toolbar;

    @Inject
    IngredientAddPresenter presenter;

    public IngredientAddFragment() {}

    public static IngredientAddFragment newInstance(OfflineIngredientHolder ingredientHolder) {
        IngredientAddFragment fragment = new IngredientAddFragment();
        Bundle args = new Bundle();
        args.putParcelable(OfflineIngredientHolder.OFFLINE_INGREDIENT_HOLDER, ingredientHolder);
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
            ingredientHolder = getArguments().getParcelable(OfflineIngredientHolder.OFFLINE_INGREDIENT_HOLDER);
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
        presenter.setView(this);
        presenter.fetchIngredientsNotInIngredientHolder(ingredientHolder, "");
        return binding.getRoot();
    }

    /**
     * Set up the on click events.
     */
    private void setOnClick() {
        binding.btnAddIngredients.setOnClickListener(l -> {
            presenter.addIngredientToIngredientHolder(binding.editTxtName.getText().toString(), ingredientHolder);
        });

        binding.editTxtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                presenter.fetchIngredientsNotInIngredientHolder(ingredientHolder, s.toString());
            }
        });
    }

    /**
     * Sets up the Adapter for the RecyclerView list of Ingredients.
     */
    private void setList() {
       adapter = new IngredientAddAdapter(ingredients, new IngredientAddItemClickedListener() {
           @Override
           public void onIngredientClicked(Ingredient ingredient) {
                presenter.addIngredientToIngredientHolder(ingredient, ingredientHolder);
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
    public void leaveFragment(Ingredient ingredient) {
        ingredientAddListener.leaveIngredientAdd(ingredient);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // todo: save the loaded and checked Ingredients from the presenter.
        outState.putParcelable(OfflineIngredientHolder.OFFLINE_INGREDIENT_HOLDER, ingredientHolder);
    }

    public interface IngredientAddItemClickedListener {

        /**
         * Called when an Ingredient is selected. Signals to add ingredient with ingredientName to ingredientHolder
         */
        void onIngredientClicked(Ingredient ingredient);
    }

    public interface IngredientAddListener {

        /**
         * Called when leaving the IngredientAdd fragment without adding any ingredients
         */
        void leaveIngredientAdd(Ingredient ingredient);
    }
}
