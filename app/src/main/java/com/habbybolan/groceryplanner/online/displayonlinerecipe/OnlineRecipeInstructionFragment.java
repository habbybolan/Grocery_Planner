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
import com.habbybolan.groceryplanner.databinding.FragmentOnlineRecipeInstructionBinding;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

public class OnlineRecipeInstructionFragment extends Fragment {

    private FragmentOnlineRecipeInstructionBinding binding;
    private OnlineRecipeContract.OnlineRecipeListener listener;

    public OnlineRecipeInstructionFragment() {}

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_online_recipe_instruction, container, false);
        binding.setInstructions(listener.getOnlineRecipe().getInstructions());
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        CustomToolbar customToolbar = new CustomToolbar.CustomToolbarBuilder("Instructions", getLayoutInflater(), binding.toolbarContainer, getContext()).build();
        customToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}