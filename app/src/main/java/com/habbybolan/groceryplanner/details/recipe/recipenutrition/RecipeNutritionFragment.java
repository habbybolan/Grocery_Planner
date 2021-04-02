package com.habbybolan.groceryplanner.details.recipe.recipenutrition;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeNutritionBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.secondarymodels.Nutrition;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.ui.PopupBuilder;

import javax.inject.Inject;

public class RecipeNutritionFragment extends Fragment implements RecipeNutritionView {

    @Inject
    RecipeNutritionPresenter recipeNutritionPresenter;
    private FragmentRecipeNutritionBinding binding;
    private RecipeNutritionListener recipeNutritionListener;
    private Toolbar toolbar;
    private RecipeNutritionAdapter adapter;

    public RecipeNutritionFragment() {}

    public static RecipeNutritionFragment newInstance() {
        RecipeNutritionFragment fragment = new RecipeNutritionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_nutrition, container, false);
        displayRecipeData();
        setToolbar();
        setList();
        setSaveCancelButton();
        return binding.getRoot();
    }

    private void setSaveCancelButton() {

        // Save the changes made to the nutritional facts
        binding.saveButton.setOnClickListener(l -> {
            recipeNutritionListener.getRecipe().updateNutrition(adapter.getCurrNutritionList());
            recipeNutritionPresenter.updateRecipe(recipeNutritionListener.getRecipe());
            adapter.saveChanges();
            setSaveCancelButtonVisibility(View.GONE);
        });
        // cancel the changes made to the nutritional facts
        binding.cancelButton.setOnClickListener(l -> {
            adapter.cancelChanges();
            setSaveCancelButtonVisibility(View.GONE);
        });
    }

    /**
     * Set the visibility of the Save and Cancel buttons
     * @param visibility    The visibility of the buttons
     */
    public void setSaveCancelButtonVisibility(int visibility) {
        binding.saveButton.setVisibility(visibility);
        binding.cancelButton.setVisibility(visibility);
    }

    private void setList() {
        adapter = new RecipeNutritionAdapter(recipeNutritionListener.getRecipe().getNutritionList(), new PropertyChangedInterface() {
            @Override
            public void onPropertyChanged() {
                setSaveCancelButtonVisibility(View.VISIBLE);
            }

            @Override
            public void onRecipeTypeSelected(Nutrition nutrition, int position, TextView v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_measurement_types, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    String type = item.getTitle().toString();
                    nutrition.setMeasurement(type);
                    v.setText(type);
                    return true;
                });
                popupMenu.show();
            }
        });
        RecyclerView rv = binding.recipeNutritionList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    private void setToolbar() {
        toolbar = binding.toolbarRecipeNutrition.toolbar;
        toolbar.inflateMenu(R.menu.menu_recipe_non_list);
        toolbar.setTitle(getString(R.string.title_recipe_nutrition));

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
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
     * Ask the user to confirm deleting the Recipe
     */
    private void deleteRecipeCheck() {
        PopupBuilder.createDeleteDialogue(getLayoutInflater(), "Recipe", binding.recipeNutritionContainer, getContext(), new PopupBuilder.DeleteDialogueInterface() {
            @Override
            public void deleteItem() {
                deleteRecipe();
            }
        });
    }

    /**
     * Delete the Recipe and leave the Fragment.
     */
    private void deleteRecipe() {
        recipeNutritionPresenter.deleteRecipe(recipeNutritionListener.getRecipe());
        recipeNutritionListener.onRecipeDeleted();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeNutritionListener = (RecipeNutritionListener) context;
    }

    /**
     * Displays the Recipe nutritional data inside the edit-text views
     */
    private void displayRecipeData() {
        // todo:
    }

    public interface RecipeNutritionListener {
        void onRecipeDeleted();
        Recipe getRecipe();
    }
}
