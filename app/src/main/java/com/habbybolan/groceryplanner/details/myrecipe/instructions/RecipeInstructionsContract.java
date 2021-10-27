package com.habbybolan.groceryplanner.details.myrecipe.instructions;

import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

public interface RecipeInstructionsContract {

    interface Presenter<T extends RecipeInstructionsView> {

        void setView(T view);
        void destroy();
    }

    interface PresenterEdit extends Presenter<RecipeInstructionsView> {

        /**
         * Calls Interactor to update the Instructions of the Recipe.
         * @param myRecipe    Recipe to update
         */
        void updateRecipe(MyRecipe myRecipe);
    }

    interface PresenterReadOnly extends Presenter<RecipeInstructionsView> {

    }

    interface Interactor {
    }

    interface InteractorEdit extends Interactor {

        /**
         * Update the Instructions of the Recipe.
         * @param myRecipe   Recipe to update
         */
        void updateRecipe(MyRecipe myRecipe);
    }

    interface RecipeInstructionsView {

    }

    interface RecipeInstructionsListener {
        MyRecipe getMyRecipe();
        void onSwapViewInstructions();
    }
}
