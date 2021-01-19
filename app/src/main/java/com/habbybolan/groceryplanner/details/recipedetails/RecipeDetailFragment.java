package com.habbybolan.groceryplanner.details.recipedetails;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeDetailBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.ui.CreatePopupWindow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeDetailFragment extends Fragment implements RecipeDetailView {

    private RecipeDetailListener recipeDetailListener;
    private List<Ingredient> ingredients = new ArrayList<>();
    private FragmentRecipeDetailBinding binding;
    private RecipeDetailAdapter adapter;
    private Recipe recipe;

    @Inject
    RecipeDetailPresenter recipeDetailPresenter;

    public RecipeDetailFragment() {}

    public static RecipeDetailFragment getInstance(@NonNull Recipe recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Recipe.RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeDetailListener = (RecipeDetailListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule(), new IngredientEditModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
        initLayout();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeDetailPresenter.setView(this);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(Recipe.RECIPE);
            if (getArguments().containsKey(Ingredient.INGREDIENT)) {
                ingredients = getArguments().getParcelableArrayList(Ingredient.INGREDIENT);
            } else
                // only the grocery saved, must retrieve its associated Ingredients
                recipeDetailPresenter.createIngredientList((Recipe) getArguments().getParcelable(Recipe.RECIPE));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ingredient_holder_details, menu);
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
            case R.id.action_delete:
                deleteRecipe();
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
        adapter = new RecipeDetailAdapter(ingredients, this);
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        // button clicker to add a new ingredient to the grocery
        binding.btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeDetailListener.createNewIngredient();
            }
        });
    }

    /**
     * Delete the recipe and end the RecipeDetailActivity.
     */
    private void deleteRecipe() {
        PopupWindow popupWindow = new PopupWindow();
        View clickableView = CreatePopupWindow.createPopupDeleteCheck(binding.recipeDetailsContainer, "recipe", popupWindow);
        clickableView.setOnClickListener(v -> {
            recipeDetailPresenter.deleteRecipe(recipe);
            recipeDetailListener.onRecipeDeleted();
            popupWindow.dismiss();
        });
    }

    @Override
    public void onIngredientSelected(Ingredient ingredient) {
        recipeDetailListener.onIngredientClicked(ingredient);
    }

    @Override
    public void showIngredientList(List<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "Loading started", Toast.LENGTH_SHORT).show();
    }

    public void reloadList() {
        recipeDetailPresenter.createIngredientList(recipe);
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Recipe Ingredients");
    }

    /**
     * Listener interface implemented by {@link com.habbybolan.groceryplanner.RecipeDetailActivity}
     */
    public interface RecipeDetailListener {

        void onIngredientClicked(Ingredient ingredient);
        void createNewIngredient();
        void onRecipeDeleted();
    }
}
