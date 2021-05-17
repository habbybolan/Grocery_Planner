package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.overview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentOnlineRecipeEditOverviewBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.OnlineRecipeModule;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.ui.CustomToolbar;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagsView;

import javax.inject.Inject;

public class OnlineRecipeEditOverviewFragment extends Fragment implements RecipeTagsView,
    OnlineRecipeEditOverviewContract.OverviewView {

    @Inject
    OnlineRecipeEditOverviewContract.Presenter presenter;

    private RecipeTagRecyclerView rvTags;
    private FragmentOnlineRecipeEditOverviewBinding binding;
    private OnlineRecipeEditOverviewContract.OverviewListener listener;
    private CustomToolbar customToolbar;

    public OnlineRecipeEditOverviewFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnlineRecipeEditOverviewContract.OverviewListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().onlineRecipeListSubComponent(new OnlineRecipeModule()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_online_recipe_edit_overview, container, false);
        presenter.setView(this);
        rvTags = new RecipeTagRecyclerView(listener.getRecipe().getRecipeTags(), this, binding.recipeOverviewRvTags, getContext());
        setClickers();
        setToolbar();
        binding.setName(listener.getRecipe().getName());
        return binding.getRoot();
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getResources().getString(R.string.overview_title), getLayoutInflater(), binding.toolbarContainer, getContext()).build();
        customToolbar.getToolbar().setNavigationOnClickListener(l -> {
            getActivity().onBackPressed();
        });
    }

    private void setClickers() {
        binding.recipeOverviewBtnAddTag.setOnClickListener(l -> {
            String title = binding.recipeOverviewTag.getText().toString();
            presenter.addRecipeTag(title, listener.getRecipe().getRecipeTags());
        });
    }

    @Override
    public void onDeleteRecipeTag(RecipeTag recipeTag) {
        // todo:
    }

    @Override
    public void updateTagAdded() {
        rvTags.itemInserted(listener.getRecipe().getRecipeTags().size()-1);
        binding.recipeOverviewNameTag.setText("");
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}