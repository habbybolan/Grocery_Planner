package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.ingredients;

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

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder> {

    private List<Ingredient> ingredients;
    private OnlineRecipeIngredientsEditContract.IngredientAdapterView view;

    public IngredientListAdapter(List<Ingredient> ingredients, OnlineRecipeIngredientsEditContract.IngredientAdapterView view) {
        this.ingredients = ingredients;
        this.view = view;
    }

    @NonNull
    @Override
    public IngredientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_list_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListAdapter.ViewHolder holder, int position) {
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
            binding.setImageResource(ingredient.getFoodType().getImageResource());
            binding.setIngredientQuantity(ingredient.getQuantity());
            binding.setIngredientQuantityType(MeasurementType.getMeasurementCode(ingredient.getQuantityMeasId()));
        }
    }
}
