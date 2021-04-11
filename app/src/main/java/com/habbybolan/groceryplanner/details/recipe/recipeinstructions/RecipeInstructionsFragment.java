package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeInstructionsBinding;
import com.habbybolan.groceryplanner.details.recipe.recipedetailactivity.RecipeDetailActivity;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.ui.PopupBuilder;

import javax.inject.Inject;

public class RecipeInstructionsFragment extends Fragment implements RecipeInstructionsView {

    @Inject
    RecipeInstructionsPresenter recipeInstructionsPresenter;

    private FragmentRecipeInstructionsBinding binding;
    private RecipeStepListener recipeStepListener;
    private Toolbar toolbar;


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
        initClickers();
        binding.editTxtInstructions.setText(recipeStepListener.getRecipe().getInstructions());
        return binding.getRoot();
    }

    /**
     * Set up button clicker functionality.
     */
    private void initClickers() {

        // clicker for saving the instructions text to the recipe
        binding.btnSave.setOnClickListener(l -> {
            String instructions = binding.editTxtInstructions.getText().toString();
            recipeStepListener.getRecipe().setInstructions(instructions);
            recipeInstructionsPresenter.updateRecipe(recipeStepListener.getRecipe());
        });

        // clicker for undoing the instructions text
        binding.btnCancel.setOnClickListener(l -> {
            binding.editTxtInstructions.setText(recipeStepListener.getRecipe().getInstructions());
        });
    }

    private void setToolbar() {
        toolbar = binding.toolbarRecipeSteps.toolbar;
        toolbar.inflateMenu(R.menu.menu_recipe_non_list);
        toolbar.setTitle(getString(R.string.title_recipe_steps));

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_sort:
                        showSortPopup(getActivity().findViewById(R.id.action_sort));
                        return true;
                    case R.id.action_delete:
                        deleteRecipeCheck();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeInstructionsPresenter.setView(this);
    }

    /**
     * Menu popup for giving different ways to sort the list.
     * @param v     The view to anchor the popup menu to
     */
    private void showSortPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.inflate(R.menu.popup_sort_grocery_list);
        popup.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
                case R.id.popup_alphabetically_grocery_list:
                    Toast.makeText(getContext(), "alphabetically", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.popup_test_grocery_list:
                    Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    /**
     * Ask the user to confirm deleting the Recipe.
     */
    private void deleteRecipeCheck() {
        PopupBuilder.createDeleteDialogue(getLayoutInflater(), "recipe", binding.recipeStepContainer, getContext(), new PopupBuilder.DeleteDialogueInterface() {
            @Override
            public void deleteItem() {
                deleteRecipe();
            }
        });
    }

    /**
     * Delete the Recipe and leave the Fragment.
     */
    private void deleteRecipe() {
        recipeInstructionsPresenter.deleteRecipe(recipeStepListener.getRecipe());
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
        Recipe getRecipe();
    }
}
