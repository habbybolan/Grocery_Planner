package com.habbybolan.groceryplanner.details.grocerydetails;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityGroceryDetailBinding;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsFragment;
import com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation.IngredientLocationFragment;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd.IngredientAddFragment;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

public class GroceryIngredientsActivity extends AppCompatActivity
                                        implements GroceryIngredientsFragment.GroceryIngredientsListener,
                                                    IngredientEditFragment.IngredientEditListener,
                                                    IngredientAddFragment.IngredientAddListener {

    private Grocery grocery;

    private final String DETAILS_TAG = "details_tag";
    private final String EDIT_TAG = "edit_tag";
    private final String ADD_TAG = "add_tag";
    private final String LOCATION_TAG = "location_tag";
    private ActivityGroceryDetailBinding binding;
    GroceryIngredientsFragment groceryIngredientsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grocery_detail);
        setDetailFragmentInfo();
    }

    /**
     * Sets up the details fragment with the Ingredient info to display.
     */
    private void setDetailFragmentInfo() {
        Bundle extras = getIntent().getExtras();
        // get the bundled Ingredients to display in the fragment if any exist.
        if (extras != null && extras.containsKey(Grocery.GROCERY)) {
            grocery = extras.getParcelable(Grocery.GROCERY);
            if (grocery != null) {
                startIngredientsFragment();
            }
        }
    }

    /**
     * Go back to the Grocery list activity.
     */
    public void goBackToGroceryList() {
        finish();
    }

    @Override
    public void onItemClicked(GroceryIngredient groceryIngredient) {
        startEditFragment(groceryIngredient);
    }

    @Override
    public void createNewItem() {
        startEditFragment(null);
    }

    @Override
    public void onGroceryDeleted() {
        goBackToGroceryList();
    }

    /**
     * Creates and starts the IngredientEditFragment, hiding the details fragment.
     * @param ingredient    The Ingredient to edit
     */
    private void startEditFragment(GroceryIngredient ingredient) {
        GroceryIngredientVisibility(View.GONE);
        IngredientEditFragment ingredientEditFragment;
        if (ingredient != null) {
            ingredientEditFragment = IngredientEditFragment.getInstance(grocery, ingredient);

            IngredientLocationFragment ingredientLocationFragment = IngredientLocationFragment.newInstance(grocery, ingredient);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_ingredient_location, ingredientLocationFragment, LOCATION_TAG)
                    .commit();
        } else {
            // create an empty ingredient to start the edit ingredient with
            ingredientEditFragment = IngredientEditFragment.getInstance(grocery, new Ingredient());
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_ingredient_edit, ingredientEditFragment, EDIT_TAG)
                .commit();

        Fade fade = new Fade();
        TransitionManager.beginDelayedTransition(binding.containerEditIngredient, fade);
        binding.containerEditIngredient.setVisibility(View.VISIBLE);
    }

    /**
     * Creates and starts the IngredientAddFragment, hiding the details fragment.
     */
    @Override
    public void gotoAddIngredients() {
        IngredientAddFragment ingredientAddFragment = IngredientAddFragment.newInstance(grocery);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_ingredient_add, ingredientAddFragment, ADD_TAG)
                .commit();
        GroceryIngredientVisibility(View.GONE);
    }

    /**
     * Creates and starts the DetailsFragment, replacing the edit fragment from container if one exists.
     */
    private void startIngredientsFragment() {
        groceryIngredientsFragment = GroceryIngredientsFragment.getInstance(grocery);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_details, groceryIngredientsFragment, DETAILS_TAG)
                .commit();
    }

    @Override
    public void leaveIngredientEdit() {
        IngredientEditFragment ingredientEditFragment = (IngredientEditFragment) getSupportFragmentManager().findFragmentByTag(EDIT_TAG);
        // destroy the ingredient edit Fragment
        if (ingredientEditFragment != null) getSupportFragmentManager().beginTransaction().remove(ingredientEditFragment).commitAllowingStateLoss();

        IngredientLocationFragment ingredientLocationFragment = (IngredientLocationFragment) getSupportFragmentManager().findFragmentByTag(LOCATION_TAG);
        // destroy the ingredient location Fragment
        if (ingredientLocationFragment != null) getSupportFragmentManager().beginTransaction().remove(ingredientLocationFragment).commitAllowingStateLoss();

        binding.containerEditIngredient.setVisibility(View.GONE);
        reloadGroceryIngredientFragment();
    }

    @Override
    public void leaveIngredientAdd() {
        IngredientAddFragment ingredientAddFragment = (IngredientAddFragment) getSupportFragmentManager().findFragmentByTag(ADD_TAG);
        // destroy the ingredient add Fragment
        if (ingredientAddFragment != null) getSupportFragmentManager().beginTransaction().remove(ingredientAddFragment).commitAllowingStateLoss();
        reloadGroceryIngredientFragment();
    }

    /**
     * Reload the Ingredient list fragment
     */
    private void reloadGroceryIngredientFragment() {
        GroceryIngredientVisibility(View.VISIBLE);
        if (groceryIngredientsFragment != null)
            groceryIngredientsFragment.reloadList();
    }

    @Override
    public void onBackPressed() {
        IngredientEditFragment ingredientEditFragment = (IngredientEditFragment) getSupportFragmentManager().findFragmentByTag(EDIT_TAG);
        IngredientAddFragment ingredientAddFragment = (IngredientAddFragment) getSupportFragmentManager().findFragmentByTag(ADD_TAG);
        // if one of the IngredientEdit or AddIngredient fragments are open, close them and reload the Ingredient list
        // otherwise, destroy and leave this activity.
       if (ingredientEditFragment != null)
            leaveIngredientEdit();
        else if (ingredientAddFragment != null)
            leaveIngredientAdd();
        else
            finish();
    }

    private void GroceryIngredientVisibility(int visibility) {
        binding.fragmentDetails.setVisibility(visibility);
    }
}
