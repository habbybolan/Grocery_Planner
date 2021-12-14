package com.habbybolan.groceryplanner.details.offlinerecipes.instructions;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallbackWithFail;
import com.habbybolan.groceryplanner.details.offlinerecipes.DetailFragmentView;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public interface RecipeInstructionsContract {

    interface Presenter<U extends Interactor<T2>, T extends RecipeInstructionsView, T2 extends OfflineRecipe> {

        void setupPresenter(T view, long recipeId);
        void destroy();
        T2 getRecipe();
        void loadUpdatedRecipe();
    }

    interface PresenterEdit extends Presenter<InteractorEdit, RecipeInstructionsView, MyRecipe> {

        /**
         * Calls Interactor to update the Instructions of the Recipe.
         */
        void updateRecipe();
    }

    /**
     * Wrapper presenter to allow dagger injection of generic Presenter with a generic interactor.
     */
    interface PresenterReadOnly<T extends Interactor<U>, U extends OfflineRecipe> extends Presenter<T, RecipeInstructionsView, U> {}

    interface PresenterMyRecipe extends PresenterReadOnly<InteractorMyRecipe, MyRecipe> {}

    interface PresenterLikedRecipe extends PresenterReadOnly<InteractorLikedRecipe, LikedRecipe> {}

    interface Interactor<T extends OfflineRecipe> {
        void fetchFullRecipe(long recipeId, DbSingleCallbackWithFail<T> callback);
    }

    interface InteractorEdit extends Interactor<MyRecipe> {

        /**
         * Update the Instructions of the Recipe.
         * @param recipe   Recipe to update
         */
        void updateRecipe(OfflineRecipe recipe);
    }

    interface InteractorMyRecipe extends Interactor<MyRecipe> {}

    interface InteractorLikedRecipe extends Interactor<LikedRecipe> {}

    interface RecipeInstructionsView extends DetailFragmentView {

        /** Display the updated recipe specific values. */
        void displayUpdatedRecipe();

        /** setup the recipe views */
        void setupRecipeViews();
    }

    interface RecipeInstructionsEditView extends RecipeInstructionsView {
        // if needed in future
    }

    interface RecipeInstructionsListener {
    }

    interface RecipeInstructionsMyRecipeListener extends RecipeInstructionsListener {
        void onSwapViewInstructions();
        void onSync();
    }

    interface RecipeInstructionsLikedRecipeListener extends RecipeInstructionsListener {
    }
}
