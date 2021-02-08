package com.habbybolan.groceryplanner.details.grocerydetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.ListAdapter;
import com.habbybolan.groceryplanner.ListViewInterface;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientListDetailsBinding;
import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public class GroceryDetailAdapter extends ListAdapter<GroceryDetailAdapter.ViewHolder, Ingredient> {

    GroceryDetailAdapter(List<Ingredient> ingredients, ListViewInterface<Ingredient> view) {
        super(view, ingredients);
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
        Ingredient ingredient = items.get(position);
        holder.bind(ingredient);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final IngredientListDetailsBinding binding;

        ViewHolder(IngredientListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // click listener for clicking on an ingredient
            binding.ingredientListContainer.setOnClickListener(v -> {
                // if in the selectMode, disable being able to go into the ingredient
                if (selectMode)
                    checkBoxClick(binding.ingredientCheckBox, getAdapterPosition(), items);
                else {
                    // otherwise, allow normal Ingredient editing
                    view.onItemSelected(items.get(getAdapterPosition()));
                }
            });

            // click listener for clicking an ingredient's check box
            binding.ingredientCheckBox.setOnClickListener(v -> {
                checkBoxClick(binding.ingredientCheckBox, getAdapterPosition(), items);
            });

            // on long click, go into select mode
            binding.ingredientListContainer.setOnLongClickListener(v -> {
                if (!selectMode) {
                    selectMode = true;
                    view.enterSelectMode();
                    notifyDataSetChanged();
                    return true;
                }
                return false;
            });
        }

        public void bind(Ingredient ingredient) {
            binding.setIngredientName(ingredient.getName());
            if (ingredient.hasPrice()) binding.setIngredientPrice(ingredient.getPrice());
            if (ingredient.hasPricePer()) binding.setIngredientPricePer(ingredient.getPricePer());
            if (ingredient.hasPriceType()) binding.setIngredientPriceType(ingredient.getPriceType());
            if (ingredient.hasQuantity()) binding.setIngredientQuantity(ingredient.getQuantity());
            if (ingredient.hasQuantityType()) binding.setIngredientQuantityType(ingredient.getQuantityType());
            // if the select mode is activated, then display checkboxes to select multiple items
            if (selectMode) {
                binding.ingredientCheckBox.setVisibility(View.VISIBLE);
            } else {
                binding.ingredientCheckBox.setVisibility(View.GONE);
                binding.ingredientCheckBox.setChecked(false);
            }
        }
    }


}
