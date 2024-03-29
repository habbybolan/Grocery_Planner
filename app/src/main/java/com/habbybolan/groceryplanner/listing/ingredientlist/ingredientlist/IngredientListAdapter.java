package com.habbybolan.groceryplanner.listing.ingredientlist.ingredientlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientListDetailsBinding;
import com.habbybolan.groceryplanner.listfragments.ListAdapter;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.MeasurementType;

import java.util.List;

public class IngredientListAdapter extends ListAdapter<IngredientListAdapter.ViewHolder, Ingredient> {

    IngredientListAdapter(List<Ingredient> ingredients, ListViewInterface view) {
        super(view, ingredients);
    }

    @NonNull
    @Override
    public IngredientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_list_details, parent, false);
        return new IngredientListAdapter.ViewHolder(binding);
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
                setOnClickItem(binding.ingredientCheckBox, getAdapterPosition());
            });

            // click listener for clicking an ingredient's check box
            binding.ingredientCheckBox.setOnClickListener(v -> {
                setOnClickItemCheckBox(binding.ingredientCheckBox, getAdapterPosition());
            });

            // on long click, go into select mode
            binding.ingredientListContainer.setOnLongClickListener(v ->
                    setOnLongClickItem(getAdapterPosition()));
        }

        void bind(Ingredient ingredient) {
            binding.setIngredientName(ingredient.getName());
            if (ingredient.hasQuantity()) binding.setIngredientQuantity(ingredient.getQuantity());
            if (ingredient.hasQuantityMeasId()) binding.setIngredientQuantityType(MeasurementType.getMeasurement(ingredient.getQuantityMeasId()));
            binding.setImageResource(ingredient.getFoodType().getImageResource());
            displayCheckBox(binding.ingredientCheckBox);
        }
    }
}
