package com.habbybolan.groceryplanner.listing.recipelist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.listfragments.ListAdapter;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeListDetailsBinding;
import com.habbybolan.groceryplanner.models.Recipe;

import java.util.List;

public class RecipeListAdapter extends ListAdapter<RecipeListAdapter.ViewHolder, Recipe> {

    RecipeListAdapter(List<Recipe> recipes, ListViewInterface view) {
        super(view, recipes);
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
        Recipe recipe = items.get(position);
        holder.bind(recipe);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final RecipeListDetailsBinding binding;

        ViewHolder(RecipeListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // click listener for clicking on an ingredient
            binding.recipeListContainer.setOnClickListener(v -> {
                setOnClickItem(binding.recipeCheckBox, getAdapterPosition());
            });

            // click listener for clicking an ingredient's check box
            binding.recipeCheckBox.setOnClickListener(v -> {
                setOnClickItemCheckBox(binding.recipeCheckBox, getAdapterPosition());
            });

            // on long click, go into select mode
            binding.recipeListContainer.setOnLongClickListener(v -> setOnLongClickItem(getAdapterPosition()));
        }

        void bind(Recipe recipe) {
            binding.setRecipeName(recipe.getName());
            displayCheckBox(binding.recipeCheckBox);
            if (recipe.getCategoryId() != null)
                binding.setRecipeCategory(String.valueOf(recipe.getCategoryId()));
        }
    }
}
