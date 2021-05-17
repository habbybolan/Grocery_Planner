package com.habbybolan.groceryplanner.online.myrecipes.myrecipesedit.overview;

import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.List;

import javax.inject.Inject;

public class OnlineRecipeEditOverviewInteractorImpl implements OnlineRecipeEditOverviewContract.Interactor{

    @Inject
    public OnlineRecipeEditOverviewInteractorImpl() {}

    @Override
    public boolean addRecipeTag(String title, List<RecipeTag> recipeTags) {
        String reformattedTitle = RecipeTag.reformatTagTitle(title);
        RecipeTag recipeTag = new RecipeTag(reformattedTitle);
        if (RecipeTag.isTagTitleValid(title) && !recipeTags.contains(recipeTag)) {
            recipeTags.add(new RecipeTag(reformattedTitle));
            return true;
        }
        return false;
    }
}
