package com.habbybolan.groceryplanner.details.recipedetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientListDetailsBinding;
import com.habbybolan.groceryplanner.models.Ingredient;

import java.util.List;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.ViewHolder> {

    private List<Ingredient> ingredients;
    private RecipeDetailView view;

    RecipeDetailAdapter(List<Ingredient> ingredients, RecipeDetailView view) {
        this.ingredients = ingredients;
        this.view = view;
    }

    @NonNull
    @Override
    public RecipeDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        IngredientListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_list_details, parent, false);
        return new RecipeDetailAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailAdapter.ViewHolder holder, int position) {
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

            binding.txtIngredientName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.onIngredientSelected(ingredients.get(getAdapterPosition()));
                }
            });
        }

        void bind(Ingredient ingredient) {
            binding.setIngredientName(ingredient.getName());
        }
    }
}
