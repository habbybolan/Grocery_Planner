package com.habbybolan.groceryplanner.ui.recipetagsadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeTagDetailsBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;

/**
 * Creates a list of RecipeTags that is either editable or read-only.
 */
class RecipeTagAdapter extends RecyclerView.Adapter<RecipeTagAdapter.ViewHolder> {

    private List<RecipeTag> recipeTags;
    private RecipeTagsView view;

    /**
     * Constructor for a modifiable recipe tag list.
     * @param recipeTags    Recipe tags to display.
     * @param view          Callback to allow interacting with the recipe tags
     */
    protected RecipeTagAdapter(List<RecipeTag> recipeTags, RecipeTagsView view) {
        this.recipeTags = recipeTags;
        this.view = view;
    }

    /**
     * Constructor for a read-only recipe tag list.
     * @param recipeTags    Recipe tags to display.
     */
    protected RecipeTagAdapter(List<RecipeTag> recipeTags) {
        this.recipeTags = recipeTags;
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
                if (view != null) {
                    RecipeTag recipeTag = recipeTags.get(getAdapterPosition());
                    // removed the RecipeTag from the display list
                    recipeTags.remove(getAdapterPosition());
                    // notify the view that the RecipeTag has been removed
                    view.onDeleteRecipeTag(recipeTag);
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }

        public void bind(RecipeTag recipeTag) {
            binding.setTitle(recipeTag.getTitle());
            // if no view, then the tags are read-only
            if (view == null) binding.btnDeleteTag.setVisibility(View.GONE);
        }
    }
}
