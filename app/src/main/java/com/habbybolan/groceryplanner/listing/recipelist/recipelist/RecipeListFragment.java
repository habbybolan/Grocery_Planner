package com.habbybolan.groceryplanner.listing.recipelist.recipelist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.CreatePopupDetailsBinding;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeListBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeListModule;
import com.habbybolan.groceryplanner.listfragments.CategoryListFragment;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;
import com.habbybolan.groceryplanner.ui.CustomToolbar;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.inject.Inject;

public class RecipeListFragment extends CategoryListFragment<Recipe> implements RecipeListView {

    @Inject
    RecipeListPresenter recipeListPresenter;

    private RecipeListListener recipeListListener;
    private FragmentRecipeListBinding binding;
    private CustomToolbar customToolbar;
    private SortType sortType = new SortType();
    private RecipeCategory recipeCategory;

    public RecipeListFragment() {}

    public RecipeListFragment newInstance(RecipeCategory recipeCategory) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putParcelable(RecipeCategory.RECIPE_CATEGORY, recipeCategory);
        return fragment;
    }

    /**
     * Listeners implemented explicitly inside RecipeListActivity. Manually attach listener.
     * @param recipeListListener     Implemented listener from RecipeListActivity
     */
    public void attachListener(RecipeListListener recipeListListener) {
        this.recipeListListener = recipeListListener;
        itemListener = recipeListListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeListSubComponent(new RecipeListModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ingredient_holder_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        String[] titleMethods = new String[]{"Recipe", "Category"};
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.title_recipe_list), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addSearch(new CustomToolbar.SearchCallback() {
                    @Override
                    public void search(String search) {
                        recipeListPresenter.searchRecipes(search);
                    }
                })
                .addSortIcon(new CustomToolbar.SortCallback() {
                    @Override
                    public void sortMethodClicked(String sortMethod) {
                        sortType.setSortType(SortType.getSortTypeFromTitle(sortMethod));
                        recipeListPresenter.createRecipeList();
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeListPresenter.setView(this);
        recipeListPresenter.fetchCategories();
        if (savedInstanceState != null) {
            recipeCategory = savedInstanceState.getParcelable(RecipeCategory.RECIPE_CATEGORY);
            // pull saved recipe list from bundled save
            listItems = savedInstanceState.getParcelableArrayList(Recipe.RECIPE);
            adapter.notifyDataSetChanged();
        } else {
            // otherwise, pull the list from database and display it.
            recipeListPresenter.createRecipeList();
        }
    }

    /**
     * Initiate the Recycler View to display list of recipes and button clickers.
     */
    private void initLayout() {
        adapter = new RecipeListAdapter(listItems, this);
        RecyclerView rv = binding.recipeList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        // on click for adding an item from floating action button
        View view = binding.recipeListBottomAction;
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_bottom_bar_add);
        floatingActionButton.setOnClickListener(v -> onAddRecipeClicked());
    }

    /**
     * Creates an Alert dialogue to create a new Recipe element.
     */
    private void onAddRecipeClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Write a Recipe list name");
        final CreatePopupDetailsBinding groceryBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.create_popup_details, null, false);
        builder.setView(groceryBinding.getRoot());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = groceryBinding.editTxtGroceryName.getText().toString();
                if (Grocery.isValidName(name))
                    createRecipe(name);
                else
                    Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    /**
     * Creates an empty Recipe with the given name.
     * @param recipeName  The name of the new Recipe to create
     */
    private void createRecipe(String recipeName) {
        Recipe.RecipeBuilder recipeBuilder = new Recipe.RecipeBuilder(recipeName);
        RecipeCategory recipeCategory = ((RecipeListActivity) getActivity()).getRecipeCategory();
        if (recipeCategory != null) recipeBuilder.setCategoryId(recipeCategory.getId());
        recipeListPresenter.addRecipe(recipeBuilder.build(), new Timestamp(System.currentTimeMillis()/1000));
    }

    /**
     * Called from activity when the recipe category list changed.
     * Reloads the list of loaded Recipe Categories.
     */
    public void onCategoryListChanged() {
        recipeListPresenter.fetchCategories();
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "Loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteSelectedItems() {
        recipeListPresenter.deleteRecipes(listItemsChecked);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Recipe.RECIPE, (ArrayList<? extends Parcelable>) listItems);
    }

    @Override
    protected void addSelectedItemsToCategory(Category category) {
        RecipeCategory recipeCategory = (RecipeCategory) category;
        recipeListPresenter.addRecipesToCategory(listItemsChecked, recipeCategory);
    }

    @Override
    protected void removeSelectedItemsFromCategory() {
        recipeListPresenter.removeRecipesFromCategory(listItemsChecked);
    }

    @Override
    protected ArrayList<RecipeCategory> getCategories() {
        return recipeListPresenter.getLoadedRecipeCategories();
    }

    @Override
    public SortType getSortType() {
        return sortType;
    }

    @Override
    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    public void setList(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
        recipeListPresenter.createRecipeList();
    }

    public interface RecipeListListener extends ItemListener<Recipe> {

        void gotoRecipeListUnCategorized();
        void gotoRecipeCategories();
    }
}
