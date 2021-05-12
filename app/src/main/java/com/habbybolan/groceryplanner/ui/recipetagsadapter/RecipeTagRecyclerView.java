package com.habbybolan.groceryplanner.ui.recipetagsadapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;

public class RecipeTagRecyclerView {

    private RecipeTagAdapter adapter;

    /**
     * Constructor for creating an adapter that allows callback communication.
     * @param recipeTags    List of recipe tags to display
     * @param view          Callback interface
     */
    public RecipeTagRecyclerView(List<? extends RecipeTag> recipeTags, RecipeTagsView view, RecyclerView recyclerView, Context context) {
        @SuppressWarnings("unchecked")
        List<RecipeTag> recipeTagsCast = (List<RecipeTag>) recipeTags;
        this.adapter = new RecipeTagAdapter(recipeTagsCast, view);
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
}
