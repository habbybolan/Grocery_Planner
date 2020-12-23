package com.habbybolan.groceryplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.details.grocerydetails.GroceryDetailFragment;
import com.habbybolan.groceryplanner.details.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;

public class GroceryDetailsActivity extends AppCompatActivity implements GroceryDetailFragment.GroceryDetailsListener, IngredientEditFragment.IngredientEditListener {

    private Grocery grocery;

    private final String DETAILS_TAG = "details_tag";
    private final String EDIT_TAG = "edit_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_detail);
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
                startDetailsFragment();
            }
        }
    }

    /**
     * Go back to the Grocery list activity.
     */
    public void goBackToGroceryList() {
        Intent intent = new Intent(this, GroceryListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onIngredientClicked(Ingredient ingredient) {
        startEditFragment(ingredient);
    }

    @Override
    public void createNewIngredient() {
        startEditFragment(new Ingredient());
    }

    @Override
    public void onGroceryDeleted() {
        goBackToGroceryList();
    }

    @Override
    public void onDoneEditing() {
        startDetailsFragment();
    }

    /**
     * Creates and starts the EditFragment, replacing the details fragment from the container
     * @param ingredient    The Ingredient to edit
     */
    private void startEditFragment(Ingredient ingredient) {
        IngredientEditFragment ingredientEditFragment = IngredientEditFragment.getInstance(grocery, ingredient);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_details, ingredientEditFragment, EDIT_TAG)
                .commit();
    }

    /**
     * Creates and starts the DetailsFragment, replacing the edit fragment from container if one exists.
     */
    private void startDetailsFragment() {
        GroceryDetailFragment groceryDetailFragment = GroceryDetailFragment.getInstance(grocery);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_details, groceryDetailFragment, DETAILS_TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(DETAILS_TAG);
        if (fragment != null) {
            // kill the activity and recover the GroceryListActivity
            goBackToGroceryList();
        } else {
            // on editing fragment, go back to the details fragment
            onDoneEditing();
        }
    }
}
