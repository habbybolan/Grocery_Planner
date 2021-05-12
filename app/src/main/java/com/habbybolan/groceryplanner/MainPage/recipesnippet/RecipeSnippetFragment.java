package com.habbybolan.groceryplanner.MainPage.recipesnippet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeSnippetBinding;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;

import javax.inject.Inject;

public class RecipeSnippetFragment extends Fragment implements RecipeSnippetView {

    private FragmentRecipeSnippetBinding binding;
    private OnlineRecipe recipe;
    private RecipeTagRecyclerView rvTags;
    private RecipeSnippetIngredientsAdapter ingredientAdapter;

    @Inject
    RecipeSnippetPresenter presenter;

    public RecipeSnippetFragment() {}

    public static RecipeSnippetFragment newInstance(OnlineRecipe recipe) {
        RecipeSnippetFragment fragment = new RecipeSnippetFragment();
        Bundle args = new Bundle();
        args.putParcelable(Recipe.RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Recipe.RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_snippet, container, false);
        setRV();
        setViews();
        return binding.getRoot();
    }

    /**
     * Set the non-RecyclerView values
     */
    private void setViews() {
        binding.setRecipeName(recipe.getName());
        binding.setLikes(String.valueOf(recipe.getLikes()));
        binding.setInstructions(recipe.getInstructions());//recipe.getInstructions());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setRV() {
        // set the tags recycler view
        rvTags = new RecipeTagRecyclerView(recipe.getRecipeTags(), binding.recipeSnippetRvTags, getContext());

        // set ingredients into RecyclerView
        RecyclerView rvIngredients  = binding.recipeSnippetRvIngredients;
        ingredientAdapter = new RecipeSnippetIngredientsAdapter(recipe.getIngredients(), this);
        rvIngredients.setAdapter(ingredientAdapter);
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}