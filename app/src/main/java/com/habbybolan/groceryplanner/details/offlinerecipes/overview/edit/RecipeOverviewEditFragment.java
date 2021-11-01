package com.habbybolan.groceryplanner.details.offlinerecipes.overview.edit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeOverviewEditBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeGroceriesAdapter;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.ui.CustomToolbar;
import com.habbybolan.groceryplanner.ui.recipetagsadapter.RecipeTagRecyclerView;

import java.util.List;

import javax.inject.Inject;

public class RecipeOverviewEditFragment extends Fragment implements RecipeOverviewContract.OverviewEditView {

    @Inject
    RecipeOverviewContract.PresenterEdit presenter;

    private FragmentRecipeOverviewEditBinding binding;
    private RecipeOverviewListener recipeOverviewListener;
    private CustomToolbar customToolbar;
    private RecipeCategory selectedRecipeCategory;

    private RecipeGroceriesAdapter groceriesAdapter;

    private RecipeTagRecyclerView tagRV;
    public RecipeOverviewEditFragment() {}

    public static RecipeOverviewEditFragment getInstance() {
        RecipeOverviewEditFragment fragment = new RecipeOverviewEditFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_overview_edit, container, false);
        presenter.setView(this);
        presenter.fetchGroceriesNotHoldingRecipe(recipeOverviewListener.getRecipe());
        setToolbar();
        setRV();
        setTagClicker();
        initClickers();
        setDisplayToCurrentRecipe();
        return binding.getRoot();
    }

    /**
     * Set up the RecyclerView for displaying Groceries holding the current Recipe
     * Set up RecyclerView for displaying the tags added to the recipe
     */
    private void setRV() {
        RecyclerView rvGroceries = binding.rvRecipeOverviewGroceries;
        groceriesAdapter = new RecipeGroceriesAdapter(presenter.getLoadedGroceriesHoldingRecipe(), this);
        rvGroceries.setAdapter(groceriesAdapter);
        presenter.fetchGroceriesHoldingRecipe(recipeOverviewListener.getRecipe());

        tagRV = new RecipeTagRecyclerView(recipeOverviewListener.getRecipe().getRecipeTags(), this, binding.recipeOverviewRvTags, getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        presenter.loadAllRecipeCategories();
        presenter.fetchRecipeCategory(recipeOverviewListener.getRecipe().getCategoryId());
    }

    @Override
    public void onResume() {
        super.onResume();
        // set the text listeners once the initial view values are set
        setEditTextListeners();
    }

    /**
     * Add a text watcher for when user changes serving size, cook time, prep time, recipe name, and description.
     * Calls the new value to be saved by saving the OfflineRecipe into room database.
     */
    private void setEditTextListeners() {
        // text watcher to save the new value
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                saveRecipe();
            }
        };
        binding.recipeOverviewServingSize.addTextChangedListener(textWatcher);
        binding.recipeOverviewName.addTextChangedListener(textWatcher);
        binding.recipeOverviewCookTime.addTextChangedListener(textWatcher);
        binding.recipeOverviewPrepTime.addTextChangedListener(textWatcher);
        binding.recipeOverviewDescription.addTextChangedListener(textWatcher);
    }

    /**
     * Sets the tag clicker for adding a new tag to the recipe.
     */
    private void setTagClicker() {
        binding.recipeOverviewBtnAddTag.setOnClickListener(l -> {
            String tagTitle = binding.recipeOverviewTag.getText().toString();
            presenter.checkAddingRecipeTag(tagTitle, recipeOverviewListener.getRecipe().getRecipeTags(), recipeOverviewListener.getRecipe());
        });
    }

    /**
     * Saves the recipe changes, values directly inside RecipeEntity.
     */
    private void saveRecipe() {
        String name = binding.recipeOverviewName.getText().toString();
        String servingSize = binding.recipeOverviewServingSize.getText().toString();
        String cookTime = binding.recipeOverviewCookTime.getText().toString();
        String prepTime = binding.recipeOverviewPrepTime.getText().toString();
        String description = binding.recipeOverviewDescription.getText().toString();

        // update the offline recipe
        presenter.updateRecipe(recipeOverviewListener.getRecipe(), name, servingSize, cookTime, prepTime, description, selectedRecipeCategory);
    }

    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.overview_title), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
                .addSwapIcon(new CustomToolbar.SwapCallback() {
                    @Override
                    public void swapClicked() {
                        recipeOverviewListener.onSwapViewOverview();
                    }
                })
                .build();
        customToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    /**
     * Delete the Recipe and leave the fragment
     */
    private void deleteRecipe() {
        recipeOverviewListener.onRecipeDeleted();
    }

    /**
     * Initiate necessary onClicks and general data.
     */
    private void initClickers() {
        // clicker for selecting category to display all categories
        binding.recipeOverviewCategory.setOnClickListener(v -> {
            // display the recipe categories if they are loaded in
            presenter.displayRecipeCategories();
        });

        // clicker for opening the Grocery list of Groceries that don't contain the recipe
        binding.btnRecipeOverviewAddToGrocery.setOnClickListener(l -> {
            presenter.displayGroceriesNotHoldingRecipe();
        });
    }

    /**
     * Sets the display to the current recipe's values.
     */
    private void setDisplayToCurrentRecipe() {
        binding.setName(recipeOverviewListener.getRecipe().getName());

        // if the recipe has no category, display it has no category
        if (recipeOverviewListener.getRecipe().getCategoryId() == null)
            binding.setCategoryName(Category.NO_CATEGORY);
        else
            // otherwise, fetch the categoryId and display it
            presenter.fetchRecipeCategory(recipeOverviewListener.getRecipe().getCategoryId());

        // set serving size, prep time, cook time, and description
        binding.setServingSize(String.valueOf(recipeOverviewListener.getRecipe().getServingSize()));
        binding.setCookTime(String.valueOf(recipeOverviewListener.getRecipe().getCookTime()));
        binding.setPrepTime(String.valueOf(recipeOverviewListener.getRecipe().getPrepTime()));
        binding.setDescription(recipeOverviewListener.getRecipe().getDescription());
    }

    @Override
    public void createCategoriesAlertDialogue(String[] categoryNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(categoryNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedRecipeCategory = presenter.getRecipeCategory(which);
                binding.setCategoryName(categoryNames[which]);
                saveRecipe();
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
    public void displayGroceriesHoldingRecipe() {
        groceriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayRecipeIngredients(List<IngredientWithGroceryCheck> ingredients, String[] ingredientNames, Grocery grocery) {
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
                            presenter.updateRecipeIngredientsInGrocery(recipeOverviewListener.getRecipe(), grocery, 1, ingredients);
                        } finally {
                            // update the stored values holding the the groceries holding and not holding the recipe
                            presenter.fetchGroceriesNotHoldingRecipe(recipeOverviewListener.getRecipe());
                            presenter.fetchGroceriesHoldingRecipe(recipeOverviewListener.getRecipe());
                        }
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            presenter.deleteRecipeFromGrocery(recipeOverviewListener.getRecipe(), grocery);
                        } finally {
                            // update the stored values holding the the groceries holding and not holding the recipe
                            presenter.fetchGroceriesNotHoldingRecipe(recipeOverviewListener.getRecipe());
                            presenter.fetchGroceriesHoldingRecipe(recipeOverviewListener.getRecipe());
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

        // set positions and sizes of the bottom dialog buttons
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
        final String[] groceryNames = presenter.getArrayOfGroceryNames(groceries);
        // builder.setView(dialogBinding.getRoot())
        builder.setTitle("Grocery to add recipe to")
                // Add action buttons
                .setItems(groceryNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // todo: could pre-load recipe ingredients, then directly display them
                        presenter.fetchRecipeIngredients(recipeOverviewListener.getRecipe(), groceries.get(which), true);
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
    public void onGroceryHoldingRecipeClicked(Grocery grocery) {
        // get the ingredients from a recipe that was added to the grocery list
        presenter.fetchRecipeIngredients(recipeOverviewListener.getRecipe(), grocery, false);
    }

    @Override
    public void displayRecipeTags() {
        tagRV.updateDisplay();
    }

    @Override
    public void updateTagDisplay() {
        tagRV.itemInserted(recipeOverviewListener.getRecipe().getRecipeTags().size()-1);
        binding.recipeOverviewTag.setText("");
    }

    @Override
    public void onDeleteRecipeTag(RecipeTag recipeTag) {
        presenter.deleteRecipeTag(recipeOverviewListener.getRecipe(), recipeTag);
    }


    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface RecipeOverviewListener extends RecipeOverviewContract.RecipeOverviewRecipeListener {
        void onRecipeDeleted();
    }
}
