package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.google.android.flexbox.FlexboxLayoutManager;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentRecipeOverviewBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.Category;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.toolbar.CustomToolbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeOverviewFragment extends Fragment implements RecipeOverviewView {

    @Inject
    RecipeOverviewPresenter recipeOverviewPresenter;
    private FragmentRecipeOverviewBinding binding;
    private RecipeOverviewListener recipeOverviewListener;
    private CustomToolbar customToolbar;
    private RecipeCategory selectedRecipeCategory;

    private RecipeGroceriesAdapter groceriesAdapter;
    private List<GroceryRecipe> groceriesHoldingRecipe = new ArrayList<>();

    private RecipeTagAdapter tagAdapter;
    private List<RecipeTag> recipeTags = new ArrayList<>();
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
        setTagClicker();
        return binding.getRoot();
    }

    /**
     * Set up the RecyclerView for displaying Groceries holding the current Recipe
     * Set up RecyclerView for displaying the tags added to the recipe
     */
    private void setRV() {
        RecyclerView rvGroceries = binding.rvRecipeOverviewGroceries;
        groceriesAdapter = new RecipeGroceriesAdapter(groceriesHoldingRecipe, this);
        rvGroceries.setAdapter(groceriesAdapter);
        recipeOverviewPresenter.fetchGroceriesHoldingRecipe(recipeOverviewListener.getRecipe());

        RecyclerView rvTags = binding.recipeOverviewRvTags;
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        rvTags.setLayoutManager(flexboxLayoutManager);
        tagAdapter = new RecipeTagAdapter(recipeTags, this);
        rvTags.setAdapter(tagAdapter);
        recipeOverviewPresenter.createRecipeTagList(recipeOverviewListener.getRecipe());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        recipeOverviewPresenter.loadAllRecipeCategories();
        recipeOverviewPresenter.fetchRecipeCategory(recipeOverviewListener.getRecipe().getCategoryId());
        initLayout();
    }

    /**
     * Sets the tag clicker for adding a new tag to the recipe.
     */
    private void setTagClicker() {
        binding.recipeOverviewBtnAddTag.setOnClickListener(l -> {
            String tagTitle = binding.recipeOverviewTag.getText().toString();
            if (recipeOverviewPresenter.isTagTitleValid(tagTitle)) {
                String reformattedTitle = recipeOverviewPresenter.reformatTagTitle(tagTitle);
                recipeOverviewPresenter.addTag(recipeOverviewListener.getRecipe(), reformattedTitle);
                binding.recipeOverviewTag.setText("");
            } else {
                Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Saves the recipe changes.
     */
    private void saveRecipe() {
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
    }

    /**
     * Cancels the recipe changes.
     */
    private void cancelRecipeChanges() {
        setDisplayToCurrentRecipe();
        selectedRecipeCategory = null;
    }


    private void setToolbar() {
        customToolbar = new CustomToolbar.CustomToolbarBuilder(getString(R.string.title_grocery_list), getLayoutInflater(), binding.toolbarContainer, getContext())
                .addDeleteIcon(new CustomToolbar.DeleteCallback() {
                    @Override
                    public void deleteClicked() {
                        deleteRecipe();
                    }
                }, "Recipe")
                .addSaveIcon(new CustomToolbar.SaveCallback() {
                    @Override
                    public void saveClicked() {
                        saveRecipe();
                    }
                })
                .addCancelIcon(new CustomToolbar.CancelCallback() {
                    @Override
                    public void cancelClicked() {
                        cancelRecipeChanges();
                    }
                })
                .build();
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

    @Override
    public void displayRecipeTags(List<RecipeTag> recipeTags) {
        this.recipeTags.clear();
        this.recipeTags.addAll(recipeTags);
        tagAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteRecipeTag(RecipeTag recipeTag) {
        recipeOverviewPresenter.deleteRecipeTag(recipeOverviewListener.getRecipe(), recipeTag);
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
