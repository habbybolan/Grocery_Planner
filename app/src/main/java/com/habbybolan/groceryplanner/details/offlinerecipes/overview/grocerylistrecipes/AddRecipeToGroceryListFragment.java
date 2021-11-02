package com.habbybolan.groceryplanner.details.offlinerecipes.overview.grocerylistrecipes;

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

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentAddRecipeToGroceryListBinding;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeGroceriesAdapter;
import com.habbybolan.groceryplanner.details.offlinerecipes.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.RecipeDetailModule;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.List;

import javax.inject.Inject;

public class AddRecipeToGroceryListFragment extends Fragment implements AddRecipeToGroceryListContract.View {

    private FragmentAddRecipeToGroceryListBinding binding;
    private RecipeGroceriesAdapter groceriesAdapter;
    private RecipeOverviewContract.RecipeOverviewListener recipeOverviewListener;

    @Inject
    AddRecipeToGroceryListContract.Presenter presenter;

    public AddRecipeToGroceryListFragment() {
        // Required empty public constructor
    }

    public static AddRecipeToGroceryListFragment newInstance() {
        AddRecipeToGroceryListFragment fragment = new AddRecipeToGroceryListFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeOverviewListener = (RecipeOverviewContract.RecipeOverviewListener) context;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipe_to_grocery_list, container, false);
        presenter.setView(this);
        initiateClickers();
        setRV();
        presenter.fetchGroceriesNotHoldingRecipe( recipeOverviewListener.getRecipe());
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(OfflineRecipe.RECIPE,  recipeOverviewListener.getRecipe());
    }

    private void initiateClickers() {
        // clicker for opening the Grocery list of Groceries that don't contain the recipe
        binding.btnRecipeAddToGrocery.setOnClickListener(l -> {
            presenter.displayGroceriesNotHoldingRecipe();
        });
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayGroceriesHoldingRecipe() {
        groceriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGroceryHoldingRecipeClicked(Grocery grocery) {
        // get the ingredients from a recipe that was added to the grocery list
        presenter.fetchRecipeIngredients( recipeOverviewListener.getRecipe(), grocery, false);
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
                            presenter.updateRecipeIngredientsInGrocery( recipeOverviewListener.getRecipe(), grocery, 1, ingredients);
                        } finally {
                            // update the stored values holding the the groceries holding and not holding the recipe
                            presenter.fetchGroceriesNotHoldingRecipe( recipeOverviewListener.getRecipe());
                            presenter.fetchGroceriesHoldingRecipe( recipeOverviewListener.getRecipe());
                        }
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            presenter.deleteRecipeFromGrocery( recipeOverviewListener.getRecipe(), grocery);
                        } finally {
                            // update the stored values holding the the groceries holding and not holding the recipe
                            presenter.fetchGroceriesNotHoldingRecipe( recipeOverviewListener.getRecipe());
                            presenter.fetchGroceriesHoldingRecipe( recipeOverviewListener.getRecipe());
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
                        presenter.fetchRecipeIngredients( recipeOverviewListener.getRecipe(), groceries.get(which), true);
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

    /**
     * Set up the RecyclerView for displaying Groceries holding the current Recipe
     * Set up RecyclerView for displaying the tags added to the recipe
     */
    private void setRV() {
        RecyclerView rvGroceries = binding.rvRecipeOverviewGroceries;
        groceriesAdapter = new RecipeGroceriesAdapter(presenter.getLoadedGroceriesHoldingRecipe(), this);
        rvGroceries.setAdapter(groceriesAdapter);
        presenter.fetchGroceriesHoldingRecipe( recipeOverviewListener.getRecipe());
    }


}