package com.habbybolan.groceryplanner.online.discover.recipelist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentOnlineRecipeListBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.OnlineRecipeModule;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.online.discover.searchfilter.OnlineRecipeTag;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class OnlineRecipeListFragment extends Fragment implements OnlineRecipeListContract.RecipeListView,
                                                                    OnlineRecipeListContract.AdapterView
{

    private FragmentOnlineRecipeListBinding binding;
    private List<OnlineRecipeTag> searchValues = new ArrayList<>();
    private SortType sortType = new SortType(SortType.SORT_RELEVANT);
    private List<OnlineRecipe> recipes = new ArrayList<>();
    private OnlineRecipeListAdapter adapter;
    private List<OnlineRecipeTag> recipeTags;
    private OnlineRecipeListContract.OnlineRecipeListListener listener;

    @Inject
    OnlineRecipeListContract.Presenter presenter;

    public OnlineRecipeListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnlineRecipeListContract.OnlineRecipeListListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().onlineRecipeListSubComponent(new OnlineRecipeModule()).inject(this);
        if (getArguments() != null) {
            searchValues = getArguments().getParcelableArrayList(RecipeTag.RECIPE_TAG);
            sortType = new SortType(getArguments().getInt(SortType.SORT_TYPE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_online_recipe_list, container, false);
        presenter.setView(this);
        setRV();
        return binding.getRoot();
    }

    private void setRV() {
        adapter = new OnlineRecipeListAdapter(recipes, this);
        RecyclerView rv = binding.rvOnlineRecipeList;
        rv.setAdapter(adapter);
        //presenter.createList(searchValues, sortType, 0, 20);
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "Loading Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRecipes(List<OnlineRecipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRecipeClicked(OnlineRecipe onlineRecipe) {
        listener.onRecipeClicked(onlineRecipe);
    }

    /**
     * Use the search strings and sortType input by the user to filter out the recipes.
     * @param recipeTags    Includes at most one recipe name search and recipe tag searches
     * @param sortType      The sortType to order the recipes
     */
    public void searchRecipes(List<OnlineRecipeTag> recipeTags, SortType sortType) {
        this.recipeTags = recipeTags;
        this.sortType = sortType;
        presenter.createSearchList(recipeTags, sortType, 0, 20);
    }

    public void searchSavedRecipes(long userId, SortType sortType) {
        presenter.createSavedList(userId, sortType, 0, 20);
    }

    public void searchUploadedRecipes(long userId, SortType sortType) {
        presenter.createUploadedList(userId, sortType, 0, 20);
    }
}