package com.habbybolan.groceryplanner.online.discover.searchfilter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.RecipeTagListDetailsBinding;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;

public class RecipeSearchedTagAdapter extends RecyclerView.Adapter<RecipeSearchedTagAdapter.ViewHolder>{

    private List<RecipeTag> recipeTags;
    private List<OnlineRecipeTag> alreadyAddedTags;
    private RecipeFilterContract.RecipeTagSearchView view;

    public RecipeSearchedTagAdapter(List<RecipeTag> recipeTags, List<OnlineRecipeTag> alreadyAddedTags, RecipeFilterContract.RecipeTagSearchView view) {
        this.recipeTags = recipeTags;
        this.alreadyAddedTags = alreadyAddedTags;
        this.view = view;
    }

    @NonNull
    @Override
    public RecipeSearchedTagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeTagListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recipe_tag_list_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSearchedTagAdapter.ViewHolder holder, int position) {
        RecipeTag recipeTag = recipeTags.get(position);
        holder.bind(recipeTag);
    }

    @Override
    public int getItemCount() {
        return recipeTags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecipeTagListDetailsBinding binding;
        public ViewHolder(RecipeTagListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.checkBoxTagSearch.setOnClickListener(l -> {
                // checkbox was selected, add to
                if (binding.checkBoxTagSearch.isChecked()) {
                    alreadyAddedTags.add(new OnlineRecipeTag(recipeTags.get(getAdapterPosition()).getTitle(), false));
                    view.tagFilterAdded(alreadyAddedTags.size()-1);
                } else {
                    view.tagFilterRemoved(recipeTags.get(getAdapterPosition()).getTitle());
                }
            });
        }

        public void bind(RecipeTag recipeTag) {
            binding.setIsChecked(isSelected(recipeTag.getTitle()));
            binding.setTitle(recipeTag.getTitle());
        }
    }

    /**
     * Check if the title inside the searched recipe tags are already added to the filter.
     * @param title Tag title to check if already added to filter
     * @return      position of the Tag title in filter, -1 otherwise
     */
    private boolean isSelected(String title) {
        for (int i = 0; i < alreadyAddedTags.size(); i++) {
            if (title.equals(alreadyAddedTags.get(i).getTitle())) return true;
        }
        return false;
    }
}
