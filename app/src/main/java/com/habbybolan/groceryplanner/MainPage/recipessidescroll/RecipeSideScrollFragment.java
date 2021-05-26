package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.habbybolan.groceryplanner.databinding.FragmentRecipeSideScrollBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeSideScrollModule;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeSideScrollFragment extends Fragment implements RecipeSideScrollView {

    @Inject
    RecipeSideScrollPresenter presenter;

    private FragmentRecipeSideScrollBinding binding;

    public static final String RECIPE_LIST_TYPE = "TYPE_KEY";
    // recipe type list with code IntDef @recipeListType, default value NEW_TYPE
    private String recipeType = RecipeListType.NEW_TYPE;
    public static final String OFFSET_KEY = "OFFSET";
    // The current offset of recipes to retrieve from web service
    private int offset = 0;
    public static final String NUM_RECIPES_KEY = "NUM_RECIPES";
    // number of recipes to retrieve from the web service per request
    private int numRecipesToLoad = 20;
    // list of recipes displayed so far
    private List<OnlineRecipe> recipes = new ArrayList<>();

    private RecipeSideScrollAdapter adapter;

    private RecipeSideScrollListener listener;

    public RecipeSideScrollFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (RecipeSideScrollListener) context;
    }

    public static RecipeSideScrollFragment newInstance(@RecipeListType String recipeType) {
        RecipeSideScrollFragment fragment = new RecipeSideScrollFragment();
        Bundle args = new Bundle();
        args.putString(RECIPE_LIST_TYPE, recipeType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeSideScrollSubComponent(new RecipeSideScrollModule()).inject(this);
        if (getArguments() != null) {
            recipeType = getArguments().getString(RECIPE_LIST_TYPE);
            if (getArguments().containsKey(OFFSET_KEY)) offset = getArguments().getInt(OFFSET_KEY);
            if (getArguments().containsKey(NUM_RECIPES_KEY)) numRecipesToLoad = getArguments().getInt(NUM_RECIPES_KEY);
            if (getArguments().containsKey(OfflineRecipe.RECIPE)) recipes = getArguments().getParcelableArrayList(OfflineRecipe.RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_side_scroll, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        // rv
        adapter = new RecipeSideScrollAdapter(recipes, this);
        RecyclerView rv = binding.rvRecipeSideScroll;
        rv.setAdapter(adapter);

        presenter.createList(recipeType);
    }

    @Override
    public void loadingStarted() {
        // todo:
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addToList(List<OnlineRecipe> recipes) {
        adapter.addToList(recipes);
    }

    @Override
    public void createNewList(List<OnlineRecipe> recipes) {
        // todo:
    }

    @Override
    public void onRecipeSelected(OnlineRecipe recipe) {
        listener.onRecipeSelected(recipe);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(OFFSET_KEY, offset);
        outState.putInt(NUM_RECIPES_KEY, numRecipesToLoad);
        outState.putString(RECIPE_LIST_TYPE, recipeType);
        outState.putParcelableArrayList(OfflineRecipe.RECIPE, (ArrayList<? extends Parcelable>) recipes);
    }

    public interface RecipeSideScrollListener {

        /**
         * On Side Scroll recipe selected, send back to Activity to display more info about the Recipe.
         * @param recipe    The Recipe selected to show more info about
         */
        void onRecipeSelected(OnlineRecipe recipe);
    }
}