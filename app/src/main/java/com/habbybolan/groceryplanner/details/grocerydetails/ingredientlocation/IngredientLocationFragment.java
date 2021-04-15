package com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentIngredientLocationBinding;
import com.habbybolan.groceryplanner.databinding.IngredientLocationDetailsBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.models.ingredientmodels.GroceryIngredient;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;

import javax.inject.Inject;

/**
Holds a list to display the relationship between a grocery list and a particular ingredient inside its list.
 */
public class IngredientLocationFragment extends Fragment implements IngredientLocationView{

    private Grocery grocery;
    private GroceryIngredient groceryIngredient;
    private FragmentIngredientLocationBinding binding;
    private IngredientLocationAdapter adapter;

    @Inject
    IngredientLocationPresenter presenter;

    public IngredientLocationFragment() {}

    public static IngredientLocationFragment newInstance(Grocery grocery, GroceryIngredient groceryIngredient) {
        IngredientLocationFragment fragment = new IngredientLocationFragment();
        Bundle args = new Bundle();
        args.putParcelable(Grocery.GROCERY, grocery);
        args.putParcelable(GroceryIngredient.GROCERY_INGREDIENT, groceryIngredient);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().groceryDetailSubComponent(new GroceryDetailModule()).inject(this);
        if (getArguments() != null) {
            grocery = getArguments().getParcelable(Grocery.GROCERY);
            groceryIngredient = getArguments().getParcelable(GroceryIngredient.GROCERY_INGREDIENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_location, container, false);
        initRV();
        initDirectRelationship();
        return binding.getRoot();
    }

    /**
     * Initiate the view if the ingredient has a direct relationship with the grocery.
     */
    private void initDirectRelationship() {
        if (groceryIngredient.getIsDirectRelationship()) {
            binding.containerDirectRelationship.setVisibility(View.VISIBLE);
            IngredientLocationDetailsBinding directBinding = binding.directRelationshipView;
            directBinding.setName("direct");

            directBinding.btnDeleteRelationship.setOnClickListener(l -> {
                presenter.deleteDirectRelationship(grocery, groceryIngredient);
                initDirectRelationship();
            });
        } else {
            binding.containerDirectRelationship.setVisibility(View.GONE);
        }
    }

    /**
     * Initiate the RecyclerView to show the recipe ingredient relationship with the grocery list.
     */
    private void initRV() {
        RecyclerView rv = binding.rvRecipeList;
        adapter = new IngredientLocationAdapter(this, groceryIngredient.getRecipeWithIngredients());
        rv.setAdapter(adapter);
    }

    @Override
    public void deleteRecipeRelationship(int position) {
        presenter.deleteRecipeRelationship(grocery, groceryIngredient, position);
        adapter.notifyDataSetChanged();
    }
}
