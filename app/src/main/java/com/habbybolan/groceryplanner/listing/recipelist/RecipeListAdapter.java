package com.habbybolan.groceryplanner.listing.recipelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeListDetailsBinding;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private RecipeListView view;

    RecipeListAdapter(List<Recipe> recipes, RecipeListView view) {
        this.recipes = recipes;
        this.view = view;
    }

    @NonNull
    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_list_details, parent, false);
        return new RecipeListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final RecipeListDetailsBinding binding;

        ViewHolder(RecipeListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.recipeListContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.onRecipeSelected(recipes.get(getAdapterPosition()));
                }
            });
        }

        void bind(Recipe recipe) {
            binding.setRecipeName(recipe.getName());
        }
    }
}
