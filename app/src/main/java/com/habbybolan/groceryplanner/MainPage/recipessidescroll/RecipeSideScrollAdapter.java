package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeSideScrollDetailsBinding;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;

import java.util.List;

public class RecipeSideScrollAdapter extends RecyclerView.Adapter<RecipeSideScrollAdapter.ViewHolder> {

    private List<OnlineRecipe> recipes;
    private RecipeSideScrollView view;

    public RecipeSideScrollAdapter(List<OnlineRecipe> recipes, RecipeSideScrollView view) {
        this.recipes = recipes;
        this.view = view;
    }

    @NonNull
    @Override
    public RecipeSideScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeSideScrollDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_side_scroll_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSideScrollAdapter.ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe.getName(), recipe.getLikes());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private RecipeSideScrollDetailsBinding binding;
        public ViewHolder(RecipeSideScrollDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.recipeSideScrollContainer.setOnClickListener(l -> {
                view.onRecipeSelected(recipes.get(getAdapterPosition()));
            });
        }

        public void bind(String recipeName, int likes) {
            binding.setRecipeName(recipeName);
            binding.setLikes(String.valueOf(likes));
        }
    }

    /**
     * Add to the current list of recipes.
     * @param recipesToAdd  Recipes to add to list.
     */
    public void addToList(List<OnlineRecipe> recipesToAdd) {
        recipes.addAll(recipesToAdd);
        notifyDataSetChanged();
    }

    /**
     * Reset the current list of recipes and replace with new.
     * @param recipes   New recipe list to replace current with.
     */
    public void resetList(List<OnlineRecipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }
}
