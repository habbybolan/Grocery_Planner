package com.habbybolan.groceryplanner.details.offlinerecipes.instructions.edit;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeInstructionsEditBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity.myrecipe.RecipeDetailsMyRecipeActivity;
import com.habbybolan.groceryplanner.details.offlinerecipes.instructions.RecipeInstructionsContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;

public class RecipeInstructionsEditFragment extends Fragment implements RecipeInstructionsContract.RecipeInstructionsEditView {

    @Inject
    RecipeInstructionsContract.PresenterEdit presenter;

    private FragmentRecipeInstructionsEditBinding binding;
    private RecipeStepListener recipeStepListener;
    private CustomToolbar customToolbar;


    public RecipeInstructionsEditFragment() {}

    public static RecipeInstructionsEditFragment getInstance() {
        RecipeInstructionsEditFragment fragment = new RecipeInstructionsEditFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_instructions_edit, container, false);
        setToolbar();
        onTextChange();
        binding.editTxtInstructions.setText(recipeStepListener.getRecipe().getInstructions());
        return binding.getRoot();
    }

    /**
     * Listen for when instructions text changes, and save the changes to the database
     */
    private void onTextChange() {
        binding.editTxtInstructions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                saveInstructions();
            }
        });
    }

    /**
     * Saves the Instruction changes to the Recipe.
     */
    private void saveInstructions() {
        String instructions = binding.editTxtInstructions.getText().toString();
        recipeStepListener.getRecipe().setInstructions(instructions);
        presenter.updateRecipe(recipeStepListener.getRecipe());
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.instructions_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeStepListener.onSwapViewInstructions();
                    }
                })
                .addSyncIcon(new CustomToolbar.SyncCallback() {
                    @Override
                    public void syncClicked() {
                        recipeStepListener.onSync();
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
     * Delete the Recipe and leave the Fragment.
     */
    private void deleteRecipe() {
        recipeStepListener.onRecipeDeleted();
    }

    /**
     * Listener interface implemented by {@link RecipeDetailsMyRecipeActivity}
     */
    public interface RecipeStepListener extends RecipeInstructionsContract.RecipeInstructionsMyRecipeListener {
        void onRecipeDeleted();
    }
}
