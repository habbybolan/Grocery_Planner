package com.habbybolan.groceryplanner.listing.recipelist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.CreateIngredientHolderDetailsBinding;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeListBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeListModule;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeListFragment extends Fragment implements RecipeListView{

    @Inject
    RecipeListPresenter recipeListPresenter;

    private RecipeListListener recipeListListener;
    private FragmentRecipeListBinding binding;
    private RecipeListAdapter adapter;

    private List<Recipe> recipes = new ArrayList<>();

    public RecipeListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeListListener = (RecipeListListener) context;
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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_sort:
                showSortPopup(getActivity().findViewById(R.id.action_sort));
                return true;
            case R.id.action_add_recipe:
                onAddRecipeClicked();
                return true;
            default:
                return false;
        }
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
                    Toast.makeText(getContext(), "alphabetically", Toast.LENGTH_SHORT).show();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);
        initLayout();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeListPresenter.setView(this);
        if (savedInstanceState != null) {
            // pull saved recipe list from bundled save
            recipes = savedInstanceState.getParcelableArrayList(Recipe.RECIPE);
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
        adapter = new RecipeListAdapter(recipes, this);
        RecyclerView rv = binding.recipeList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        View view = binding.recipeListBottomAction;
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_bottom_bar_add);
        floatingActionButton.setOnClickListener(v -> onAddRecipeClicked());

        ImageButton gotoGroceryButton = view.findViewById(R.id.btn_goto_other_list);
        gotoGroceryButton.setOnClickListener(v -> recipeListListener.gotoGroceryList());
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
        recipeListPresenter.addRecipe(new Recipe.RecipeBuilder(recipeName)
                .build());
    }

    @Override
    public void showRecipeList(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        recipeListListener.onRecipeClicked(recipe);
        recipeListPresenter.destroy();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Recipe.RECIPE, (ArrayList<? extends Parcelable>) recipes);
    }

    public interface RecipeListListener {

        void gotoGroceryList();
        void onRecipeClicked(Recipe recipe);
    }
}
