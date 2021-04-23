package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeTagDetailsBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;

public class RecipeTagAdapter extends RecyclerView.Adapter<RecipeTagAdapter.ViewHolder> {

    private List<RecipeTag> recipeTags;
    private RecipeOverviewView view;

    public RecipeTagAdapter(List<RecipeTag> recipeTags, RecipeOverviewView view) {
        this.recipeTags = recipeTags;
        this.view = view;
    }

    @NonNull
    @Override
    public RecipeTagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeTagDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_tag_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeTagAdapter.ViewHolder holder, int position) {
        RecipeTag recipeTag = recipeTags.get(position);
        holder.bind(recipeTag);
    }

    @Override
    public int getItemCount() {
        return recipeTags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecipeTagDetailsBinding binding;

        public ViewHolder(RecipeTagDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnDeleteTag.setOnClickListener(l -> {
                view.deleteRecipeTag(recipeTags.get(getAdapterPosition()));
                recipeTags.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }

        public void bind(RecipeTag recipeTag) {
            binding.setTitle(recipeTag.getTitle());
        }
    }
}
