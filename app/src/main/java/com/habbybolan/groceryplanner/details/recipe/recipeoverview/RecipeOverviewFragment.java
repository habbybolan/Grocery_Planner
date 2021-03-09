package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeOverviewBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;

import java.util.List;

import javax.inject.Inject;

public class RecipeOverviewFragment extends Fragment implements RecipeOverviewView {

    @Inject
    RecipeOverviewPresenter recipeOverviewPresenter;
    private FragmentRecipeOverviewBinding binding;
    private RecipeOverviewListener recipeOverviewListener;
    private Recipe recipe;
    private RecipeCategory recipeCategory;

    private boolean recipeNameChanged = false;
    private boolean recipeCategoryChanged = false;
    private boolean recipeTagsChanged = false;

    public RecipeOverviewFragment() {}

    public static RecipeOverviewFragment getInstance(Recipe recipe, RecipeCategory recipeCategory) {
        RecipeOverviewFragment fragment = new RecipeOverviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(Recipe.RECIPE, recipe);
        args.putParcelable(RecipeCategory.RECIPE_CATEGORY, recipeCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule(), new IngredientEditModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeOverviewListener = (RecipeOverviewListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_overview, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeOverviewPresenter.setView(this);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Recipe.RECIPE);
            recipeCategory = getArguments().getParcelable(RecipeCategory.RECIPE_CATEGORY);
        }
        showRecipeOverview();
        recipeOverviewPresenter.loadAllRecipeCategories();
        initLayout();
    }

    private void showRecipeOverview() {
        // todo: use local variables
        Recipe recipe = recipeOverviewListener.getRecipe();
        RecipeCategory recipeCategory = recipeOverviewListener.getRecipeCategory();
        binding.setName(recipe.getName());
        // todo: label
        if (recipeCategory != null) binding.setCategoryName(recipeCategory.getName());
    }

    /**
     * Initiate necessary onClicks and general data.
     */
    private void initLayout() {
        binding.setName(recipe.getName());
        if (recipeCategory != null) binding.setCategoryName(recipeCategory.getName());

        binding.recipeOverviewCategory.setOnClickListener(v -> {
            // display the recipe categories if they are loaded in
            recipeOverviewPresenter.displayRecipeCategories();
        });

        binding.recipeOverviewName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }
            @Override
            public void afterTextChanged(Editable s) {
                // if the text for the recipe name is changed from recipe's name, set as changed
                if (!recipe.getName().equals(binding.recipeOverviewName.getText()))
                    recipeNameChanged = true;
                else
                    recipeNameChanged = false;
                saveButtonVisibility();
            }
        });
    }

    @Override
    public void createCategoriesAlertDialogue(String[] categoryNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(categoryNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), categoryNames[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    /**
     * Displays the save button if any recipe value has changed, otherwise hide button
     */
    private void saveButtonVisibility() {
        if (recipeCategoryChanged || recipeNameChanged || recipeTagsChanged)
            binding.saveButton.setVisibility(View.VISIBLE);
        else
            binding.saveButton.setVisibility(View.GONE);
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface RecipeOverviewListener {

        Recipe getRecipe();
        RecipeCategory getRecipeCategory();
        List<RecipeCategory> getRecipeCategories();
    }
}
