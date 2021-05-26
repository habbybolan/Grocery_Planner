package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeInstructionsBinding;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailActivity;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import javax.inject.Inject;

public class RecipeInstructionsFragment extends Fragment implements RecipeInstructionsView {

    @Inject
    RecipeInstructionsPresenter recipeInstructionsPresenter;

    private FragmentRecipeInstructionsBinding binding;
    private RecipeStepListener recipeStepListener;
    private CustomToolbar customToolbar;


    public RecipeInstructionsFragment() {}

    public static RecipeInstructionsFragment getInstance() {
        RecipeInstructionsFragment fragment = new RecipeInstructionsFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_instructions, container, false);
        setToolbar();
        binding.editTxtInstructions.setText(recipeStepListener.getOfflineRecipe().getInstructions());
        return binding.getRoot();
    }

    /**
     * Saves the Instruction changes to the Recipe.
     */
    private void saveInstructions() {
        String instructions = binding.editTxtInstructions.getText().toString();
        recipeStepListener.getOfflineRecipe().setInstructions(instructions);
        recipeInstructionsPresenter.updateRecipe(recipeStepListener.getOfflineRecipe());
    }

    /**
     * Cancel the instruction changes.
     */
    private void cancelInstructionChanges() {
        binding.editTxtInstructions.setText(recipeStepListener.getOfflineRecipe().getInstructions());
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.instructions_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
                .addSaveIcon(new CustomToolbar.SaveCallback() {
                    @Override
                    public void saveClicked() {
                        saveInstructions();
                    }
                })
                .addCancelIcon(new CustomToolbar.CancelCallback() {
                    @Override
                    public void cancelClicked() {
                        cancelInstructionChanges();
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
        recipeInstructionsPresenter.setView(this);
    }

    /**
     * Delete the Recipe and leave the Fragment.
     */
    private void deleteRecipe() {
        recipeInstructionsPresenter.deleteRecipe(recipeStepListener.getOfflineRecipe());
        recipeStepListener.onRecipeDeleted();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Recipe Steps");
    }

    /**
     * Listener interface implemented by {@link RecipeDetailActivity}
     */
    public interface RecipeStepListener {

        void onRecipeDeleted();
        OfflineRecipe getOfflineRecipe();
    }
}
