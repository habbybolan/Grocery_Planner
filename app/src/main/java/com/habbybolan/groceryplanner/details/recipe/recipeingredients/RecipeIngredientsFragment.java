package com.habbybolan.groceryplanner.details.recipe.recipeingredients;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeDetailBinding;
import com.habbybolan.groceryplanner.details.recipe.recipedetailactivity.RecipeDetailActivity;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.listfragments.NonCategoryListFragment;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.ui.PopupBuilder;

import javax.inject.Inject;

public class RecipeIngredientsFragment extends NonCategoryListFragment<Ingredient> {

    private RecipeDetailListener recipeDetailListener;
    private FragmentRecipeDetailBinding binding;
    private Toolbar toolbar;

    @Inject
    RecipeIngredientsPresenter recipeIngredientsPresenter;

    public RecipeIngredientsFragment() {}

    public static RecipeIngredientsFragment getInstance() {
        RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeDetailListener = (RecipeDetailListener) context;
        attachListener(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
        initLayout();
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        toolbar = binding.toolbarRecipeDetails.toolbar;
        toolbar.inflateMenu(R.menu.menu_ingredient_holder_details);
        toolbar.setTitle(getString(R.string.title_recipe_ingredients));

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        return true;
                    case R.id.action_sort:
                        showSortPopup(getActivity().findViewById(R.id.action_sort));
                        return true;
                    case R.id.action_delete:
                        deleteRecipeCheck();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeIngredientsPresenter.setView(this);
        if (getArguments() != null && getArguments().containsKey(Ingredient.INGREDIENT)) {
             listItems = getArguments().getParcelableArrayList(Ingredient.INGREDIENT);
        } else {
            // retrieve ingredients associated with recipe
            recipeIngredientsPresenter.createIngredientList(recipeDetailListener.getRecipe());
        }
    }

    /**
     * Menu popup for giving different ways to sort the list.
     * @param v     The view to anchor the popup menu to
     */
    private void showSortPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.inflate(R.menu.popup_sort_grocery_list);
        popup.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
                case R.id.popup_alphabetically_grocery_list:
                    Toast.makeText(getContext(), "alphabetically", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.popup_test_grocery_list:
                    Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    /**
     * Initiates the Recycler View to display list of Ingredients and button clickers.
     */
    private void initLayout() {
        adapter = new RecipeIngredientsAdapter(listItems, this);
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        // on click for adding an Ingredient from floating action button
        View view = binding.recipeDetailBottomAction;
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_bottom_bar_add);
        floatingActionButton.setOnClickListener(v -> recipeDetailListener.createNewItem());

        ImageButton btnAddIngredients = view.findViewById(R.id.btn_add_multiple);
        btnAddIngredients.setOnClickListener(l -> {
            recipeDetailListener.gotoAddIngredients();
        });
    }



    /**
     * Ask user to confirm deleting the Recipe
     */
    private void deleteRecipeCheck() {
        PopupBuilder.createDeleteDialogue(getLayoutInflater(), "Recipe", binding.recipeDetailsContainer, getContext(), new PopupBuilder.DeleteDialogueInterface() {
            @Override
            public void deleteItem() {
                deleteRecipe();
            }
        });
    }

    private void deleteRecipe() {
        recipeIngredientsPresenter.deleteRecipe(recipeDetailListener.getRecipe());
        recipeDetailListener.onRecipeDeleted();
    }

    public void reloadList() {
        recipeIngredientsPresenter.createIngredientList(recipeDetailListener.getRecipe());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Recipe Ingredients");
    }

    @Override
    public void deleteSelectedItems() {
        recipeIngredientsPresenter.deleteIngredients(recipeDetailListener.getRecipe(), listItemsChecked);
    }

    /**
     * Listener interface implemented by {@link RecipeDetailActivity}
     */
    public interface RecipeDetailListener extends ItemListener<Ingredient> {

        void createNewItem();
        void onRecipeDeleted();
        Recipe getRecipe();
        void gotoAddIngredients();
    }
}
