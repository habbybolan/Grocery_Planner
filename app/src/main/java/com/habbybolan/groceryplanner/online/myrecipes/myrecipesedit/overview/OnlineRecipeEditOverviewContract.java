package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.overview;

import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;

public interface OnlineRecipeEditOverviewContract {

    interface Presenter {
        void addRecipeTag(String title, List<RecipeTag> recipeTags);

        void setView(OverviewView view);
        void destroy();
    }

    interface Interactor {

        boolean addRecipeTag(String title, List<RecipeTag> recipeTags);
    }

    interface OverviewView {
        void updateTagAdded();

        void loadingFailed(String message);
    }

    interface OverviewListener {

        OnlineRecipe getRecipe();
    }
}
