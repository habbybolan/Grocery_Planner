package com.habbybolan.groceryplanner.ui.recipetagsadapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;

/**
 * Abstracts the creation of {@link RecipeTagAdapter}.
 * Creates either an editable list, or a read-only list of RecipeTags.
 */
public class RecipeTagRecyclerView {

    private RecipeTagAdapter adapter;
    private List<? extends RecipeTag> recipeTags;

    /**
     * Constructor for creating an adapter that allows callback communication.
     * @param recipeTags    List of recipe tags to display
     * @param view          Callback interface
     */
    public RecipeTagRecyclerView(List<? extends RecipeTag> recipeTags, RecipeTagsView view, RecyclerView recyclerView, Context context) {
        @SuppressWarnings("unchecked")
        List<RecipeTag> recipeTagsCast = (List<RecipeTag>) recipeTags;
        this.adapter = new RecipeTagAdapter(recipeTagsCast, view);
        this.recipeTags = recipeTags;
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Constructor for creating an adapter with no callback communication
     * @param recipeTags    List of recipe tags to display
     */
    public RecipeTagRecyclerView(List<RecipeTag> recipeTags, RecyclerView recyclerView, Context context) {
        this.adapter = new RecipeTagAdapter(recipeTags);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void updateDisplay() {
        adapter.notifyDataSetChanged();
    }

    public void itemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    public void removeTag(int position) {
        adapter.notifyItemRemoved(position);
    }

    public void itemChanged(int position) {
        adapter.notifyItemChanged(position);
    }
}
