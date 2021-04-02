package com.habbybolan.groceryplanner.listing.recipelist.recipecategorylist;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.habbybolan.groceryplanner.listfragments.ListViewInterface;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeCategoryPresenterImpl implements RecipeCategoryPresenter {

    private RecipeCategoryInteractor recipeCategoryInteractor;
    private ListViewInterface view;

    // true if the recipe categories are being loaded
    private boolean loadingRecipeCategories = false;
    private ObservableArrayList<RecipeCategory> loadedRecipeCategories = new ObservableArrayList<>();

    public RecipeCategoryPresenterImpl(RecipeCategoryInteractor recipeCategoryInteractor) {
        this.recipeCategoryInteractor = recipeCategoryInteractor;
        setRecipeCategoryCallback();
    }

    // set up callback for loading recipeCategories
    private void setRecipeCategoryCallback() {
        loadedRecipeCategories.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<RecipeCategory>>() {
            @Override
            public void onChanged(ObservableList<RecipeCategory> sender) {}

            @Override
            public void onItemRangeChanged(ObservableList<RecipeCategory> sender, int positionStart, int itemCount) {}
            @Override
            public void onItemRangeInserted(ObservableList<RecipeCategory> sender, int positionStart, int itemCount) {
                // set the loaded recipe categories as loaded in
                loadingRecipeCategories = false;
                displayRecipeCategories();
            }
            @Override
            public void onItemRangeMoved(ObservableList<RecipeCategory> sender, int fromPosition, int toPosition, int itemCount) {}
            @Override
            public void onItemRangeRemoved(ObservableList<RecipeCategory> sender, int positionStart, int itemCount) {}
        });
    }

    @Override
    public void destroy() {
        view = null;
    }

    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void deleteRecipeCategory(RecipeCategory recipeCategory) {
        recipeCategoryInteractor.deleteRecipeCategory(recipeCategory);
    }

    @Override
    public void deleteRecipeCategories(List<RecipeCategory> recipeCategories) {
        recipeCategoryInteractor.deleteRecipeCategories(recipeCategories);
    }

    @Override
    public void addRecipeCategory(RecipeCategory recipeCategory) {
        recipeCategoryInteractor.addRecipeCategory(recipeCategory);
        fetchRecipeCategories();
    }

    @Override
    public void setView(ListViewInterface view) {
        this.view = view;
    }

    @Override
    public void fetchRecipeCategories() {
        try {
            loadingRecipeCategories = true;
            recipeCategoryInteractor.fetchRecipeCategories(loadedRecipeCategories);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            view.loadingFailed("Failed to retrieve Recipe Categories");
            loadingRecipeCategories = false;
        }
    }

    private boolean isCategoriesReady() {
        return !loadingRecipeCategories;
    }

    private void displayRecipeCategories() {
        if (isViewAttached() && isCategoriesReady())
            view.showList(loadedRecipeCategories);
    }
}
