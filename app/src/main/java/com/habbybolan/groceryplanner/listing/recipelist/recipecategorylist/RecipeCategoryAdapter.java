package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.listfragments.ListAdapter;
import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeListDetailsBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.List;

public class RecipeCategoryAdapter extends ListAdapter<RecipeCategoryAdapter.ViewHolder, RecipeCategory> {

    RecipeCategoryAdapter(List<RecipeCategory> recipeCategories, ListViewInterface view) {
        super(view, recipeCategories);
    }

    @NonNull
    @Override
    public RecipeCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_list_details, parent, false);
        return new RecipeCategoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCategoryAdapter.ViewHolder holder, int position) {
        RecipeCategory recipeCategory = items.get(position);
        holder.bind(recipeCategory);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RecipeListDetailsBinding binding;

        public ViewHolder(RecipeListDetailsBinding binding) {
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

        void bind(RecipeCategory recipeCategory) {
            binding.setRecipeName(recipeCategory.getName());
            displayCheckBox(binding.recipeCheckBox);
        }

    }
}
