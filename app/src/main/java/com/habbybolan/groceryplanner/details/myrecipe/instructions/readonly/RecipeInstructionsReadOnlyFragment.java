package com.habbybolan.groceryplanner.details.myrecipe.instructions.readonly;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeInstructionsReadOnlyBinding;
import com.habbybolan.groceryplanner.details.myrecipe.detailsactivity.RecipeIngredientsActivity;
import com.habbybolan.groceryplanner.details.myrecipe.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;


public class RecipeInstructionsReadOnlyFragment extends Fragment implements RecipeInstructionsContract.RecipeInstructionsView {

    @Inject
    RecipeInstructionsContract.PresenterReadOnly presenter;

    private FragmentRecipeInstructionsReadOnlyBinding binding;
    private RecipeStepListener recipeStepListener;
    private CustomToolbar customToolbar;


    public RecipeInstructionsReadOnlyFragment() {}

    public static RecipeInstructionsReadOnlyFragment getInstance() {
        RecipeInstructionsReadOnlyFragment fragment = new RecipeInstructionsReadOnlyFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeStepListener = (RecipeStepListener) context;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_instructions_read_only, container, false);
        setToolbar();
        binding.textInstructions.setText(recipeStepListener.getMyRecipe().getInstructions());
        return binding.getRoot();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.instructions_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeStepListener.onSwapViewInstructions();
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
    }

    /**
     * Listener interface implemented by {@link RecipeIngredientsActivity}
     */
    public interface RecipeStepListener extends RecipeInstructionsContract.RecipeInstructionsListener{
    }
}