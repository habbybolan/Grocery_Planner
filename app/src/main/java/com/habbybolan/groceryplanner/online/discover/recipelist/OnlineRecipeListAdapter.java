package com.habbybolan.groceryplanner.online.discover.recipelist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeListDetailsBinding;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;

import java.util.List;

public class OnlineRecipeListAdapter extends RecyclerView.Adapter<OnlineRecipeListAdapter.ViewHolder> {

    private List<OnlineRecipe> recipes;
    private OnlineRecipeListContract.AdapterView view;

    public OnlineRecipeListAdapter(List<OnlineRecipe> recipes, OnlineRecipeListContract.AdapterView view) {
        this.recipes = recipes;
        this.view = view;
    }

    @NonNull
    @Override
    public OnlineRecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_list_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineRecipeListAdapter.ViewHolder holder, int position) {
        OnlineRecipe onlineRecipe = recipes.get(position);
        holder.bind(onlineRecipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RecipeListDetailsBinding binding;

        public ViewHolder(RecipeListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.recipeListContainer.setOnClickListener(l -> {
                view.onRecipeClicked(recipes.get(getAdapterPosition()));
            });
        }

        public void bind(OnlineRecipe onlineRecipe) {
            binding.setRecipeName(onlineRecipe.getName());
        }
    }
}
