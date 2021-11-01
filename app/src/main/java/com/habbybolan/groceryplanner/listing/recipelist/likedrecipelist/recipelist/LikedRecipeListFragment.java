package com.habbybolan.groceryplanner.listing.recipelist.likedrecipelist.recipelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentLikedRecipeListBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeListModule;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListFragment;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListState;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListStateImpl;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Displays all recipes that the user has liked.
 */
public class LikedRecipeListFragment extends RecipeListFragment implements LikedRecipeListContract.View {

    @Inject
    LikedRecipeListContract.Presenter presenter;

    private RecipeListListener recipeListListener;
    private FragmentLikedRecipeListBinding binding;
    private CustomToolbar customToolbar;
    private SortType sortType = new SortType();
    private RecipeListState<OfflineRecipe> state;

    private int offset = 0;
    private int size = 10;

    public LikedRecipeListFragment() {
        // Required empty public constructor
    }

    public static LikedRecipeListFragment newInstance(RecipeCategory recipeCategory) {
        LikedRecipeListFragment fragment = new LikedRecipeListFragment();
        Bundle args = new Bundle();
        args.putParcelable(RecipeCategory.RECIPE_CATEGORY, recipeCategory);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeListSubComponent(new RecipeListModule()).inject(this);
        setHasOptionsMenu(true);
        // get the recipe category to save in state model if it exists
        RecipeCategory recipeCategory = savedInstanceState != null ? savedInstanceState.getParcelable(RecipeCategory.RECIPE_CATEGORY) : null;
        state = new RecipeListStateImpl(recipeCategory, new SortType(), offset, size);
        presenter.setState(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_liked_recipe_list, container, false);
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        presenter.setView(this);
        presenter.fetchCategories();
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(OfflineRecipe.RECIPE) != null) {
            // pull saved recipe list from bundled save
            listItems = savedInstanceState.getParcelableArrayList(OfflineRecipe.RECIPE);
            adapter.notifyDataSetChanged();
        } else {
            // otherwise, pull the list from database and display it.
            presenter.createRecipeList();
        }
    }

    private void setToolbar() {
        String[] titleMethods = new String[]{"Recipe", "Category"};
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.title_recipe_list), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        presenter.searchRecipes(search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        presenter.createRecipeList();
                    }
                }, SortType.SORT_LIST_MOST)
                .allowClickTitle(new CustomToolbar.TitleSelectCallback() {
                    @Override
                    public void selectTitle(int pos) {
                        switch (pos) {
                            case 0:
                                recipeListListener.gotoRecipeListUnCategorized();
                                break;
                            case 1:
                                recipeListListener.gotoRecipeCategories();
                                break;
                        }
                    }
                }, titleMethods)
                .build();
    }

    private void initLayout() {
        adapter = new LikedRecipeListAdapter(listItems, this);
        RecyclerView rv = binding.recipeList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    @Override
    protected void addSelectedItemsToCategory(Category category) {
        RecipeCategory recipeCategory = (RecipeCategory) category;
        presenter.addRecipesToCategory(listItemsChecked, recipeCategory);
    }

    @Override
    protected void removeSelectedItemsFromCategory() {
        presenter.removeRecipesFromCategory(listItemsChecked);
    }

    @Override
    protected ArrayList<? extends Category> getCategories() {
        return presenter.getLoadedRecipeCategories();
    }

    @Override
    public void deleteSelectedItems() {
        presenter.unlikeRecipes(listItemsChecked);
    }

    @Override
    public SortType getSortType() {
        return state.getSortType();
    }

    @Override
    public void resetList(RecipeCategory recipeCategory) {
        state.setRecipeCategory(recipeCategory);
        presenter.createRecipeList();
    }

    /**
     * Called from activity when the recipe category list changed.
     * Reloads the list of loaded Recipe Categories.
     */
    @Override
    public void onCategoryListChanged() {
        presenter.fetchCategories();
    }

    /**
     * manually set listeners due to activity {@link com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity} using the same interface twice.
     * @param recipeListListener    Listener to set
     */
    public void attachListener(RecipeListListener recipeListListener) {
        this.recipeListListener = recipeListListener;
        itemListener = recipeListListener;
    }
}