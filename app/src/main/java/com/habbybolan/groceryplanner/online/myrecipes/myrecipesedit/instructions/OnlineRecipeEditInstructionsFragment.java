package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.instructions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentOnlineRecipeEditInstructionsBinding;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class OnlineRecipeEditInstructionsFragment extends Fragment {

    private FragmentOnlineRecipeEditInstructionsBinding binding;
    private OnlineRecipeInstructionsContract.instructionsListener listener;
    private CustomToolbar customToolbar;

    public OnlineRecipeEditInstructionsFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnlineRecipeInstructionsContract.instructionsListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_online_recipe_edit_instructions, container, false);
        binding.setInstructions(listener.getRecipe().getInstructions());
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getResources().getString(R.string.instructions_title), getLayoutInflater(), binding.toolbarContainer, getContext()).build();
        customToolbar.getToolbar().setNavigationOnClickListener(l -> {
            getActivity().onBackPressed();
        });
    }
}