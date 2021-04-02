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
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.CreateIngredientHolderDetailsBinding;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeListBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeListModule;
import com.habbybolan.groceryplanner.listfragments.CategoryListFragment;
import com.habbybolan.groceryplanner.listing.recipelist.RecipeListActivity;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeListFragment extends CategoryListFragment<Recipe> implements RecipeListView {

    @Inject
    RecipeListPresenter recipeListPresenter;

    private RecipeListListener recipeListListener;
    private FragmentRecipeListBinding binding;
    private Toolbar toolbar;

    public RecipeListFragment() {}

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeListListener = (RecipeListListener) context;
        attachListener(getContext());
    }*/

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

    /**
     * Menu popup for giving different ways to sort the list.
     * @param v     The view to anchor the popup menu to
     */
    private void showSortPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.inflate(R.menu.popup_sort_recipe_list);
        popup.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
                case R.id.popup_alphabetically_recipe_list:
                    Toast.makeText(getContext(), "list alphabetically", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.popup_test_recipe_list:
                    Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }


    private void setToolbar() {
        toolbar = binding.toolbarRecipeList.toolbar;
        toolbar.inflateMenu(R.menu.menu_ingredient_holder_list);
        binding.toolbarRecipeList.setTitle(getString(R.string.title_recipe_list));

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    return true;
                case R.id.action_sort:
                    showSortPopup(getActivity().findViewById(R.id.action_sort));
                    return true;
                default:
                    return false;
            }
        });

        // onClick event on toolbar title to swap between Recipe List and Category List
        binding.toolbarRecipeList.toolbarTitle.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.inflate(R.menu.menu_recipe_list_displayed);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_recipe_list:
                        recipeListListener.gotoRecipeListUnCategorized();
                        return true;
                    case R.id.popup_recipe_category:
                        recipeListListener.gotoRecipeCategories();
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeListPresenter.setView(this);
        recipeListPresenter.fetchCategories();
        attachCategoryToPresenter();
        if (savedInstanceState != null) {
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
        attachCategoryToPresenter();
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
     * Called when category has changed. Stores the category and loads the recipes inside that category.
     */
    public void resetList() {
        attachCategoryToPresenter();
        recipeListPresenter.createRecipeList();
    }

    /**
     * Creates an Alert dialogue to create a new Recipe element.
     */
    private void onAddRecipeClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Write a Recipe list name");
        final CreateIngredientHolderDetailsBinding groceryBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.create_ingredient_holder_details, null, false);
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
        recipeListPresenter.addRecipe(recipeBuilder.build());
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
    public void storeAllRecipeCategories(List<RecipeCategory> recipeCategories) {
        // todo: probably will remove and keep data inside presenter
    }

    private void attachCategoryToPresenter() {
        // todo: listener badly implemented
        if (recipeListListener != null)
            recipeListPresenter.attachCategory(recipeListListener.getRecipeCategory());
    }

    public interface RecipeListListener extends ItemListener<Recipe> {

        void gotoGroceryList();
        void gotoRecipeListUnCategorized();
        void gotoRecipeCategories();
        RecipeCategory getRecipeCategory();
    }
}
