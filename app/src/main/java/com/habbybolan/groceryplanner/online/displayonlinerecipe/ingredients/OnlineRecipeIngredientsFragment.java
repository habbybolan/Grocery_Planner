package com.habbybolan.groceryplanner.online.displayonlinerecipe.ingredients;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentOnlineRecipeIngredientsBinding;
import com.habbybolan.groceryplanner.online.displayonlinerecipe.OnlineRecipeContract;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class OnlineRecipeIngredientsFragment extends Fragment {

    private FragmentOnlineRecipeIngredientsBinding binding;
    private OnlineRecipeContract.OnlineRecipeListener listener;

    public OnlineRecipeIngredientsFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnlineRecipeContract.OnlineRecipeListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_online_recipe_ingredients, container, false);
        setToolbar();
        OnlineRecipeIngredientsAdapter adapter = new OnlineRecipeIngredientsAdapter(listener.getOnlineRecipe().getIngredients());
        binding.rvIngredientList.setAdapter(adapter);
        return binding.getRoot();
    }

    private void setToolbar() {
        CustomToolbar customToolbar = new CustomToolbar.CustomToolbarBuilder("Ingredients", getLayoutInflater(), binding.toolbarContainer, getContext()).build();
        customToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}