package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeOverviewBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.Category;
import com.habbybolan.groceryplanner.models.Recipe;
import com.habbybolan.groceryplanner.models.RecipeCategory;
import com.habbybolan.groceryplanner.ui.PopupBuilder;

import javax.inject.Inject;

public class RecipeOverviewFragment extends Fragment implements RecipeOverviewView {

    @Inject
    RecipeOverviewPresenter recipeOverviewPresenter;
    private FragmentRecipeOverviewBinding binding;
    private RecipeOverviewListener recipeOverviewListener;
    private Toolbar toolbar;
    private RecipeCategory selectedRecipeCategory;

    public RecipeOverviewFragment() {}

    public static RecipeOverviewFragment getInstance() {
        RecipeOverviewFragment fragment = new RecipeOverviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().recipeDetailSubComponent(new RecipeDetailModule()).inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeOverviewListener = (RecipeOverviewListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_overview, container, false);
        setToolbar();
        setSaveCancelClickers();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeOverviewPresenter.setView(this);
        recipeOverviewPresenter.loadAllRecipeCategories();
        recipeOverviewPresenter.fetchRecipeCategory(recipeOverviewListener.getRecipe().getCategoryId());
        initLayout();
    }

    /**
     * Click functionality of Save and Cancel button
     */
    private void setSaveCancelClickers() {

        binding.saveButton.setOnClickListener(l -> {
            // update name
            recipeOverviewListener.getRecipe().setName(binding.recipeOverviewName.getText().toString());
            // update RecipeCategory if selected one
            if (selectedRecipeCategory != null) {
                recipeOverviewListener.getRecipe().setCategoryId(selectedRecipeCategory.getId());
            } else {
                // otherwise, the 'No Category' was selected
                recipeOverviewListener.getRecipe().setCategoryId(null);
            }
            recipeOverviewPresenter.updateRecipe(recipeOverviewListener.getRecipe());
            // todo: tag
            setVisibilitySaveCancelButton(View.GONE);
        });

        binding.cancelButton.setOnClickListener(l -> {
            setDisplayToCurrentRecipe();
            // todo: tag
            selectedRecipeCategory = null;
            setVisibilitySaveCancelButton(View.GONE);
        });
    }

    private void setToolbar() {
        toolbar = binding.toolbarRecipeOverview.toolbar;
        toolbar.inflateMenu(R.menu.menu_recipe_non_list);
        toolbar.setTitle(getString(R.string.title_recipe_overview));

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
     * Sets the visibility of the save and cancel button
     * @param visibility    Visibility to set buttons to
     */
    private void setVisibilitySaveCancelButton(int visibility) {
        binding.saveButton.setVisibility(visibility);
        binding.cancelButton.setVisibility(visibility);
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
        PopupBuilder.createDeleteDialogue(getLayoutInflater(), "Recipe", binding.recipeOverviewContainer, getContext(), new PopupBuilder.DeleteDialogueInterface() {
            @Override
            public void deleteItem() {
                deleteRecipe();
            }
        });
    }

    /**
     * Delete the Recipe and leave the fragment
     */
    private void deleteRecipe() {
        recipeOverviewPresenter.deleteRecipe(recipeOverviewListener.getRecipe());
        recipeOverviewListener.onRecipeDeleted();
    }


    /**
     * Initiate necessary onClicks and general data.
     */
    private void initLayout() {
        setDisplayToCurrentRecipe();

        // clicker for selecting category to display all categories
        binding.recipeOverviewCategory.setOnClickListener(v -> {
            // display the recipe categories if they are loaded in
            recipeOverviewPresenter.displayRecipeCategories();
        });

        binding.recipeOverviewName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }
            @Override
            public void afterTextChanged(Editable s) {
                setVisibilitySaveCancelButton(View.VISIBLE);
            }
        });
    }

    /**
     * Sets the display to the current recipe's values, resetting any value that might be set manually by the user.
     */
    private void setDisplayToCurrentRecipe() {
        binding.setName(recipeOverviewListener.getRecipe().getName());

        // if the recipe has no category, display it has no category
        if (recipeOverviewListener.getRecipe().getCategoryId() == null)
            binding.setCategoryName(Category.NO_CATEGORY);
        else
            // otherwise, fetch the categoryId and display it
            recipeOverviewPresenter.fetchRecipeCategory(recipeOverviewListener.getRecipe().getCategoryId());
    }

    @Override
    public void createCategoriesAlertDialogue(String[] categoryNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(categoryNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedRecipeCategory = recipeOverviewPresenter.getRecipeCategory(which);
                binding.setCategoryName(categoryNames[which]);
                setVisibilitySaveCancelButton(View.VISIBLE);
            }
        });
        builder.show();
    }

    @Override
    public void displayRecipeCategory(RecipeCategory recipeCategory) {
        selectedRecipeCategory = recipeCategory;
        binding.setCategoryName(recipeCategory.getName());
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface RecipeOverviewListener {

        void onRecipeDeleted();
        Recipe getRecipe();
    }
}
