package com.habbybolan.groceryplanner.details.ingredientdetails.ingredientadd;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientListDetailsBinding;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;

import java.util.List;

public class IngredientAddAdapter extends RecyclerView.Adapter<IngredientAddAdapter.ViewHolder> {

    private List<Ingredient> ingredients;
    private IngredientAddFragment.IngredientAddItemClickedListener listener;

    IngredientAddAdapter(List<Ingredient> ingredients, IngredientAddFragment.IngredientAddItemClickedListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_list_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAddAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        IngredientListDetailsBinding binding;
        public ViewHolder(IngredientListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.txtIngredientName.setOnClickListener(v -> listener.onIngredientClicked(ingredients.get(getAdapterPosition())));
        }

        public void bind(Ingredient ingredient) {
            binding.setIngredientName(ingredient.getName());
            binding.setImageResource(ingredient.getFoodType().getImageResource());
        }
    }
}
