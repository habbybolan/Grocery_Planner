package com.habbybolan.groceryplanner.details.offlinerecipes.instructions;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public interface RecipeInstructionsContract {

    interface Presenter<T extends RecipeInstructionsView> {

        void setView(T view);
        void destroy();
    }

    interface PresenterEdit extends Presenter<RecipeInstructionsView> {

        /**
         * Calls Interactor to update the Instructions of the Recipe.
         * @param recipe    Recipe to update
         */
        void updateRecipe(OfflineRecipe recipe);
    }

    /**
     * Wrapper presenter to allow dagger injection of generic Presenter with a generic interactor.
     */
    interface PresenterReadOnly extends Presenter<RecipeInstructionsView> {}

    interface Interactor {
    }

    interface InteractorEdit extends Interactor {

        /**
         * Update the Instructions of the Recipe.
         * @param recipe   Recipe to update
         */
        void updateRecipe(OfflineRecipe recipe);
    }

    interface RecipeInstructionsView {
        // if needed in future
    }

    interface RecipeInstructionsEditView extends RecipeInstructionsView {
        // if needed in future
    }

    interface RecipeInstructionsListener {
        OfflineRecipe getRecipe();
    }

    interface RecipeInstructionsMyRecipeListener extends RecipeInstructionsListener {
        void onSwapViewInstructions();
        void onSync();
    }

    interface RecipeInstructionsLikedRecipeListener extends RecipeInstructionsListener {
    }
}
