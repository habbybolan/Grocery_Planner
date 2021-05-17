package com.habbybolan.groceryplanner.online.displayonlinerecipe.ingredients;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientListDetailsBinding;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.MeasurementType;

import java.util.List;

public class OnlineRecipeIngredientsAdapter extends RecyclerView.Adapter<OnlineRecipeIngredientsAdapter.ViewHolder> {

    private List<Ingredient> ingredients;

    public OnlineRecipeIngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public OnlineRecipeIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_list_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineRecipeIngredientsAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private IngredientListDetailsBinding binding;

        public ViewHolder(IngredientListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ingredient ingredient) {
            binding.setIngredientName(ingredient.getName());
            binding.setIngredientQuantity(ingredient.getQuantity());
            binding.setIngredientQuantityType(MeasurementType.getMeasurement(ingredient.getQuantityMeasId()));
            binding.setImageResource(ingredient.getFoodType().getImageResource());
        }
    }
}
