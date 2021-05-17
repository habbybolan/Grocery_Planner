package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.ingredients;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentOnlineRecipeEditIngredientsBinding;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class OnlineRecipeEditIngredientsFragment extends Fragment implements
    OnlineRecipeIngredientsEditContract.IngredientAdapterView {

    private FragmentOnlineRecipeEditIngredientsBinding binding;
    private OnlineRecipeIngredientsEditContract.IngredientListener listener;
    private IngredientListAdapter adapter;
    private CustomToolbar customToolbar;

    public OnlineRecipeEditIngredientsFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnlineRecipeIngredientsEditContract.IngredientListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_online_recipe_edit_ingredients, container, false);
        adapter = new IngredientListAdapter(listener.getRecipe().getIngredients(), this);
        binding.ingredientList.setAdapter(adapter);
        setToolbar();
        return binding.getRoot();
    }

    @Override
    public void onIngredientClicked(Ingredient ingredient) {
        // todo:
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getResources().getString(R.string.ingredients_title), getLayoutInflater(), binding.toolbarContainer, getContext()).build();
        customToolbar.getToolbar().setNavigationOnClickListener(l -> {
            getActivity().onBackPressed();
        });
    }
}