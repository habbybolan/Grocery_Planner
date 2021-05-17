package com.habbybolan.groceryplanner.online.discover.searchfilter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeFilterBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeFilterModule;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@BindingMethods({
        @BindingMethod(type = android.widget.ImageView.class,
                attribute = "app:srcCompat",
                method = "setImageDrawable") })
public class RecipeFilterFragment extends Fragment implements RecipeTagsView,
        RecipeFilterContract.RecipeFilterView,
        RecipeFilterContract.RecipeTagSearchView {

    private FragmentRecipeFilterBinding binding;
    // RecyclerView that holds the adapter to display the filter tags
    private RecipeTagRecyclerView rvFilterTags;
    // Displays the searched RecipeTag that can be added to the filterRecipeTags list
    private List<OnlineRecipeTag> filterRecipeTags = new ArrayList<>();
    // Adapter to display the searched RecipeTag from the RESTApi
    private RecipeSearchedTagAdapter recipeTagsAdapter;
    // Display the RecipeTag chosen to filter the recipe
    private List<RecipeTag> searchedRecipeTags = new ArrayList<>();
    // Helps communication with Activity and other fragments in activity
    private RecipeFilterListener listener;

    @Inject
    RecipeFilterContract.Presenter presenter;

    public RecipeFilterFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (RecipeFilterListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().discoverSubComponent(new RecipeFilterModule()).inject(this);
        if (getArguments() != null) {
            // todo:
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_filter, container, false);
        presenter.setView(this);
        setSearchTagList();
        binding.setSortMethod(SortType.SORT_ALPHABETICAL_ASC_TITLE);
        rvFilterTags = new RecipeTagRecyclerView(filterRecipeTags, this, binding.discoverRvTags, getContext());
        setClickers();
        binding.setFilterVisible(true);
        return binding.getRoot();
    }

    private void setSearchTagList() {
        recipeTagsAdapter = new RecipeSearchedTagAdapter(searchedRecipeTags, filterRecipeTags, this);
        RecyclerView rvTags = binding.discoverRvTagSearch;
        rvTags.setAdapter(recipeTagsAdapter);
        presenter.createTagList("");
    }

    private void setClickers() {
        // clicker for adding a recipe search filter
        binding.btnRecipeSearch.setOnClickListener(l -> {
            String text = binding.editTxtRecipeSearch.getText().toString();
            SortType sortType = new SortType(SortType.SORT_ALPHABETICAL_ASC);
            presenter.addRecipeSearch(text, filterRecipeTags, sortType, new RecipeFilterContract.RecipeSearchCallback() {
                @Override
                public void onRecipeSearchAdded() {
                    rvFilterTags.itemChanged(0);
                    filterRecipes();
                    binding.editTxtRecipeSearch.setText("");
                }

                @Override
                public void onRecipeSearchReplaced() {
                    rvFilterTags.itemChanged(0);
                    filterRecipes();
                    binding.editTxtRecipeSearch.setText("");
                }

                @Override
                public void onFail() {
                    // todo: fail message?
                }
            });
        });

        // clicker for adding a new tag search filter
        binding.btnTagSearch.setOnClickListener(l -> {
            String text = binding.editTxtTagSearch.getText().toString();
            presenter.createTagList(text);
            binding.editTxtTagSearch.setText("");
        });

        binding.txtBtnSortTypes.setOnClickListener(l -> {
            String[] sortMethods = SortType.getSortListType(SortType.SORT_LIST_ALL);
            PopupMenu popup = new PopupMenu(getContext(), binding.txtBtnSortTypes);
            Menu menu = popup.getMenu();
            for (int i = 0; i < sortMethods.length; i++) {
                menu.add(Menu.NONE , i, i, sortMethods[i]);
                int finalI = i;
                menu.findItem(i).setOnMenuItemClickListener(k -> {
                    binding.setSortMethod(sortMethods[finalI]);
                    filterRecipes();
                    return true;
                });
            }
            popup.show();
        });

        // clickers and animation for selecting the top bar to expand/contract the filter views
        binding.barExpandFilter.setOnClickListener(l -> {
            if (binding.containerRecipeFilter.getVisibility() == View.VISIBLE) {
                binding.containerRecipeFilter.animate()
                        .alpha(0.0f)
                        .setDuration(200)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                binding.setFilterVisible(false);
                            }
                        });
            } else {
                binding.containerRecipeFilter.animate()
                        .alpha(1.0f)
                        .setDuration(0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                binding.setFilterVisible(true);
                            }
                        });
            }
        });
    }

    @Override
    public void onDeleteRecipeTag(RecipeTag recipeTag) {
        int position = presenter.findLocationOfTagRemovedFromFilter(recipeTag.getTitle(), searchedRecipeTags);
        recipeTagsAdapter.notifyItemChanged(position);
        filterRecipes();
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
    public void showSearchedTagList(List<RecipeTag> recipeTags) {
        this.searchedRecipeTags.clear();
        this.searchedRecipeTags.addAll(recipeTags);
        recipeTagsAdapter.notifyDataSetChanged();
    }

    /**
     * Using the use input filters, search for new recipes.
     */
    private void filterRecipes() {
        String text = binding.txtBtnSortTypes.getText().toString();
        listener.searchForRecipe(filterRecipeTags, new SortType(SortType.getSortTypeFromTitle(text)));
    }

    @Override
    public void tagFilterAdded(int position) {
        rvFilterTags.itemInserted(position);
        filterRecipes();
    }

    @Override
    public void tagFilterRemoved(String title) {
        rvFilterTags.removeTag(presenter.removeTag(filterRecipeTags, title));
        filterRecipes();
    }

    public interface RecipeFilterListener {
        void searchForRecipe(List<OnlineRecipeTag> recipeTags, SortType sortType);
    }
}