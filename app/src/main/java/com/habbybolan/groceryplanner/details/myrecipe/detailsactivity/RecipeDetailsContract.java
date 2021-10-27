package com.habbybolan.groceryplanner.details.myrecipe.detailsactivity;

import com.habbybolan.groceryplanner.DbSingleCallback;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;

import java.util.concurrent.ExecutionException;

public interface RecipeDetailsContract {

    interface Presenter {
        void setView(DetailsView view);
        void destroy();

        void loadFullRecipe(long recipeId);
    }

    interface Interactor {

        /**
         * Retrieve the full recipe from the room database.
         * @param recipeId  Id of recipe to retrieve in full
         * @param callback  Callback to signal the OfflineRecipe is loaded
         */
        void fetchRecipe(long recipeId, DbSingleCallback<MyRecipe> callback) throws ExecutionException, InterruptedException;
    }

    interface DetailsView {

        void showRecipe(MyRecipe myRecipe);
    }
}
