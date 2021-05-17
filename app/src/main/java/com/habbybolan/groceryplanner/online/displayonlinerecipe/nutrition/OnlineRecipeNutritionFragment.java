package com.habbybolan.groceryplanner.online.displayonlinerecipe.nutrition;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentOnlineRecipeNutritionBinding;
import com.habbybolan.groceryplanner.online.displayonlinerecipe.OnlineRecipeContract;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class OnlineRecipeNutritionFragment extends Fragment {

    private FragmentOnlineRecipeNutritionBinding binding;
    private OnlineRecipeContract.OnlineRecipeListener listener;

    public OnlineRecipeNutritionFragment() {}

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_online_recipe_nutrition, container, false);
        setToolbar();
        OnlineRecipeNutritionAdapter adapter = new OnlineRecipeNutritionAdapter(listener.getOnlineRecipe().getNutritionList());
        binding.recipeNutritionList.setAdapter(adapter);
        return binding.getRoot();
    }

    private void setToolbar() {
        CustomToolbar customToolbar = new CustomToolbar.CustomToolbarBuilder("Nutrition", getLayoutInflater(), binding.toolbarContainer, getContext()).build();
        customToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}