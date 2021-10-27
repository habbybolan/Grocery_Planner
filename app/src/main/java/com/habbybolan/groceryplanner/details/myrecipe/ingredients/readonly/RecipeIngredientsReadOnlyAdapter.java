package com.habbybolan.groceryplanner.details.myrecipe.ingredients.readonly;

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

public class RecipeIngredientsReadOnlyAdapter extends RecyclerView.Adapter<RecipeIngredientsReadOnlyAdapter.ViewHolder> {

    private List<Ingredient> ingredients;

    RecipeIngredientsReadOnlyAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public RecipeIngredientsReadOnlyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_list_details, parent, false);
        return new RecipeIngredientsReadOnlyAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsReadOnlyAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final IngredientListDetailsBinding binding;

        ViewHolder(IngredientListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Ingredient ingredient) {
            binding.setIngredientName(ingredient.getName());
            if (ingredient.hasQuantity()) binding.setIngredientQuantity(ingredient.getQuantity());
            if (ingredient.hasQuantityMeasId()) binding.setIngredientQuantityType(MeasurementType.getMeasurement(ingredient.getQuantityMeasId()));
            binding.setImageResource(ingredient.getFoodType().getImageResource());
        }
    }
}
