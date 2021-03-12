package com.habbybolan.groceryplanner.details.grocerydetails;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityGroceryDetailBinding;
import com.habbybolan.groceryplanner.details.grocerydetails.groceryingredients.GroceryIngredientsFragment;
import com.habbybolan.groceryplanner.details.grocerydetails.ingredientChecklist.IngredientChecklistFragment;
import com.habbybolan.groceryplanner.details.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public class GroceryDetailsActivity extends AppCompatActivity
                                                    implements GroceryIngredientsFragment.GroceryDetailsListener,
                                                    IngredientEditFragment.IngredientEditListener,
                                                    IngredientChecklistFragment.IngredientChecklistListener {

    private Grocery grocery;

    private final String DETAILS_TAG = "details_tag";
    private final String EDIT_TAG = "edit_tag";
    private final String CHECKLIST_TAG = "checklist_tag";
    private ActivityGroceryDetailBinding binding;

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
    public void gotoChecklist(List<Ingredient> ingredients) {
        GroceryIngredientVisibility(View.GONE);
        IngredientChecklistFragment ingredientChecklistFragment = IngredientChecklistFragment.newInstance(ingredients);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_checklist, ingredientChecklistFragment, CHECKLIST_TAG)
                .commit();
    }

    @Override
    public void onDoneEditing() {
        startIngredientsFragment();
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
    private void startIngredientsFragment() {
        GroceryIngredientsFragment groceryIngredientsFragment = GroceryIngredientsFragment.getInstance(grocery);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_details, groceryIngredientsFragment, DETAILS_TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        GroceryIngredientsFragment fragment = (GroceryIngredientsFragment) getSupportFragmentManager().findFragmentByTag(DETAILS_TAG);
        if (fragment != null) {
            goBackToGroceryList();
        } else {
            // on editing fragment, go back to the details fragment
            onDoneEditing();
        }
    }

    private void GroceryIngredientVisibility(int visibility) {
        binding.fragmentDetails.setVisibility(visibility);
    }

    @Override
    public void leaveCheckList() {
        IngredientChecklistFragment ingredientChecklistFragment = (IngredientChecklistFragment) getSupportFragmentManager().findFragmentByTag(CHECKLIST_TAG);
        // destroy the ingredient checklist
        if (ingredientChecklistFragment != null) getSupportFragmentManager().beginTransaction().remove(ingredientChecklistFragment).commitAllowingStateLoss();
        // set grocery ingredient list as visible again
        GroceryIngredientVisibility(View.VISIBLE);
    }
}
