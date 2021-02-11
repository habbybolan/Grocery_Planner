package com.habbybolan.groceryplanner;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.databinding.ActivityGroceryDetailBinding;
import com.habbybolan.groceryplanner.details.grocerydetails.GroceryDetailFragment;
import com.habbybolan.groceryplanner.details.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;

public class GroceryDetailsActivity extends AppCompatActivity implements GroceryDetailFragment.GroceryDetailsListener, IngredientEditFragment.IngredientEditListener {

    private Grocery grocery;

    private final String DETAILS_TAG = "details_tag";
    private final String EDIT_TAG = "edit_tag";
    private ActivityGroceryDetailBinding binding;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grocery_detail);
        setToolBar();
        setDetailFragmentInfo();
    }

    /**
     * Sets up the toolbar with Up button.
     */
    private void setToolBar() {
        toolbar = (Toolbar) binding.toolbarGroceryDetail;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
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
        finish();
    }

    @Override
    public void onItemClicked(Ingredient ingredient) {
        startEditFragment(ingredient);
    }

    @Override
    public void createNewItem() {
        startEditFragment(new Ingredient());
    }

    @Override
    public void onGroceryDeleted() {
        goBackToGroceryList();
    }

    @Override
    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
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
        GroceryDetailFragment fragment = (GroceryDetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILS_TAG);
        if (fragment != null) {
            goBackToGroceryList();
        } else {
            // on editing fragment, go back to the details fragment
            onDoneEditing();
        }
    }
}
