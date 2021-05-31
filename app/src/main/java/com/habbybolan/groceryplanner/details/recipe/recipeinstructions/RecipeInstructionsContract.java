package com.habbybolan.groceryplanner.details.recipe.recipeinstructions;

import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

public interface RecipeInstructionsContract {

    interface Presenter {

        void setView(RecipeInstructionsView view);
        void destroy();

        /**
         * Calls Interactor to update the Instructions of the Recipe.
         * @param offlineRecipe    Recipe to update
         */
        void updateRecipe(OfflineRecipe offlineRecipe);
    }

    interface Interactor {

        /**
         * Update the Instructions of the Recipe.
         * @param offlineRecipe    Recipe to update
         */
        void updateRecipe(OfflineRecipe offlineRecipe);
    }

    interface RecipeInstructionsView {

    }
}
