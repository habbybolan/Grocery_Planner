package com.habbybolan.groceryplanner.listing.ingredientlist;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityIngredientListBinding;
import com.habbybolan.groceryplanner.details.ingredientdetails.ingredientedit.IngredientEditFragment;
import com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist.IngredientListFragment;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

/**
 * !!! SET TO POSSIBLY DELETE !!!
 */

public class IngredientListActivity extends AppCompatActivity implements IngredientListFragment.IngredientListListener, IngredientEditFragment.IngredientEditListener {

    private ActivityIngredientListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ingredient_list);
    }

    @Override
    public void onItemClicked(Ingredient ingredient) {
        startEditFragment(ingredient);
    }

    @Override
    public void createNewItem() {
        startEditFragment(new Ingredient());
    }

    private void startEditFragment(Ingredient ingredient) {
        IngredientEditFragment ingredientEditFragment = IngredientEditFragment.getInstance(ingredient);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_ingredient_edit, ingredientEditFragment, getResources().getString(R.string.INGREDIENT_EDIT_TAG))
                .commit();
        ingredientListVisibility(View.GONE);
    }

    /**
     * Sets the visibility of the ingredient list fragment.
     * @param visibility    Visibility value to set IngredientListFragment
     */
    private void ingredientListVisibility(int visibility) {
        binding.fragmentIngredientList.setVisibility(visibility);
    }

    @Override
    public void leaveIngredientEdit() {
        IngredientEditFragment ingredientEditFragment = (IngredientEditFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.INGREDIENT_EDIT_TAG));
        // destroy the ingredient edit Fragment
        if (ingredientEditFragment != null) getSupportFragmentManager().beginTransaction().remove(ingredientEditFragment).commitAllowingStateLoss();
        // reload the ingredient list fragment to update with any changes made in IngredientEditFragment
        ingredientListVisibility(View.VISIBLE);
        IngredientListFragment ingredientListFragment = (IngredientListFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.INGREDIENT_LIST_TAG));
        ingredientListFragment.reloadList();
    }

    @Override
    public void onBackPressed() {
        IngredientEditFragment ingredientEditFragment = (IngredientEditFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.INGREDIENT_EDIT_TAG));
        // if the IngredientEditFragment is not null, then destroy the IngredientEditFragment
        if (ingredientEditFragment != null)
            leaveIngredientEdit();
        else
            finish();
    }
}
