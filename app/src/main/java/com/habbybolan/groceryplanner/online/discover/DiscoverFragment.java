package com.habbybolan.groceryplanner.online.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentDiscoverBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DiscoverFragment extends Fragment implements RecipeTagsView {

    private FragmentDiscoverBinding binding;
    private List<DiscoverRecipeTag> discoverRecipeTags = new ArrayList<>();
    private RecipeTagRecyclerView rvTags;

    @Inject
    DiscoverPresenter presenter;

    public DiscoverFragment() {}

    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        // todo:
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // todo:
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false);
        rvTags = new RecipeTagRecyclerView(discoverRecipeTags, this, binding.discoverRvTags, getContext());
        setClickers();
        return binding.getRoot();
    }

    private void setClickers() {
        // clicker for adding a recipe search filter
        binding.btnRecipeSearch.setOnClickListener(l -> {
            String text = binding.editTxtRecipeSearch.getText().toString();
            SortType sortType = new SortType();
            sortType.setSortType(SortType.SORT_ALPHABETICAL_ASC);
            if (presenter.isValidSearch(text)) {
                presenter.addRecipeSearch(text, discoverRecipeTags, sortType);
            }
        });

        // clicker for adding a new tag search filter
        binding.btnTagSearch.setOnClickListener(l -> {

        });
    }

    @Override
    public void deleteRecipeTag(RecipeTag recipeTag) {
        // todo:
    }
}