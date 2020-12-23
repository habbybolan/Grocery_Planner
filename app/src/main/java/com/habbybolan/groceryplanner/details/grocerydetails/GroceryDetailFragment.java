package com.habbybolan.groceryplanner.details.grocerydetails;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.di.module.IngredientEditModule;
import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentGroceryDetailBinding;
import com.habbybolan.groceryplanner.di.module.GroceryDetailModule;
import com.habbybolan.groceryplanner.di.GroceryApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 */
public class GroceryDetailFragment extends Fragment implements GroceryDetailsView {

    private FragmentGroceryDetailBinding binding;
    private GroceryDetailAdapter adapter;
    private List<Ingredient> ingredients = new ArrayList<>();
    private GroceryDetailsListener groceryDetailsListener;
    private Grocery grocery;

    @Inject
    GroceryDetailsPresenter groceryDetailsPresenter;

    private GroceryDetailFragment() {}

    public static GroceryDetailFragment getInstance(@NonNull Grocery grocery) {
        Bundle args = new Bundle();
        args.putParcelable(Grocery.GROCERY, grocery);
        GroceryDetailFragment fragment = new GroceryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        groceryDetailsListener = (GroceryDetailsListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().groceryDetailSubComponent(new GroceryDetailModule(), new IngredientEditModule()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grocery_detail, container, false);
        initLayout();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set up the view for view methods to be accessed from the presenter
        groceryDetailsPresenter.setView(this);
        if (getArguments() != null) {
            grocery = getArguments().getParcelable(Grocery.GROCERY);
            if (getArguments().containsKey(Ingredient.INGREDIENT)) {
                ingredients = getArguments().getParcelableArrayList(Ingredient.INGREDIENT);
            } else
                // only the grocery saved, must retrieve its associated Ingredients
                groceryDetailsPresenter.createIngredientList((Grocery) getArguments().getParcelable(Grocery.GROCERY));
        }
    }

    /**
     * Initiates the Recycler View to display list of Ingredients and button clickers.
     */
    private void initLayout() {
        adapter = new GroceryDetailAdapter(ingredients, this);
        RecyclerView rv = binding.ingredientList;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        // button clicker to add a new ingredient to the grocery
        binding.btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groceryDetailsListener.createNewIngredient();
            }
        });

        // button clicker to delete this grocery
        binding.btnDeleteGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGrocery();
            }
        });
    }

    /**
     * Delete the grocery from database and go back to the Grocery List
     */
    private void deleteGrocery() {
        groceryDetailsPresenter.deleteGrocery(grocery);
        groceryDetailsListener.onGroceryDeleted();
    }


    @Override
    public void onIngredientSelected(Ingredient ingredient) {
        groceryDetailsListener.onIngredientClicked(ingredient);
    }

    @Override
    public void showIngredientList(List<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "Loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface GroceryDetailsListener {

        void onIngredientClicked(Ingredient ingredientId);
        void createNewIngredient();
        void onGroceryDeleted();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Ingredient.INGREDIENT, (ArrayList<? extends Parcelable>) ingredients);
        outState.putParcelable(Grocery.GROCERY, grocery);
    }
}
