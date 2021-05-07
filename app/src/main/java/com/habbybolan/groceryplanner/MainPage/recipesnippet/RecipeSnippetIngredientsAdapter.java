package com.habbybolan.groceryplanner.MainPage.recipesnippet;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeSnippetIngredientDetailsBinding;
import com.habbybolan.groceryplanner.models.primarymodels.Ingredient;
import com.habbybolan.groceryplanner.models.secondarymodels.MeasurementType;

import java.util.List;

public class RecipeSnippetIngredientsAdapter extends RecyclerView.Adapter<RecipeSnippetIngredientsAdapter.ViewHolder> {

    private List<Ingredient> ingredients;
    private RecipeSnippetView view;

    public RecipeSnippetIngredientsAdapter(List<Ingredient> ingredients, RecipeSnippetView view) {
        this.ingredients = ingredients;
        this.view = view;
    }

    @NonNull
    @Override
    public RecipeSnippetIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeSnippetIngredientDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_snippet_ingredient_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSnippetIngredientsAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecipeSnippetIngredientDetailsBinding binding;

        public ViewHolder(RecipeSnippetIngredientDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ingredient ingredient) {
            binding.setIngredientName(ingredient.getName());
            binding.setQuantity(ingredient.getQuantity());
            binding.setMeasurementType(MeasurementType.getMeasurementCode(ingredient.getQuantityMeasId()));
            binding.setImageResource(ingredient.getFoodType().getImageResource());
        }
    }
}
