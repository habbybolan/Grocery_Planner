package com.habbybolan.groceryplanner.details.grocerydetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.models.Ingredient;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.IngredientListDetailsBinding;

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
