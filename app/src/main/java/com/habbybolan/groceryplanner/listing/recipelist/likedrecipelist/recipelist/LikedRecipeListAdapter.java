package com.habbybolan.groceryplanner.listing.recipelist.likedrecipelist.recipelist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeListDetailsBinding;
import com.habbybolan.groceryplanner.listfragments.ListAdapter;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.List;

public class LikedRecipeListAdapter extends ListAdapter<LikedRecipeListAdapter.ViewHolder, OfflineRecipe> {

    public LikedRecipeListAdapter(List<OfflineRecipe> offlineRecipes, ListViewInterface view) {
        super(view, offlineRecipes);
    }

    @NonNull
    @Override
    public LikedRecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_list_details, parent, false);
        return new LikedRecipeListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedRecipeListAdapter.ViewHolder holder, int position) {
        OfflineRecipe offlineRecipe = items.get(position);
        holder.bind(offlineRecipe);
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

        void bind(OfflineRecipe offlineRecipe) {
            binding.setRecipeName(offlineRecipe.getName());
            displayCheckBox(binding.recipeCheckBox);
            if (offlineRecipe.getCategoryId() != null)
                binding.setRecipeCategory(String.valueOf(offlineRecipe.getCategoryId()));
        }
    }
}
