package com.habbybolan.groceryplanner.online.displayonlinerecipe;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentOnlineRecipeOverviewBinding;
import com.habbybolan.groceryplanner.ui.CustomToolbar;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;

public class OnlineRecipeOverviewFragment extends Fragment{

    private OnlineRecipeContract.OnlineRecipeListener listener;
    private FragmentOnlineRecipeOverviewBinding binding;
    private RecipeTagRecyclerView rvTags;

    public OnlineRecipeOverviewFragment() {
    }

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_online_recipe_overview, container, false);
        rvTags = new RecipeTagRecyclerView(listener.getOnlineRecipe().getRecipeTags(), binding.onlineRecipeOverviewRvTags, getContext());
        setToolbar();
        binding.setName(listener.getOnlineRecipe().getName());
        binding.setServingSize(String.valueOf(listener.getOnlineRecipe().getServingSize()));
        return binding.getRoot();
    }

    private void setToolbar() {
        CustomToolbar customToolbar = new CustomToolbar.CustomToolbarBuilder("Overview", getLayoutInflater(), binding.toolbarContainer, getContext()).build();
        customToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

}