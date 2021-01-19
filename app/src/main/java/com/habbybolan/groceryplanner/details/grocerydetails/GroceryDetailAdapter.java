package com.habbybolan.groceryplanner.details.grocerydetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientListDetailsBinding;
import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public class GroceryDetailAdapter extends RecyclerView.Adapter<GroceryDetailAdapter.ViewHolder> {

    private List<Ingredient> ingredients;
    private GroceryDetailsView view;

    GroceryDetailAdapter(List<Ingredient> ingredients, GroceryDetailsView view) {
        this.ingredients = ingredients;
        this.view = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_list_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

            // click listener for clicking on an ingredient
            binding.ingredientListContainer.setOnClickListener(v ->
                    view.onIngredientSelected(ingredients.get(getAdapterPosition())));

            // click listener for clicking an ingredient's check box
            binding.ingredientCheckBox.setOnClickListener(v -> {
                if (binding.ingredientCheckBox.isChecked())
                    view.onItemCheckBoxSelected(ingredients.get(getAdapterPosition()));
                else
                    view.onItemCheckBoxUnSelected(ingredients.get(getAdapterPosition()));
            });
        }

        void bind(Ingredient ingredient) {
            binding.setIngredientName(ingredient.getName());
            if (ingredient.hasPrice()) binding.setIngredientPrice(ingredient.getPrice());
            if (ingredient.hasPricePer()) binding.setIngredientPricePer(ingredient.getPricePer());
            if (ingredient.hasPriceType()) binding.setIngredientPriceType(ingredient.getPriceType());
            if (ingredient.hasQuantity()) binding.setIngredientQuantity(ingredient.getQuantity());
            if (ingredient.hasQuantityType()) binding.setIngredientQuantityType(ingredient.getQuantityType());
        }
    }
}
