package com.habbybolan.groceryplanner.details.offlinerecipes.overview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeGroceriesDetailsBinding;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;

import java.util.List;

/**
 * Displays the Grocery lists that currently hold this recipe.
 * @param <T> View to hold method for calling the container fragment
 */
public class RecipeGroceriesAdapter<T extends RecipeOverviewContract.OverviewView> extends RecyclerView.Adapter<RecipeGroceriesAdapter.ViewHolder> {

    private List<GroceryRecipe> groceries;
    private RecipeOverviewContract.OverviewView view;

    public RecipeGroceriesAdapter(List<GroceryRecipe> groceries, RecipeOverviewContract.OverviewView view) {
        this.groceries = groceries;
        this.view = view;
    }

    @NonNull
    @Override
    public RecipeGroceriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeGroceriesDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_groceries_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeGroceriesAdapter.ViewHolder holder, int position) {
        GroceryRecipe groceryRecipe = groceries.get(position);
        holder.bind(groceryRecipe);
    }

    @Override
    public int getItemCount() {
        return groceries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecipeGroceriesDetailsBinding binding;

        public ViewHolder(RecipeGroceriesDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.groceriesHoldingRecipeContainer.setOnClickListener(l -> {
                view.onGroceryHoldingRecipeClicked(groceries.get(getAdapterPosition()));
            });
        }

        public void bind(GroceryRecipe grocery) {
            binding.setGroceryName(grocery.getName());
            binding.setRecipeAmount(grocery.getAmount());
        }
    }
}
