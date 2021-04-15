package com.habbybolan.groceryplanner.details.grocerydetails.ingredientlocation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientLocationDetailsBinding;
import com.habbybolan.groceryplanner.models.ingredientmodels.RecipeWithIngredient;

import java.util.List;

public class IngredientLocationAdapter extends RecyclerView.Adapter<IngredientLocationAdapter.ViewHolder>{

    private List<RecipeWithIngredient> recipes;
    private IngredientLocationView view;

    public IngredientLocationAdapter(IngredientLocationView view, List<RecipeWithIngredient> recipes) {
        this.recipes = recipes;
        this.view = view;
    }

    @NonNull
    @Override
    public IngredientLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientLocationDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_location_details, parent, false);
        return new IngredientLocationAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientLocationAdapter.ViewHolder holder, int position) {
        RecipeWithIngredient recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private IngredientLocationDetailsBinding binding;

        public ViewHolder(IngredientLocationDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnDeleteRelationship.setOnClickListener(l -> {
                view.deleteRecipeRelationship(getAdapterPosition());
            });
        }

        public void bind(RecipeWithIngredient recipe) {
            binding.setName(recipe.getRecipeName());
        }
    }
}
