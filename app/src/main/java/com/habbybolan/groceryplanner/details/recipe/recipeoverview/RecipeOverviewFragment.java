package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.DialogAddRecipeToGroceryBinding;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeOverviewBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.ui.PopupBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeOverviewFragment extends Fragment implements RecipeOverviewView {

    @Inject
    RecipeOverviewPresenter recipeOverviewPresenter;
    private FragmentRecipeOverviewBinding binding;
    private RecipeOverviewListener recipeOverviewListener;
    private Toolbar toolbar;
    private RecipeCategory selectedRecipeCategory;
    private RecipeGroceriesAdapter adapter;
    private List<GroceryRecipe> groceriesHoldingRecipe = new ArrayList<>();

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
        recipeOverviewPresenter.setView(this);
        recipeOverviewPresenter.fetchGroceriesNotHoldingRecipe(recipeOverviewListener.getRecipe());
        setToolbar();
        setRV();
        setSaveCancelClickers();
        return binding.getRoot();
    }

    /**
     * Set up the RecyclerView for displaying Groceries holding the current Recipe
     */
    private void setRV() {
        RecyclerView rv = binding.rvRecipeOverviewGroceries;
        adapter = new RecipeGroceriesAdapter(groceriesHoldingRecipe, this);
        rv.setAdapter(adapter);
        recipeOverviewPresenter.fetchGroceriesHoldingRecipe(recipeOverviewListener.getRecipe());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeOverviewPresenter.loadAllRecipeCategories();
        recipeOverviewPresenter.fetchRecipeCategory(recipeOverviewListener.getRecipe().getCategoryId());
        initLayout();
    }

    /**
     * Click functionality of Save and Cancel button.
     */
    private void setSaveCancelClickers() {
        // onClick save button
        binding.saveButton.setOnClickListener(l -> {
            // update name
            recipeOverviewListener.getRecipe().setName(binding.recipeOverviewName.getText().toString());
            // update serving size
            recipeOverviewListener.getRecipe().setServingSize(Integer.parseInt(binding.recipeOverviewServingSize.getText().toString()));
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

        // onClick cancel button
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

        // text change listener to display the save button
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

        // clicker for opening the Grocery list of Groceries that don't contain the recipe
        binding.btnRecipeOverviewAddToGrocery.setOnClickListener(l -> {
            recipeOverviewPresenter.displayGroceriesNotHoldingRecipe();
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
        binding.setServingSize(String.valueOf(recipeOverviewListener.getRecipe().getServingSize()));
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
    public void displayGroceriesHoldingRecipe(List<GroceryRecipe> groceries) {
        this.groceriesHoldingRecipe.clear();
        this.groceriesHoldingRecipe.addAll(groceries);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void displayRecipeIngredients(ArrayList<IngredientWithGroceryCheck> ingredients, String[] ingredientNames, Grocery grocery) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Select Ingredients to add to Grocery")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(ingredientNames, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, set ingredient as selected
                                    ingredients.get(which).setIsInGrocery(true);
                                } else {
                                    // Else, set ingredient as not selected
                                    ingredients.get(which).setIsInGrocery(false);
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            // update the recipe ingredients inside the grocery
                            recipeOverviewPresenter.updateRecipeIngredientsInGrocery(recipeOverviewListener.getRecipe(), grocery, 1, ingredients);
                        } finally {
                            // update the stored values holding the the groceries holding and not holding the recipe
                            recipeOverviewPresenter.fetchGroceriesNotHoldingRecipe(recipeOverviewListener.getRecipe());
                            recipeOverviewPresenter.fetchGroceriesHoldingRecipe(recipeOverviewListener.getRecipe());
                        }
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            recipeOverviewPresenter.deleteRecipeFromGrocery(recipeOverviewListener.getRecipe(), grocery);
                        } finally {
                            // update the stored values holding the the groceries holding and not holding the recipe
                            recipeOverviewPresenter.fetchGroceriesNotHoldingRecipe(recipeOverviewListener.getRecipe());
                            recipeOverviewPresenter.fetchGroceriesHoldingRecipe(recipeOverviewListener.getRecipe());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        // set the pre-selection of the items
        for (int i = 0; i < ingredients.size(); i++) {
            dialog.getListView().setItemChecked(i, ingredients.get(i).getIsInGrocery());
        }

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnPositive.setBackgroundColor(getContext().getResources().getColor(R.color.colorRedAccentMedium));
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setBackgroundColor(getContext().getResources().getColor(R.color.colorRedAccentMedium));
        Button btnNeutral = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        btnNeutral.setBackgroundColor(getContext().getResources().getColor(R.color.colorRedAccentMedium));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
        btnNeutral.setLayoutParams(layoutParams);
    }

    @Override
    public void displayGroceriesNotHoldingRecipe(List<Grocery> groceries) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //DialogueListBinding dialogBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialogue_list, binding.recipeOverviewContainer, false);
        final String[] groceryNames = recipeOverviewPresenter.getArrayOfGroceryNames(groceries);
        // builder.setView(dialogBinding.getRoot())
        builder.setTitle("Grocery to add recipe to")
                // Add action buttons
                .setItems(groceryNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // todo: could pre-load recipe ingredients, then directly display them
                        recipeOverviewPresenter.fetchRecipeIngredients(recipeOverviewListener.getRecipe(), groceries.get(which), true);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnPositive.setBackgroundColor(getContext().getResources().getColor(R.color.colorRedAccentMedium));
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setBackgroundColor(getContext().getResources().getColor(R.color.colorRedAccentMedium));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }

    @Override
    public void displayRecipeIngredientsInGrocery(Grocery grocery) {
        // get the ingredients from a recipe that was added to the grocery list
        recipeOverviewPresenter.fetchRecipeIngredients(recipeOverviewListener.getRecipe(), grocery, false);
    }

    /**
     * Opens dialogue that allows changing the amount of times the recipe is put in the Grocery,
     * as well as removing the recipe from the Grocery.
     * @param grocery   Grocery holding the recipe to alter
     * @return          Dialog to display
     */
    public Dialog createEditRecipeInGroceryDialog(GroceryRecipe grocery) {
        DialogAddRecipeToGroceryBinding dialogBiding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_add_recipe_to_grocery, binding.recipeOverviewContainer, false);
        dialogBiding.setGroceryName(grocery.getName());
        dialogBiding.setRecipeAmount(String.valueOf(grocery.getAmount()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(binding.getRoot())
                // Add action buttons
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // todo: save
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // todo: cancel
                    }
                });
        return builder.create();
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
